import {CountryEnum} from "./country.enum";

export interface AddressModel {

    street: string;
    number: string;
    zipCode: string;
    city: string;
    country: CountryEnum;

}