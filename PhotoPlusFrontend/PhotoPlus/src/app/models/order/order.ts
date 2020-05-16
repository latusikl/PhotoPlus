import { PaymentMethod } from '../payment-method/payment-method';
import { OrderStatus } from '../order-status/order-status';

export class Order{
  code?: string;
  userCode: string;
  addressCode: string;
  orderStatus: OrderStatus;
  paymentMethod: PaymentMethod;
  date: Date;
  price: number;
}