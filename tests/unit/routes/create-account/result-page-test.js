import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | create-account/result-page', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:create-account/result-page');
    assert.ok(route);
  });
});
