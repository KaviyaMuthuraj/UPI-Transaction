import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
export default class UpdateUpistateController extends Controller {
  @service router;
  @service store;
  @tracked updationState = false;
  @tracked accountValidation = false;
  @tracked errMsg = '';
  @action
  getSelectedOption(option) {
    this.selectedOption = option;
  }
  @action
  togglePassword(value) {
    let password = value.target.previousElementSibling;
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);
    value.target.classList.toggle('fa-eye-slash');
  }
  @action
  validateAccount() {
    const self = this;
    this.accountDetails = this.store.queryRecord('account-detail', {accountNumber: this.accountNumber,UPIPIN: this.UPIPIN})
      .then(() => {
        self.accountValidation = true;
        self.errMsg = '';
      })
      .catch((error) => {
        if(error.errors[0].status == "500") {
          this.errMsg = "Something went Wrong..! Please try again later"
        } else if(error.errors[0].status == "400") {
          this.errMsg = error.errors[0].detail;
        }
      });
  }
  @action
  updateUPIState() {
    const self = this;
    let accountDetails1 = this.store.peekRecord('account-detail',self.accountNumber);
    accountDetails1.UPIState = this.selectedOption;
    accountDetails1.save()
      .then(function() {
        self.updationState = true;
        self.errMsg = '';
      })
      .catch((error) => {
        if(error.errors[0].status == "500") {
          this.errMsg = "Something went Wrong..! Please try again later"
        } else if(error.errors[0].status == "400") {
          this.errMsg = error.errors[0].detail;
        }
      });
  }
}
