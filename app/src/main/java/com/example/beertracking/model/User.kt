package com.example.beertracking.model

class User(var name: String, var password: String) {

    override fun toString(): String {
        return "Name: $name\nPassword: $password"
    }
}