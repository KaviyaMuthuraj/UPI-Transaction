import Model,{attr} from '@ember-data/model';
import { hasMany } from 'ember-data/relationships';
export default class AccountDetailsModel extends Model {
  @hasMany('transaction') transactions;
  @attr('string') userName;
  @attr('string') UPIPIN;
  @attr('number',{defaultValue : 0}) accountBalance;
  @attr('boolean',{defaultValue : true}) UPIState;
}
