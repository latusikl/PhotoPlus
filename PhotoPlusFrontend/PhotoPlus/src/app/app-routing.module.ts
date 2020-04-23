import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { CartComponent } from './components/cart/cart.component';
import { ForumComponent } from './components/forum/forum.component';
import { SectionBodyComponent } from './components/section/body/section-body.component';
import { SectionEditComponent } from './components/section/edit/section-edit.component';
import { SectionRemoveComponent } from './components/section/remove/section-remove.component';


const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  { path: 'cart', component: CartComponent},
  { path: 'forum', component: ForumComponent},
  { path: 'forum/section/:code', component: SectionBodyComponent},
  { path: 'forum/section/edit/:code', component: SectionEditComponent}, // TODO
  { path: 'forum/section/remove/:code', component: SectionRemoveComponent} // TODO
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
