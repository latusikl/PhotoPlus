import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AddressDto} from "../../../models/address/address-dto";

@Component({
    selector: 'app-user-address',
    templateUrl: './user-address.component.html',
    styleUrls: ['../user-details.component.scss']
})
export class UserAddressComponent implements OnInit {

    constructor(private http: HttpClient, private  formBuilder: FormBuilder) {
    }

    @Input("userAddress")
    userAddress: AddressDto;

    @Input("newAddress")
    newAddress: boolean = false;

    addressForm: FormGroup

    submitted: boolean = false;

    formDisabled: boolean;

    ngOnInit(): void {
        this.createAddressForm();

        if (!this.newAddress) {
            this.addressForm.disable();
            this.fillForm();
            this.formDisabled=true;
        }
        else{
            this.formDisabled=false;
        }
    }


    createAddressForm(): void {
        this.addressForm = this.formBuilder.group({
            street: ['', [Validators.required, Validators.minLength(4), Validators.pattern(new RegExp("[a-zA-Z]+"))]],
            number: ['', Validators.required],
            city: ['', Validators.required],
            zipCode: ['', Validators.required],
            countryCode: ['', Validators.required]
        });
    }

    fillForm() {
        this.addressForm.controls["street"].setValue(this.userAddress.street);
        this.addressForm.controls["number"].setValue(this.userAddress.number);
        this.addressForm.controls["city"].setValue(this.userAddress.city);
        this.addressForm.controls["zipCode"].setValue(this.userAddress.zipCode);
        this.addressForm.controls["countryCode"].setValue(this.userAddress.country);
    }

    get f() {
        return this.addressForm.controls;
    }


    onSubmit() {
        this.submitted = true;
        if (this.addressForm.invalid) {
            return;
        }
        console.log("Valid data")
    }

    postAddress(){
        // const newAddressToPost =
    }

    disable() {
        this.addressForm.disable();
        this.formDisabled = true;
    }

    enable() {
        this.addressForm.enable();
        this.formDisabled = false;
    }

}
