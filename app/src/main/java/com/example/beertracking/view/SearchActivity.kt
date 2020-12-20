package com.example.beertracking.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.beertracking.R
import com.example.beertracking.model.Beer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

//global variables
private var mDatabaseReference: DatabaseReference? = null
private var mAuth: FirebaseAuth? = null
private var userId: String? = null

private var btnSearchBeer: Button? = null
private var btnAddBeer: Button? = null

private var beerlist: ArrayList<Button?>? = null

class SearchActivity : BaseAppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        //set firebase instances
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/").getReference("beer")
        userId = mAuth!!.currentUser!!.uid

        //set contentview and thereafter the layout items and listeners
        setContentView(R.layout.activity_search)

        btnSearchBeer = findViewById<View>(R.id.searchbeer_button) as Button
        btnSearchBeer!!.setOnClickListener {searchBeer(it)}
        btnAddBeer = findViewById<View>(R.id.addbeer_button) as Button
        btnAddBeer!!.setOnClickListener { addBeer(it) }


        super.onCreate(savedInstanceState)
    }

    private fun searchBeer(view: View){

        val mapBeer = emptyMap<String, String>().toMutableMap()
        beerlist = ArrayList()

        mDatabaseReference!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val input = findViewById<EditText>(R.id.name_text).text.toString()

                for (or in snapshot.children){
                    if (or.child("name").value.toString().startsWith(input, true))
                    mapBeer += mapOf(or.key.toString() to or.child("name").value.toString())
                    println(mapBeer)
                }

                removeButtons()

                val linearlayout = findViewById<LinearLayout>(R.id.beer_linearlayout)

                for ((k, v) in mapBeer){
                    val newButton = Button(this@SearchActivity)
                    newButton.text = v
                    newButton.setOnClickListener {
                        Toast.makeText(this@SearchActivity, v, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SearchActivity, AddActivityActivity::class.java).apply {
                            putExtra(EXTRA_MESSAGE, v)
                        }
                        startActivity(intent)
                    }
                    linearlayout.addView(newButton)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun removeButtons(){
        val layout = findViewById<LinearLayout>(R.id.beer_linearlayout)
        layout.removeAllViews()
    }

    private fun addBeer(view: View){
        val intent = Intent(this, AddBeerActivity::class.java).apply {}
        startActivity(intent)
    }
}