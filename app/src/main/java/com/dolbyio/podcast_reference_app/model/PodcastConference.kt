package com.dolbyio.podcast_reference_app.model

import org.json.JSONObject

data class PodcastConference(
    val alias: String,
    val conferenceID: String,
    val avatarURL: String,
) {
    companion object {
        fun fromJSON(obj: JSONObject): PodcastConference {
            return PodcastConference(
                conferenceID = obj.getString("confId"),
                alias = obj.getString("alias"),
                avatarURL = "https://avatars.dicebear.com/api/initials/Example.svg"
            )
        }
    }
}