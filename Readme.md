This is a test work

Project test tag filter on the target page. see resources/test.properties/url.

Scenario:
GIVEN: the home page of the application is open. popular tags are displayed on the right side. the Global Feed of articles is displayed on the left side.
WHEN: the user selects a tag with a simple click.
THEN: the tag is applied. the feed named #teg_name appears. it should not contain articles not marked with the selected tag.

All tags filters could be tested at once or separate by ordinal. see TagPageTest comments.

Project could run headless. see resources/test.properties/with.head.

Please, find descriptions in classes.

