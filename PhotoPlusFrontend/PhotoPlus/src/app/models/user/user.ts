import { Role } from '../role/role.enum';

export class User {
    login: string;
    password: string;
    name: string;
    surname: string;
    email: string;
    code: string;
    number: string; /* Phone number */
    role: Role;
}