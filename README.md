# sms

#Required Softwares\
 &nbsp; **Java 1.8**\
 &nbsp; **Postgras14**\
 &nbsp; **Maven Latest Version**\
 &nbsp; Using **Hazelcast** in memory cache embedded version. so no need to install.
 
 
#**Steps  To Run the Code**\
  &nbsp;  1) Navigate to pom.xml location on your local machine\
  &nbsp;    2) mvn clean install\
    &nbsp;  3) mvn spring-boot:run

Once Server is started you can access below endopoints.

 &nbsp; POST - http://localhost:8080/outbound/sms 

 &nbsp;  POST -http://localhost:8080/inbound/sms 


Use Below postman collection to test from POST MAN:
https://github.com/sairamdikondawar/sms/blob/master/src/test/java/com/sms/demo/sms%20demo.postman_collection.json
