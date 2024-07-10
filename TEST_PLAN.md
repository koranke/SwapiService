# SWAPI Test Plan

## Scope
For the purposes of this assessment, the testing scope is limited to the endpoint for people.
All the endpoints follow the same pattern of request and response with only the data entity
changing, so the test resources and test cases will be similar for all of them.
I have included the supporting code for planets and a single test as a demonstration of what it looks like
for additional data entity API.

## People API notes
* Data entity returned as "result.properties".
* Additional metadata about data entity included in "result".
* No auth needed.

### Get All
* Can either get all or provide a "name" query parameter that filters results.
* If "get all", returns a list of links.
* "limit" and "page" query parameters are ostensibly supported when using "get all".  Will need to confirm.
* Current behavior when using an invalid "page" number is to return the last page.  Need to confirm expected behavior.
* If searching with "name" filter, returns a list of fully populated people entities and is not paginated.
This could be a serious issue if the result set is too large.
* Filter appears to be a "contains" filter.

### Get By Id
* Returns the matching "result" record if found.
* Returns a message about "not found" if not found.
* Does not distinguish between invalid IDs (for example, "xxx") and IDs that don't exist.


## People API Test Notes
Ideally a test framework would have direct access to the underlying DB or have the ability to POST data in
order to identify data scenarios or create data scenarios so that test verifications are dynamic and accurate.
Without either, we are stuck hard-coding target scenarios and expected results.  This increases the risk of unreliable tests
and increases the cost of creating and maintaining tests.  In a real situation, this would be something for the team
to address.

Since we don't have a source of truth regarding the actual values of any individual data entity, we will only verify
a sampling of values.  In a real situation, we would need to verify all values in a response. We would also potentially have
many more test variations.  For example, when getting a person by ID, there may be different patterns of data, in terms of 
value types, fields populated or not populated, etc.

### Get All
* Basic get.  
  * Verify results found and pagination size correct.  
  * Verify link item details for a single item.
  * Verify link URL is valid / returns result
* Search with single result.
  * Verify link URL and that name matches filter.
* Search with multiple results.
  * Verify that all names match filter.
* Search with no results.

### Get By Id
* Basic get for known ID. Verify response data.
* Get for invalid ID