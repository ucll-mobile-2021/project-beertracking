package com.example.beertracking.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beertracking.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture.view.*

// This class will make a PictureHolder for every item in the list it has received.
class PictureAdapter(private val items: ArrayList<ArrayList<String>>) : RecyclerView.Adapter<PictureHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PictureHolder {
        for (item in items) {
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
        //Set the values given in the arraylist to the items in the layout and load the picture using picasso.
        holder: PictureHolder,
        position: Int) {
            var item = items.get(position)

                holder!!.itemView.tvImgDescription.text = item.get(0)
                holder!!.itemView.tvImgUser.text = item.get(1)
                holder!!.itemView.tvImgBeer.text = item.get(2)
                holder!!.itemView.tvImgDate.text = item.get(3)

                Picasso.get().load(item.get(4).toString()).fit()
                    .into(holder.itemView.imgView)
            }

    override fun getItemCount(): Int {
        return items.size
    }


}



