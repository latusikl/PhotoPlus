import { Component, OnInit, ViewChild, ComponentFactoryResolver, ViewContainerRef, Renderer2 } from '@angular/core';
import { LoginService } from 'src/app/services/login/login.service';
import { ActivatedRoute, UrlSegment, Router } from '@angular/router';
import { ManageOrdersComponent } from './subcomponents/manage-orders/manage-orders.component';

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
              private componentFactoryResolver:ComponentFactoryResolver,
              private renderer: Renderer2) { }

  ngOnInit(): void {
    this.activatedRoute.url.subscribe(urlSegment => {
      if(!urlSegment[1]){
        return;
      }
      switch(urlSegment[1].path){
        case 'orders':{
          this.mountOrdersComponent(); 
        }
        case 'newProduct':{
          this.mountNewProductComponent();
        }
      }
    })
  }

  mountOrdersComponent(){
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ManageOrdersComponent);
    this.displayElement.createComponent(componentFactory,0);
  }

  mountNewProductComponent(){
    // const componentFactory = this.componentFactoryResolver.resolveComponentFactory()
  }

  get auth():LoginService{
    return this.loginService;
  }

}
