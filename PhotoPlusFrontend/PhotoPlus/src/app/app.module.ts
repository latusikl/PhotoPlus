import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { MatCardModule } from "@angular/material/card";

import {AppComponent} from './app.component';
import {HomeComponent} from './components/home/home.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {LoginComponent} from './components/login/login.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {AuthInterceptor} from "./interceptors/auth-interceptor";
import {CartComponent} from './components/cart/cart.component';
import {ErrorModalComponent} from './components/error-modal/error-modal.component';
import {SuccessModalComponent} from './components/success-modal/success-modal.component';
import { ErrorInterceptor } from './interceptors/error-interceptor';
import { CategoryComponent } from './components/category/category.component';
import { ForumComponent } from './components/forum/forum.component';
import { SectionComponent } from './components/forum/section/header/section-header.component';
import { SectionBodyComponent } from './components/forum/section/body/section-body.component';
import { SectionEditComponent } from './components/forum/section/edit/section-edit.component';
import { SectionRemoveComponent } from './components/forum/section/remove/section-remove.component';
import { SectionAddComponent } from './components/forum/section/add/section-add.component';
import { TopicBodyComponent } from './components/forum/section/body/topic/body/topic-body.component';
import { TopicHeaderComponent } from './components/forum/section/body/topic/header/topic-header.component';
import { TopicAddComponent } from './components/forum/section/body/topic/add/topic-add.component';
import { TopicEditComponent } from './components/forum/section/body/topic/edit/topic-edit.component';
import { TopicRemoveComponent } from './components/forum/section/body/topic/remove/topic-remove.component';
import { PostComponent } from './components/forum/section/body/topic/body/post/post.component';
import { ProductComponent } from './components/product/product.component';
import { OrderComponent } from './components/order/order.component';
import { AdminPanelComponent } from './components/admin-panel/admin-panel.component';
import { ManageOrdersComponent } from './components/admin-panel/subcomponents/manage-orders/manage-orders.component';
import { CreateNewProductComponent } from './components/admin-panel/subcomponents/create-new-product/create-new-product.component';

@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        RegistrationComponent,
        LoginComponent,
        CartComponent,
        ErrorModalComponent,
        SuccessModalComponent,
        CategoryComponent,
        ForumComponent,
        SectionComponent,
        SectionBodyComponent,
        SectionEditComponent,
        SectionRemoveComponent,
        SectionAddComponent,
        TopicBodyComponent,
        TopicHeaderComponent,
        TopicAddComponent,
        TopicEditComponent,
        TopicRemoveComponent,
        PostComponent,
        ProductComponent,
        OrderComponent,
        AdminPanelComponent,
        ManageOrdersComponent,
        CreateNewProductComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
        MatCardModule,
        NgbModule
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: ErrorInterceptor,
          multi: true
      }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
