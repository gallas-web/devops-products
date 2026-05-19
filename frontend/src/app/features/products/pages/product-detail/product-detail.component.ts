import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzRateModule } from 'ng-zorro-antd/rate';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzBreadCrumbModule } from 'ng-zorro-antd/breadcrumb';
import { NzSkeletonModule } from 'ng-zorro-antd/skeleton';

import { ProductService } from '../../../../core/services/product.service';
import { CartService } from '../../../../core/services/cart.service';
import { ProductDetail } from '../../../../core/models/product.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NzCardModule, NzButtonModule, NzRateModule, NzTagModule, NzBadgeModule,
    NzSpinModule, NzEmptyModule, NzGridModule, NzIconModule, NzInputNumberModule,
    NzBreadCrumbModule, NzSkeletonModule
  ],
  template: `
    <div class="detail-container">
      <!-- Breadcrumb -->
      <nz-breadcrumb style="margin-bottom: 24px">
        <nz-breadcrumb-item>
          <a href="/" style="color: #1890ff">Accueil</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>\n          <a *ngIf="product" [href]="'/shop?category=' + product.category.id" style="color: #1890ff">
            {{ product.category.name }}\n          </a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>{{ product?.name || 'Chargement...' }}</nz-breadcrumb-item>
      </nz-breadcrumb>

      <nz-spin [nzSpinning]="loading">
        <div *ngIf="product && !loading" class="product-detail">
          <div nz-row [nzGutter]="32">
            <!-- Image Section -->
            <div nz-col [nzXs]="24" [nzMd]="12" [nzLg]="10">
              <div class="image-section">
                <img [src]="product.imageUrl || '/assets/placeholder.png'" 
                     [alt]="product.name"
                     class="product-image"
                     onError="this.src='/assets/placeholder.png'" />
              </div>
            </div>

            <!-- Info Section -->
            <div nz-col [nzXs]="24" [nzMd]="12" [nzLg]="14">
              <div class="info-section">
                <!-- Breadcrumb-like path -->
<span class="category-tag">{{ product.category.name }}</span>

                <!-- Title -->
                <h1 class="product-title">{{ product.name }}</h1>

                <!-- Rating -->
                <div class="rating-section">
                  <nz-rate 
                    [ngModel]="product.rating" 
                    nzDisabled 
                    [nzCount]="5">
                  </nz-rate>
                  <span class="rating-text">({{ product.reviewCount }} avis)</span>
                </div>

                <!-- Price -->
                <div class="price-section">
                  <span class="price">{{ product.price | currency:'EUR':'symbol':'1.2-2':'fr-FR' }}</span>
                  <nz-tag *ngIf="product.status === 'ACTIVE'" nzColor="green">Disponible</nz-tag>
                  <nz-tag *ngIf="product.status === 'INACTIVE'" nzColor="red">Indisponible</nz-tag>
                </div>

                <!-- Stock Status -->
                <div class="stock-section" [ngClass]="{'stock-low': product.quantity < 10, 'stock-empty': product.quantity === 0}">
                  <span *ngIf="product.quantity > 10" style="color: #52c41a">
                    ✓ En stock ({{ product.quantity }} disponibles)
                  </span>
                  <span *ngIf="product.quantity > 0 && product.quantity <= 10" style="color: #faad14">
                    ⚠ Stock faible ({{ product.quantity }} disponible{{ product.quantity > 1 ? 's' : '' }})
                  </span>
                  <span *ngIf="product.quantity === 0" style="color: #f5222d">
                    ✗ Rupture de stock
                  </span>
                </div>

                <!-- Description -->
                <div class="description">
                  <p>{{ product.description }}</p>
                </div>

                <!-- Add to Cart Section -->
                <div class="cart-section">
                  <div class="quantity-selector">
                    <label>Quantité:</label>
                    <nz-input-number 
                      [(ngModel)]="quantity" 
                      [nzMin]="1" 
                      [nzMax]="product.quantity"
                      [nzStep]="1">
                    </nz-input-number>
                  </div>
                  
                  <div class="action-buttons">
                    <button nz-button 
                            nzType="primary" 
                            nzSize="large"
                            [disabled]="product.quantity === 0 || addingToCart"
                            (click)="addToCart()"
                            style="flex: 1">
                      <span *ngIf="!addingToCart" nz-icon nzType="shopping-cart"></span>
                      <span>{{ addingToCart ? 'Ajout en cours...' : 'Ajouter au panier' }}</span>
                    </button>
                    
                    <button nz-button 
                            nzType="default" 
                            nzSize="large"
                            (click)="toggleWishlist()">
                      <span *ngIf="!inWishlist" nz-icon nzType="heart"></span>
                      <span *ngIf="inWishlist" nz-icon nzType="heart" nzTheme="fill" style="color: #f5222d"></span>
                      {{ inWishlist ? 'Dans favoris' : 'Ajouter aux favoris' }}
                    </button>
                  </div>
                </div>

                <!-- Specifications -->
                <div class="specifications" *ngIf="product.specifications">
                  <h3>Spécifications</h3>
                  <p>{{ product.specifications }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Reviews Section -->
          <div class="reviews-section" style="margin-top: 48px">
            <h2>Avis clients ({{ product.reviewCount }})</h2>
            
            <div *ngIf="product.reviews && product.reviews.length > 0">
              <div *ngFor="let review of product.reviews" class="review-item">
                <div class="review-header">
                  <strong>{{ review.userName }}</strong>
                  <nz-rate [ngModel]="review.rating" nzDisabled [nzCount]="5" nzAllowHalf></nz-rate>
                </div>
                <h4>{{ review.title }}</h4>
                <p>{{ review.comment }}</p>
                <small style="color: #999">{{ review.createdAt | date:'short':'':'fr-FR' }}</small>
              </div>
            </div>

            <nz-empty 
              *ngIf="!product.reviews || product.reviews.length === 0"
              nzNotFoundContent="Aucun avis pour ce produit">
            </nz-empty>
          </div>
        </div>

        <nz-empty 
          *ngIf="!loading && !product"
          nzNotFoundContent="Produit non trouvé">
        </nz-empty>
      </nz-spin>
    </div>
  `,
  styles: [`
    .detail-container {
      padding: 24px;
    }

    .product-detail {
      background: white;
      border-radius: 8px;
      padding: 24px;
    }

    .image-section {
      height: 500px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f5f5;
      border-radius: 8px;
      overflow: hidden;
    }

    .product-image {
      width: 100%;
      height: 100%;
      object-fit: contain;
    }

    .info-section {
      padding: 20px 0;
    }

    .category-tag {
      display: inline-block;
      background: #f0f0f0;
      padding: 4px 12px;
      border-radius: 4px;
      font-size: 12px;
      color: #666;
      margin-bottom: 12px;
    }

    .product-title {
      font-size: 28px;
      font-weight: 600;
      margin: 12px 0 16px;
    }

    .rating-section {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 16px;
    }

    .rating-text {
      color: #666;
      font-size: 14px;
    }

    .price-section {
      display: flex;
      align-items: center;
      gap: 16px;
      margin: 16px 0;
    }

    .price {
      font-size: 32px;
      font-weight: 700;
      color: #f5222d;
    }

    .stock-section {
      padding: 12px;
      background: #f6f8fb;
      border-left: 4px solid #52c41a;
      border-radius: 4px;
      margin: 16px 0;
    }

    .stock-section.stock-low {
      border-left-color: #faad14;
    }

    .stock-section.stock-empty {
      border-left-color: #f5222d;
    }

    .description {
      margin: 24px 0;
      font-size: 14px;
      line-height: 1.6;
      color: #333;
    }

    .cart-section {
      border-top: 1px solid #eee;
      padding-top: 20px;
      margin-top: 24px;
    }

    .quantity-selector {
      margin-bottom: 16px;
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .action-buttons {
      display: flex;
      gap: 12px;
    }

    .specifications {
      margin-top: 32px;
      padding: 20px;
      background: #fafafa;
      border-radius: 8px;
    }

    .specifications h3 {
      margin-top: 0;
      margin-bottom: 12px;
    }

    .reviews-section {
      border-top: 1px solid #eee;
      padding-top: 24px;
    }

    .review-item {
      padding: 16px;
      border: 1px solid #eee;
      border-radius: 8px;
      margin-bottom: 12px;
    }

    .review-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;
    }

    .review-item h4 {
      margin: 8px 0;
      font-size: 14px;
      font-weight: 600;
    }

    .review-item p {
      margin: 8px 0;
      font-size: 13px;
      color: #666;
    }
  `]
})
export class ProductDetailComponent implements OnInit {
  productId!: number;
  product: ProductDetail | null = null;
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
      next: (product) => {
        this.product = product;
        this.loading = false;
      },
      error: () => {
        this.message.error('Produit non trouvé');
        this.loading = false;
        this.router.navigate(['/']);
      }
    });
  }

  addToCart(): void {
    if (!this.product || this.product.quantity === 0) return;

    this.addingToCart = true;
    this.cartService.addToCart({ productId: this.productId, quantity: this.quantity }).subscribe({
      next: () => {
        this.message.success(`${this.product?.name} ajouté au panier!`);
        this.quantity = 1;
        this.addingToCart = false;
      },
      error: () => {
        this.message.error('Erreur lors de l\'ajout au panier');
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
