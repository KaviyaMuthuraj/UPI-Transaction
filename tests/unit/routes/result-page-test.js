import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | result-page', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:result-page');
    assert.ok(route);
  });
});
