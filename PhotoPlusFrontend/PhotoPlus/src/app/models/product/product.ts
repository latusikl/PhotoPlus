import { Link } from '../link/link';
import { Category } from '../category/category';

export class Product {
  code: string;
  name: string;
  category: Category;
  price: number;
  description: string;
  storeQuantity: number;
  links?: Link[]
  imagesUrl?: string[];
  imageCodes: string[];
  dataLinks?: Map<string, string>;
}
