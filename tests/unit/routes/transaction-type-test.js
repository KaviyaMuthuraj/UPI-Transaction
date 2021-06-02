import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | transaction-type', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:transaction-type');
    assert.ok(route);
  });
});
