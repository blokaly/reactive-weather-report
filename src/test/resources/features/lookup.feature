Feature: Lookup weather report

  Background:
    Given the redis server is flushed

  Scenario: City name validation failed
    When the client calls /weather/abc_
    Then the client receives status code of 400

  Scenario: Weather report from weatherstack
    Given the mock server is reset
    And the weatherstack response of /current?access_key=xxx&unit=m&query=abc with:
      """
      {"current": {"temperature": 20, "wind_speed": 5}}
      """
    When the client calls /weather/abc
    Then the client receives status code of 200
    And the client received temperature should be 20

  Scenario: Weather report from openweather
    Given the mock server is reset
    And the weatherstack response of /current?access_key=xxx&unit=m&query=abc with:
      """
      {"success": false}
      """
    And the openweather response of /weather?appid=xxx&units=metric&q=abc with:
      """
      {"cod": 200, "main":{"temp": 35.7}, "wind":{"speed": 2}}
      """
    When the client calls /weather/abc
    Then the client receives status code of 200
    And the client received temperature should be 35.7