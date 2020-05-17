import { Role } from '../role/role.enum';
import { Link } from '../link/link';

export class User {
    login: string;
    password: string;
    name: string;
    surname: string;
    email: string;
    code: string;
    number: string; /* Phone number */
    role: Role;
    links?: Link[];
}
