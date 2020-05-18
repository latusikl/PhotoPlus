import { Component, OnInit, ViewChild, ComponentFactoryResolver, ViewContainerRef, Renderer2 } from '@angular/core';
import { LoginService } from 'src/app/services/login/login.service';
import { ActivatedRoute, UrlSegment, Router } from '@angular/router';
import { ManageMultipleOrdersComponent } from './subcomponents/manage-multiple-orders/manage-multiple-orders.component';
import { ManageProductComponent } from './subcomponents/manage-product/manage-product.component';
import { GenerateReportComponent } from './subcomponents/generate-report/generate-report.component';
import { AddDeliveryComponent } from './subcomponents/add-delivery/add-delivery.component';
import { ChangePrivilegesComponent } from './subcomponents/change-privileges/change-privileges.component';
import { DeleteUsersComponent } from './subcomponents/delete-users/delete-users.component';
import { ManageSingleOrderComponent } from './subcomponents/manage-single-order/manage-single-order.component';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent implements OnInit {

  @ViewChild('anchor', {static: true, read: ViewContainerRef})
  displayElement: ViewContainerRef;

  constructor(private loginService: LoginService,
              private activatedRoute: ActivatedRoute, private router: Router,
              private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit(): void {
    this.activatedRoute.url.subscribe(urlSegments => {
      this.mountSubcomponentView(urlSegments);
    });
  }

  mountSubcomponentView(urlSegment: UrlSegment[]) {
    // User właśnie wszedł na panel, można zmienić który komponent ma się domyślnie ładować
    if (!urlSegment[1]) {
      return;
    }
    switch (urlSegment[1].path) {
      case 'orders': {
        if (!urlSegment[2]) {
          this.mountMultipleOrdersComponent();
          break;
        } else {
          this.mountSingleOrderComponent();
          break;
        }
      }
      case 'manageProducts': {
        this.mountManageProductsComponent();
        break;
      }
      case 'delivery': {
        this.mountAddDeliveryComponent();
        break;
      }
      case 'generateReports': {
        this.mountGenerateReportsComponent();
        break;
      }
      case 'changePrivileges': {
        this.mountChangePrivilegesComponent();
        break;
      }
      case 'deleteUser': {
        this.mountDeleteUserComponent();
        break;
      }
    }
  }

  mountMultipleOrdersComponent() {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ManageMultipleOrdersComponent);
    this.displayElement.createComponent(componentFactory, 0);
  }

  mountSingleOrderComponent() {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ManageSingleOrderComponent);
    this.displayElement.createComponent(componentFactory, 0);
  }

  mountManageProductsComponent() {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ManageProductComponent);
    this.displayElement.createComponent(componentFactory, 0);
  }

  mountAddDeliveryComponent() {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(AddDeliveryComponent);
    this.displayElement.createComponent(componentFactory, 0);
  }

  mountChangePrivilegesComponent() {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ChangePrivilegesComponent);
    this.displayElement.createComponent(componentFactory, 0);
  }

  mountDeleteUserComponent() {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(DeleteUsersComponent);
    this.displayElement.createComponent(componentFactory, 0);
  }

  mountGenerateReportsComponent() {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(GenerateReportComponent);
    this.displayElement.createComponent(componentFactory, 0);
  }



  navigate(url: string) {
    this.router.navigate([url]);
  }

  get auth(): LoginService {
    return this.loginService;
  }

}
