import Model,{attr} from '@ember-data/model';
import { belongsTo } from 'ember-data/relationships';
export default class TransactionModel extends Model {
  @belongsTo('account-detail',{
    async : false
  }) account;
  @attr('string') otherAccount;
  @attr('number') amount;
  @attr('string') transactionType;
  @attr('date') dateTime;
  @attr('string') userName;
}
