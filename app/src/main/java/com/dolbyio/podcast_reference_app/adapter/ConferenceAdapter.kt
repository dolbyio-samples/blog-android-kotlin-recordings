package com.dolbyio.podcast_reference_app.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dolbyio.podcast_reference_app.activity.PodcastSessionActivity
import com.dolbyio.podcast_reference_app.model.PodcastConference
import com.dolbyio.podcast_reference_app.model.UsersHelper
import com.dolbyio.podcast_reference_app.networking.ConferenceSession
import com.voxeet.promise.Promise
import com.voxeet.promise.solve.Solver
import com.voxeet.promise.solve.ThenVoid
import com.voxeet.sdk.models.Conference
import podcast_reference_app.R

class ConferenceAdapter(
    private val mPodcastConferences: List<PodcastConference>,
    val context: Context
) :
    RecyclerView.Adapter<ConferenceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvConferenceName: TextView = itemView.findViewById(R.id.tv_conference_name)
        val btnListen: Button = itemView.findViewById(R.id.btn_listen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val liveSessionView: View =
            inflater.inflate(R.layout.live_session_view_layout, parent, false)
        return ViewHolder(liveSessionView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conference: PodcastConference = mPodcastConferences[position]
        holder.tvConferenceName.text = conference.alias
        holder.btnListen.setOnClickListener {
            // open session info is created with hardcoded Participant info from UsersHelper class
            // in your own application, name, externalId, & avatarUrl would be replaced with your user's own info
            val openPromise = ConferenceSession.openSession(
                UsersHelper.PARTICIPANTS[0].name!!,
                UsersHelper.PARTICIPANTS[0].externalId!!,
                UsersHelper.PARTICIPANTS[0].avatarUrl!!
            )
            listenToConference(openPromise, conference)
        }
    }

    override fun getItemCount(): Int {
        return mPodcastConferences.size
    }

    private fun listenToConference(
        openPromise: Promise<Boolean>,
        podcastConference: PodcastConference
    ) {
        Log.d(TAG, "inside function")
        openPromise
            .then { result: Boolean?, solver: Solver<Any?>? ->
                Log.d(TAG, "joining conference")
                val joinPromise =
                    ConferenceSession.joinConference(conferenceID = podcastConference.conferenceID)
                joinPromise.then<Any>(ThenVoid { conference: Conference? ->
                    val intent = Intent(context, PodcastSessionActivity::class.java)
                    context.startActivity(intent)
                })
                    .error {
                        ConferenceSession.error()
                        Log.e(TAG, "Error joining conference")
                    }
            }
            .error {
                ConferenceSession.error()
                Log.e(TAG, "Error creating join promise")
            }
    }

    companion object {
        private const val TAG = "ConferenceAdapter"
    }
}