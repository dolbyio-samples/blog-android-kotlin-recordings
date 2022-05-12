package com.dolbyio.podcast_reference_app.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dolbyio.podcast_reference_app.networking.ConferenceSession
import com.voxeet.VoxeetSDK
import com.voxeet.android.media.MediaStream
import com.voxeet.android.media.stream.MediaStreamType
import com.voxeet.promise.solve.ErrorPromise
import com.voxeet.promise.solve.Solver
import com.voxeet.sdk.events.v2.*
import com.voxeet.sdk.json.RecordingStatusUpdatedEvent
import com.voxeet.sdk.views.VideoView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import podcast_reference_app.R
import podcast_reference_app.databinding.ActivityPodcastSessionBinding


class PodcastSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPodcastSessionBinding
    private var isMuted: Boolean = true
    private var isVideoOn: Boolean = false
    private var isRecording: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPodcastSessionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        updateParticipants()
        initializeListeners()
    }

    override fun onResume() {
        super.onResume()
        updateViews()
        ConferenceSession.register()
    }

    override fun onPause() {
        super.onPause()
        ConferenceSession.unregister()
    }

    private fun updateViews() {
        if (!ConferenceSession.isOpen()) {
            return
        }

        if (ConferenceSession.getCurrentConference() != null) {
            val res =
                if (isMuted) R.drawable.ic_baseline_mic_24 else R.drawable.ic_baseline_mic_off_24
            binding.fabMic.setImageResource(res)

            if (isVideoOn) {
                binding.fabVideo.setImageResource(R.drawable.ic_baseline_videocam_off_24)
                binding.videoHost.visibility = View.VISIBLE
                binding.videoOtherHost.visibility = View.VISIBLE
                binding.tvVideoHost.visibility = View.GONE
                binding.tvOtherVideoHost.visibility = View.GONE
            } else {
                binding.fabVideo.setImageResource(R.drawable.ic_baseline_videocam_24)
                binding.videoHost.visibility = View.GONE
                binding.tvVideoHost.visibility = View.VISIBLE
            }
        }
    }

    private fun updateParticipants() {
        val participantsList = ConferenceSession.getParticipants()
        val names = mutableListOf<String?>()

        for (participant in participantsList) {
            if (participant.streams().size > 0) names.add(participant.info!!.name)
        }
        binding.tvParticipants.text = "Participants: ${TextUtils.join(", ", names)}"
    }

    private fun initializeListeners() {
        binding.fabMic.setOnClickListener { toggleAudio() }

        binding.fabEndPodcast.setOnClickListener { endPodcast() }

        binding.fabRecording.setOnClickListener { recordConference() }

        binding.fabVideo.setOnClickListener { toggleVideo() }
    }

    private fun toggleVideo() {
        if (isVideoOn) {
            onStopVideo()
        } else {
            onStartVideo()
        }
        isVideoOn = !isVideoOn
    }


    private fun onStartVideo() {
        ConferenceSession.startVideo()
            .then { _: Boolean?, _: Solver<Any?>? ->
                updateViews()
                updateStreams()
                Log.d(TAG, "Video turned on")
            }
            .error(error())
    }

    private fun recordConference() {
        if (isRecording) {
            ConferenceSession.stopRecording()?.then {
                Log.d(TAG, "Recording has stopped...")
            }?.error {
                Log.e(TAG, it.localizedMessage)
            }
        } else {
            ConferenceSession.startRecording()?.then {
                Log.d(TAG, "Recording has started...")
            }?.error {
                Log.e(TAG, it.localizedMessage)
            }
        }
        isRecording = !isRecording
    }

    private fun toggleAudio() {
        if (isMuted) {
            muteMic(isMuted)
            Log.d(TAG, "Mic is muted...")
        } else {
            muteMic(isMuted)
            Log.d(TAG, "Mic no longer muted...")
        }
        isMuted = !isMuted
        updateViews()
    }

    private fun endPodcast() {
        val promiseClose = ConferenceSession.closeSession()
        promiseClose
            .then { _: Boolean?, _: Solver<Any?>? ->
                Log.d(TAG, "Session is closed...")
                finish()
            }.error(error())
    }

    private fun muteMic(isMuted: Boolean) {
        ConferenceSession.muteMic(isMuted)
    }

    private fun error(): ErrorPromise? {
        return ErrorPromise { error: Throwable ->
            Toast.makeText(this@PodcastSessionActivity, "ERROR...", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error with conference")
            Log.e(TAG, error.localizedMessage)
            error.printStackTrace()
            updateViews()
        }
    }

    private fun setEnabled(views: List<View>, enabled: Boolean) {
        for (view in views) view.isEnabled = enabled
    }

    private fun add(list: MutableList<View>, id: Int): PodcastSessionActivity? {
        list.add(findViewById(id))
        return this
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ParticipantAddedEvent?) {
        updateParticipants()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ParticipantUpdatedEvent?) {
        updateParticipants()
    }

    private fun updateStreams() {
        for (user in ConferenceSession.getParticipants()) {
            val isLocal = user.id == VoxeetSDK.session().participantId
            val stream: MediaStream? = user.streamsHandler().getFirst(MediaStreamType.Camera)
            val video: VideoView = if (isLocal) binding.videoHost else binding.videoOtherHost
            if (null != stream && stream.videoTracks().isNotEmpty()) {
                video.visibility = View.VISIBLE
                video.attach(user.id!!, stream)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: StreamAddedEvent?) {
        updateStreams()
        updateViews()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: StreamUpdatedEvent?) {
        updateStreams()
        updateViews()
    }

    private fun onStopVideo() {
        ConferenceSession.stopVideo()
            .then { _: Boolean?, _: Solver<Any?>? ->
                updateViews()
                Log.d(TAG, "Video turned off")
            }
            .error(error())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: StreamRemovedEvent?) {
        updateStreams()
        updateViews()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RecordingStatusUpdatedEvent) {
        var message: String? = null
        when (event.recordingStatus) {
            "RECORDING" -> message = "Recording started"
            "NOT_RECORDING" -> message = "Recording stopped"
            else -> {
            }
        }
        if (null != message) {
            Toast.makeText(this@PodcastSessionActivity, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, message)
        }
    }

    companion object {
        private const val TAG = "PodcastSessionActivity"
    }
}