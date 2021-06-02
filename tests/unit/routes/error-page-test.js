import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | error-page', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:error-page');
    assert.ok(route);
  });
});
