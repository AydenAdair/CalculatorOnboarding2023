# CalculatorOnboarding2023
This calculator has the 4 primary operations (+, -, *, /) and has a "dummy" authenticated endpoint to view the audit log.
The audit log is stored in memory, and will only show what has occured during the one session of the server being open.

## How To Run
1. Make a clone of the repository
2. Run `mvn clean install`
    1. If you are having issues downloading the Confluent dependencies, you may need to use `assume` [seen here](https://confluentinc.atlassian.net/wiki/spaces/Engineering/pages/3024984353).
3. Execute the main method in the Java file `CalculatorApp.java`
4. Use the calculator via the Swagger UI at: http://localhost:8080/openapi/swagger-ui/index.html
5. The `audit` endpoint has the credentials of `username: admin` and `password: password`

## Tests
`TestCalculator.java` has multiple integration tests using the `RetrofitClient` that simulates using the different endpoints and testing their results.
