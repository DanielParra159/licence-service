# licence-service
Licence service following Spring microservices in action book

Connected to Organization Service (https://github.com/DanielParra159/Organization-Service) using Eureka discovery service (https://github.com/DanielParra159/Discovery-Service).
Use Hystrix for circuit breaker and fallback. 

GET http://localhost:8080/v1/organizations/{organizationId}/licenses/{licenceId} to get license
GET http://localhost:8080/v1/organizations/{organizationId}/licenses/ to get all licenses from organizationId
POST http://localhost:8080/v1/organizations/{organizationId}/licenses/ to license 
http://localhost:8080/actuator/health to check service status