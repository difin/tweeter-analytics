[<img src="https://img.shields.io/travis/playframework/play-java-starter-example.svg"/>](https://travis-ci.org/playframework/play-java-starter-example)

# TweetAnalytics

This is a web application, which developed on the top of play framework and follows Model - View - Controller
(MVC) architecture style.

## Main Components of Application.

### Controllers

Controller is like gateway of the application. It handles incoming http request and redirect it based on requested page.

Our application has one controller **ApplicationController** , which supports 3 redirects.
1. Homepage Redirects.
2. Homepage Redirects with searched result.
3. User Profile redirects.

Definition of this redirect mentioned on **ROUTES file**.

### Models
Models are for storing data from incoming JSON response for further processing.
**3 Models**
1. Tweet                 - Store tweets in object form.
2. UserProfile           - Store fetched user profile's necessary data in object.
3. UserProfileAndTweets  - Store user profile object and associated tweet object into one.

### Service
Services are main heart of the application. Services are responsible for interaction with twitter API asynchronously and retrieve result based on input parameters.
**3 Models**
1. TenTweetsForKeywordService  - Responsible to interact with twitter api and retrieve 10 tweets result based on keyword.
2. TwitterAuthenticator        - Responsible for generating access token based on consumer key and secret value.
3. UserProfileService          - Responsible for retrieving user profile based on tweet.

### Views
Views provide a way to have wonderful User Interface to analyze result.
**2 Views**
1. Main          - It is homepage of application. Index will be attached to it for providing search facility.
2. UserProfile   - Provide a view to display user profile.

### Tests
We have designed tests for every functionality to validate result.
Test cases are inside separate package and inside we have sub packages too that separates test for every segment of architecture.
**Controller**
1. ApplicationControllerTest   - Test the controller with the help of mokito testing framework.

**Models**
1. UserProfileAndTweetsTest    - Test the given model functionality with help of dummy testing data of user profile and tweets.(JUnit Framework)
2. UserProfileTest             - Test whether user profile has tweets attached to it or not.(JUnit Framework)

**Service**
1. TenTweetsForKeywordServiceTest  - Test given service with test authentication and then passing dummy tweets and validate response.
2. TwitterAuthenticationTest       - Test authentication with token and token encoding on Base64 scheme.
3. UserProfileServiceTest          - Test User Profile service with fake server setup and setting up fake user profile and tweet data.

**Views**
1. IndexTest       -
2. MainTest        -
3. UserProfileTest -

## Running
1. Build Project to support Eclipse.
   run `sbt eclipse` in project directory.

2. We can run project with help of `sbt run`. It will compile project first and then run it.
   Default URL will be `localhost:9000` . 
   
3. Run test with JaCoCo using `sbt test` .