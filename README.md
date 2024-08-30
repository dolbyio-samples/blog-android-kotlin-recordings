# :warning: This repository is no longer maintained :warning:

[![License](https://img.shields.io/github/license/dolbyio-samples/blog-android-kotlin-recordings)](LICENSE)

# Android Podcast Recording Sample App

This repository monitors previous recordings using Dolby.io REST API following [this blog](https://dolby.io/blog/build-a-podcast-recording-app-for-android-using-the-dolby-io-monitor-api/).

# Overview
In this application, a Dolby.io Voice Call session will be setup to create a recording of a conference and retrieve a list of previous recordings.

# Requirements
There are a few requirements you should consider like: 
- Android Development Environment - IDE
- [Dolby.io Account](https://dolby.io/)
- Dolby.io Monitor API - [Key and Secret Key](https://docs.dolby.io/communications-apis/docs/guides-api-authentication)
- Android Device or Emulator 

# Getting Started
1. Clone this repository
2. Setup Serverless functions on Netlify -- see [./server/README.md](./server/README.md) for instructions.
4. Open the projejct in Android Studio.
5. Update **ConferenceSession.kt**, **LiveSessionFragment.kt**, and **ReplayFragment.kt** with your Netlify function URLs you deployed.
6. Build the project and run it in an emulator.  *NOTE: To experience audio/video you will need to load onto a physical mobile device.*

# Report a Bug 
In the case any bugs occur, report it using Github issues, and we will see to it. 

# Forking
We welcome your interest in trying to experiment with our repos. 

# Feedback 
If there are any suggestions or if you would like to deliver any positive notes, feel free to open an issue and let us know!

# Learn More
For a deeper dive, we welcome you to review the following:
 - [Communications API](https://docs.dolby.io/communications-apis/docs)
 - [Android Client Overview](https://docs.dolby.io/communications-apis/docs/android-overview)
 - [Getting Started with Android Sample App](https://docs.dolby.io/communications-apis/docs/getting-started-with-android)
 - [Add Conference Audio Moderation Using Hive.ai and the Dolby.io Android SDK](https://dolby.io/blog/add-conference-audio-moderation-using-hive-ai-and-the-dolby-io-android-sdk/)
 - [Using Kotlin to Create Your First Audio Voice Call on Android with Dolby.io](https://dolby.io/blog/using-kotlin-to-create-your-first-audio-voice-call-on-android-with-dolby-io/)
 - [Recording audio on Android with examples](https://dolby.io/blog/recording-audio-on-android-with-examples/)

# About Dolby.io
Using decades of Dolby's research in sight and sound technology, Dolby.io provides APIs to integrate real-time streaming, voice & video communications, and file-based media processing into your applications. [Sign up for a free account](https://dashboard.dolby.io/signup/) to get started building the next generation of immersive, interactive, and social apps.

<div align="center">
  <a href="https://dolby.io/" target="_blank"><img src="https://img.shields.io/badge/Dolby.io-0A0A0A?style=for-the-badge&logo=dolby&logoColor=white"/></a>
&nbsp; &nbsp; &nbsp;
  <a href="https://docs.dolby.io/" target="_blank"><img src="https://img.shields.io/badge/Dolby.io-Docs-0A0A0A?style=for-the-badge&logoColor=white"/></a>
&nbsp; &nbsp; &nbsp;
  <a href="https://dolby.io/blog/category/developer/" target="_blank"><img src="https://img.shields.io/badge/Dolby.io-Blog-0A0A0A?style=for-the-badge&logoColor=white"/></a>
</div>

<div align="center">
&nbsp; &nbsp; &nbsp;
  <a href="https://youtube.com/@dolbyio" target="_blank"><img src="https://img.shields.io/badge/YouTube-red?style=flat-square&logo=youtube&logoColor=white" alt="Dolby.io on YouTube"/></a>
&nbsp; &nbsp; &nbsp; 
  <a href="https://twitter.com/dolbyio" target="_blank"><img src="https://img.shields.io/badge/Twitter-blue?style=flat-square&logo=twitter&logoColor=white" alt="Dolby.io on Twitter"/></a>
&nbsp; &nbsp; &nbsp;
  <a href="https://www.linkedin.com/company/dolbyio/" target="_blank"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=flat-square&logo=linkedin&logoColor=white" alt="Dolby.io on LinkedIn"/></a>
</div>


