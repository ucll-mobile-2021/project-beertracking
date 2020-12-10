package com.example.beertracking.view

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.beertracking.R

class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        setHasOptionsMenu(true)
        val inflatedView = inflater.inflate(R.layout.fragment_search, container, false)

        //GET the textviews
        val name : TextView = inflatedView.findViewById(R.id.name_text)

        //GET Beer
        val b : Button = inflatedView.findViewById(R.id.searchbeer_button)
        b.setOnClickListener {getBeers(it, name) }

        return inflatedView
    }

    private fun getBeers(view: View, name: TextView) {
        /*     val beers = MainActivity.GlobalVariable.database.getBeersByName(name.text.toString())
        name.text = ""
        val action = SearchFragmentDirections.actionAddFragmentToSearchedFragment(beers)
        NavHostFragment.findNavController(this@SearchFragment).navigate(action)
     */
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