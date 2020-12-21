package com.example.beertracking.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.beertracking.R
import com.google.android.material.navigation.NavigationView

open class 
BaseAppCompatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // This is a selfmade variant of the regular AppCompatActivity
    // (the default class that needs to be extended to create an extension)
    // It adds the navigationbar. In here you can add new navigation items if needed
    // Let every activity that needs the navbar implement this.
    // IMPORTANT! you need to add certain code in a certain order to your layoutfiles in order for the navbar to work
    // Look at existing xml files for more info
    // IMPORANT #2! in the classes extending this class, make sure you call your super.oncreate() method
    // as the very last thing in its own oncreate method!

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

        // Just add an entry down below for eacht new navigationtitem
        // Dont forget to add it in the navdrawer_menu.xml file as well

        when (id) {
            R.id.addBeerActivity -> {
                return launch<SearchActivity>()
            }
            R.id.encyclopediaActivity -> {
                return launch<EncyclopediaActivity>()
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

            R.id.addFriendActivity -> {
                return launch<FriendsActivity>()
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