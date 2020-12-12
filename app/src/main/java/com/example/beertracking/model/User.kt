package com.example.beertracking.model


class User{
    lateinit var firstname: String
    lateinit var lastname: String

    constructor(){

    }

    constructor(firstname:String,lastname:String){
        this.firstname = firstname
        this.lastname = lastname
    }
}