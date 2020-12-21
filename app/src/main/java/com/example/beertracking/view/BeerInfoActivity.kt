package com.example.beertracking.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import com.example.beertracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.*

//global variables
private var mDatabaseReference: DatabaseReference? = null
private var mAuth: FirebaseAuth? = null
private var userId: String? = null

//class to show the about info
class BeerInfoActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set firebase instances
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/").getReference("beer")
        userId = mAuth!!.currentUser!!.uid

        setContentView(R.layout.activity_beerinfo)

        mDatabaseReference!!.addValueEventListener(object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (or in snapshot.children){
                    if (or.key == intent.getStringExtra(EXTRA_MESSAGE)) {
                        findViewById<TextView>(R.id.name_text).text = or.child("name").value.toString()
                        findViewById<TextView>(R.id.style_text).text = "Style: ${or.child("style").value.toString()}"
                        findViewById<TextView>(R.id.origin_text).text = "Origin: ${or.child("origin").value.toString()}"
                        findViewById<TextView>(R.id.alcohol_text).text = "ABV: ${or.child("alcohol").value.toString()}%"
                        findViewById<TextView>(R.id.description_text).text = "Description: ${or.child("description").value.toString()}"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        super.onCreate(savedInstanceState)
    }
}