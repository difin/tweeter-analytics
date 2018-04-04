[<img src="https://img.shields.io/travis/playframework/play-java-starter-example.svg"/>](https://travis-ci.org/playframework/play-java-starter-example)

# Reactive TweetAnalytics

This web application is developed with Play Framework-Reactive Stocks.
It enables searching for the most recent tweets for a given search phrase using Twitter API and viewing Twitter profile info together with user's latest tweets.
It dynamically refreshes to show any incoming tweets by using Stream live updates for the user interface. The reactive behavior is implemented using asynchronous server push solution, using WebSockets and Akka Actors.


## Group Members

     Name      | Student ID
----------------- | ---------

Deepika Dembla    | 40036900    
Dmitriy Fingerman | 26436579    
Nikita Baranov    | 40012854    
Mayank H. Acharya | 40036106    
Tumer Horloev     | 40019108   


## Division of Work (Assignment-2)

 Deepika Dembla: 
 
 * test.TwitterSearchSchedulerActorTest.java
 * test.TwitterSearchSchedulerActorProtocolTest.java
 * Javadoc
 * README
 
 Dmitriy Fingerman:
 
 * conf.routes
 * controllers.ResponsiveApplicationController.java
 * actors.TwitterSearchSchedulerActorProtocol.java
 * services.TwitterAuthenticator.java
 * test.Utils.WebSocketTestClient.java
 * test.controllers.ResponsiveApplicationControllerTest.java
 * test.views.responsiveTweetsTest.java
 * test.services.SchedulingServiceTest.java
 
 
 Nikita Baranov:
 
 * conf.routes
 * views.index.scala.html
 * views.main.scala.html
 * views.responsiveTweets.scala.html
 * test.views.mainTest.java
 * test.controllers.ResponsiveApplicationControllerTest.java
 * test.Utils.WebSocketTestClient.java
 
 Mayank H. Acharya:
 
 * actors.TwitterSearchSchedulerActor.java
 * services.SchedulingService.java
 
 Tumer Horloev:
 
 * actors.TwitterSearchActor.java
 * actors.TwitterSearchActorProtocol.java
 * test.actors.TwitterSearchActorTest.java
 * test.actors.TwitterSearchActorProtocolTest.java
 

## Division of Work (Assignment-1)

 Deepika Dembla: 

 * test.controllers.ApplicationControllerTest.java

 Dmitriy Fingerman: 

 * conf.routes
 * controllers.ApplicationController.java
 * services.TwitterAuthenticator.java
 * services.UserProfileService.java
 * models.UserProfile.java
 * models.UserProfileAndTweet.java

 Nikita Baranov:
	
 * controllers.ApplicationController.java
 * views.index.scala.html
 * views.main.scala.html
 * views.userProfile.html
 * test.views.IntexTest.java
 * test.views.mainTest.java
 * test.views.userProfileTest.java

 Mayank H. Acharya:

 * models.Tweet.java
 * services.TenTweetsForKeywordService.java
 * Javadoc
 * README 
	
 Tumer Horloev:

 * test.models.UserProfileAndTweetTest.java
 * test.models.UserProfileTest.java
 * test.services.TenTweetsForKeywordServiceTest.java
 * test.services.TwitterAuthenticatorTest.java
 * test.services.UserProfileServiceTest.java

## How to Build
 * To build the project with Eclipse support:
   execute `sbt clean compile eclipse` in project directory.
   
 * To run tests:
   execute `sbt test`.

 * To run tests with Jacoco test coverage report:
   execute `sbt jacoco`.

 * To run the akka project:
   execute `"runMain com.lightbend.akka.sample.ClassName"`
  
## How to Run
 * To run project, execute `sbt run`.
 * After starting up, the application can be accessed using web browser at `localhost:9000`. 
