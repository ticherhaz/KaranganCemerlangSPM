package net.ticherhaz.karangancemerlangspm.version2025.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.ticherhaz.karangancemerlangspm.JenisKaranganActivity
import net.ticherhaz.karangancemerlangspm.PeribahasaActivity
import net.ticherhaz.karangancemerlangspm.TipsKaranganActivity
import net.ticherhaz.karangancemerlangspm.databinding.Version2025ActivityMainBinding
import net.ticherhaz.karangancemerlangspm.version2025.adapter.MenuAdapter
import net.ticherhaz.karangancemerlangspm.version2025.enumerator.PermissionNotificationCondition
import net.ticherhaz.karangancemerlangspm.version2025.retrofit.Resource
import net.ticherhaz.karangancemerlangspm.version2025.tools.DialogUtils
import net.ticherhaz.karangancemerlangspm.version2025.tools.Tools.goPlayStore
import net.ticherhaz.karangancemerlangspm.version2025.tools.Tools.launchKcspmLiteApp
import net.ticherhaz.karangancemerlangspm.version2025.tools.Tools.showToast
import net.ticherhaz.karangancemerlangspm.version2025.ui.donation.DonationActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: Version2025ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val menuAdapter: MenuAdapter by lazy {
        MenuAdapter(object : MenuAdapter.MenuAdapterCallback {
            override fun onKCSPMLiteClicked() {
                launchKcspmLiteApp()
            }

            override fun onEssayListClicked() {
                startActivity(Intent(this@MainActivity, JenisKaranganActivity::class.java))
            }

            override fun onEssayTipsClicked() {
                startActivity(Intent(this@MainActivity, TipsKaranganActivity::class.java))
            }

            override fun onIdiomClicked() {
                startActivity(Intent(this@MainActivity, PeribahasaActivity::class.java))
            }

            override fun onForumClicked() {
                showToast("Coming Soon")
                //startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
            }

            override fun onDonationClicked() {
                startActivity(Intent(this@MainActivity, DonationActivity::class.java))
            }

            override fun onSettingsClicked() {
                showToast("Coming Soon")
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = Version2025ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        initWindowInsets()

        initAdView()

        binding.buttonCrash.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        startLifeCycle()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            askNotificationPermission()
        }
    }

    private fun startLifeCycle() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    collectStateMenu()
                }
                launch {
                    collectStateAnnouncement()
                }
                launch {
                    collectStateAppVersion()
                }
            }
        }
    }

    private suspend fun collectStateAppVersion() {
        viewModel.appVersionState.collect {
            when (it) {
                is Resource.Initialize -> {
                }

                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    it.data?.let { system ->
                        if (!system.mod) {
                            showToast("System is under maintenance.")
                        }

                        // TODO: Version right now is 520. Please update when the new version is released.
                        if (system.versi != 520) {
                            showAlertDialog()
                        }
                    }
                }

                is Resource.Error -> {
                }
            }
        }
    }

    private fun showAlertDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Update App")
        builder.setMessage("There is a new update available.")
        builder.setPositiveButton("Update") { dialog, which ->
            goPlayStore()
        }
        builder.setNegativeButton("Ignore for now") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private suspend fun collectStateMenu() {
        viewModel.menuState.collect {
            when (it) {
                is Resource.Initialize -> {
                }

                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    val menuList = it.data
                    menuAdapter.submitList(menuList)
                    binding.recyclerView.adapter = menuAdapter
                    binding.recyclerView.setHasFixedSize(true)
                }

                is Resource.Error -> {
                }
            }
        }
    }

    private suspend fun collectStateAnnouncement() {
        viewModel.announcementState.collect {
            when (it) {
                is Resource.Initialize -> {
                }

                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    binding.tvAnnouncement.isSelected = true
                    binding.tvAnnouncement.text = it.data
                }

                is Resource.Error -> {
                }
            }
        }
    }

    private var adView: AdView? = null

    private fun initAdView() {
        // Create a new ad view.
        adView = AdView(this@MainActivity)
        adView?.adUnitId = "ca-app-pub-1320314109772118/3718413418"
        // Request an anchored adaptive banner with a width of 360.
        adView?.setAdSize(
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                this@MainActivity,
                360
            )
        )
        // Replace ad container with new ad view.
        binding.adViewContainer.removeAllViews()
        binding.adViewContainer.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    private fun destroyBanner() {
        // Remove banner from view hierarchy.
        val parentView = adView?.parent
        if (parentView is ViewGroup) {
            parentView.removeView(adView)
        }

        // Destroy the banner ad resources.
        adView?.destroy()

        // Drop reference to the banner ad.
        adView = null
    }

    override fun onDestroy() {
        destroyBanner()
        super.onDestroy()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Granted
            lifecycleScope.launch {
                viewModel.initIsPermissionNotificationGranted(PermissionNotificationCondition.GRANTED)
            }
        } else {
            // Not granted
            /*showToast(
                "Notification permission denied. You will not receive notifications."
            )*/
            lifecycleScope.launch {
                viewModel.initIsPermissionNotificationGranted(PermissionNotificationCondition.NOT_GRANTED)
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Granted
                lifecycleScope.launch {
                    viewModel.initIsPermissionNotificationGranted(PermissionNotificationCondition.GRANTED)
                }
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                DialogUtils.showNotificationRationaleDialog(
                    context = this@MainActivity,
                    requestPermissionLauncher = requestPermissionLauncher
                )
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            lifecycleScope.launch {
                viewModel.initIsPermissionNotificationGranted(PermissionNotificationCondition.GRANTED)
            }
        }
    }

    private fun initWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}