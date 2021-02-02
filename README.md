# General Info
This is an android native Java application from the book The Big Nerd Ranch Guide Android Programming,3rd Edition. It draws and displays a recent Flickr photo from the location of
the phone on location zoomed in Google Map.

The project's main targets are
- create and open an HTTPURL connection (somewhat similar to XMLHttpRequest), make queries using Flickr REST API flickr.photos.getRecent with location parameters
- use Google Map Services through Google Play Store API
- receive and parse the inputstream in JSON format asynchronously.
- download, cache the first recent photo
= draw the photo on top of location zoomed-in Google Map and present.


I added the followings to the standard code described in the book
- parse the received JSON responses using GSON library instead of the JSONObject library.
- add an LRUCache so that it downloads and caches the previous and the next 10 photos to achieve smooth scrolling performance.
- add progress bar.

# Technology
- Java
- Android SDK
- HTTP/REST (Flickr REST API)
- GSON

# Setup
Please follow the guidelines 
https://developer.android.com/studio/run





