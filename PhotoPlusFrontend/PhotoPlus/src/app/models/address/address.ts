import { Link } from "../link/link";

// TODO delete unnecessarry fields and types
export class Address {
  code?: string;
  street: string;
  number: string;
  zipCode: string;
  city: string;
  country?: string;
  userCode: string;
  links?: Link[];
}
