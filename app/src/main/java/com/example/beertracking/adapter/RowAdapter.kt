package com.example.beertracking.adapter

import android.content.Context
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.example.beertracking.R
import com.example.beertracking.model.Beer
import com.example.beertracking.view.SearchFragmentDirections
import com.example.beertracking.view.SearchedFragment
import com.example.beertracking.view.SearchedFragmentDirections
import kotlinx.android.synthetic.main.layout_searchedbeers.view.*

class RowAdapter(var ctx: Context, var resource : Int, var items : List<Beer>): ArrayAdapter<Beer>(ctx,resource,items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(ctx)
        val view : View = layoutInflater.inflate(resource, null)
        var textView : TextView = view.findViewById(R.id.beer)
        var button : Button = view.findViewById(R.id.btn)

        var beer : Beer = items[position]

        textView.text = beer.name
        button.setOnClickListener {
            //val action = SearchedFragmentDirections.actionSearchedFragmentToCheckinFragment(beer)
            //NavHostFragment.findNavController().navigate(action)
            println("You pressed this check-in: "+beer.name)
        }

        return view
    }
}