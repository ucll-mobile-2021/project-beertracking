package com.example.beertracking.model

import java.util.*

class CheckIn(var user: User, var beer: Beer, var description: String, var date: Date) {

    override fun toString(): String {
        return "Name: ${user.name}\nBeer: ${beer.name}\nDescription: $description\nDate: $date"
    }
}