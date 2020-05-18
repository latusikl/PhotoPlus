import { PaymentMethod } from '../payment-method/payment-method';
import { OrderStatus } from '../order-status/order-status';
import { OrderItem } from '../orderItem/order-item';
import { Address } from '../address/address';

// TODO fix types and delete unnecessary fields
export class Order{
  code?: string;
  userCode: string;
  addressCode: string;
  orderStatus: OrderStatus | string;
  paymentMethod: PaymentMethod | string;
  date: Date | string;
  price: number;
  orderItems?: OrderItem[]
  address?: Address;

}

