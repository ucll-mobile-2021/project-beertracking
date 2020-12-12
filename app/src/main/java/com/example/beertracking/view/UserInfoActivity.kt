package com.example.beertracking.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.beertracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserInfoActivity : BaseAppCompatActivity() {
    //Firebase references
    private var mFireDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    //UI elements
    private var tvFirstName: TextView? = null
    private var tvUid: TextView? = null
    private var tvLastName: TextView? = null
    private var tvEmail: TextView? = null
    private var tvFriends: TextView? = null
    private var tvEmailVerifiied: TextView? = null
    private var btnLogOut: Button? = null
    private var mProgressBar: ProgressDialog? = null


    var userId: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_user_info)
        mProgressBar = ProgressDialog(this)
        mProgressBar!!.setMessage("Registering User...")
        mProgressBar!!.show()

        mFirebaseInstance = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mFireDatabase = mFirebaseInstance!!.getReference("Users")
        mAuth = FirebaseAuth.getInstance();
        val user = FirebaseAuth.getInstance().currentUser

        tvFirstName = findViewById<View>(R.id.tv_first_name) as TextView
        tvUid = findViewById<View>(R.id.tv_uid) as TextView
        tvLastName = findViewById<View>(R.id.tv_last_name) as TextView
        tvFriends = findViewById<View>(R.id.tv_email) as TextView
        tvEmailVerifiied = findViewById<View>(R.id.tv_email_verifiied) as TextView
        tvEmail = findViewById<View>(R.id.tv_email) as TextView

        btnLogOut = findViewById<View>(R.id.btn_logout) as Button
        btnLogOut!!.setOnClickListener { logOut() }

        var mUser = mAuth!!.currentUser
        userId = mUser!!.uid!!.toString()
        tvEmail!!.text = mUser!!.email!!.toString()
        tvEmailVerifiied!!.text = mUser!!.isEmailVerified.toString()
        tvUid!!.text = userId


        mFireDatabase!!.child(userId!!).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                tvFirstName!!.text = snapshot.child("firstName").value.toString()
                tvLastName!!.text = snapshot.child("lastName").value.toString()

                tvUid!!.text = userId


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "Failed to read user", error.toException())
            }
        })

        super.onCreate(savedInstanceState)
        mProgressBar!!.hide()


    }


    private fun logOut() {
        val intent = Intent(this@UserInfoActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}