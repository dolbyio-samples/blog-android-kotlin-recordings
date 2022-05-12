package com.dolbyio.podcast_reference_app.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.dolbyio.podcast_reference_app.adapter.RecordingsAdapter
import com.dolbyio.podcast_reference_app.application.PodcastApplication
import com.dolbyio.podcast_reference_app.model.Recording
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import podcast_reference_app.R

private const val ARG_PARAM1 = "param1"

class ReplayFragment : Fragment() {
    private var param1: String? = null
    private lateinit var mRecordings: ArrayList<Recording>
    private lateinit var mAdapter: RecordingsAdapter

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
        var view: View = inflater.inflate(R.layout.fragment_replay, container, false)
        val rvRecordings = view.findViewById<RecyclerView>(R.id.rv_replay)
        mRecordings = arrayListOf()
        fetchRecordings()
        mAdapter = RecordingsAdapter(mRecordings, view.context)
        rvRecordings.adapter = mAdapter
        rvRecordings.layoutManager = LinearLayoutManager(context)
        return view
    }

    private fun fetchRecordings() {
        val url = "https://android-di-token-server.netlify.app/api/getRecordings/"
        val queue = Volley.newRequestQueue(PodcastApplication.applicationContext())
        val gson = GsonBuilder().setPrettyPrinting().create() // Used for json pretty print
        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            {
                val jsonArray = it as JSONArray
                for (i in 0 until jsonArray.length()) {
                    val currentRecording = jsonArray.get(i) as JSONObject
                    mRecordings.add(Recording.fromJSONRecordingObject(currentRecording))
                }
                if (mRecordings.isEmpty()) {
                    Toast.makeText(
                        PodcastApplication.applicationContext(),
                        "There are no recordings moment...",
                        Toast.LENGTH_LONG
                    ).show()
                }
                mAdapter.notifyDataSetChanged()
            },
            {
                Log.e("ReplayFragment", it.localizedMessage)
            }
        )

        queue.add(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ReplayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}