# Popular Movies
Android app to show popular movies (Created for Udacity android nanodegree)

### API Key
Replace "<API_KEY>" on FetchMoviesTask line 66 to your [themoviedb.org][1] API key before running this app. You can get one at [The Movie DB][1].

### Functionalities
 1. Present the user with a grid arrangement of movie posters upon launch.
 2. Allow your user to change sort order via a setting:
    - The sort order can be by most popular or by highest-rated
    - Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
       * original title
       * movie poster image thumbnail
       * A plot synopsis (called overview in the api)
       * user rating (called vote_average in the api)
       * release date

### Libraries
  * Uses [Picasso][2] to load images to UI

### Screenshots

<img src="https://github.com/biamacedo/popular-movies-android/blob/master/screenshots/screenshot1.png" width="200">
<img src="https://github.com/biamacedo/popular-movies-android/blob/master/screenshots/screenshot2.png" width="200">
<img src="https://github.com/biamacedo/popular-movies-android/blob/master/screenshots/screenshot3.png" width="200">
<img src="https://github.com/biamacedo/popular-movies-android/blob/master/screenshots/screenshot4.png" width="200">


[1]: https://www.themoviedb.org

[2]: http://square.github.io/picasso/
