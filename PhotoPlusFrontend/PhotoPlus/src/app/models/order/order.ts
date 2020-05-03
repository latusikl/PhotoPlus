import { Product } from '../product/product';

export class Order {
        code: number;
        userCode: number;
        orderStatus: string;
        paymentMethod: string;
        price: number;
        orderItems: Product[];
}
