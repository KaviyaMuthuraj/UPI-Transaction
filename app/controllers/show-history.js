import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
export default class ShowHistoryController extends Controller {
  @service router;
  @service store;
  @tracked transactionValidation = false;
  @tracked errMsg = '';
  @action
  togglePassword(value) {
    let password = value.target.previousElementSibling;
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);
    value.target.classList.toggle('fa-eye-slash');
  }
  @action
  showHistory() {
    const self = this;
    this.store.queryRecord('transaction', {accountNumber: this.accountNumber,UPIPIN: this.UPIPIN}) //GET -> transactions
    .then(() => {
      self.transactionValidation = true;
      self.transactionDetails = self.store.peekAll('transaction');
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
