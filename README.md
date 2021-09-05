
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
### GET /api/accounts
_Response_
<pre>
[
    {
        "id": 1,
        "accountNumber": "12345678",
        "balance": 994300.0,
        "currency": "HKD",
        "customerId": 10001
    },
    {
        "id": 2,
        "accountNumber": "88888888",
        "balance": 1005700.0,
        "currency": "HKD",
        "customerId": 10001
    }
]
</pre>

### GET /api/accounts/{accountNumber}
_Response_
<pre>
{
    "id": 2,
    "accountNumber": "88888888",
    "balance": 1005700.0,
    "currency": "HKD",
    "customerId": 10001
}
</pre>

### POST /api/accounts/transfer
_Request_
<pre>
{
    "sourceAccount" : "12345678",
    "amount" : 1900, 
    "targetAccount" : "88888888",
    "customerId": 10001
}
</pre>
_Response_
<pre>
{
    "targetAccount": {
        "id": 2,
        "accountNumber": "88888888",
        "balance": 1005700.0,
        "currency": "HKD",
        "customerId": 10001
    },
    "sourceAccount": {
        "id": 1,
        "accountNumber": "12345678",
        "balance": 994300.0,
        "currency": "HKD",
        "customerId": 10001
    }
}
</pre>


## Testing
- Open the Postman collection in the docs/ folder in Postman.
- Set the {{ACCOUNT_MANAGER_URL}} to http://localhost:8080

### Data has been pre-populated with the following values:

#### Customer
<pre>
customerId = 10001
X-Access-Token = abcdef-1234-567890
</pre>

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










