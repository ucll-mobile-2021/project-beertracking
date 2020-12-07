package com.example.beertracking.view

import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.beertracking.R
import com.example.beertracking.database.InMemory
import com.example.beertracking.databinding.ActivityMainBinding
import com.example.beertracking.model.Beer
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration : AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()

        if (mAuth!!.currentUser == null ){
            super.onCreate(savedInstanceState)

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        else {

            super.onCreate(savedInstanceState)
            val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
                this,
                R.layout.activity_main
            )
            drawerLayout = binding.drawerLayout
            val navController = this.findNavController(R.id.myNavHostFragment)
            NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
            appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
            NavigationUI.setupWithNavController(binding.navView, navController)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    object GlobalVariable {
        var database = InMemory()
    }
}