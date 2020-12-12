package com.example.beertracking.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.beertracking.R
import com.example.beertracking.model.FriendInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FriendsActivity : BaseAppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null

    private var mDatabase: FirebaseDatabase? = null

    private var mAuth: FirebaseAuth? = null

    private  var listview: ListView? = null
    private var alreadyFriends: Boolean? = false;

    //global variables
    private var userId: String? = null
    private var friendId: String? = null

    private var friendlist: ArrayList<String>? = null

    private var etFriendId: EditText? = null
    private var btnAddFriend: Button? = null
    private var mProgressBar: ProgressDialog? = null
    private var adapter: ArrayAdapter<FriendInfo>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        mProgressBar = ProgressDialog(this)
        mProgressBar!!.setMessage("Fetching Data...")
        mProgressBar!!.show()
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        val user = mAuth!!.currentUser
        userId = user!!.uid
        mDatabaseReference = mDatabase!!.getReference("Users")

        setContentView(R.layout.activity_friends)


        btnAddFriend = findViewById<View>(R.id.add_friend_btn) as Button
        btnAddFriend!!.setOnClickListener { addFriend() }

        friendlist = ArrayList<String>()

        listview = findViewById<View>(R.id.friendlistview) as ListView

        val adapter = ArrayAdapter(this, R.layout.friend_info, R.id.friendusername,
            friendlist!!
        )

        // TO SHOW THE LIST OF CURRENT FRIENDS
        mDatabaseReference!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.child(userId!!).child("friends").children) {
                    for (ds2 in snapshot.children) {
                        if (ds2.key == ds.key) {
                            System.out.println("DS2__________________")
                            System.out.println(ds2)
                            var firstname =  ds2.child("firstName").value.toString()
                            var lastname = ds2.child("lastName").value.toString()
                            System.out.println(firstname)
                            System.out.println(lastname)
                            System.out.println("---------------------------------------")
                            friendlist!!.add(firstname + " " + lastname)
                        }

                    }
                    listview!!.setAdapter(adapter)
                }
            }

                override fun onCancelled(error: DatabaseError) {
                    //Who needs implementations anyway, right?
                }
            })



        mProgressBar!!.hide()
        super.onCreate(savedInstanceState)

    }




    private fun addFriend() {
        etFriendId = findViewById<View>(R.id.et_friendid) as EditText
        friendId = etFriendId!!.text.toString()
        initlistener()
        if (TextUtils.isEmpty(friendId)) {
            Toast.makeText(applicationContext, "Enter friend's UID", Toast.LENGTH_LONG).show()
        }
        else{
            if ((friendId)== userId) {
                Toast.makeText(applicationContext, "This is your own UID. Use your friend's UID", Toast.LENGTH_LONG).show()
                etFriendId!!.setText("")
            }
            else {

                if (alreadyFriends == false) {

                    val currentUserDb = mDatabaseReference!!.child(userId!!).child("friends")
                    currentUserDb.child(friendId!!).setValue("true")

                    val currentUserDb2 = mDatabaseReference!!.child(friendId!!).child("friends")
                    currentUserDb2.child(userId!!).setValue("true")
                    Toast.makeText(applicationContext, "Jullie zijn bevriend!", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(this@FriendsActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    etFriendId!!.setText("")
                    Toast.makeText(
                        applicationContext,
                        "Jullie waren al bevriend!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    private fun initlistener() {
        mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val idcorrect = snapshot.child(friendId!!)
                if (idcorrect == null) {
                    Toast.makeText(applicationContext, "Not a correct UID", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val exists = snapshot.child(userId!!).child("friends").child(friendId!!).value;

                    if (exists == null) {
                        alreadyFriends = false
                    } else {
                        alreadyFriends = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            //Who needs implementations....
            }

        })


    }

}