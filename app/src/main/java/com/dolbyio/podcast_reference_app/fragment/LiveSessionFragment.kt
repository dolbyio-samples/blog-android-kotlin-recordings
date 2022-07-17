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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dolbyio.podcast_reference_app.adapter.ConferenceAdapter
import com.dolbyio.podcast_reference_app.application.PodcastApplication
import com.dolbyio.podcast_reference_app.model.PodcastConference
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import podcast_reference_app.R


private const val ARG_PARAM1 = "param1"

class LiveSessionFragment : Fragment() {
    private lateinit var mPodcastConferences: ArrayList<PodcastConference>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mAdapter: ConferenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_live_session, container, false)
        val rvLiveSessions = view.findViewById<RecyclerView>(R.id.rv_live_sessions)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(refreshListener)
        mPodcastConferences = arrayListOf()
        fetchConferences()
        mAdapter = ConferenceAdapter(mPodcastConferences, view.context)
        rvLiveSessions.adapter = mAdapter
        rvLiveSessions.layoutManager = LinearLayoutManager(context)
        return view
    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefreshLayout.isRefreshing = true
        fetchConferences()
    }

    private fun fetchConferences() {
        val url = "https://your-token-server.netlify.app/api/getConferencesInfo/"
        val queue = Volley.newRequestQueue(PodcastApplication.applicationContext())
        val gson = GsonBuilder().setPrettyPrinting().create()
        val request = object: JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                swipeRefreshLayout.isRefreshing = false
                val jsonArray = it.get("conferences") as JSONArray
                for (i in 0 until jsonArray.length()) {
                    val currentConference = jsonArray.get(i) as JSONObject
                    mPodcastConferences.add(PodcastConference.fromJSON(currentConference))
                }
                if (mPodcastConferences.isEmpty()) {
                    Toast.makeText(
                        PodcastApplication.applicationContext(),
                        "There are no live sessions at the moment...",
                        Toast.LENGTH_LONG
                    ).show()
                }
                mAdapter.notifyDataSetChanged()
                val json = gson.toJson(mPodcastConferences) // pretty print out json
            },
            {
                Log.e("LiveSessionFragment", it.localizedMessage)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["appidentifier"] = PodcastApplication.applicationContext().packageName
                return headers
            }
        }
        queue.add(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            LiveSessionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
