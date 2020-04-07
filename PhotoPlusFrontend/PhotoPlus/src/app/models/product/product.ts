import { Link } from '../link/link';

export class Product {
  code: number;
  name: string;
  price: number;
  description: string;
  links: Link[]
  category: string;
  imagesUrl: string[];
}
