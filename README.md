
# Account Manager REST API
## Stack
- Language: Kotlin
- Database: H2
- Framework: Spring Boot
- Build Tool: Gradle

## Description
Implements a REST API that allows a customer to:
- view all of his accounts
- view a specific account given the account number
- transfer from one of his accounts to another account

## Installation 
- Clone this repository to local machine
- Open as a project in Intellij
- build then run ApplicationManagerApplicationKt

## Endpoints
GET /api/customers/{customerId}/accounts

GET /api/customers/{customerId}/accounts/{accountNumber}

POST /api/customers/accounts/transfer

## Testing
- Open the Postman collection in the docs/ folder in Postman.
- Set the {{ACCOUNT_MANAGER_URL}} to http://localhost:8080

### Data has been pre-populated with the following values:

#### customerId = 10001

#### First account
<pre>
"id": 1,
"accountNumber": "12345678",
"balance":  1000000.0,
"currency": "HKD",
"customerId": 10001
</pre>

####  Second account
<pre>
"id": 2,
"accountNumber": "12345678",
"balance": 1000000.0,
"currency": "HKD",
"customerId": 10001
</pre>










