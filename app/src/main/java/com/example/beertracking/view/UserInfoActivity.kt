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

//class to add a beer
//extends baseapp to allow for navigation

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
    private var verifyButton: Button? = null

    var userId: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        //set the layout and progress bar
        setContentView(R.layout.activity_user_info)
        mProgressBar = ProgressDialog(this)
        mProgressBar!!.setMessage("Registering User...")
        mProgressBar!!.show()

        //set the instances
        mFirebaseInstance = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mFireDatabase = mFirebaseInstance!!.getReference("Users")
        mAuth = FirebaseAuth.getInstance();
        val user = FirebaseAuth.getInstance().currentUser


        //set the layout items
        tvFirstName = findViewById<View>(R.id.tv_first_name) as TextView
        tvUid = findViewById<View>(R.id.tv_uid) as TextView
        tvLastName = findViewById<View>(R.id.tv_last_name) as TextView
        tvFriends = findViewById<View>(R.id.tv_email) as TextView
        tvEmailVerifiied = findViewById<View>(R.id.tv_email_verifiied) as TextView
        tvEmail = findViewById<View>(R.id.tv_email) as TextView

        //set the buttons and their listeners
        btnLogOut = findViewById<View>(R.id.btn_logout) as Button
        btnLogOut!!.setOnClickListener { logOut() }

        verifyButton = findViewById<View>(R.id.btn_verify) as Button
        verifyButton!!.setOnClickListener { verifyEmail() }

        //set some data you can get from the auth like email, verified and uid
        var mUser = mAuth!!.currentUser
        userId = mUser!!.uid!!.toString()
        tvEmail!!.text = mUser!!.email!!.toString()
        tvEmailVerifiied!!.text = mUser!!.isEmailVerified.toString()
        tvUid!!.text = userId

        //start a listener for the values on the realtime database
        mFireDatabase!!.child(userId!!).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // put the first and lastname into the layout
                tvFirstName!!.text = snapshot.child("firstName").value.toString()
                tvLastName!!.text = snapshot.child("lastName").value.toString()
                tvUid!!.text = userId


            }

            override fun onCancelled(error: DatabaseError) {
                // who needs error implementation...
            }
        })
        //call to get the navbar in and hide the progressbar
        super.onCreate(savedInstanceState)
        mProgressBar!!.hide()


    }

    //logout send you back to the loginactivity
    private fun logOut() {
        FirebaseAuth.getInstance().signOut();
        val intent = Intent(this@UserInfoActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verifyEmail() {
        // This sends an automatic email to the user to verify his email
        // the settings for this email can be change at
        // https://console.firebase.google.com/project/beertracker-56e99/authentication/users
        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UserInfoActivity,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@UserInfoActivity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


}