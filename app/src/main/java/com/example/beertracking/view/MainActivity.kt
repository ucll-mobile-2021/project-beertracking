package com.example.beertracking.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.example.beertracking.R
import com.google.firebase.auth.FirebaseAuth


class MainActivity : BaseAppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)

        if (mAuth!!.currentUser == null ){

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            super.onCreate(savedInstanceState)

        }
        else {
            //TODO Implementatie Overview
            super.onCreate(savedInstanceState)
        }
    }
}

