import { Link } from '../link/link';

export class Product {
  code: string;
  name: string;
  category: string;
  price: number;
  description: string;
  storeQuantity: number;
  links?: Link[]
  imagesUrl?: string[];
  imageCodes: string[];
  dataLinks?: Map<string, string>;
}
