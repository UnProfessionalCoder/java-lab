Feature: Check addition in Google calculatorcontent
    In order to verify that Google calculator work correctly
    As a user of Google
    I should be able to get correct addition result

    Scenario: Addition1
        Given I open Google1
        When I enter "2+2" in search textbox1
        Then I should get result as1 "4"
