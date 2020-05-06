import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Address} from "../../../models/address/address";

@Component({
    selector: 'app-user-address',
    templateUrl: './user-address.component.html',
    styleUrls: ['../user-details.component.scss']
})
export class UserAddressComponent implements OnInit {

    constructor(private http: HttpClient, private  formBuilder: FormBuilder) {
    }

    @Input("userAddress")
    userAddress: Address;

    @Input("newAddress")
    newAddress: boolean = false;

    addressForm: FormGroup

    submitted: boolean = false;

    formDisabled: boolean = true;

    ngOnInit(): void {
        this.createAddressForm();

        if (!this.newAddress) {
            this.addressForm.disable();
            this.fillForm();
        }
    }


    createAddressForm(): void {
        this.addressForm = this.formBuilder.group({
            street: ['', [Validators.required, Validators.minLength(4), Validators.pattern(new RegExp("[a-zA-Z]+"))]],
            number: ['', Validators.required, Validators.pattern(new RegExp("\w+"))],
            city: ['', Validators.required, Validators.pattern(new RegExp("[a-zA-Z]+"))],
            zipCode: ['', Validators.required, Validators.pattern(new RegExp("\d"))],
            countryCode: ['', Validators.required],
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

    disable() {
        this.addressForm.disable();
        this.formDisabled = true;
    }

    enable() {
        this.addressForm.enable();
        this.formDisabled = false;
    }

}
