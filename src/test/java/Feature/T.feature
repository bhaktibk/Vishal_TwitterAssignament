Feature: Test case for twitter application

  Scenario Outline: Twitter application test case
    Given Login to 'https://twitter.com/' with '<username>' and '<Password>'
    And Navigate Profile Page of logged '<username>'
    And upload a profile picture from '<location>'
    When update '<FieldsToUpdate>' as '<FieldsValue>'
    Then Values of '<FieldsToUpdate>' should be '<FieldsValue>' on profile section

    Examples:
      | username | Password   | location | FieldsToUpdate    | FieldsValue                               |
      | Test2879305885 | Password@123 | Test.jpg | Bio,Location,Website | Selenium Automation User,Pune,twitter.com |


  Scenario Outline: Fetch tweets of the page and validate
    Given Login to 'https://twitter.com/' with '<username>' and '<Password>'
    When Tweets are searched of the page '<TwitterPage>'
    And Fetch the tweets from last "2" hours
    Then Split the tweets which are more than "50" characters
    Examples:
      | username | Password   | TwitterPage        |
      | Test2879305885 | Password@123 | The Times Of India |
      