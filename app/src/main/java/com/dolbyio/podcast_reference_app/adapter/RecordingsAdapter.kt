package com.dolbyio.podcast_reference_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dolby.voice.devicemanagement.utils.Utils
import com.dolbyio.podcast_reference_app.model.Recording
import podcast_reference_app.R

class RecordingsAdapter(
    private val mRecordings: List<Recording>,
    val context: Context
) : RecyclerView.Adapter<RecordingsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAlias: TextView = itemView.findViewById(R.id.tv_podcast_alias)
        val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordingsAdapter.ViewHolder {
        val context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val recordingView: View =
            inflater.inflate(R.layout.recording_layout, parent, false)
        return ViewHolder(recordingView)
    }

    override fun onBindViewHolder(holder: RecordingsAdapter.ViewHolder, position: Int) {
        val recording: Recording = mRecordings[position]
        holder.tvAlias.text = recording.alias
        val minutes = context.resources.getQuantityString(
            R.plurals.minutes,
            recording.minutes,
            recording.minutes
        )
        val seconds = context.resources.getQuantityString(
            R.plurals.seconds,
            recording.seconds,
            recording.seconds
        )
        var time = if (recording.minutes > 0 && recording.seconds == 0) {
            minutes
        } else if (recording.minutes == 0 && recording.seconds > 0) {
            seconds
        } else {
            "$minutes & $seconds"
        }
        holder.tvDuration.text = time
    }

    override fun getItemCount(): Int {
        return mRecordings.size
    }
}