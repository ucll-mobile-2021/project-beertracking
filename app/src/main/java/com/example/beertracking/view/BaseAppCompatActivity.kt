package com.example.beertracking.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.beertracking.R
import com.example.beertracking.model.User
import com.example.beertracking.view.AddBeerActivity
import com.example.beertracking.view.MainActivity
import com.example.beertracking.view.UserInfoActivity
import com.google.android.material.navigation.NavigationView

open class BaseAppCompatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected  var drawerLayout: DrawerLayout? = null
    protected  var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        navigationView = findViewById(R.id.navigation_view) as NavigationView

        navigationView!!.setNavigationItemSelectedListener(this)



        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = true

        val navigationViewHeaderView = navigationView!!.getHeaderView(0)
    }

    private inline fun <reified T : Activity> launch(): Boolean {
        if (this is T) return closeDrawer()
        val intent = Intent(applicationContext, T::class.java)
        startActivity(intent)
        finish()
        return true
    }

    private fun closeDrawer(): Boolean {
        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.addBeerActivity -> {
                return launch<AddBeerActivity>()
            }

            R.id.userInfoActivity -> {
                return launch<UserInfoActivity>()
            }

            R.id.mainActivity -> {
                return launch<MainActivity>()
            }

            R.id.aboutActivity -> {
                return launch<AboutActivity>()
            }

        }
        return false
    }
    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}