package com.example.beertracking.view

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beertracking.R
import com.example.beertracking.model.Picture
import com.example.beertracking.model.PictureHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture.view.*
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
    private var imageReference: StorageReference? = null
    private var fileRef: StorageReference? = null

    private var mAdapter: FirebaseRecyclerAdapter<Picture, PictureHolder>? = null
    private var rcvListImg: RecyclerView? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        //set instances of storage, auth and realtime database
        mFirebaseInstance = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/")
        mFireDatabase = mFirebaseInstance!!.getReference("Users")
        mAuth = FirebaseAuth.getInstance()
        userId = mAuth!!.currentUser!!.uid
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

            var count = 0

            // call upon the realtime database
            mFireDatabase!!.addListenerForSingleValueEvent(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    // add the user to the friends list so we see our own pictures in the overview as well
                    friends.add(userId!!)
                    // add all friends of the current user to the friends list
                    for (ds in snapshot.child(userId!!).child("friends").children) {
                            friends.add(ds.key.toString())
                    }

                            // set the query for the storage. increase this number to load more pictures.
                            // I think we will never have more then 50 pictures in the storage sooooo
                            // also not implemented loading while scrolling, so it will ALL first load, THEN show the overview page
                            // so change this number if performace is slow.
                            val query = dataReference!!.limitToLast(50)

                            // set the recycler to call upon the storage with the query and get the data
                            var options: FirebaseRecyclerOptions<Picture> = FirebaseRecyclerOptions.Builder<Picture>()
                                .setQuery(query, Picture::class.java)
                                .build()

                            //with each file we get from the storage we will now do something...
                            mAdapter = object: FirebaseRecyclerAdapter<Picture, PictureHolder>(
                                options
                            ){
                                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
                                    for (friend in friends) {
                                    }
                                    //create a viewholder of the class PictureHolder
                                    // and add it to the layout inside the recyclerview
                                    var view: View = LayoutInflater.from(parent.context).inflate(
                                        R.layout.item_picture,
                                        parent,
                                        false
                                    )
                                    return PictureHolder(view)
                                }

                                override fun onBindViewHolder(
                                    holder: PictureHolder,
                                    position: Int,
                                    model: Picture
                                ) {
                                    var count = 0;
                                    // for each friend in the earlier made friendslist
                                    for (friend in friends) {
                                        //we check if the userID in the picture download link (so the id of the uploader)
                                        // is in the friendslist. not the cleanest solution but it works since all links
                                        // are built the same and all IDS are the same length
                                        var id = model!!.url.substring(87, 115)
                                        if (friend == id) {
                                            //if they are friends we load the picture info in the pictureholder
                                            holder!!.itemView.tvImgDescription.text = model!!.description
                                            holder!!.itemView.tvImgUser.text = model!!.user
                                            holder!!.itemView.tvImgBeer.text = model!!.beer
                                            holder!!.itemView.tvImgDate.text = model!!.date
                                            
                                            Picasso.get().load(model!!.url).fit()
                                                .into(holder.itemView.imgView)
                                        }


                                    }


                                }

                                override fun onError(error: DatabaseError) {
                                    //Who needs error implementations...
                                }
                    }
                    // add the adapter to the recyclerview and start it
                    rcvListImg!!.adapter = mAdapter
                    mAdapter!!.startListening()

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
        mAdapter!!.stopListening()
    }

}

