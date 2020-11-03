package com.example.beertracking.database

import android.os.Environment
import android.util.Log
import com.example.beertracking.model.Beer
import com.google.gson.Gson
import java.io.File

class InMemory() {

    val beers = ArrayList<Beer>()

    init {
        beers.add(Beer("Stella Artois", "Best beer in the world"))
        beers.add(Beer("Jupiler", "De walen moeten ook met een bier komen"))
        beers.add(Beer("Duvel", "Sssht hier rust den duvel"))
    }



}