package com.example.beertracking.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.beertracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserInfoActivity : AppCompatActivity() {
    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    //UI elements
    private var tvFirstName: TextView? = null
    private var tvLastName: TextView? = null
    private var tvEmail: TextView? = null
    private var tvEmailVerifiied: TextView? = null
    private var btnLogOut: Button? = null
    private var btnLoginPlease: Button? = null
    private var normal: Boolean = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (( snapshot.child("firstName").value) == null){
                    speciallayout()
                }
                else{
                    normallayout()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun normallayout() {
        this.normal=true;
        setContentView(R.layout.activity_user_info)
        mDatabase = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        tvFirstName = findViewById<View>(R.id.tv_first_name) as TextView
        tvLastName = findViewById<View>(R.id.tv_last_name) as TextView
        tvEmail = findViewById<View>(R.id.tv_email) as TextView
        tvEmailVerifiied = findViewById<View>(R.id.tv_email_verifiied) as TextView
        btnLogOut = findViewById<View>(R.id.btn_logout) as Button
        btnLogOut!!.setOnClickListener { logOut() }

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        tvEmail!!.text = mUser.email
        tvEmailVerifiied!!.text = mUser.isEmailVerified.toString()
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvFirstName!!.text = snapshot.child("firstName").value as String
                tvLastName!!.text = snapshot.child("lastName").value as String
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun speciallayout(){
        this.normal = false;
        setContentView(R.layout.activity_user_info_special)
        mDatabase = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        btnLoginPlease = findViewById<View>(R.id.btn_login_please) as Button
        btnLoginPlease!!.setOnClickListener { logOut() }
    }
    private fun logOut() {
        mAuth!!.signOut();
        val intent = Intent(this@UserInfoActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}