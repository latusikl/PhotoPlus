import {User} from "./user";
import {Link} from "../link/link";

export class UserModel extends  User
{
    public links: Link[];
    public role: String;
    public code: String;
}