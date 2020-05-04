import { Product } from '../product/product';

export class Order {
        userCode: number;
        orderStatus: string;
        paymentMethod: string;
        price: number;
        date: string;
        orderItems:[{
          orderCode: string;
          productCode: number,
          quantity: number
        }]
}
