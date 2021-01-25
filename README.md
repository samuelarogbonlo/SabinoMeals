# SabinoMeals

An open-source android (native-mobile) application that is currently built with Kotlin, Retrofit, LiveData, Coroutines, Firebase, and an MVVM architecture. Some major tools like RxJava, Databinding, Butterknife, Dexter, GreenRobot, Braintree payment API and other cool stuff. This project used the spiral SDLC to ensure the process of development and integration is continuous. 

# Architecture

![pasted image 0](https://user-images.githubusercontent.com/47984109/105698617-65f61280-5f06-11eb-8e3e-ed710f1423f8.png)

![pasted image 0 (1)](https://user-images.githubusercontent.com/47984109/105698770-a2c20980-5f06-11eb-9491-9ff4b002cd72.png)

The above images shows an illustration of the system architecture of the application to be designed. Basically, system architecture helps in the separation of concerns and logic as well as creating a testable, scalable, and robust code. It also helps in ease control and manipulation of code in case of a mishap in APIs or databases in use. The system architecture to be used in this application is the MVVM model which is one of the most used architectures in application development. The acronym MVVM stands for Model, View, and ViewModel. The model holds the date of the application then the view represents the UI of the application then the ViewModel functions as a link between the Model and the View and transforms data from the model as well as provides data callbacks to update the View.

# Technologies 
* Kotlin: The major native language used to build the application
* Retrofit: A REST Client for Android which makes it relatively easy to retrieve and upload JSON (or other structured data) via a REST based webservice.
* ViewModel: To store and manage UI-related data in a lifecycle conscious way.
* LiveData: To handle data in a lifecycle-aware fashion.
* Navigation Component: To handle all navigations and also passing of data between destinations.
* Coroutines: Used to manage the local storage i.e. writing to and reading from the database. Coroutines help in managing background threads and reduces the need for callbacks.
* Data Binding: To declaratively bind UI components in layouts to data sources.
* Room persistence library: Provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* Android KTX: Helps to write more concise, idiomatic Kotlin code.
* Firebase: Used to host the backend of the application
* ButterKnife: A light weight library to inject views into Android components 

# Contribution

All contributions are welcome. If you are interested in seeing a particular feature implemented in this app, please open a new issue so after which you can make a PR!

# Other Informnation

The application is still under building and will be completed in due time. Meanwhile the major part of the application has been completed. 



