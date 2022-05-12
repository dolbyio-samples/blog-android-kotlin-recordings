package com.dolbyio.podcast_reference_app.model

import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class Recording(
    val alias: String,
    val confId: String,
    val minutes: Int,
    val seconds: Int
) {
    companion object {
        fun fromJSONRecordingObject(obj: JSONObject): Recording {
            return Recording(
                alias = obj.getString("alias"),
                confId = obj.getString("confId"),
                minutes = TimeUnit.MILLISECONDS.toMinutes(obj.getInt("duration").toLong()).toInt(),
                seconds = (TimeUnit.MILLISECONDS.toSeconds(obj.getInt("duration").toLong()) -
                        TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(
                                obj.getInt("duration").toLong()
                            )
                        )).toInt(),
            )
        }
    }
}