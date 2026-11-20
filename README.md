# Dispatch Load Balancer

A Java Spring Boot service which assign orders to vehicle using priority, capacity and distance.

## Live Links
- **Live API Base URL:** `https://dispatch-load-balancer-production-7f933df9.up.railway.app`
- **Swagger UI Documentation:**`https://dispatch-load-balancer-production-7f933df9.up.railway.app/swagger-ui/index.html`

## Tech Stack & Architecture
- **Backend:** Java 21, Spring Boot, Spring Web, Spring Validation, Spring JDBC (`JdbcTemplate` for batch upserts)
- **Database:** PostgreSQL (Supabase)
- **API Docs & Testing:** Springdoc OpenAPI (Swagger UI), JUnit 5, Mockito, MockMvc
- **Architecture:** Clean design (`Controller` -> `Service` -> `Repository`) with separate DTOs for request/response, Proper Validation, and centralized exception handling.

## What It Does
- User provide a list of vehicles and orders to service in separate request.
- It stores those in the DB and when user call the GET /plan api then generate and return a dispatch plan.
- Plan is made using greedy heuristic approach and is not optimal.
- Plan keep in mind the ordering of priority and capacity of vehicle and choose nearest.
- It returns the plan and user can clean up database to generate new plan using DELETE api.

## Try It in 2 Minutes
Use Swagger, or these commands (sample files are in `samples/`, using the assignment's 30 orders and 5 vehicles):

```bash
export BASE=https://dispatch-load-balancer-production-7f933df9.up.railway.app
 
curl -X DELETE $BASE/api/dispatch/delete
curl -X POST $BASE/api/dispatch/vehicles -H 'Content-Type: application/json' -d @samples/vehicles.json
curl -X POST $BASE/api/dispatch/orders -H 'Content-Type: application/json' -d @samples/orders.json
curl $BASE/api/dispatch/plan
```

Note: the delete call wipes all orders and vehicles.



## API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/dispatch/vehicles` | Batch upsert fleet vehicle details |
| `POST` | `/api/dispatch/orders` | Batch upsert delivery orders with (`HIGH`, `MEDIUM`, `LOW`) priorities.|
| `GET` | `/api/dispatch/plan` | Generate vehicle dispatch plan and send it in response|
| `DELETE` | `/api/dispatch/delete` | Clear all orders and vehicles for clean re-testing |


## Sample Run (assignment data: 30 orders, 5 vehicles)
- Total order weight is 645 and total fleet capacity is 500,  so all cannot be carried some must drop.
- Loads per vehicle: 100, 80, 115, 85 and 105. No vehicle is going over the capacity.
- Distances for each vehicle (km): 14.44, 0, 24.74, 19.8 and 28.88.
- 7 orders are unassigned, and all of them are `LOW` priority. Each order with  `HIGH` and `MEDIUM` priority was assigned.


## How Planner   work
- When a user hits the GET api for plan the request goes to the Dispatch Service.
- The dispatch service then fetches the orders and vehicles from the DB.
- If either Orders or Vehicles are empty then the service throws a custom exception and return 200 OK with a message.
- The orders are sorted on the priority with the help of rank provided in enums.
- We generate a calculation object for each vehicle to store states like weight carried and current coordinates.
- We insert all the calculation object in hashmap for fast lookup.
- The main part starts here we use a loop for orders and an internal loop for vehicle.
- Each order loops through all the vehicles it first checks capacity then it checks distance.
- If the distance from current vehicle less then the previous one we save this vehicle id in selected vehicle and move to next.
- Note that if there is a tie in distance the first one is always selected and the order doesn't change vehicle to later one to save ordering
- After loop end if we saw that the order have selected vehicle we put it into assigned orders of vehicle and update vehicle coordinates and add to it .
- If it doesn't have selected vehicle we insert it into unassigned list.
- In the end we loop through all the calculation object and create the response dto and put all the data from the calculation object also the distance is rounded to 2 decimal places.


## Key Assumptions
- No authentication, because the assignment does not ask for the authentication.
- The plan is computed on request and not stored in DB.
- Ids come from the client with the data and are used as primary keys.
- Posting an id that already exists updates the record for that id in DB.
- A repeated id inside one request gets the whole request rejected.
- Weights and capacities are whole numbers, as observed from the assignments sample data.
- All orders and vehicles form one global dataset, so everyone gets same data no isolation.
- Distance runs from the vehicle's start through its stops, with no return trip.
- Priority only decide which ordr get assigned first it does not decide the visiting  order.


## Design Decisions
- **Batch upsert with JdbcTemplate:** one `INSERT ... ON CONFLICT DO UPDATE` statement in batches of 500. `saveAll` with client ids does an extra select per row.
- **Entities and DTOs are separate:** the database and the api shape are different database uses entity while api uses dto no entity is revealed to the api.
- **Saves are synchronous and transactional:** saves are done synchronously so the user can get a clear response and transaction is used so that all the data is saved.
- **No Strategy or Factory pattern:** there is only one main working path so strategy and factory were not used.
- **Haversine is a stateless Spring component:** one shared instance is safe for concurrent requests.


## Error Handling
- Every error use the same response shape: an error code, a message and a timestamp.
- Field validation failures return the code `VALIDATION_FAILED`, with the failing fields listed.
- Repeated ids return `DUPLICATE_ORDER_ID` or `DUPLICATE_VEHICLE_ID`.
- Requesting a plan with no orders or no vehicles returns a clear message.

## Local Setup & Running Tests
1. **Configure the Postgres environment variables**
   ```bash
   export DATABASE_URL=jdbc:postgresql://<host>:<port>/<db>
   export DATABASE_USERNAME=<user>
   export DATABASE_PASSWORD=<password>
   ```
2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
3. **Run the automated tests (20 tests total covering Haversine math, dispatch logic, and controller validation):**
   ```bash
   mvn test
   ```

## Testing
- Total 20 unit test and controller tests.
- Planner tests have standard exception for empty vehicle and order test ,priority order test, capacity limit test, ties, nearest vehicle and unassigned orders, also it uses real haversine not mock.
- Vehicle and Order service tests cover duplicate id and service working test. While service Controller test the missing field and wrong value types.

## Limitations and Next Steps
- The result is a greedy heuristic. Stops follow assignment order, so a vehicle can double back. Reordering each vehicle's stops by nearest neighbour would cut distance without changing who carries what.
- There is only a single constant reason for not assigned so adding a dynamic reasoning for each order unassigned would be better.
- The delete endpoint has no authentication and work on global dataset so everything gets wiped.
- One global dataset mean every user gets the same plan and mix up can happen so should scope it by user ir session.
- A write and plan calls at the same time could mix up data due to the write to either order or vehicle mixing up with data.
- Vehicle ids are sorted as text to provide the same order everytime, so `VEH10` can sort before `VEH2`.
- Requests are processed on demand so we can process very large processing background to reduce the latency of plan.
