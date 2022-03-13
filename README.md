# Full-stack-Development-for-Personalized-News-Recommendation
  
  Instagram Flavor News Android app based on Google Component Architectural MVVM Pattern
  
  ![sample-overview](https://user-images.githubusercontent.com/50295329/158078460-2a3d07dd-905d-4041-9480-68599a9b51b3.gif)

#### 1. Home
Click like or swipe to right to like news. The liked news will be saved.
Click unlike or swipe to left to unlike news. 

![home](https://user-images.githubusercontent.com/50295329/158078393-d236d324-d170-4d4d-a9b8-9ebf7d4ee98c.png)

#### 2. Save
The saved news will be shown in the Save tab. Click the heart-shaped button of any saved news to remove it from the Save list.

![saved_news](https://user-images.githubusercontent.com/50295329/158078401-eba7b509-b07d-45cb-8c15-2098cdfc0bc7.png)

#### 3. Search
Use the search bar to search news. 

![search](https://user-images.githubusercontent.com/50295329/158078405-a5472835-a724-4d13-8a5f-4ffff3a50130.png)

#### 4. News Detail
Click any saved news or any search result to show news details.

![news_detail](https://user-images.githubusercontent.com/50295329/158078410-000b4245-ede6-4008-9471-19210dbf8b1c.png)

### LifeCycle

<img width="605" alt="04" src="https://user-images.githubusercontent.com/50295329/158078493-ad86018b-ab78-4bb8-b2fc-0546e828b90c.png">

### NewsAPI

[https://newsapi.org/](https://newsapi.org/)


### MVVM pattern

Today we will follow Android recommended architecture pattern MVVM to structure our code: We will create a Repository as an intermediate container for providing data; The network requests are hidden behind the Repository; We will build ViewModel classes and use LiveData to provide updates for the future UI views.
App architecture
So, have you heard of ... `MVC`, `MVP`, `MVI`, `VIPER`, `MVVM` or `VIPER`?

Do you need one? Not necessarily. Some popular apps scale well without complicated architectures, e.g. (Instagram). On the other end some apps have heavy architectures, e.g. (Uber). There's no right or wrong in each. It comes down to different philosophies. A simple architecture means lean and lightweight both in terms of app performance and app development cycles. A heavy architecture supports more complicated hierarchies and a bigger development team.
We don't need to go into the details of each pattern, the key focus is a simple idea "Separation of Concerns":
Support for more complicated business logic.

<img width="596" alt="06" src="https://user-images.githubusercontent.com/50295329/158078518-eaefab21-7f0f-4f10-a38f-36397fbade78.png">

Support for more complicated business logic.
Lower maintenance cost.
Scaling for team collaborations.
Better testability and Isolation.

Android provides `MVVM` support built into the ecosystem with Jetpack. It's a "opinionated" package, vs iOS which has no preference on architecture choices in most cases. Android makes the effort of adopting `MVVM` very easily. So while your app would be fine without an architecture at an early stage, it's no-brainer to get the architecture right from the beginning. The package also solves other tricky issues like lifecycle handling conveniently.
`MVVM` stands for Model-View-ViewModel, I suggest this good detailed explanation. On a high level:

<img width="575" alt="07" src="https://user-images.githubusercontent.com/50295329/158078526-1f40d381-7741-45c9-b7a7-ec8d92e8a3a5.png">

### Local Database with Room

Overview
Many apps persist data locally. The most popular example would be WeChat. Your chat history won’t be synced to the cloud. Instead, the chat history is kept locally. Today, we will be working on saving the favorite articles to the local database with an ORM framework Room.

Room Introduction
You have seen Hibernate in the Spring MVC framework. Room is a similar annotation-based ORM solution for SQLite on Android. You can save lots of time and effort writing error-prone raw SQL queries by using a much more modern Object-oriented accessors.
But why do you need Room or SQLite on mobile?

Apps doesn’t need network access. (Not many these days)

Apps need to support offline mode. Trello

Hybrid apps consume both network and local generated structure data.


<img width="631" alt="08" src="https://user-images.githubusercontent.com/50295329/158078541-57a912ff-35dd-47dc-93b4-cf6d797c565d.png">


the local database on Android was really bad with direct SQL queries and manual parsing. Room has the following concepts:
Database contains the database holder and serves as the main access point for the underlying connection to your app’s persisted, relational data.
Entity represents a table within the database.
DAO contains the methods used for accessing the database.


### Repository Integration

How does the database work with the rest of the MVVM architecture?

<img width="610" alt="09" src="https://user-images.githubusercontent.com/50295329/158078553-d93c4efa-ff9f-492b-b771-633cf05598c9.png">


### Multiple Thread

<img width="613" alt="10" src="https://user-images.githubusercontent.com/50295329/158078557-b9ba2343-8014-4eaf-afc0-1e4034c9141d.png">

How to handle those heavy works?

Main Thread: All UI Interaction should stay in the UI Thread, eg: TextView.setText(), RecyclerView.notifyDataSetChange()
Background Thread: All the heavy works have to be moved to Background thread, when execution is done, move the data back to UI thread by Handler and consume.

More info About Thread Handling
AsyncTask enables proper and easy use of the UI thread. This class allows you to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers.
