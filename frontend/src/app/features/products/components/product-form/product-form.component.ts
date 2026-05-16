import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { CATEGORIES, Product, ProductRequest, STATUSES } from '../../../../core/models/product.model';

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
export class ProductFormComponent implements OnChanges {
  @Input() visible = false;
  @Input() product: Product | null = null;
  @Input() loading = false;
  @Output() save = new EventEmitter<ProductRequest>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;
  categories = CATEGORIES;
  statuses = STATUSES;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', Validators.maxLength(500)],
      price: [null, [Validators.required, Validators.min(0.01)]],
      quantity: [0, [Validators.required, Validators.min(0)]],
      category: ['', Validators.required],
      status: ['ACTIVE', Validators.required]
    });
  }

  ngOnChanges(): void {
    if (this.product) {
      this.form.patchValue(this.product);
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
