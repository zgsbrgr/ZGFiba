package com.zgsbrgr.demo.fiba.ui.matches

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.zgsbrgr.demo.fiba.MyActivityViewModel
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.ActivityMatchesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchesActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel by viewModels<MyActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMatchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isOffline.collect { notConnected ->
                    if (notConnected) {
                        Snackbar
                            .make(
                                binding.root,
                                resources.getString(R.string.not_connected),
                                Snackbar.LENGTH_LONG
                            )
                            .show()
                    }
                }
            }
        }
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
