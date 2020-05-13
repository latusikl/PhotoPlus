import { Product } from '../product/product';
import { Address } from '../address/address';
import { OrderItem } from '../orderItem/order-item';

export class Order {
  userCode: string;
  orderStatus: string;
  paymentMethod: string;
  price: number;
  date: string;
  addressCode: number;
  orderItems: OrderItem[]
  address: Address;
}
