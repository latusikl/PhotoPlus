import { Product } from '../product/product';

export class OrderItem {
    orderCode: string;
    productCode: string;
    product: Product;
    quantity: number;
}
