package com.example.beertracking.model


class FriendInfo{
    lateinit var firstName: String
    lateinit var lastName: String



    constructor(){

    }

    constructor(firstName:String,lastName:String){
        this.firstName = firstName
        this.lastName = lastName
    }
}