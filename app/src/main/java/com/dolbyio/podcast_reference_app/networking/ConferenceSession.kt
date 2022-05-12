package com.dolbyio.podcast_reference_app.networking

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dolbyio.podcast_reference_app.application.PodcastApplication
import com.voxeet.VoxeetSDK
import com.voxeet.promise.Promise
import com.voxeet.promise.PromiseInOut
import com.voxeet.promise.solve.ErrorPromise
import com.voxeet.promise.solve.ThenPromise
import com.voxeet.sdk.json.ParticipantInfo
import com.voxeet.sdk.json.internal.ParamsHolder
import com.voxeet.sdk.models.Conference
import com.voxeet.sdk.models.Participant
import com.voxeet.sdk.services.builders.ConferenceCreateOptions
import com.voxeet.sdk.services.builders.ConferenceJoinOptions
import com.voxeet.sdk.services.conference.information.ConferenceInformation

/**
 * Singleton helper class for all functions related to Dolby.io Communications SDK
 */

object ConferenceSession {

    const val TAG = "ConferenceSession"

    fun fetchSecureToken(callback: (token: String?) -> Unit, tokenType: String = "client") {
        val serverURL =
            "https://android-di-token-server.netlify.app/.netlify/functions/getTokenGenerator"
        val queue = Volley.newRequestQueue(PodcastApplication.applicationContext())

        var request = object : JsonObjectRequest(
            Method.POST, serverURL, null,
            Response.Listener { response ->
                val token = response.get("access_token")
                PodcastApplication.clientToken = token as String
                Log.d(TAG, "Access token is: $token")
                callback(token)
            },
            Response.ErrorListener {
                Log.e(TAG, "Request failed: ${it.localizedMessage}")
                callback(null)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["appidentifier"] = PodcastApplication.applicationContext().packageName
                headers["authtoken"] = tokenType
                return headers
            }
        }

        queue.add(request)
    }

    fun createConference(
        conferenceName: String
    ): PromiseInOut<Conference, Conference> {
        val paramsHolder = ParamsHolder()
        paramsHolder.setVideoCodec("VP8")
        paramsHolder.setDolbyVoice(true)
        paramsHolder.setLiveRecording(true)

        val conferenceCreateOptions = ConferenceCreateOptions.Builder()
            .setConferenceAlias(conferenceName)
            .setParamsHolder(paramsHolder)
            .build()

        val createPromise = VoxeetSDK.conference().create(conferenceCreateOptions)
        return joinCall(createPromise)
    }

    fun joinConference(conferenceID: String): PromiseInOut<Conference, Conference> {
        val currentConference = getCurrentConferenceObject(conferenceID)
        val conferenceJoinOptions = ConferenceJoinOptions.Builder(currentConference).build()
        val joinPromise = VoxeetSDK.conference().join(conferenceJoinOptions)
        return joinCall(joinPromise)
    }

    private fun joinCall(conferencePromise: Promise<Conference>): PromiseInOut<Conference, Conference> {
        val joinPromise = conferencePromise.then(ThenPromise { conference ->
            val conferenceJoinOptions: ConferenceJoinOptions =
                ConferenceJoinOptions.Builder(conference).build()
            Log.d("$TAG conference alias: ", conference.alias.toString())
            return@ThenPromise VoxeetSDK.conference().join(conferenceJoinOptions)
        })
        return joinPromise
    }

    fun error(): ErrorPromise {
        return ErrorPromise { error: Throwable ->
            Log.e(TAG, "Error with conference")
            Log.e(TAG, error.printStackTrace().toString())
            Log.e(TAG, error.localizedMessage)
        }
    }

    fun register() {
        VoxeetSDK.instance().register(this)
    }

    fun unregister() {
        VoxeetSDK.instance().unregister(this)
    }

    fun muteMic(isMuted: Boolean) {
        VoxeetSDK.conference().mute(isMuted)
    }

    private fun getCurrentConferenceObject(conferenceID: String): Conference {
        return VoxeetSDK.conference().getConference(conferenceID)
    }

    fun openSession(
        name: String,
        externalID: String = "",
        avatarURL: String = ""
    ): Promise<Boolean> {
        return VoxeetSDK.session().open(ParticipantInfo(name, externalID, avatarURL))
    }

    fun closeSession(): Promise<Boolean> {
        VoxeetSDK.conference().leave()
        return VoxeetSDK.session().close()
    }

    fun getParticipants(): MutableList<Participant> {
        return VoxeetSDK.conference().participants
    }

    fun startVideo(): Promise<Boolean> {
        return VoxeetSDK.conference().startVideo()
    }

    fun stopVideo(): Promise<Boolean> {
        return VoxeetSDK.conference().stopVideo()
    }

    fun startRecording(): Promise<Boolean>? {
        return VoxeetSDK.recording().start()
    }

    fun stopRecording(): Promise<Boolean>? {
        return VoxeetSDK.recording().stop()
    }

    fun isOpen(): Boolean {
        return VoxeetSDK.session().isOpen
    }

    fun getCurrentConference(): ConferenceInformation? {
        return VoxeetSDK.conference().currentConference
    }
}