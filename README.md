### About
A side project for keeping track of your times playing board games (locally on your device).

Currently very WIP; it displays my personal board game collection from [BoardGameGeek](https://boardgamegeek.com/collection/user/tavarus), the screens aren't finished, and there isn't any actual logging yet.

### Tech used
* Jetpack Compose (+ compose navigation, material3 themes, and [Coil for images](https://coil-kt.github.io/coil/) )  for the UI
* [Retrofit](https://square.github.io/retrofit/) for network requests
* [BoardGameGeek XML API](https://boardgamegeek.com/wiki/page/BGG_XML_API2) for board game information
* [TikXML](https://github.com/Tickaroo/tikxml) for XML parsing
* [Hilt](https://dagger.dev/hilt/) for dependency injection

### Broad TODO
* Create logging screen for individual games (including optional scores/notes/players/game expansions)
* Persist logged game + player info in local database
* Ask for user input for default collection (either their BGG collection or blank?)
* Search + add games to collection
* Stats screen! Stuff like most played games, which players won most in the games you played, game genre that you scored the lowest in, etc (potentially could use BGG's tags for this).
