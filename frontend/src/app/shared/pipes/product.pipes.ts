import { Pipe, PipeTransform } from '@angular/core';
import { Product } from '../../core/models/product.model';

@Pipe({ name: 'activeCount', standalone: true, pure: false })
export class ActiveCountPipe implements PipeTransform {
  transform(products: Product[]): number {
    return products?.filter(p => p.status === 'ACTIVE').length ?? 0;
  }
}

@Pipe({ name: 'lowStockCount', standalone: true, pure: false })
export class LowStockCountPipe implements PipeTransform {
  transform(products: Product[]): number {
    return products?.filter(p => p.quantity < 10).length ?? 0;
  }
}
