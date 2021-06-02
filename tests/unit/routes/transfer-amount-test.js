import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | transfer-amount', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:transfer-amount');
    assert.ok(route);
  });
});
