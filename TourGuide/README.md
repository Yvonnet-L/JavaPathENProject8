TourGuide V1.0
========================
![Java Version](https://img.shields.io/badge/Java-1.8-blue)
![Spring Version](https://img.shields.io/badge/Spring-2.5.4-green)
![Gradle Version](https://img.shields.io/badge/Gradle-2.1.6-orange)
![Docker Version](https://img.shields.io/badge/Docker-4.2.0-blue)

Description
------------
TripMaster aims, with the TourGuide application, to change the rules of the game on how to obtain information 
on tourist attractions close to users, accompanied by promotional offers from tour operator partners.

**`TourGuide`**  is the micro-service centralizing requests and which is based on 3 other micro-services (gpsUtil, TripPricer, 
RewardsCentral). 
* **`GpsUtil`** : Returns the location of a user by his id and provides the list of attractions with their locations.
* **`TripPricer`** : Allows you to obtain 5 offers from 5 providers according to the preferences and discount points of the User.
* **`RewardsCentral`** : Allows the calculation of a user's discount for an attraction.

You will find below a diagram detailing their connection
## Prerequisites
### Technologies
- Java 1.8 JDK
- Gradle 2.1.6
- Docker

### Installing
Install Java:
* https://www.oracle.com/fr/java/technologies/javase-downloads.html

Install Gradle
* https://gradle.org/install/

Install Docker:
* https://www.docker.com/products/docker-desktop

Then you can test the endpoints with Postman for example.
* https://www.postman.com/downloads/

## Run the application

Once the project is in your preferred IDE, before launching it, you will obviously have to have installed
the prerequisites listed above.
To run the application, you will first have to go to each root directory of the 4 micro-services to launch 2
commands with Git Bash for example:
* Build each micro-services, with gradle:
  ``` 
  * $ ./gradlew build
  ``` 
* Then you will need to build each microservices docker image using the command
  ``` 
  * $ docker build -t NAME_OF_YOUR_IMAGE . 
  * ex: $ docker build -t ms-gpsUtil .
    (Be careful not to forget the full point at the end)
  ``` 
* Once all the docker images are built, all you have to do is build the docker-compose, which will be launched 
 automatically, using the command
   ``` 
  * $ docker-compose up
  ``` 
  
* All you have to do is test the application using Postman through the endPoints listed just below.
  



## ENDPOINTS
``` 
Index
```
- **GET** http://localhost:9000/
```
user's localisation
```    
- **GET** http://localhost:9000/getLocation
     - ex:  http://localhost:9000/getLocation?userName=internalUser77
```
Location of all users
``` 
- **GET** http://localhost:9000/getAllCurrentLocations

```
List of 5 provider offers for an attraction
``` 
- **GET** http://localhost:9000/getTripDeals
    - ex: http://localhost:9000/getTripDeals?userName=internalUser77
```
List of the 5 attractions closest to the user with their locations and the distance between them
``` 
- **GET** http://localhost:9000/getNearbyAttractions
    - ex: http://localhost:9000/getNearbyAttractions?userName=internalUser77
```
detail of a user
```    
- **GET** http://localhost:9000/getUser
    - ex:  http://localhost:9000/getUser?userName=internalUser77
```
detail of user's references
```    
- **GET** http://localhost:9000/getUserPreferences
    - ex:  http://localhost:9000/getUserPreferences?userName=internalUser77
```
UpDate of user's references
```    
- **PUT** http://localhost:9000/addUserPreferences
    - ex:  http://localhost:9000/addUserPreferences?userName=internalUser77
    - With a json body 
      - ex: {
          "attractionProximity": 300,
          "lowerPricePoint": 100,
          "highPricePoint": 600,
          "tripDuration": 6,
          "ticketQuantity": 4,
          "numberOfAdults": 2,
          "numberOfChildren": 1
          }


## Modelization
###  Class Diagram
![Model](ShemaModel.jpg)
###  TourGuide Architectural Overview
![TourGuide Architectural Overview](TourGuide%20Architectural%20Overview.png)