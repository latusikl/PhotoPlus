export enum OrderStatus{
  PENDING = 'pending', 
  PAID = 'paid',
  READY_TO_SHIP = 'ready_to_ship',
  SHIPPED = 'shipped',
  DELIVERED = 'delivered'
}

export type SingleOrderStatus = keyof typeof OrderStatus;
