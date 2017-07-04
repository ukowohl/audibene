Audibene Case Study
===================

```
// build application
mvn clean install
// build application and setup test database (will setup H2 in Memory DB for testing the persistence layer)
mvn clean install -Pintegration-test
```
In order to run the application, please configure the database according to **application.yaml** file. Run locally with local spring profile!

See API for more information under resources/api-definition.yaml

**Important**: Immutables (https://immutables.github.io/) are used, please enable Annotation Processing as described in Immutables Documentation 

Following design principles have been taken into account:

 - Entity stubs existing:
	 - Customer
	 - Employee
	 - Appointment
 - Split responsibility: Entities are loosely coupled (especially no coupling via Hibernate). I did this in order to avoid strict coupling, if necessary it is easily possible to split the complete service into dedicated services responsible only for their respective entities. 

**Use Cases taken into account**

 - Audiologist (Employee) wants to create customers:
	 - I assumed that customer does not belong to dedicated Audiologist, since customers can change Audiologists as well without being updated or getting another customer-id/account.
	 - In order to achieve more flexibility, I added CRUD operations to all entity controllers as well. 
	 - **PUT /application/customers** to serve the use case, use CRUD operations for side use cases
	 - Not included is searching and filtering

 - Audiologist (Employee) wants to create appointments with customer
	 - Appointment holds several data: 
		 - Begin and End
		 - Customer (ID)
		 - Employee (ID)
		 - Empty Rating and no comment from customer
	 - In order to not overcomplicate it, I skipped such fields like contact-information since this demo shows only the main principle. Usually it makes sense to add those kind of fields, a logic, how to determine the contact or how to bind contacts from customers to appointment would have to be defined but does not fit into 6 hours of work. 
	 - For simplification I skipped additional checks e.g. for time conflicts as well. It makes sense to have those things in any proper environment. 
	 - For simplification there is an update endpoint which is not configured/implemented yet. Updatable fields would have to be defined.  
	 - **PUT /api/appointments** for creating and appointment and use CRUD operations for side use cases. 
	 - Not included is searching and filtering

 - Audiologist wants to see all Appointments and their ratings
	 - I understood this use case in a way that an Audiologist wants to see any appointment (related to the Audiologist or to any of his colleagues) and their respective ratings. No rating is seen as a rating as well from my perspective. 
	 - It can be achieved by calling **GET /api/appointments** with pagination query parameters 

 - Next week appointments for an Audiologist
	 - In order to be really flexible and not having to deploy once again when the requirement changes towards next day appointments or next two week appointments, I leave the decision for the displayed time range to the frontend. 
	 - Can be achieved by **GET /api/employees/{employee-id}/appointments** with employee id as path and start, end Query Parameter in ISO8601DateFormat 
	 - Not included is filtering for canceled appointments since I skipped appointment cancelation since this would be related to product decisions. Currently only DELETE is an option to cancel an appointment but in that case we of course loose information which might not be the desired behaviour from product side. 
 - Next Appointment for a customer
 - Rate last appointment of customer:
    - Can be achieved by getting the last appointment for a customer and then post the rating (Stars + Comment) including appointment-id and customer-id. It will return 404 if one of both IDs does not match. 
