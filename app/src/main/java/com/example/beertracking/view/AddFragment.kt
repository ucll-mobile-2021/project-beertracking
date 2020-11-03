package com.example.beertracking.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.beertracking.R
import com.example.beertracking.model.Beer

class AddFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        setHasOptionsMenu(true)
        val inflatedView = inflater.inflate(R.layout.fragment_add, container, false)

        //Get the textviews
        val name : TextView = inflatedView.findViewById(R.id.name_text)
        val description : TextView = inflatedView.findViewById(R.id.description_text)

        //ADD Beer
        val b : Button = inflatedView.findViewById(R.id.addbeer_button)
        b.setOnClickListener {addBeer(it, name, description) }

        return inflatedView
    }

    private fun addBeer(view:View, name:TextView, description:TextView){
        //Add the beers
        MainActivity.GlobalVariable.database.addBeer(Beer(name.text.toString(),description.text.toString()))

        //Clear the textviews
        name.text = ""
        description.text = ""

        //makes a toast
        Toast.makeText(activity,"Beer added successfully!", Toast.LENGTH_LONG).show()

        //Redirect
        view.findNavController().navigate(R.id.action_addFragment_to_overviewFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}