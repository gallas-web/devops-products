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

  constructor(private fb: FormBuilder, private productService: ProductService) {
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
    } else {
      this.form.reset({ status: 'ACTIVE', quantity: 0 });
    }
  }

  get isEditMode(): boolean {
    return !!this.product;
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
    this.cancel.emit();
  }
}
