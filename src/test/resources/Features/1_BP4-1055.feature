@BP4-1055
Feature: Test BDD

  @TEST_BP4-1038
  Scenario: Test BDD
  Scenario: Test BDD
    Given I am on the Google homepage
    When I accept any Google consent or cookie prompts
    And I search for "Sri Lanka"
    Then I should see search results related to "Sri Lanka"
		