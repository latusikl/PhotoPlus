import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CountryCode } from '../../../models/address/countryCode';
import { AddressService } from '../../../services/address/address.service';
import { Address } from '../../../models/address/address';
import { FieldChange } from "../../../services/patch/field-change";

@Component({
    selector: 'app-user-address',
    templateUrl: './user-address.component.html',
    styleUrls: ['../user-details.component.scss']
})
export class UserAddressComponent implements OnInit {

    constructor(private formBuilder: FormBuilder, private addressService: AddressService) {
    }

    @Input('newAddress')
    newAddress = false;

    @Input('userAddress')
    userAddress: Address;

    // To remove warning about same ID when multiple addresses
    @Input('componentId')
    componentId: string;

    addressForm: FormGroup;

    isSubmitted = false;

    isFormDisabled: boolean;

    countryCodeSet = CountryCode.getCodes();

    changedFields = new FieldChange();

    ngOnInit(): void {
        this.createAddressForm();
        if (!this.newAddress) {
            this.addressForm.disable();
            this.fillForm();
            this.isFormDisabled = true;
        } else {
            this.isFormDisabled = false;
        }
    }


    createAddressForm(): void {
        this.addressForm = this.formBuilder.group({
            street: ['', [Validators.required, Validators.minLength(4), Validators.pattern(new RegExp('[a-zA-Z]+'))]],
            number: ['', Validators.required],
            city: ['', Validators.required],
            zipCode: ['', Validators.required],
            countryCode: ['', Validators.required]
        });
    }

    fillForm() {
        this.addressForm.controls.street.setValue(this.userAddress.street);
        this.addressForm.controls.number.setValue(this.userAddress.number);
        this.addressForm.controls.city.setValue(this.userAddress.city);
        this.addressForm.controls.zipCode.setValue(this.userAddress.zipCode);
        this.addressForm.controls.countryCode.setValue(this.userAddress.country);
    }

    get f() {
        return this.addressForm.controls;
    }

    onSaveNew() {
        this.isSubmitted = true;
        if (this.isValid()) {
            this.addressService.postAddressForLoggedUser(this.addressForm.value);
        }
        return;
    }

    onSaveExisting() {
        this.isSubmitted = true;
        if (this.isValid()) {
            this.addressService.patchAddressForLoggedUser(this.changedFields, this.userAddress.code);
        }
        return;
    }

    isValid(): boolean {
        if (this.addressForm.invalid) {
            return false;
        }
        return true;
    }

    registerChange(key: string, value: string): void {
        this.changedFields.registerChange(key, value);
    }

    onDelete() {
        this.addressService.deleteAddressForLoggedUser(this.userAddress.code);
    }

    disable() {
        this.addressForm.disable();
        this.isFormDisabled = true;
    }

    enable() {
        this.addressForm.enable();
        this.isFormDisabled = false;
    }

}
