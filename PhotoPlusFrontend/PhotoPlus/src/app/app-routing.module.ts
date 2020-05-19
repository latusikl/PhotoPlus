import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ProductsComponent } from './components/products/products.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { CartComponent } from './components/cart/cart.component';
import { CategoryComponent } from './components/category/category.component';
import { ForumComponent } from './components/forum/forum.component';
import { SectionBodyComponent } from './components/forum/section/body/section-body.component';
import { SectionEditComponent } from './components/forum/section/edit/section-edit.component';
import { SectionRemoveComponent } from './components/forum/section/remove/section-remove.component';
import { SectionAddComponent } from './components/forum/section/add/section-add.component';
import { TopicBodyComponent } from './components/forum/section/body/topic/body/topic-body.component';
import { TopicEditComponent } from './components/forum/section/body/topic/edit/topic-edit.component';
import { TopicRemoveComponent } from './components/forum/section/body/topic/remove/topic-remove.component';
import { TopicAddComponent } from './components/forum/section/body/topic/add/topic-add.component';
import { ProductComponent } from './components/product/product.component';
import { OrderComponent } from './components/order/order.component';
import { AdminPanelComponent } from './components/admin-panel/admin-panel.component';
import { HomeComponent } from './components/home/home.component';
import { ImageDisplayComponent } from './components/image-display/image-display.component';
import { AboutComponent } from './components/about/about.component';
import { UserDetailsComponent } from "./components/user-details/user-details.component";
import { SearchComponent } from './components/search/search.component';
import {UserShowOrdersComponent} from "./components/user-show-orders/user-show-orders.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'products', component: ProductsComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  { path: 'cart', component: CartComponent },
  { path: 'category', component: CategoryComponent },
  { path: 'forum', component: ForumComponent },
  { path: 'product/:productCode', component: ProductComponent },
  { path: 'order', component: OrderComponent },
  { path: 'forum/section/:sectionCode', component: SectionBodyComponent },
  { path: 'forum/edit/section/:sectionCode', component: SectionEditComponent },
  { path: 'forum/remove/section/:sectionCode', component: SectionRemoveComponent },
  { path: 'forum/add/section', component: SectionAddComponent },
  { path: 'forum/topic/:topicCode', component: TopicBodyComponent },
  { path: 'forum/edit/topic/:topicCode', component: TopicEditComponent },
  { path: 'forum/remove/topic/:topicCode', component: TopicRemoveComponent },
  { path: 'forum/add/topic/:sectionCode', component: TopicAddComponent },
  { path: 'manage', component: AdminPanelComponent },
  { path: 'manage/orders', component: AdminPanelComponent },
  { path: 'manage/orders/:orderCode', component: AdminPanelComponent },
  { path: 'manage/manageProducts', component: AdminPanelComponent},
  { path: 'manage/delivery', component: AdminPanelComponent},
  { path: 'manage/changePrivileges', component: AdminPanelComponent},
  { path: 'manage/deleteUser', component: AdminPanelComponent},
  { path: 'manage/generateReports', component: AdminPanelComponent},
  { path: 'imageDisplay/:imageCode', component: ImageDisplayComponent},
  { path: 'about', component: AboutComponent},
  { path: 'user/details', component: UserDetailsComponent },
  { path: 'search/:searchedText', component: SearchComponent },
  { path: 'user/orders', component: UserShowOrdersComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
