import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { NzRateModule } from 'ng-zorro-antd/rate';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzBreadCrumbModule } from 'ng-zorro-antd/breadcrumb';
import { NzSkeletonModule } from 'ng-zorro-antd/skeleton';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzMessageModule, NzMessageService } from 'ng-zorro-antd/message';
import { RouterLink } from '@angular/router';

import { ProductService } from '../../../../core/services/product.service';
import { CartService } from '../../../../core/services/cart.service';
import { ProductDetail, Product } from '../../../../core/models/product.model';
import { FcfaPipe } from '../../../../shared/pipes/fcfa.pipe';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NzCardModule,
    NzButtonModule,
    NzRateModule,
    NzTagModule,
    NzBadgeModule,
    NzSpinModule,
    NzEmptyModule,
    NzGridModule,
    NzIconModule,
    NzInputNumberModule,
    NzBreadCrumbModule,
    NzSkeletonModule,
    NzMessageModule,
    RouterLink,
    FcfaPipe
  ],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  productId!: number;
  product: ProductDetail | null = null;
  relatedProducts: Product[] = [];
  loading = true;
  addingToCart = false;
  quantity = 1;
  inWishlist = false;


  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private message: NzMessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.productId = Number(params.get('id'));
      this.loadProduct();
    });
  }

  loadProduct(): void {
    this.loading = true;
    this.productService.getById(this.productId).subscribe({
      next: product => {
        this.product = product;
        this.quantity = 1;
        this.loading = false;
        this.loadRelatedProducts();
      },
      error: () => {
        this.message.error('Produit non trouvé');
        this.loading = false;
        this.router.navigate(['/']);
      }
    });
  }

  getProductImage(product: Product): string {
    if (product.imageUrl) return product.imageUrl;
    const imageId = Math.abs(product.id % 100);
    return `https://loremflickr.com/500/500/technology?random=${imageId}`;
  }

  getProductCardImage(product: Product): string {
    return this.getProductImage(product);
  }


  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement | null;
    if (!img) return;
    img.src = 'https://dummyimage.com/500x500/cccccc/969696?text=Image+Not+Found';
  }

  addToCart(): void {
    if (!this.product || this.product.quantity === 0) return;

    this.addingToCart = true;
    this.cartService
      .addToCart({ productId: this.productId, quantity: this.quantity })
      .subscribe({
        next: () => {
          this.message.success(`${this.product?.name} ajouté au panier!`);
          this.quantity = 1;
          this.addingToCart = false;
        },
        error: () => {
          this.message.error("Erreur lors de l'ajout au panier");
          this.addingToCart = false;
        }
      });
  }

  loadRelatedProducts(): void {
    if (!this.product) return;

    const categoryId = this.product.category?.id;
    if (!categoryId) {
      this.relatedProducts = [];
      return;
    }

    this.productService.getAll({
      categoryId,
      page: 0,
      size: 6,
      sortBy: 'createdAt',
      sortDir: 'desc',
      status: 'ACTIVE'
    }).subscribe({
      next: page => {
        const content = page?.content ?? [];
        this.relatedProducts = content.filter(p => p.id !== this.product?.id).slice(0, 6);
      },
      error: () => {
        this.relatedProducts = [];
      }
    });
  }

  viewProductDetail(id: number): void {
    this.router.navigate(['/product', id]);
  }

  addFromRelated(p: Product): void {
    if (this.addingToCart) return;
    this.productId = p.id;

    this.quantity = 1;
    this.addingToCart = true;
    this.cartService
      .addToCart({ productId: p.id, quantity: this.quantity })
      .subscribe({
        next: () => {
          this.message.success(`${p.name} ajouté au panier!`);
          this.addingToCart = false;
        },
        error: () => {
          this.message.error("Erreur lors de l'ajout au panier");
          this.addingToCart = false;
        }
      });
  }

  toggleWishlist(): void {
    this.inWishlist = !this.inWishlist;
    const action = this.inWishlist ? 'ajouté aux' : 'retiré des';
    this.message.success(`Produit ${action} favoris`);
  }
}