# sms

#Required Softwars
 Java 1.8
 Postgras14
 Maven
 
 
#Steps  To Run the Code
    1) Navigate to pom.xml location on your local machine
    2) mvn clean install
    3) mvn spring-boot:run

Once Server is started you can access below endopoints.

POST - http://localhost:8080/outbound/sms

POST -http://localhost:8080/inbound/sms


Use Below postman collection to test from POST MAN:
https://github.com/sairamdikondawar/sms/blob/master/src/test/java/com/sms/demo/sms%20demo.postman_collection.json
