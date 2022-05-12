package com.dolbyio.podcast_reference_app.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.dolbyio.podcast_reference_app.fragment.CreatePodcastFragment
import com.dolbyio.podcast_reference_app.fragment.LiveSessionFragment
import com.dolbyio.podcast_reference_app.fragment.ReplayFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import podcast_reference_app.R
import podcast_reference_app.databinding.ActivityMainBinding

const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: ChipNavigationBar

    private val permissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(findViewById(binding.myToolbar.id))
        bottomNav = binding.bottomNavBar
        bottomNav.setItemSelected(R.id.createPodcastFragment)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, CreatePodcastFragment.newInstance("Initial transaction"))
            .commit()

        bottomNav.setOnItemSelectedListener {
            when (it) {
                R.id.createPodcastFragment -> replaceFragment(CreatePodcastFragment.newInstance("CreatePodcastFragment"))
                R.id.livePodcastsFragment -> replaceFragment(LiveSessionFragment.newInstance("LiveSessionFragment"))
                R.id.replayFragment -> replaceFragment(ReplayFragment.newInstance("ReplayFragment"))
            }
        }

        requestPermissions()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.container.id, fragment).commit()
    }

    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@MainActivity, permissions, REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            permissionGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }
}