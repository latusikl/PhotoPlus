import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';

import {ReactiveFormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
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
import { ForumComponent } from './components/forum/forum.component';
import { SectionComponent } from './components/forum/section/header/section-header.component';
import { SectionBodyComponent } from './components/forum/section/body/section-body.component';
import { SectionEditComponent } from './components/forum/section/edit/section-edit.component';
import { SectionRemoveComponent } from './components/forum/section/remove/section-remove.component';
import { SectionAddComponent } from './components/forum/section/add/section-add.component';
import { ProductComponent } from './components/product/product.component';
import { OrderComponent } from './components/order/order.component';

@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        RegistrationComponent,
        LoginComponent,
        CartComponent,
        ErrorModalComponent,
        SuccessModalComponent,
        ForumComponent,
        SectionComponent,
        SectionBodyComponent,
        SectionEditComponent,
        SectionRemoveComponent,
        SectionAddComponent,
        ProductComponent,
        OrderComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
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
