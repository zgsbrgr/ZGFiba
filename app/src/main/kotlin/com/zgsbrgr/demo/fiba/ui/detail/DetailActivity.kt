package com.zgsbrgr.demo.fiba.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.zgsbrgr.demo.fiba.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.detail_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(
            R.navigation.detail_nav, intent.extras
        )

        val topLevel: Set<Int> = setOf()
        appBarConfiguration = AppBarConfiguration(
            topLevel
        )

        handleBackPress()

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController.currentDestination?.id == navController.graph.startDestinationId)
                        finish()
                    else
                        onSupportNavigateUp()
                }
            }
        )
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
