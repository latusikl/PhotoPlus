import { Role } from '../role/role.enum';

export class LoggedUser {
    code: string;
    login: string;
    role: Role;
}