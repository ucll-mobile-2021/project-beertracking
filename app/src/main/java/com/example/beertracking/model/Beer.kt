package com.example.beertracking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Beer(var name: String, var description: String, var alcohol: Double, var origin: String, var style: String): Parcelable {

    override fun toString(): String {
        return "Name: $name\nDescription: $description\nAlcohol: $alcohol%\nOrigin: $origin\nStyle: $style"
    }
}

@Parcelize
class Beers: ArrayList<Beer>(), Parcelable