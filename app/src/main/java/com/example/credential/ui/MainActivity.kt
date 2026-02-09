package com.example.credential.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.credential.R
import com.example.credential.utils.extensions.addFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openFragment()
        window.statusBarColor = ContextCompat.getColor(this, R.color.start_color)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.end_color)
    }

    private fun openFragment() {
        supportFragmentManager.addFragment(ListFragment.newInstance(), R.id.fragment_container)
    }
}