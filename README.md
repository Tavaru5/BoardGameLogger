### About
A side project for keeping track of board games you've played along with scores (locally on your device).

Currently pretty WIP:
* Logging works, but logged games can't be viewed yet.
* The homepage shows my personal [BoardGameGeek collection](https://boardgamegeek.com/collection/user/tavarus)
* Game pages are mid-rework.

Issues are tracked within the repo using github's issue system.

### Tech used
* Jetpack Compose (+ compose navigation, material3 themes, and [Coil for images](https://coil-kt.github.io/coil/) )  for the UI
* [Retrofit](https://square.github.io/retrofit/) for network requests
* [BoardGameGeek XML API](https://boardgamegeek.com/wiki/page/BGG_XML_API2) for board game information
* [TikXML](https://github.com/Tickaroo/tikxml) for XML parsing
* [Hilt](https://dagger.dev/hilt/) for dependency injection
* [Room](https://developer.android.com/jetpack/androidx/releases/room) for local databases

### Contributing
Since this is mainly a side project to keep myself occupied, I'm not really looking for contributions.

Feel free to fork and make it your own though! For the BGG API to work you'll need a BGG API key (explained [here](https://boardgamegeek.com/using_the_xml_api)) in a local `secrets.properties` file.
