import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
export default class ViewBalanceController extends Controller {
  @service router;
  @service store;
  @tracked accountValidation = false;
  @tracked errMsg = '';
  @action
  togglePassword(value) {
    let password = value.target.previousElementSibling;
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);
    value.target.classList.toggle('fa-eye-slash');
  }
  @action
  viewBalance() {
    const self = this;
    this.store.queryRecord('account-detail', {accountNumber: this.accountNumber,UPIPIN: this.UPIPIN})
    .then((account) => {
      self.accountValidation = true;
      self.userName = account.userName;
      self.accountBalance = account.accountBalance;
    })
    .catch((error) => {
      if(error.errors[0].status == "500") {
        this.errMsg = "Something went Wrong..! Please verify your values.."
      } else if(error.errors[0].status == "400") {
        this.errMsg = error.errors[0].detail;
      }
    });
  }
}
