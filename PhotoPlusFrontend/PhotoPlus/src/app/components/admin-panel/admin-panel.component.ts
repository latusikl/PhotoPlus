import { Component, OnInit, ViewChild, ComponentFactoryResolver, ViewContainerRef, Renderer2 } from '@angular/core';
import { LoginService } from 'src/app/services/login/login.service';
import { ActivatedRoute, UrlSegment, Router } from '@angular/router';
import { ManageOrdersComponent } from './subcomponents/manage-orders/manage-orders.component';
import { CreateProductComponent } from './subcomponents/create-product/create-product.component';
import { GenerateReportComponent } from './subcomponents/generate-report/generate-report.component';
import { AddDeliveryComponent } from './subcomponents/add-delivery/add-delivery.component';
import { AddEmployeeComponent } from './subcomponents/add-employee/add-employee.component';
import { DeleteUsersComponent } from './subcomponents/delete-users/delete-users.component';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent implements OnInit {

  @ViewChild("anchor",{static: true, read: ViewContainerRef})
  displayElement: ViewContainerRef;

  constructor(private loginService: LoginService,
              private activatedRoute: ActivatedRoute,private router: Router,
              private componentFactoryResolver:ComponentFactoryResolver) { }

  ngOnInit(): void {
    this.activatedRoute.url.subscribe(urlSegment => {
      if(!urlSegment[1]){
        return;
      }
      switch(urlSegment[1].path){
        case 'orders':{
          this.mountOrdersComponent(); 
          break;
        }
        case 'newProduct':{
          this.mountNewProductComponent();
          break;
        }
        case 'delivery':{
          this.mountAddDeliveryComponent();
          break;
        }
        case 'generateReports':{
          this.mountGenerateReportsComponent();
          break;
        }
        case 'newEmployee':{
          this.mountNewEmployeeComponent();
          break;
        }
        case 'deleteUser':{
          this.mountDeleteUserComponent();
          break;
        }
      }
    })
  }

  mountOrdersComponent(){
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ManageOrdersComponent);
    this.displayElement.createComponent(componentFactory,0);
  }

  mountNewProductComponent(){
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(CreateProductComponent);
    this.displayElement.createComponent(componentFactory, 0);
  }

  mountAddDeliveryComponent(){
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(AddDeliveryComponent);
    this.displayElement.createComponent(componentFactory,0);
  }

  mountNewEmployeeComponent(){
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(AddEmployeeComponent);
    this.displayElement.createComponent(componentFactory,0);
  }

  mountDeleteUserComponent(){
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(DeleteUsersComponent);
    this.displayElement.createComponent(componentFactory,0);
  }

  mountGenerateReportsComponent(){
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(GenerateReportComponent);
    this.displayElement.createComponent(componentFactory,0);
  }



  navigate(url: string){
    this.router.navigate([url]);
  }

  get auth():LoginService{
    return this.loginService;
  }

}
