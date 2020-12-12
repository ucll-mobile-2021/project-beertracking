package com.example.beertracking.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Picture {
    var date: String = ""
    var description: String = ""
    var user: String = ""
    var beer: String = ""
    var url: String = ""


    constructor(){}

    constructor(date: String, description: String, user: String, beer: String, url: String) {
        this.description = description
        this.date = date
        this.user = user
        this.beer = beer
        this.url = url
    }
}