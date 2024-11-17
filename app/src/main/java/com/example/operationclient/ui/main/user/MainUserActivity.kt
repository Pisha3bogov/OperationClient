package com.example.operationclient.ui.main.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.operationclient.R
import com.example.operationclient.databinding.ActivityMainAdminBinding
import com.example.operationclient.databinding.ActivityMainUserBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainUserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main_user)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.addFragmentUser, R.id.reportFragmentUser, R.id.operationFragmentUser
            )
        )

        val toolbar = Toolbar(this)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



    }
}