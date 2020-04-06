import { Link } from './link';

export class Product {
  code: number;
  name: string;
  price: number;
  description: string;
  links: Link[]
  category: string;
  imagesUrl: string[];
}
