# Lunch

[![License Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-green.svg)](https://github.com/ustwo/android-boilerplate/blob/master/LICENSE.md)


## Description
This is a 1-day project for exploring a more Rx-driven MVP architecture.

Find a decent place to eat lunch! 

Enter a food and a location (autocompleted) into a text box, select the sort order, and click Search to find places in the area. The place search and location autocomplete is done via Google Maps APIs.

## Architecture Overview
I wanted to try to be as reactive as possible in this project, but likely to a fault. Still, some areas could benefit from being more reactive, while in others it seems cumbersome. This was my first time using RxJava2.

Presenters (via `@Inject`-constructors) and are injected into fragments/views.

All the presenters' dependencies are injected only through the constructor, including Rx schedulers, which supports testing.

Interaction between presenters and views is reactive. In other words, a view interface exposes e.g. an Observable or a Subject, and the presenter observes view events on that stream. Conversely, the presenter exposes its own streams for the view to observe. This was intended to try to reduce coupling between the view and the presenter. The drawback is that tests are difficult to set up, and code is tricker to follow. I haven't used this pattern before, and the lesson is that it should be used with care because it can make it difficult to follow the flow when writing tests or debugging.

The interaction between presenters is actually a bit tricky, as we're trying to avoid coupling. My approach was to use a shared dependency that exposes an observable stream (see `api.places.PlacesProvider`). This allows multiple presenters to kick off a place search (e.g. if a refresh is needed), and multiple observers to a set of results.

The API calls are obviously managed and chained by Rx. For example, when an address is entered and we perform a search for nearby places, this series of steps takes place: 
1. Observe search start (e.g. button click, throttled w/Rx)
2. Publish that search has started
3. API call to geocode the entered address into lat/long
4. Publish geocoded location name (so that e.g. search field can fill it in, and user knows where we're searching)
5. API call to get nearby places
6. Sort results manually if API doesn't support the sorting method we need
7. Publish result
8. Publish that search ended (for e.g. progress indicators)


Frameworks used:
* Dependency injection via Dagger 2
* Reactive views and presenters via RxJava2 with RxAndroid and RxBinding
* Layout injection via Butterknife
* Test mocks via Mockito
* UI test interactions via Espresso


## Status
I did not build the details views yet, but a selected item can be observed and the view details API is in. Maybe at some point :)

There is only one unit tested class. I wanted to understand how testing changed with RxJava2.

Because the UI is rudimentary now, there is only one Espresso UI test. I put it in to have boilerplate in place. The boilerplate sets up the page object pattern of testing.

Data binding framework wasn't used, got away with Butterknife for now. If I had built out the various Adapters properly instead of using simple ones, DB would be extremely beneficial and concise.

Error handling pretty much just shows a toast and logs via Timber. A conditional retry or emitting default values on error would be a good next step.

Because views are reactive now, and there's a certain amount of logic taking place in them, I would decouple the MVP views from Android entirely. This would 1. allow injecting the views and 2. allow unit-testing some view logic, resulting in better overall coverage. This can be done by creating a POJO view base, and some kind of a "view host" interface to be implemented by Android hosts.

Since there's just really one screen (i.e. all elements are always visible), dependency scopes don't play a big role, but expanding the applications to other screens would require more careful scoping. Only the results dependencies have a scope (`results.di.ResultsScope`).

The client configuration is all in the app (e.g. not remote, and not obfuscated), but design allows for extending it.


## Acknowledgements
[ustwo Android boilerplate](https://github.com/ustwo/android-boilerplate)


## Keystore
The release keys are encrypted. To decrypt: 
```
openssl aes-256-cbc -d -in release.cipher -out release.keystore -k THE_SECRET_KEY
```


## License
[Apache 2.0](https://github.com/ustwo/android-boilerplate/blob/master/LICENSE.md)

## Contact
[Vasiliy Kulakov](mailto:one@kulakov.us)
