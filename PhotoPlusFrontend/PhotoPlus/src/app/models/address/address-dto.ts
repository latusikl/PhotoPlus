import {Link} from "../link/link";
import {AddressModel} from "./address-model";

export interface AddressDto extends AddressModel {

    code: string;
    userCode: string;
    links: Link[];

}
