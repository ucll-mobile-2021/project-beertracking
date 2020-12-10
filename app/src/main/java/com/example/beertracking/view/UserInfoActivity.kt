package com.example.beertracking.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.beertracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserInfoActivity : BaseAppCompatActivity() {
    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    //UI elements
    private var tvFirstName: TextView? = null
    private var tvUid: TextView? = null
    private var tvLastName: TextView? = null
    private var tvEmail: TextView? = null
    private var tvEmailVerifiied: TextView? = null
    private var btnLogOut: Button? = null
    private var btnAddFriend: Button? = null
    private var etID: EditText? = null
    private var id: String? = null
    private var currentuserfriends: ArrayList<String>? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
        setContentView(R.layout.activity_user_info)
        tvFirstName = findViewById<View>(R.id.tv_first_name) as TextView
        tvUid = findViewById<View>(R.id.tv_uid) as TextView
        tvLastName = findViewById<View>(R.id.tv_last_name) as TextView
        tvEmail = findViewById<View>(R.id.tv_email) as TextView
        tvEmailVerifiied = findViewById<View>(R.id.tv_email_verifiied) as TextView
        btnLogOut = findViewById<View>(R.id.btn_logout) as Button
        btnLogOut!!.setOnClickListener { logOut() }
        btnAddFriend = findViewById<View>(R.id.btn_add_friend) as Button
        btnAddFriend!!.setOnClickListener { addFriend() }
        tvEmail!!.text = mUser.email
        tvEmailVerifiied!!.text = mUser.isEmailVerified.toString()

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvUid!!.text = mUser.uid
                tvFirstName!!.text = snapshot.child("firstName").value as String
                tvLastName!!.text = snapshot.child("lastName").value as String
                currentuserfriends = snapshot.child("friends").value as ArrayList<String>
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        super.onCreate(savedInstanceState)


    }


    private fun addFriend(){
        etID = findViewById<View>(R.id.addfriend) as EditText
        id = etID?.text.toString()
        currentuserfriends!!.add(id!!)
        val mUserReference2 = mDatabaseReference!!.child(id!!)
        if (mUserReference2 != null) {
            var friends2 = ArrayList<String>()
            mUserReference2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                        friends2 = snapshot.child("friends").value as ArrayList<String>

                        if (friends2[0] == "temp"){
                            friends2.removeAt(0);
                        }
                }



                override fun onCancelled(databaseError: DatabaseError) {}
            })
            System.out.println(friends2)
            val mUser = mAuth!!.currentUser

            friends2.add(mUser!!.uid);

            mUserReference2.child("friends").setValue(friends2)

            val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
            var friends = ArrayList<String>()

            mUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                        friends = snapshot.child("friends").value as ArrayList<String>
                        System.out.println("friends-------------------")
                        System.out.println(friends)
                        if (friends[0] == "temp"){
                            friends.removeAt(0);
                        }
                    System.out.println("friends again-------------------")
                    System.out.println(friends)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            friends.add(id!!);
            System.out.println("friends after add id-------------------")
            System.out.println(friends)
            mUserReference.child("test").setValue("test")
            System.out.println("friends after add test-------------------")
            System.out.println(friends)
            mUserReference.child("friends").setValue(friends)


            val intent = Intent(this@UserInfoActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun logOut() {
        mAuth!!.signOut();
        val intent = Intent(this@UserInfoActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}