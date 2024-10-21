# Movie Application

This is a movie application built using MVVM (Model-View-ViewModel) architecture, Jetpack Compose, Paging 3, Dagger Hilt, Room, and Coil for modern Android development. The app allows users to search for movies, view movie details, and supports offline functionality.

## Features
- **Movie Listing:** Displays a list of movies in a 2-column grid layout.
- **Search Functionality:** Users can search for movies using a search bar.
- **Movie Details:** Clicking on a movie navigates to a detailed view with additional information.
- **Offline Support:** The app caches movie data using Room, allowing users to access the previously viewed movies even when offline.
- **Swipe to Refresh:** Pull-to-refresh functionality for refreshing the movie list.
- **Paging: Efficiently** loads large sets of data using Paging 3.

## Tech Stack
- **Kotlin:** The main programming language used for the app.
- **Jetpack Compose:** For building the UI in a declarative way.
- **Paging 3:** For efficient data loading and pagination.
- **Dagger Hilt:** For dependency injection.
- **Room:** For local data caching and offline access.
- **Retrofit:** For networking and API calls.
- **Coil:** For image loading.
- **Accompanist:** For additional Compose utilities like Swipe to Refresh.


## Architecture: MVVM
The Movie Application is built using the MVVM (Model-View-ViewModel) architecture pattern, which promotes a clear separation of concerns and easier testing and scalability.

- **Model:** Represents the data layer of the app. This includes the Movie data class, Room database entities, and Retrofit API service.
- **View:** Represents the UI layer of the app, built with Jetpack Compose. The MovieListScreen and MovieDetailScreen are examples of views displaying movie information.
- **ViewModel:** Manages UI-related data and handles the logic to communicate between the View and Model. The MovieViewModel provides data to the views and performs data fetching through the repository.
The MVVM pattern ensures that the UI (View) stays in sync with the data (Model) without needing to directly handle data fetching or persistence.

## Setup and Installation
1. Clone the repository:
```console
git clone https://github.com/blagojBojcevski/MovieApplication.git
cd movie-app
 ```
2. Open the project in Android Studio.

3. Ensure that the following dependencies are correctly installed:

- Kotlin 1.5+
- Android SDK
- Gradle 7+
4. Add your API key for the movie API (like TMDb) in your gradle.properties file:

```console
API_KEY=your_api_key_here
 ```
5. Sync the project with Gradle and build the project.

## API Integration
This app integrates with The Movie Database (TMDb) API to fetch and display movie data.

- Base URL: https://api.themoviedb.org/3/
- Key Endpoints:
   - GET /movie/popular
   - GET /search/movie
   - GET /movie/{movie_id}
   
## Usage
- Launch the app and browse through the popular movies list.
- Search for movies using the search bar.
- Tap on a movie to view its details.
- Pull down to refresh the movie list.

## Offline Support
The app uses Room to cache the movie list locally. When the user is offline, the app will display the cached data.
