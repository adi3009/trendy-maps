## Trendy Maps

A web app developed in Play framework (Scala). A fun project to view twitter trends on a map location.
First tries to center map at user location, center at London otherwise. Double click anywhere on map to know what is trending there.

### Prerequisites

* Create a new [TwitterApp](https://apps.twitter.com/). As trendy maps use app only authentication to fetch trends we will need consumer key and consumer secret.

* Create an account on [MapBox](https://www.mapbox.com/). Then go to [MapBox Editor](https://www.mapbox.com/studio/classic/projects/) and create a new map box editor project.

  * Note down Map ID for the project
  * Also write down default access token (can find out from Home screen or account section)


### Running app
    ./activator run -DTWITTER_CONSUMER_KEY="consumer_key" -DTWITTER_CONSUMER_SECRET="consumer_secret" -DMAPBOX_ID="mapbox_id" -DMAPBOX_ACCESS_TOKEN="mapbox_access_token"
