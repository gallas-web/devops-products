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
    FcfaPipe
  ],
  template: `
    <div class="detail-container">
      <!-- Breadcrumb -->
      <nz-breadcrumb style="margin-bottom: 24px">
        <nz-breadcrumb-item>
          <a href="/" style="color: #1890ff">Accueil</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a *ngIf="product" [href]="'/shop?category=' + product.category.id" style="color: #1890ff">
            {{ product.category.name }}
          </a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>{{ product?.name || 'Chargement...' }}</nz-breadcrumb-item>
      </nz-breadcrumb>

      <nz-spin [nzSpinning]="loading">
        <div *ngIf="product && !loading" class="product-detail">
          <div nz-row [nzGutter]="32">
            <!-- Image Section -->
            <div nz-col [nzXs]="24" [nzMd]="12" [nzLg]="10">
              <div class="image-section">
                <img
                  [src]="getProductImage(product)"
                  [alt]="product.name"
                  class="product-image"
                  (error)="onImageError($event)"
                />
              </div>
            </div>

            <!-- Info Section -->
            <div nz-col [nzXs]="24" [nzMd]="12" [nzLg]="14">
              <div class="info-section">
                <span class="category-tag">{{ product.category.name }}</span>

                <h1 class="product-title">{{ product.name }}</h1>

                <div class="rating-section">
                  <nz-rate [ngModel]="product.rating" nzDisabled [nzCount]="5"></nz-rate>
                  <span class="rating-text">({{ product.reviewCount }} avis)</span>
                </div>

                <!-- Price -->
                <div class="price-section">
                  <span class="price">{{ product.price | fcfa }}</span>
                  <nz-tag *ngIf="product.status === 'ACTIVE'" nzColor="green">Disponible</nz-tag>
                  <nz-tag *ngIf="product.status === 'INACTIVE'" nzColor="red">Indisponible</nz-tag>
                </div>

                <!-- Stock Status -->
                <div
                  class="stock-section"
                  [ngClass]="{
                    'stock-low': product.quantity < 10,
                    'stock-empty': product.quantity === 0
                  }"
                >
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

                <div class="description">
                  <p>{{ product.description }}</p>
                </div>

                <div class="cart-section">
                  <div class="quantity-selector">
                    <label>Quantité:</label>
                    <nz-input-number
                      [(ngModel)]="quantity"
                      [nzMin]="1"
                      [nzMax]="product.quantity"
                      [nzStep]="1"
                    ></nz-input-number>
                  </div>

                  <div class="action-buttons">
                    <button
                      nz-button
                      nzType="primary"
                      nzSize="large"
                      [disabled]="product.quantity === 0 || addingToCart"
                      (click)="addToCart()"
                      style="flex: 1"
                    >
                      <span *ngIf="!addingToCart" nz-icon nzType="shopping-cart"></span>
                      <span>{{ addingToCart ? 'Ajout en cours...' : 'Ajouter au panier' }}</span>
                    </button>

                    <button nz-button nzType="default" nzSize="large" (click)="toggleWishlist()">
                      <span *ngIf="!inWishlist" nz-icon nzType="heart"></span>
                      <span
                        *ngIf="inWishlist"
                        nz-icon
                        nzType="heart"
                        nzTheme="fill"
                        style="color: #f5222d"
                      ></span>
                      {{ inWishlist ? 'Dans favoris' : 'Ajouter aux favoris' }}
                    </button>
                  </div>
                </div>

                <div class="specifications" *ngIf="product.specifications">
                  <h3>Spécifications</h3>
                  <p>{{ product.specifications }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Related products (same category) -->
          <div class="related-section" *ngIf="product" style="margin-top: 56px">
            <div class="related-head">
              <h2>Produits de la même catégorie</h2>
              <span class="related-subtitle">Suggestions basées sur : {{ product.category.name }}</span>
            </div>

            <div class="related-grid" *ngIf="relatedProducts?.length; else noRelated">
              <article
                class="related-card"
                *ngFor="let p of relatedProducts"
                (click)="viewProductDetail(p.id)"
              >
                <div class="related-image">
                  <img [src]="getProductCardImage(p)" [alt]="p.name" />
                </div>
                <div class="related-body">
                  <div class="related-meta">

                    <span class="related-category">{{ p.category.name }}</span>
                    <span class="related-stock">{{ p.quantity }} en stock</span>
                  </div>
                  <h3 class="related-title">{{ p.name }}</h3>
                  <div class="related-footer">
                    <span class="related-price">{{ p.price | fcfa }}</span>
                    <button nz-button nzType="primary" nzSize="small" (click)="$event.stopPropagation(); addFromRelated(p)" [disabled]="p.quantity === 0">
                      <span nz-icon nzType="shopping-cart"></span>
                      Ajouter
                    </button>
                  </div>
                </div>
              </article>
            </div>

            <ng-template #noRelated>
              <nz-empty nzNotFoundContent="Aucun produit similaire pour le moment"></nz-empty>
            </ng-template>
          </div>

          <!-- Reviews Section -->
          <div class="reviews-section" style="margin-top: 48px">

            <h2>Avis clients ({{ product.reviewCount }})</h2>


            <div *ngIf="product.reviews && product.reviews.length > 0">
              <div *ngFor="let review of product.reviews" class="review-item">
                <div class="review-header">
                  <strong>{{ review.userName }}</strong>
                  <nz-rate
                    [ngModel]="review.rating"
                    nzDisabled
                    [nzCount]="5"
                    nzAllowHalf
                  ></nz-rate>
                </div>
                <h4>{{ review.title }}</h4>
                <p>{{ review.comment }}</p>
                <small style="color: #999">{{ review.createdAt | date:'short':'':'fr-FR' }}</small>
              </div>
            </div>

            <nz-empty
              *ngIf="!product.reviews || product.reviews.length === 0"
              nzNotFoundContent="Aucun avis pour ce produit"
            ></nz-empty>
          </div>
        </div>

        <nz-empty *ngIf="!loading && !product" nzNotFoundContent="Produit non trouvé"></nz-empty>
      </nz-spin>
    </div>
  `,
  styles: [
    `
    .detail-container {
  padding: 32px;
  background: #f5f7fb;
  min-height: 100vh;
}

/* =========================
   CARD PRINCIPALE
========================= */
.product-detail {
  background: #fff;
  border-radius: 18px;
  padding: 28px;
  box-shadow: 0 12px 40px rgba(15, 23, 42, 0.08);
  border: 1px solid rgba(15, 118, 110, 0.08);
}

/* =========================
   IMAGE PRODUIT
========================= */
.image-section {
  height: 520px;
  background: linear-gradient(135deg, #eef2f7, #f8fafc);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  padding: 24px;
  transition: transform 0.4s ease;
}

.image-section:hover .product-image {
  transform: scale(1.05);
}

/* =========================
   INFO SECTION
========================= */
.info-section {
  padding: 10px 20px;
}

.category-tag {
  display: inline-flex;
  padding: 6px 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 800;
  background: #ecfdf3;
  color: #0f766e;
  letter-spacing: 0.4px;
}

.product-title {
  font-size: 34px;
  font-weight: 900;
  margin: 14px 0;
  color: #0f172a;
  line-height: 1.2;
}

/* =========================
   RATING
========================= */
.rating-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 14px;
}

.rating-text {
  color: #64748b;
  font-weight: 600;
}

/* =========================
   PRICE
========================= */
.price-section {
  display: flex;
  align-items: center;
  gap: 14px;
  margin: 18px 0;
}

.price {
  font-size: 38px;
  font-weight: 900;
  color: #0f766e;
  letter-spacing: -0.5px;
}

/* =========================
   STOCK CARD
========================= */
.stock-section {
  padding: 14px 16px;
  border-radius: 12px;
  background: #f8fafc;
  border-left: 5px solid #22c55e;
  margin: 16px 0;
  font-weight: 700;
}

.stock-section.stock-low {
  border-left-color: #f59e0b;
}

.stock-section.stock-empty {
  border-left-color: #ef4444;
}

/* =========================
   DESCRIPTION
========================= */
.description {
  margin: 22px 0;
  font-size: 15px;
  line-height: 1.7;
  color: #475569;
}

/* =========================
   CART SECTION
========================= */
.cart-section {
  margin-top: 26px;
  padding-top: 20px;
  border-top: 1px solid #e2e8f0;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 14px;
}

/* =========================
   BUTTONS PREMIUM
========================= */
.action-buttons {
  display: flex;
  gap: 12px;
}

.action-buttons button {
  border-radius: 12px;
  font-weight: 800;
  transition: all 0.25s ease;
}

.action-buttons button[nzType="primary"] {
  background: #0f766e;
  border-color: #0f766e;
  box-shadow: 0 10px 20px rgba(15, 118, 110, 0.25);
}

.action-buttons button[nzType="primary"]:hover {
  background: #115e59;
  transform: translateY(-2px);
}

.action-buttons button[nzType="default"]:hover {
  transform: translateY(-2px);
}

/* =========================
   SPECIFICATIONS
========================= */
.specifications {
  margin-top: 34px;
  padding: 18px;
  background: #f8fafc;
  border-radius: 14px;
  border: 1px solid #e2e8f0;
}

.specifications h3 {
  margin-bottom: 10px;
  font-weight: 900;
}

/* =========================
   RELATED PRODUCTS
========================= */
.related-section {
  margin-top: 60px;
}

.related-head h2 {
  font-size: 26px;
  font-weight: 900;
  margin-bottom: 4px;
}

.related-subtitle {
  color: #64748b;
  font-weight: 600;
}

/* GRID */
.related-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(210px, 1fr));
  gap: 16px;
  margin-top: 18px;
}

/* CARD */
.related-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
  transition: all 0.25s ease;
  cursor: pointer;
}

.related-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.12);
}

.related-image {
  height: 160px;
  background: #f1f5f9;
}

.related-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  padding: 12px;
}

.related-body {
  padding: 12px;
}

.related-meta {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  font-weight: 800;
  color: #0f766e;
}

.related-title {
  font-size: 15px;
  font-weight: 900;
  margin: 8px 0;
}

.related-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.related-price {
  font-weight: 900;
  color: #0f766e;
}

/* =========================
   REVIEWS
========================= */
.reviews-section {
  margin-top: 60px;
}

.review-item {
  background: #fff;
  border: 1px solid #e2e8f0;
  padding: 16px;
  border-radius: 14px;
  margin-bottom: 12px;
  transition: 0.2s;
}

.review-item:hover {
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.08);
}

.review-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

/* =========================
   RESPONSIVE
========================= */
@media (max-width: 900px) {
  .product-detail {
    padding: 16px;
  }

  .image-section {
    height: 380px;
  }

  .action-buttons {
    flex-direction: column;
  }
}
    `
  ]
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
    // Réutilise la même logique que l'image principale mais avec une taille légèrement différente.
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

    // On réutilise le même endpoint que la boutique via un filtre categoryId.
    // On exclut le produit courant côté client pour garder une liste pertinente.
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


