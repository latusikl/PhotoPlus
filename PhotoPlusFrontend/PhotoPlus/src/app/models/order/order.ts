import { Product } from '../product/product';

export class Order {
  userCode: number;
  orderStatus: string;
  paymentMethod: string;
  price: number;
  date: string;
  addressCode: number;
  orderItems: [{
    orderCode: string;
    productCode: number;
    quantity: number;
  }]
  address: {
    street: string;
    number: number;
    zipCode: string;
    city: string;
    countryCode: string;
    userCode: number;

  }
}
