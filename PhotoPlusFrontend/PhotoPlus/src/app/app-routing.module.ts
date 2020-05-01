import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { CartComponent } from './components/cart/cart.component';
import { ForumComponent } from './components/forum/forum.component';
import { SectionBodyComponent } from './components/forum/section/body/section-body.component';
import { SectionEditComponent } from './components/forum/section/edit/section-edit.component';
import { SectionRemoveComponent } from './components/forum/section/remove/section-remove.component';
import { SectionAddComponent } from './components/forum/section/add/section-add.component';
import { TopicBodyComponent } from './components/forum/section/body/topic/body/topic-body.component';
import { TopicEditComponent } from './components/forum/section/body/topic/edit/topic-edit.component';
import { TopicRemoveComponent } from './components/forum/section/body/topic/remove/topic-remove.component';
import { TopicAddComponent } from './components/forum/section/body/topic/add/topic-add.component';


const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  { path: 'cart', component: CartComponent},
  { path: 'forum', component: ForumComponent},
  { path: 'forum/section/:sectionCode', component: SectionBodyComponent},
  { path: 'forum/edit/section/:sectionCode', component: SectionEditComponent},
  { path: 'forum/remove/section/:sectionCode', component: SectionRemoveComponent},
  { path: 'forum/add/section', component: SectionAddComponent},
  { path: 'forum/topic/:topicCode', component: TopicBodyComponent},
  { path: 'forum/edit/topic/:topicCode', component: TopicEditComponent },
  { path: 'forum/remove/topic/:topicCode', component: TopicRemoveComponent},
  { path: 'forum/add/topic/:sectionCode', component: TopicAddComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
