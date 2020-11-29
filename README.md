# CS6440-IP-FamilyHealthTracker

Family Health Tracker Web Interface to view your's and your family members health information at a single place.

## App Public URL (deployed to heroku)
Deployed App public url - 
[https://pkhosla3-cs6440-trackerapp.herokuapp.com/](https://pkhosla3-cs6440-trackerapp.herokuapp.com/)

Deployed Backend service url -
[https://pkhosla3-cs6440-trackerbackend.herokuapp.com](https://pkhosla3-cs6440-trackerbackend.herokuapp.com) 
 
 API endpoints mentioned in the instruction below can be used to test backend API.

## Local build and deployment using Docker (RECOMMENDED)

### Pre-requisites
* docker-cli

### Build and deployment steps

Run the following Steps/Commands:
    
    * Clone the repo in your local.
    * cd cs6440-IP-FamilyTracker 
    * docker-compose build
    * docker-compose up
  Note: Docker build runs slow

  Start a web browser and navigate to: 
  [http://localhost:3000](http://localhost:3000)
  
  If you want to hit the backend service endpoint:
  [http://localhost:8086/home](http://localhost:8086/home)
  
  Get Patient Personal Complete Info (includes family members) by Patient Id
          `http://localhost:8086/api/patient/<id>`

## (Non Docker) Local build and deployment

### Pre-requisites
* JDK 1.8 installed and PATH set
* NodeJS 14.6
* nginx

### Build and deployment steps

#### Backend

1. Open Project's tracker-backend folder in IntelliJ (or any other IDE)

2. From the top menu in IntelliJ, Click Run > Select "Edit Configurations"

3. Select "Spring Boot App" in the left navigation pane by using + button on top. Once selected ensure Application.java class is entered as the main class.
        
    ![Run Config](https://github.gatech.edu/pkhosla3/CS6440-IP-FamilyHealthTracker/blob/master/docs/run-config.png)

4. Run the backend service using the 'Run Application' option under Run menu.

5. Once the server has run, go to
        
    [http://localhost:8086/home](http://localhost:8086/home)
    
    Get Patient Personal Complete Info (includes family members) by Patient Id
        `http://localhost:8086/api/patient/<id>`

#### Frontend

   * Open terminal and go to tracker-frontend folder in project codebase
   
   
           * cd cs6440-IP-FamilyTracker/tracker-frontend 
           * yarn install
           * yarn build
           * yarn start
    
   * Once all the above are successful, frontend service will start at the port 3000.
    Start a web browser and navigate to: 
    [http://localhost:3000](http://localhost:3000)

 
  
    
    
