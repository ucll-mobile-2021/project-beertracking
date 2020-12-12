package com.example.beertracking.database

import com.example.beertracking.model.Beer
import com.example.beertracking.model.Beers
import com.example.beertracking.seed.Seed

class InMemory() {

    var beers = ArrayList<Beer>()
    val seed = Seed()

    init {
        beers = seed.beers()
    }

    fun addBeer(beer : Beer){
        beers.add(beer)
    }

    fun getBeersByName(name: String): Beers {
        val output = Beers()
        for (beer in beers){
            if (beer.name.contains(name, ignoreCase = true)){
                output.add(beer)
            }
        }
        return output
    }

}