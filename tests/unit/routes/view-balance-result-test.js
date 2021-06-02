import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | view-balance-result', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:view-balance-result');
    assert.ok(route);
  });
});
