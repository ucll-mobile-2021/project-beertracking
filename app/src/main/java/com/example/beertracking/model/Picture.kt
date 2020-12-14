package com.example.beertracking.model

import com.google.firebase.database.IgnoreExtraProperties
// Class to store the information about a picture.
//
// needed for the adaptor for the recyclerview for
// the mainactivitylayout for the mainactivity for the app.
@IgnoreExtraProperties
class Picture {
    var date: String = ""
    var description: String = ""
    var user: String = ""
    var beer: String = ""
    var url: String = ""
    var userId: String = ""


    constructor(){}

    constructor(date: String, description: String, user: String, beer: String, url: String, userId: String) {
        this.description = description
        this.date = date
        this.user = user
        this.beer = beer
        this.url = url
        this.userId = userId
    }
}