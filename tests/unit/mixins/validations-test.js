import EmberObject from '@ember/object';
import ValidationsMixin from 'upi-transaction/mixins/validations';
import { module, test } from 'qunit';

module('Unit | Mixin | validations', function() {
  // TODO: Replace this with your real tests.
  test('it works', function (assert) {
    let ValidationsObject = EmberObject.extend(ValidationsMixin);
    let subject = ValidationsObject.create();
    assert.ok(subject);
  });
});
