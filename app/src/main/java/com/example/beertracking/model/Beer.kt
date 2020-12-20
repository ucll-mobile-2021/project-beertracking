package com.example.beertracking.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
class Beer{
    var name : String = ""
    var description : String = ""
    var alcohol : Double = 0.0
    var origin : String = ""
    var style : String = ""

    constructor(){}

    constructor(name: String, description: String, alcohol: Double, origin: String, style: String) {
        this.description = description
        this.name = name
        this.alcohol = alcohol
        this.origin = origin
        this.style = style
    }

    override fun toString(): String {
        return "Name: $name\nDescription: $description\nAlcohol: $alcohol%\nOrigin: $origin\nStyle: $style"
    }
}