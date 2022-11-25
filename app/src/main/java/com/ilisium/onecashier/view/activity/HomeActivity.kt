package com.ilisium.onecashier.view.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ilisium.onecashier.R
import com.ilisium.onecashier.data.viewmodel.ProfileViewModel
import com.ilisium.onecashier.data.viewmodel.ViewModelFactory
import com.ilisium.onecashier.databinding.ActivityHomeBinding
import com.ilisium.onecashier.helper.GlobalVar
import com.ilisium.onecashier.helper.HelperInterfaces
import com.ilisium.onecashier.helper.viewBinding
import com.ilisium.onecashier.model.Status
import com.ilisium.onecashier.view.base.BaseActivity
import com.ilisium.onecashier.view.fragment.HomeFragment


class HomeActivity : BaseActivity() ,NavigationView.OnNavigationItemSelectedListener {
    private val binding by viewBinding(ActivityHomeBinding::inflate)

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var profileViewModel: ProfileViewModel

    lateinit var produkHelperInterface: HelperInterfaces.ProdukHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        TedPermission.create()
            .setPermissionListener(permissionlistener)
//            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check();

        initViewModel()
        setView()

        getOutlet()
        setListener()
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(this@HomeActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(
                this@HomeActivity,
                "Permission Denied\n$deniedPermissions",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setView(){
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        navView.setNavigationItemSelectedListener(this)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_kasir,
                R.id.nav_logout,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val user = headerView.findViewById<View>(R.id.user) as TextView

        user.text = session.user?.name

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.isNotEmpty()){
                    GlobalVar.searchProduk = query!!
                    produkHelperInterface.searchProduk(query!!)
                } else {
                    GlobalVar.searchProduk = ""
                    produkHelperInterface.searchProduk("")
                }

                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                if (query!!.isNotEmpty()){
                    GlobalVar.searchProduk = query!!
                    produkHelperInterface.searchProduk(query!!)
                } else {
                    GlobalVar.searchProduk = ""
                    produkHelperInterface.searchProduk("")
                }

                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initViewModel(){
        profileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(apiInterface)
        ).get(ProfileViewModel::class.java)
    }

    private fun getOutlet(){
        profileViewModel.getOutlet(
            session.user!!.outlet_id.toString()
        ).observe(this){
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        val data = resource.data?.response
                        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                        val headerView = navigationView.getHeaderView(0)
                        val outlet = headerView.findViewById<View>(R.id.nameOutlet) as TextView

                        outlet.text = data?.name

                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    private fun setListener() {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        Log.d("itemId", id.toString())

        when (id) {
            R.id.nav_logout -> {
                dialogLogoutAccount()
            }
            R.id.history -> {
                hiddenBottombar()
                findNavController(R.id.nav_host_fragment).navigate(R.id.historyFragment)
            }
            R.id.nav_kasir -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_kasir)
            }
            R.id.nav_buka_kasir -> {
                hiddenBottombar()
                val bundle = bundleOf("isOpen" to true)
                findNavController(R.id.nav_host_fragment).navigate(R.id.openCashierFragment, bundle)
            }
            R.id.nav_tutup_kasir -> {
                hiddenBottombar()
                val bundle = bundleOf("isOpen" to false)
                findNavController(R.id.nav_host_fragment).navigate(R.id.openCashierFragment, bundle)
            }

        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun hiddenBottombar(){
        binding.appBarMain.contentMain.bottomBar.visibility = View.GONE
    }

    fun showBottombar(){
        binding.appBarMain.contentMain.bottomBar.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (getForegroundFragment() is HomeFragment){
//            finish()
            dialogLogout()
        }
        else {
//            super.onBackPressed()
        }

    }

    fun dialogLogout() {
        val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
        builder1.setMessage("Yakin ingin keluar dari Aplikasi?")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "Ya"
        ) { dialog, id ->
            finish()
        }
        builder1.setNegativeButton(
            "Tidak"
        ) { dialog, id -> dialog.cancel() }
        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }

    fun dialogLogoutAccount() {
        val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
        builder1.setMessage("Yakin ingin keluar dari Akun?")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "Ya"
        ) { dialog, id ->
            session.isLogin(false)
            session.user = null
            session.token = ""
            session.isOpenCashier = false
//            toast(session.isOpenCashier.toString())

            val i = Intent(this@HomeActivity, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            finish()
        }
        builder1.setNegativeButton(
            "Tidak"
        ) { dialog, id -> dialog.cancel() }
        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }


    // to check current active fragment
    private fun getForegroundFragment(): Fragment? {
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun openActivity(intent: Intent) {
        startActivity(intent)
        setOveridePendingTransisi(this@HomeActivity)
    }

//    override val context = this@HomeActivity
}