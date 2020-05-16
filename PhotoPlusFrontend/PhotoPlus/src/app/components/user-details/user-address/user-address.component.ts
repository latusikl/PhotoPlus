import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AddressDto} from "../../../models/address/address-dto";
import {CountryCode} from "../../../models/address/countryCode";
import {UserAddressService} from "../../../services/user/user-address.service";
import {PatchFieldChangeService} from "../../../services/patch/patch-field-change.service";

@Component({
    selector: 'app-user-address',
    templateUrl: './user-address.component.html',
    styleUrls: ['../user-details.component.scss']
})
export class UserAddressComponent implements OnInit {

    constructor(private  formBuilder: FormBuilder, private userAddressService: UserAddressService, private patchFieldChangeService: PatchFieldChangeService) {
    }

    @Input("newAddress")
    newAddress: boolean = false;

    @Input("userAddress")
    userAddress: AddressDto;

    @Input("ownerCode")
    ownerCode: string;

    //To remove warning about same ID when multiple addresses
    @Input("componentId")
    componentId: string;

    addressForm: FormGroup

    isSubmitted: boolean = false;

    isFormDisabled: boolean;

    ADDRESS_PATCH_ENDPOINT = "address/editAddress/";

    countryCodeSet = CountryCode.getCodes();

    changedFields = this.patchFieldChangeService.getNewFieldChange();

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

    onSaveNew() {
        this.isSubmitted = true;
        if (this.isValid()) {
            this.userAddressService.post(this.addressForm.value, this.ownerCode);
        }
        return;
    }

    onSaveExisting() {
        this.isSubmitted = true;
        if (this.isValid()) {
            this.patchFieldChangeService.sendPatchRequest(this.ADDRESS_PATCH_ENDPOINT + this.userAddress.code, this.changedFields);
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
        this.userAddressService.delete(this.ownerCode, this.userAddress.code);
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
