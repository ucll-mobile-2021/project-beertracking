package com.example.beertracking.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.beertracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

// Class to show your current friends
class FriendsActivity : BaseAppCompatActivity() {

    //globar variables

    private var mDatabaseReference: DatabaseReference? = null

    private var mDatabase: FirebaseDatabase? = null

    private var mAuth: FirebaseAuth? = null

    private  var listview: ListView? = null
    private var alreadyFriends: Boolean? = false;

    private var userId: String? = null
    private var friendId: String? = null

    private var friendlist: ArrayList<String>? = null

    private var etFriendId: EditText? = null
    private var btnAddFriend: Button? = null
    private var mProgressBar: ProgressDialog? = null
    private var ids: ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        //set firebase instances
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        val user = mAuth!!.currentUser
        userId = user!!.uid
        mDatabaseReference = mDatabase!!.getReference("Users")

        //set contentview and thereafter the layout items and listeners
        setContentView(R.layout.activity_friends)
        btnAddFriend = findViewById<View>(R.id.add_friend_btn) as Button
        btnAddFriend!!.setOnClickListener { addFriend() }
        listview = findViewById<View>(R.id.friendlistview) as ListView


        friendlist = ArrayList()
        ids = ArrayList()

        // create an adapter to fill the layout with friends dynamically
        val adapter = ArrayAdapter(this, R.layout.friend_info, R.id.friendusername,
            friendlist!!
        )

        // TO SHOW THE LIST OF CURRENT FRIENDS
        mDatabaseReference!!.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (or in snapshot.children){
                    // add friend ids of current user to the id arraylist
                    ids!!.add(or.key.toString())
                }
                for (ds in snapshot.child(userId!!).child("friends").children) {
                    for (ds2 in snapshot.children) {
                        if (ds2.key == ds.key) {
                            // add full names to the arraylist of names of friends
                            var firstname =  ds2.child("firstName").value.toString()
                            var lastname = ds2.child("lastName").value.toString()
                            friendlist!!.add(firstname + " " + lastname)
                        }

                    }
                    //setting the adapter
                    listview!!.setAdapter(adapter)
                }
            }

                override fun onCancelled(error: DatabaseError) {
                    //Who needs implementations anyway, right?
                }
            })


        //calling super.oncreate for navbar
        super.onCreate(savedInstanceState)

    }




    private fun addFriend() {
        //get ready for a shitshow....

        etFriendId = findViewById<View>(R.id.et_friendid) as EditText
        friendId = etFriendId!!.text.toString()
        // call function
        initlistener()
        // check if textfield is not empty
        if (TextUtils.isEmpty(friendId)) {
            Toast.makeText(applicationContext, "Enter friend's UID", Toast.LENGTH_LONG).show()
        }
        else{
            // check if the filled in uid is not his own uid.
                //remember: treat the users like idi... creative people!
            if ((friendId)== userId) {
                Toast.makeText(applicationContext, "This is your own UID. Use your friend's UID", Toast.LENGTH_LONG).show()
                etFriendId!!.setText("")
            }
            else {
                // if you two aren't friends yet
                if (alreadyFriends == false) {
                    // check the ids to see if the filled in id is an existing id and not gibberish
                    var idexists = false
                    for (id in ids!!){
                        if (id == friendId){
                            idexists = true
                        }
                    }

                    //if the uid filled in exists...
                    if (idexists){
                        //add the friend to the current users friends
                        val currentUserDb = mDatabaseReference!!.child(userId!!).child("friends")
                        currentUserDb.child(friendId!!).setValue("true")

                        //add the current user to the friends friends
                        val currentUserDb2 = mDatabaseReference!!.child(friendId!!).child("friends")
                        currentUserDb2.child(userId!!).setValue("true")

                        //let them know!
                        Toast.makeText(applicationContext, "Jullie zijn bevriend!", Toast.LENGTH_LONG)
                            .show()

                        //load the overview page again
                        val intent = Intent(this@FriendsActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                    else   {
                        //ouch, the uid is not correct!
                        etFriendId!!.setText("")
                        Toast.makeText(applicationContext, "This UID is not correct!", Toast.LENGTH_LONG)
                            .show()

                    }

                } else {
                    //ow, they were already friends..
                    etFriendId!!.setText("")
                    Toast.makeText(applicationContext, "Jullie waren al bevriend!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun initlistener() {
        //make a singelavalueeventlistener
        mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //check if there exists a user with the filled in uid
                try { val idcorrect = snapshot.child(friendId!!)
                if (idcorrect == null) {
                    //it doesnt exist!
                    Toast.makeText(applicationContext, "Not a correct UID", Toast.LENGTH_LONG)
                        .show()
                } else {
                    // ok it exists. lets get his friends and check if they are alreay befriended
                    val exists = snapshot.child(userId!!).child("friends").child(friendId!!).value;

                    if (exists == null) {
                        alreadyFriends = false
                    } else {
                        alreadyFriends = true
                    }
                }

                }
                //catch an exception that will be thrown if it doesn't exist.
                // since the db link will not exist in that case
                catch (e: Exception){
                    Toast.makeText(applicationContext, "Not a correct UID", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            //Who needs implementations....
            }

        })

        //I will be honest, no idea why i check twice if they are already befriended.
        // I had a reason though! I think...
        // oh well, it works. NO TOUCHIE


    }

}