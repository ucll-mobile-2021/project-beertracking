package com.example.beertracking.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.beertracking.R
import com.example.beertracking.adapter.RowAdapter
import com.example.beertracking.model.Beers

class SearchedFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        setHasOptionsMenu(true)

        val inflatedView = inflater.inflate(R.layout.fragment_searched, container, false)
        val t : ListView = inflatedView.findViewById(R.id.searchedBeers)
        val beers : Beers = SearchedFragmentArgs.fromBundle(arguments!!).beers

        val adapter = RowAdapter(context!!, R.layout.layout_searchedbeers, beers.toList())
        t.adapter = adapter

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