# STAR WARS API TEST PROJECT

[![Build and Test](https://github.com/koranke/SwapiService/actions/workflows/test.yml/badge.svg?branch=master)](https://github.com/koranke/SwapiService/actions/workflows/test.yml) [![CodeQL](https://github.com/koranke/SwapiService/actions/workflows/github-code-scanning/codeql/badge.svg?branch=master)](https://github.com/koranke/SwapiService/actions/workflows/github-code-scanning/codeql)

## How to run
1. Project requires Maven 3.x and java 17.  If you use IntelliJ, it includes Maven and can auto-download Java for you.
2. If using IntelliJ, you can directly run tests from the test classes.  See the classes under src/test/.
3. If command line, navigate to the project directory and run Maven (assuming it is in path).

mvn clean test


## Design
The basic idea is to centralize all HTTP client functionality in a base class
and extend that for individual endpoint groups.  Each individual endpoint group class
can then implement its own helper methods as desired, depending on the needs
of testing.

In this case, since all the endpoints follow the same pattern except for the handled data entity, we can further reduce
code duplication by using a generic swapi base class also.  This class can be used to handle the common
functionality of the swapi endpoints.  If there were different endpoints with more varied response objects, we
would just extend from the ApiBase class instead.

Tests typically have two different needs when calling an API. They can either
be calling an API to test it or they can be calling it to assist with
scenario setup or data verification.  If a test is testing the endpoint, then it
needs full access to the raw results of the call.  It also may want to manually set headers
and query parameters.  On the other hand, if a test is just using an endpoint for setup or verification, 
then it's better to keep things simple, use appropriate defaults for headers and possibly query parameters,
and hide the details from the test.

By naming convention, this is handled by providing two different versions of an
API, one with "tryXxx()" and the other as "Xxx()".  For example,
"tryGetPerson()" should be used when testing the API.  It returns the raw response.
"GetPerson()", on the other hand, internally validates a successful call
and returns the deserialized result.

## Missing Items
In a real situation, we would have test logging for debugging and would also
have a configuration solution so that tests can run in multiple environments.
We might also have custom reporting and integration with a test management
solution like JIRA.
