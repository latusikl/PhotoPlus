import { Link } from '../link/link';

export class Product {
  code: string;
  name: string;
  price: number;
  storeQuantity: number;
  description: string;
  links: Link[]
  category: string;
  imagesUrl: string[];
}
