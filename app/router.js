import EmberRouter from '@ember/routing/router';
import config from 'upi-transaction/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}
Router.map(function () {
  this.route('transaction-type');
  this.route('transfer-amount');
  this.route('create-account');
  this.route('view-balance');
  this.route('show-history');
  this.route('update-UPIState');
});
