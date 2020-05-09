import { Link } from '../link/link';

export class Product {
  code: string;
  name: string;
  price: number;
  storeQuantity: number;
  description: string;
  quantity: number;
  links: Link[]
  category: string;
  imagesUrl: string[];
  imageCodes: string[];
  dataLinks: Map<string, string>;
}
