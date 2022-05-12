package com.dolbyio.podcast_reference_app.application

import android.app.Application
import com.dolbyio.podcast_reference_app.networking.ConferenceSession.fetchSecureToken
import com.voxeet.VoxeetSDK

class PodcastApplication : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var bearerToken: String
        lateinit var clientToken: String
        private var instance: PodcastApplication? = null

        fun applicationContext(): PodcastApplication {
            return instance as PodcastApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        fetchSecureToken({ accessToken ->
            val token = accessToken ?: return@fetchSecureToken
            clientToken = token
            VoxeetSDK.initialize(token) { _, callback ->
                fetchSecureToken({ accessToken ->
                    val token = accessToken ?: return@fetchSecureToken
                    clientToken = token
                    callback.ok(token)
                })
            }
        })

        fetchSecureToken({ accessToken ->
            val token = accessToken ?: return@fetchSecureToken
            bearerToken = token
        }, "bearer")

    }
}