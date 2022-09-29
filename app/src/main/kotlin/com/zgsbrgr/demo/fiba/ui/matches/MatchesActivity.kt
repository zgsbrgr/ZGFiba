package com.zgsbrgr.demo.fiba.ui.matches

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.ActivityMatchesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding = ActivityMatchesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.matches_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(
            R.navigation.matches_nav,
            intent.extras
        )

        appBarConfiguration = AppBarConfiguration(
            setOf()
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (navController.currentDestination?.id == navController.graph.startDestinationId) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else navController.navigateUp(appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(
            navController
        ) || super.onOptionsItemSelected(item)
    }
}
