import {Link} from "../link/link";
import {CountryEnum} from "./country.enum";

export class Address {

    code : string;
    userCode: string;
    links : Link[];

    street: string;
    number: string;
    zipCode: string;
    city: string;
    country: CountryEnum;

}
