# MovieTrack

MovieTrack is an Android application that leverages modern Android development tools. Designed with a clean architecture approach, the application aims to provide a robust, maintainable, and scalable framework. This project showcases the implementation of the MVVM pattern, using LiveData and Jetpack Compose for a responsive and interactive UI.

## Key Features

- **MVVM Architecture**: Utilizes the Model-View-ViewModel (MVVM) pattern to ensure a separation of concerns and improve the manageability of the code.
- **Jetpack Compose**: Employs Jetpack Compose to build the UI, enabling a more concise and reactive approach compared to traditional XML-based layouts.
- **LiveData**: Uses LiveData to observe data changes and handle UI updates, ensuring lifecycle safety.
- **Hilt for Dependency Injection**: Implements Hilt for dependency injection to decouple the application components and improve scalability.

## Technologies Used

- **Kotlin**: As the primary language for Android development, Kotlin offers safety features like nullability and immutability, which help to prevent common bugs.
- **Android Studio Canary**: Developed using the latest version of Android Studio Canary to access cutting-edge tools and features.
- **Gradle 8.4**: Uses Gradle 8.4 for efficient and reliable build management.






<img src="https://github.com/dhananjayandroid/MovieTrack/assets/12845736/bbaf5a8c-2e52-4af9-a152-04d8d409f042" height="512">
<img src="https://github.com/dhananjayandroid/MovieTrack/assets/12845736/a1a03b83-101b-4859-88a9-ad4063b4b91e" height="512">
<img src="https://github.com/dhananjayandroid/MovieTrack/assets/12845736/e463777b-7def-4e56-882f-de0e700c94db" height="512">
<img src="https://github.com/dhananjayandroid/MovieTrack/assets/12845736/0ccbc057-a3a0-4a6b-ac00-19f15ef4e719" height="512">



## BitRise CI

Last distributed APK from the Bitrise distribution cab be downloaded from [here](https://app.bitrise.io/app/482c85c2-44b8-451e-adfc-3f842190d60f/build/205f1f05-5155-4fd8-8c33-312a45328cea/artifact/4c5dd09d0ad44f5c/p/14a0ae71833e6062815878e39af4f197).

<img src="https://github.com/dhananjayandroid/MovieTrack/assets/12845736/72272dfe-09ef-4396-b7b0-e6e979e6c976">


## Project Structure

The app is organized into several packages, each with a clear responsibility:

- **`data`**: This is the data layer of the app, structured as follows:
  - **`datasource`**:
    - **`local`**:
      - `dao`: Contains `MovieDao` for database access.
      - `AppDatabase`: Manages app's SQLite database and serves as the app's main access point to the persisted data.
      - `DataStoreManager`: Manages data storage using the DataStore library.
    - **`remote`**:
      - `ApiURL`: Holds the base URL for the API.
      - `MoviesApiService`: Defines the endpoints for the Retrofit API service.

- **`model`**: Holds data classes that represent the entities in the app. Each class in this package is fully tested.
  - `Movie`: Data class representing a movie object.
  - `MovieResponse`: Model for the movie response from the API.

- **`repository`**: Contains `MovieRepository`, which provides a clean API for data access to the rest of the app. It has nearly full test coverage.

- **`di`**: Dependency injection modules for providing app-wide dependencies:
  - `AppModule`: Defines app-level dependencies.
  - `RepositoryModule`: Provides dependencies related to data repository.

- **`ui`**: The UI layer of the app, broken down into:
  - **`screens`**:
    - `activity`:
      - `MainActivity`: The main activity that hosts the app's UI.
      - `MoviesViewModel`: Manages UI-related data for the MainActivity and survives configuration changes.
    - **`widgets`**:
      - `LoadingIndicator`: Custom composable for displaying loading animations.
      - `MovieCard`: Composable that displays a movie's information.
      - `SearchAppBar`: Custom composable for the app's top search bar.
      - `MovieDetailScreen`: Detailed view for movie information.
      - `MovieListScreen`: Screen that displays the list of movies.

- `MovieTrackApplication`: Custom Application class to initialize app-wide components like Hilt.

## Getting Started

To get a local copy up and running, follow these simple steps:

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/MoviesApp.git

2. Open the project in Android Studio:
   Ensure that you have the latest Canary version of Android Studio installed to support all features and dependencies.

3.  Build the project:
    Navigate to the root directory of the project in your terminal and run:
     ```bash
     ./gradlew assembleDebug

4. Run the application:
   Launch the app on an emulator or a physical device.


## Contribution

Contributions are welcome! Please read the contribution guidelines for how to contribute to the project.


## License

This project is licensed under the MIT License - see the LICENSE file for details.
