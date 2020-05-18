import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatDividerModule } from '@angular/material/divider';
import { MatPaginatorIntl, MatPaginatorModule } from '@angular/material/paginator';
import { AppComponent } from './app.component';
import { ProductsComponent } from './components/products/products.component';
import { HomeComponent } from './components/home/home.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthInterceptor } from './interceptors/auth-interceptor';
import { CartComponent } from './components/cart/cart.component';
import { ErrorModalComponent } from './components/error-modal/error-modal.component';
import { SuccessModalComponent } from './components/success-modal/success-modal.component';
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
import { ManageMultipleOrdersComponent } from './components/admin-panel/subcomponents/manage-multiple-orders/manage-multiple-orders.component';
import { ManageProductComponent } from './components/admin-panel/subcomponents/manage-product/manage-product.component';
import { DeleteUsersComponent } from './components/admin-panel/subcomponents/delete-users/delete-users.component';
import { GenerateReportComponent } from './components/admin-panel/subcomponents/generate-report/generate-report.component';
import { ChangePrivilegesComponent } from './components/admin-panel/subcomponents/change-privileges/change-privileges.component';
import { AddDeliveryComponent } from './components/admin-panel/subcomponents/add-delivery/add-delivery.component';
import { ManageSingleOrderComponent } from './components/admin-panel/subcomponents/manage-single-order/manage-single-order.component';
import { ImageCarouselComponent } from './components/admin-panel/subcomponents/manage-product/image-carousel/image-carousel.component';
import { ImageDisplayComponent } from './components/image-display/image-display.component';
import { PageSelectComponent } from './components/page-select/page-select.component';
import { CustomMatPaginatorIntl } from './helpers/custom-paginator-intl';
import { AboutComponent } from './components/about/about.component';
import { MatSelectModule } from '@angular/material/select';
import { UserDetailsComponent } from "./components/user-details/user-details.component";
import { UserDataComponent } from "./components/user-details/user-data/user-data.component";
import { UserAddressComponent } from "./components/user-details/user-address/user-address.component";
import { SearchComponent } from './components/search/search.component';
import { ProductCardComponent } from './components/product-card/product-card.component';
import { UserShowOrdersComponent } from './components/user-show-orders/user-show-orders.component';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ProductsComponent,
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
    ManageMultipleOrdersComponent,
    ManageProductComponent,
    DeleteUsersComponent,
    GenerateReportComponent,
    ChangePrivilegesComponent,
    AddDeliveryComponent,
    ManageSingleOrderComponent,
    ImageCarouselComponent,
    ImageDisplayComponent,
    PageSelectComponent,
    AboutComponent,
    UserDetailsComponent,
    UserDataComponent,
    UserAddressComponent,
    SearchComponent,
    ProductCardComponent,
    UserShowOrdersComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatCardModule,
    MatDividerModule,
    MatExpansionModule,
    MatPaginatorModule,
    NgbModule,
    FormsModule,
    MatSelectModule
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
    },
    {
      provide: MatPaginatorIntl,
      useClass: CustomMatPaginatorIntl
    }
  ],
  bootstrap: [AppComponent]

})
export class AppModule {
}
