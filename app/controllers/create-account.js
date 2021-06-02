import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import EmberValidations from 'ember-validations';
import { tracked } from '@glimmer/tracking';
export default class CreateAccountController extends Controller.extend(EmberValidations){
  @service router;
  @service store;
  @tracked validation = false;
  @tracked errMsg = '';
  // constructor() {
  //   super(...arguments);
  // }
  // accountDetails = this.store.createRecord('account-details');
  // validations = {
  //   'model.userName' : {
  //     presence: true,
  //     length: { minimum: 3,
  //       message : "Atleast have an 3 characters" }
  //   },
  //   'model.accountNumber' : {
  //     presence: true,
  //     length: { is: 10,
  //       message: "Must have an 10 numbers"},
  //     pattern: { without: /^([a-zA-Z])+$/,
  //       message: 'Must not be alphabets' }
  //   },
  //   'model.UPIPIN': {
  //     presence: true,
  //     length: { is: 6,
  //       message: "Must have an 6 numbers"},
  //     pattern: { without: /^([a-zA-Z])+$/,
  //       message:"Must not be alphabets"}
  //   }
  // }
  @action
  togglePassword(value) {
    let password = value.target.previousElementSibling;
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);
    value.target.classList.toggle('fa-eye-slash');
  }
  @action
  createAccount(){
    const self = this;
    let accountDetails = this.store.createRecord('account-detail',{id : this.accountNumber});
    accountDetails.userName = self.userName;
    accountDetails.UPIPIN = self.UPIPIN;
    accountDetails.save()
      .then(() => {
        self.validation = true;
      })
      .catch((error) => {
        accountDetails.unloadRecord();
        if(error.errors[0].status == "500") {
          this.errMsg = "Something went Wrong..! Please try again later"
        } else if(error.errors[0].status == "400") {
          this.errMsg = error.errors[0].detail;
        }
      });
  }
}
