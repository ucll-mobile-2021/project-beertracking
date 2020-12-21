 package com.example.beertracking.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beertracking.R
import com.example.beertracking.model.Picture
import com.example.beertracking.model.PictureAdapter
import com.example.beertracking.model.PictureHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture.view.*
import java.math.BigInteger
import java.util.concurrent.CountDownLatch


//class to show overview of pictures from himself and friends.
// extends baseapp for navbar
class MainActivity : BaseAppCompatActivity() {
    //set variables
    private var mAuth: FirebaseAuth? = null
    private var mFireDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var userId: String? = null

    private var dataReference: DatabaseReference? = null

    private var mAdapter: RecyclerView.Adapter<PictureHolder>? = null
    private var rcvListImg: RecyclerView? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        //set instances of storage, auth and realtime database
        mFirebaseInstance = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mFireDatabase = mFirebaseInstance!!.reference
        mAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_main)

        //check if we are logged in. just to be sure
        // call login activity if not
        if (mAuth!!.currentUser == null ){
            mAuth!!.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            super.onCreate(savedInstanceState)

        }
        //otherwise...
        else {
            userId = mAuth!!.currentUser!!.uid
            //local variable and a reference to the storage
            var friends: ArrayList<String> = ArrayList()
            dataReference = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/").getReference(
                "pictures"
            )

            // assign the layoutmanager for the recyclerview item and set some settings
            val layoutManager = LinearLayoutManager(this)
            layoutManager.reverseLayout = false
            rcvListImg = findViewById(R.id.rv_main)

            rcvListImg!!.setHasFixedSize(false)
            rcvListImg!!.layoutManager = layoutManager

            // call upon the realtime database
            mFireDatabase!!.addListenerForSingleValueEvent(object :
                ValueEventListener {

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onDataChange(snapshot: DataSnapshot) {
                    // add the user to the friends list so we see our own pictures in the overview as well
                    friends.add(userId!!)
                    // add all friends of the current user to the friends list
                    for (ds in snapshot.child("Users").child(userId!!).child("friends").children) {
                        friends.add(ds.key.toString())
                    }

                    // making a map of dates and urls of pictures.
                    // BUT only of pictures from friends within the friendslist

                    var map = mutableMapOf<BigInteger, ArrayList<String>>()
                    var picturesToBeLoaded = ArrayList<ArrayList<String>>()


                    for (xs in snapshot.child("pictures").children) {
                        for (friend in friends) {
                            println(xs.child("userId").value.toString())
                            if (friend == xs.child("userId").value.toString()) {

                                var info = ArrayList<String>()

                                var description = xs.child("description").value.toString()
                                info.add(description)

                                var user = xs.child("user").value.toString()
                                info.add(user)

                                var beer = xs.child("beer").value.toString()
                                info.add(beer)

                                var date = xs.child("date").value.toString()
                                info.add(date)

                                var urlie = xs.child("url").value.toString()
                                info.add(urlie)


                                map.put(date.toBigInteger(), info)
                            }
                        }
                    }

                    // Sort the map so the highest date (=newest) stands in front
                    var sortedtempmap = map.toSortedMap(reverseOrder())
                    Log.d("DEBUG", map.toString())
                    Log.d("DEBUG", sortedtempmap.toString())

                    // put the sorted map into an arraylist so the adapter can use it
                    for (x in sortedtempmap){

                        picturesToBeLoaded.add(x.value)
                    }


                    // we give the arraylist to the adapter so it can be set using the specially made class.
                    mAdapter = PictureAdapter(picturesToBeLoaded)

                    // add the adapter to the recyclerview and start it
                    rcvListImg!!.adapter = mAdapter

                }

                override fun onCancelled(error: DatabaseError) {
                    //Who needs error implementations...
                }
            })
            //to create the navbar
            super.onCreate(savedInstanceState)
        }
    }

    //stop the adapterlistener if the page closes
    override fun onStop() {
        super.onStop()
        if( mAdapter != null){
        }

    }

}

