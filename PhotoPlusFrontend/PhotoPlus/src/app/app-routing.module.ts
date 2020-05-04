import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './components/home/home.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {LoginComponent} from './components/login/login.component';
import {CartComponent} from './components/cart/cart.component';
import {ForumComponent} from './components/forum/forum.component';
import {SectionBodyComponent} from './components/forum/section/body/section-body.component';
import {SectionEditComponent} from './components/forum/section/edit/section-edit.component';
import {SectionRemoveComponent} from './components/forum/section/remove/section-remove.component';
import {SectionAddComponent} from './components/forum/section/add/section-add.component';
import {UserDetailsComponent} from "./components/user-details/user-details.component";


const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'login', component: LoginComponent},
    {path: 'cart', component: CartComponent},
    {path: 'user/details', component: UserDetailsComponent},
    {path: 'forum', component: ForumComponent},
    {path: 'forum/section/:code', component: SectionBodyComponent},
    {path: 'forum/edit/section/:code', component: SectionEditComponent},
    {path: 'forum/remove/section/:code', component: SectionRemoveComponent},
    {path: 'forum/add/section', component: SectionAddComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
