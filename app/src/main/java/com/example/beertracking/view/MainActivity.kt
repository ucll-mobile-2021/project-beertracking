package com.example.beertracking.view

import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture.view.*



class MainActivity : BaseAppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    private var dataReference: DatabaseReference? = null
    private var imageReference: StorageReference? = null
    private var fileRef: StorageReference? = null

    private var mAdapter: FirebaseRecyclerAdapter<Picture, PictureHolder>? = null
    private var rcvListImg: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)
        //storageReference = FirebaseStorage.getInstance("gs://beertracker-56e99.appspot.com/pictures").getReference("pictures")

        if (mAuth!!.currentUser == null ){
            mAuth!!.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            super.onCreate(savedInstanceState)

        }
        else {
            dataReference = FirebaseDatabase.getInstance("https://beertracker-56e99-default-rtdb.europe-west1.firebasedatabase.app/").getReference("pictures")
            imageReference = FirebaseStorage.getInstance("gs://beertracker-56e99.appspot.com/").reference.child("pictures")

            val layoutManager = LinearLayoutManager(this)
            layoutManager.reverseLayout = false
            rcvListImg = findViewById(R.id.rv_main)
            rcvListImg!!.setHasFixedSize(false)
            rcvListImg!!.layoutManager = layoutManager

            val query = dataReference!!.limitToLast(3)

            var options: FirebaseRecyclerOptions<Picture> = FirebaseRecyclerOptions.Builder<Picture>()
                .setQuery(query, Picture::class.java)
                .build()

            mAdapter = object: FirebaseRecyclerAdapter< Picture, PictureHolder>(
               options){
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
                    var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
                    return PictureHolder(view)
                }

                override fun onBindViewHolder(
                    holder: PictureHolder,
                    position: Int,
                    model: Picture
                ) {
                    holder!!.itemView.tvImgDescription.text = model!!.description
                    holder!!.itemView.tvImgUser.text = model!!.user
                    holder!!.itemView.tvImgBeer.text = model!!.beer
                    holder!!.itemView.tvImgDate.text = model!!.date
                    Picasso.get().load(model!!.url).fit().into(holder.itemView.imgView)



                }

                override fun onError(error: DatabaseError) {
                    System.out.println("DATABASE ERROR")
                }
            }
            rcvListImg!!.adapter = mAdapter

            super.onCreate(savedInstanceState)
        }
    }

    override fun onStart() {
        super.onStart()
        mAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter!!.stopListening()
    }

}

