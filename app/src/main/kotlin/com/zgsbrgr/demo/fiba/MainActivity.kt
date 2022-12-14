package com.zgsbrgr.demo.fiba

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
import com.google.android.material.snackbar.Snackbar
import com.zgsbrgr.demo.fiba.databinding.ActivityMainBinding
import com.zgsbrgr.demo.fiba.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel by viewModels<MyActivityViewModel>()

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(binding.root)

        val topLevelDestinations = setOf(
            R.id.home
        )

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinations
        )

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
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (topLevelDestinations.contains(destination.id)) supportActionBar?.hide() else supportActionBar?.show()
//        }

        // setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(
            navController
        ) || super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "Main"
    }
}
