import Controller from '@ember/controller';
import { action } from '@ember/object';
export default class TransactionTypeController extends Controller {
  @action
  reloadPage() {
    window.location.reload();
  }
}
