package com.dolbyio.podcast_reference_app.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dolbyio.podcast_reference_app.activity.PodcastSessionActivity
import com.dolbyio.podcast_reference_app.networking.ConferenceSession
import com.voxeet.promise.Promise
import com.voxeet.promise.solve.Solver
import com.voxeet.promise.solve.ThenVoid
import com.voxeet.sdk.models.Conference
import podcast_reference_app.databinding.FragmentCreatePodcastBinding

private const val ARG_PARAM1 = "param1"

class CreatePodcastFragment : Fragment() {

    private var param1: String? = null
    private lateinit var binding: FragmentCreatePodcastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePodcastBinding.inflate(inflater, container, false)
        initializeListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.etPodcastName.setText("")
        binding.etHostName.setText("")
    }

    private fun initializeListener() {
        binding.btnCreate.setOnClickListener {
            val podcastName: String = binding.etPodcastName.text.toString()
            val hostName: String = binding.etHostName.text.toString()
            val hostNameWithNoSpaces = hostName.replace(" ", "+")
            val avatarURL =
                "https://ui-avatars.com/api/?size=128&name=$hostNameWithNoSpaces&background=random"
            if (podcastName.isNotEmpty()) {
                val openPromise: Promise<Boolean> = ConferenceSession.openSession(
                    hostName,
                    (1000..9999).random().toString(),
                    avatarURL
                )
                openPromise
                    .then { result: Boolean?, solver: Solver<Any?>? ->
                        Toast.makeText(context, "log in successful", Toast.LENGTH_SHORT)
                            .show()
                        val createPromise = ConferenceSession.createConference(podcastName)
                        createPromise
                            .then<Any>(ThenVoid { conference: Conference? ->
                                Toast.makeText(context, "started...", Toast.LENGTH_SHORT).show()
                                val podcastSessionActivity =
                                    Intent(activity, PodcastSessionActivity::class.java)
                                startActivity(podcastSessionActivity)
                            })
                            .error { error_in: Throwable? ->
                                Toast.makeText(
                                    context,
                                    "Could not create conference",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(TAG, error_in?.localizedMessage ?: "Null")
                            }
                    }
                    .error(ConferenceSession.error())

            } else {
                Toast.makeText(context, "Podcast name can't be empty", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            CreatePodcastFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }

        private const val TAG = "CreatePodcastFragment"
    }
}