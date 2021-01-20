package com.example.beertracking.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.beertracking.R
import com.example.beertracking.model.Beer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//class to show the about info
class AddBeerActivity : BaseAppCompatActivity() {

    private var btnAddBeer: Button? = null
    var mAuth: FirebaseAuth? = null
    private var userId: String? = null

    private var dataReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //set the links to the realtime database and fill in variables
        mAuth = FirebaseAuth.getInstance()
        userId = mAuth!!.currentUser!!.uid
        dataReference = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/").getReference("beer")

        // link the layout
        setContentView(R.layout.activity_add_beer)

        //If the user is not logged in we redirect to the LoginActivity
        if (mAuth!!.currentUser == null ){
            val intent = Intent(this@AddBeerActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            super.onCreate(savedInstanceState)

        } else {
            // add some basic listeners for the buttons and tell them where to go
            btnAddBeer = findViewById<View>(R.id.addbeer_button) as Button
            btnAddBeer!!.setOnClickListener { addBeer(it)}

        }

        super.onCreate(savedInstanceState)
    }

    private fun addBeer(view: View){
        val name = findViewById<View>(R.id.name_text) as EditText
        val description = findViewById<View>(R.id.description_text) as EditText
        val alcohol = findViewById<View>(R.id.alcohol_number) as EditText
        val origin = findViewById<View>(R.id.origin_text) as EditText
        val style = findViewById<View>(R.id.style_text) as EditText

        if (name.text.isNullOrBlank() || description.text.isNullOrBlank() || alcohol.text.isNullOrBlank() || origin.text.isNullOrBlank() || style.text.isNullOrBlank()) {
            Toast.makeText(this, "Your input is invalid please try again", Toast.LENGTH_SHORT).show()
        } else {
            val info = Beer(name.text.toString(), description.text.toString() , alcohol.text.toString().toDouble(), origin.text.toString(), style.text.toString())
            val key = dataReference!!.push().key

            dataReference!!.child(key!!).setValue(info)

            Toast.makeText(this, "Successfully added the beer!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, SearchActivity::class.java).apply {}
            startActivity(intent)
        }
    }
}