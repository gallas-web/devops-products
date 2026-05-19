/* eslint-disable @angular-eslint/prefer-inject */
/* eslint-disable @angular-eslint/no-output-native */
import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzMessageService } from 'ng-zorro-antd/message';
import { Category, Product, ProductRequest, STATUSES } from '../../../../core/models/product.model';
import { ProductService } from '../../../../core/services/product.service';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule,
    NzFormModule, NzInputModule, NzInputNumberModule,
    NzSelectModule, NzButtonModule, NzModalModule, NzSpinModule
  ],
  templateUrl: './product-form.component.html'
})
export class ProductFormComponent implements OnChanges, OnInit {
  @Input() visible = false;
  @Input() product: Product | null = null;
  @Input() loading = false;
  @Output() save = new EventEmitter<ProductRequest>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;
  categories: Category[] = [];
  statuses = STATUSES;
  imagePreview: string | null = null;
  uploadingImage = false;

  constructor(
    private fb: FormBuilder, 
    private productService: ProductService,
    private message: NzMessageService
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', Validators.maxLength(2000)],
      price: [null, [Validators.required, Validators.min(0.01)]],
      quantity: [0, [Validators.required, Validators.min(0)]],
      categoryId: [null, Validators.required],
      imageUrl: [''],
      specifications: [''],
      status: ['ACTIVE', Validators.required]
    });
  }

  ngOnInit(): void {
    this.productService.getCategories().subscribe(categories => this.categories = categories);
  }

  ngOnChanges(): void {
    if (this.product) {
      this.form.patchValue({
        ...this.product,
        categoryId: this.product.category?.id
      });
      this.imagePreview = this.product.imageUrl;
    } else {
      this.form.reset({ status: 'ACTIVE', quantity: 0 });
      this.imagePreview = null;
    }
  }

  get isEditMode(): boolean {
    return !!this.product;
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const files = input.files;

    if (files && files.length > 0) {
      const file = files[0];

      // Valider le type de fichier
      if (!file.type.match(/image\/(jpg|jpeg|png|gif|webp)/)) {
        this.message.error('Veuillez sélectionner une image valide (JPG, PNG, GIF, WebP)');
        return;
      }

      // Valider la taille (max 5MB)
      const maxSize = 5 * 1024 * 1024;
      if (file.size > maxSize) {
        this.message.error('La taille de l\'image doit être inférieure à 5MB');
        return;
      }

      // Afficher l'aperçu
      const reader = new FileReader();
      reader.onload = (e) => {
        this.imagePreview = e.target?.result as string;
      };
      reader.readAsDataURL(file);

      // Télécharger l'image
      this.uploadImage(file);
    }
  }

  uploadImage(file: File): void {
    this.uploadingImage = true;
    this.productService.uploadImage(file).subscribe({
      next: (response) => {
        this.form.patchValue({ imageUrl: response.url });
        this.message.success('Image téléchargée avec succès');
        this.uploadingImage = false;
      },
      error: (error) => {
        this.message.error('Erreur lors du téléchargement de l\'image');
        console.error('Upload error:', error);
        this.uploadingImage = false;
      }
    });
  }

  removeImage(): void {
    this.imagePreview = null;
    this.form.patchValue({ imageUrl: '' });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.save.emit(this.form.value as ProductRequest);
    } else {
      Object.values(this.form.controls).forEach(c => {
        c.markAsDirty();
        c.updateValueAndValidity({ onlySelf: true });
      });
    }
  }

  onCancel(): void {
    this.form.reset({ status: 'ACTIVE', quantity: 0 });
    this.imagePreview = null;
    this.cancel.emit();
  }
}
