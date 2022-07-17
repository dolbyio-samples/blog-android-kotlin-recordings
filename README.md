
# Android Podcast Recording Sample App

This application demonstrates:

- How to setup a Dolby.io Voice Call session
- Create a recording of a conference
- Retrieve a list of previous recordings using the Dolby.io Monitor REST API

You can learn more about this application from the blog post:
https://dolby.io/blog/build-a-podcast-recording-app-for-android-using-the-dolby-io-monitor-api/

## Getting Started

Star or watch this repository so you won't forget about it when we push updates.

1. Clone this repository
2. Setup Serverless functions on Netlify -- see [./server/README.md](./server/README.md) for instructions.
4. Open the projejct in Android Studio.
5. Update **ConferenceSession.kt**, **LiveSessionFragment.kt**, and **ReplayFragment.kt** with your Netlify function URLs you deployed.
6. Build the project and run it in an emulator.  *NOTE: To experience audio/video you will need to load onto a physical mobile device.*


