import { PaymentMethod } from '../payment-method/payment-method';

export class Order{
  code?: string;
  userCode: string;
  addressCode: string;
  orderStatus: string;
  paymentMethod: PaymentMethod;
  date: Date;
  price: number;
}