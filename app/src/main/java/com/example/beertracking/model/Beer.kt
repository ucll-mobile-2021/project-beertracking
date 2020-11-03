package com.example.beertracking.model

class Beer(var name: String, var description: String) {

    override fun toString(): String {
        return "Name: $name\nDescription: $description"
    }
}