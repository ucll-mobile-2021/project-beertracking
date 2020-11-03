package com.example.beertracking.view

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.beertracking.R
import com.example.beertracking.database.InMemory
import com.example.beertracking.model.Beer

class OverviewFragment : Fragment() {

    var db = InMemory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        setHasOptionsMenu(true)

        val inflatedView = inflater.inflate(R.layout.fragment_overview, container, false)
        val t : TextView = inflatedView.findViewById(R.id.beers);

        var text : String = ""
        for (beer in db.beers){
            text += beer.toString() + "\n\n"
        }

        t.text = text

        return inflatedView
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