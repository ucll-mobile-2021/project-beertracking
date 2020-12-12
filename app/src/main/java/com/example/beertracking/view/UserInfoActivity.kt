package com.example.beertracking.view

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.beertracking.R
import com.example.beertracking.model.User
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

    var userId: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_user_info)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFireDatabase = mFirebaseInstance!!.getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser

        tvFirstName = findViewById<View>(R.id.tv_first_name) as TextView
        tvUid = findViewById<View>(R.id.tv_uid) as TextView
        tvLastName = findViewById<View>(R.id.tv_last_name) as TextView
        tvFriends = findViewById<View>(R.id.tv_email) as TextView
        tvEmailVerifiied = findViewById<View>(R.id.tv_email_verifiied) as TextView
        btnLogOut = findViewById<View>(R.id.btn_logout) as Button
        btnLogOut!!.setOnClickListener { logOut() }


        if (user != null) {
            userId = user.uid
            tvUid!!.text = userId
        }

        addUserChangeListener()

        super.onCreate(savedInstanceState)


    }



    private fun addUserChangeListener() {

        mFireDatabase!!.child(userId!!).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user==null){
                    Log.e("error", "Failed to read user")
                    return
                }
                Log.e("INFO", "USER HAS CHANGED")

                tvFirstName!!.text = user.firstname
                tvLastName!!.text = user.lastname

                tvUid!!.text = userId

                var mUser = mAuth!!.currentUser
                tvEmailVerifiied!!.text = mUser!!.isEmailVerified.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "Failed to read user", error.toException())
            }

        })

    }

    //TODO User INfo niet meer juist gegeven.

    private fun logOut() {
        val intent = Intent(this@UserInfoActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}