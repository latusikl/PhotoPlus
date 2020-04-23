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
import { SectionComponent } from './components/section/section.component';
import { SectionBodyComponent } from './components/section-body/section-body.component';

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
        SectionBodyComponent
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
