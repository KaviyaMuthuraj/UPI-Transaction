import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | show-history-result', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:show-history-result');
    assert.ok(route);
  });
});
