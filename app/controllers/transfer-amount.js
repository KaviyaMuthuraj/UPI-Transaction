import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
export default class TransferAmountController extends Controller {
  @service router;
  @service store;
  @tracked transferState = false;
  @tracked errMsg = '';
  @tracked accountValid = false;
  accountDetail = ''
  @action
  togglePassword(value) {
    let password = value.target.previousElementSibling;
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);
    value.target.classList.toggle('fa-eye-slash');
  }
  @action
  verifyAccount() {
    this.store.queryRecord('account-detail', {accountNumber: this.accountNumber,UPIPIN: this.UPIPIN})
      .then(() => {
          this.accountValid = true;
          this.errMsg = '';
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
  transferAmount() {
    const self = this;
    this.accountDetail = this.store.peekRecord('account-detail',this.accountNumber);
    let transaction = this.store.createRecord('transaction');
    transaction.account = this.accountDetail;
    transaction.otherAccount = this.otherAccount;
    transaction.amount = this.amount;
    transaction.save()
      .then(() => {
        self.transferState = true;
      })
      .catch((error) => {
        transaction.unloadRecord();
        if(error.errors[0].status == "500") {
          this.errMsg = "Something went Wrong..! Please try again later"
        } else if(error.errors[0].status == "400") {
          this.errMsg = error.errors[0].detail;
        }
      });
  }
}

