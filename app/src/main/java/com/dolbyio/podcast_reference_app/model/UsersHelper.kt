package com.dolbyio.podcast_reference_app.model

import com.voxeet.sdk.json.ParticipantInfo

object UsersHelper {

    val PARTICIPANTS = arrayListOf<ParticipantInfo>(
        ParticipantInfo("Alexis", "6664", "https://cdn.voxeet.com/images/team-alexis.png"),
        ParticipantInfo(
            "Benoit",
            "1114",
            "https://cdn.voxeet.com/images/team-benoit-senard.png"
        ),
        ParticipantInfo("Barnabé", "7774", "https://cdn.voxeet.com/images/team-barnabe.png"),
        ParticipantInfo("Corentin", "8884", "https://cdn.voxeet.com/images/team-corentin.png"),
        ParticipantInfo(
            "Julie",
            "5554",
            "https://cdn.voxeet.com/images/team-julie-egglington.png"
        ),
        ParticipantInfo("Raphael", "4444", "https://cdn.voxeet.com/images/team-raphael.png"),
        ParticipantInfo("Romain", "9994", "https://cdn.voxeet.com/images/team-romain.png"),
        ParticipantInfo(
            "Stephane",
            "2224",
            "https://cdn.voxeet.com/images/team-stephane-giraudie.png"
        ),
        ParticipantInfo("Thomas", "3334", "https://cdn.voxeet.com/images/team-thomas.png")
    )
}