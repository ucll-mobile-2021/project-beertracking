package com.example.beertracking.seed

import com.example.beertracking.model.Beer
import java.util.*
import kotlin.collections.ArrayList

class Seed() {

    val beers = ArrayList<Beer>()

    init {
        seedBeers()
    }

    fun seedBeers() {
        beers.add(
            Beer(
                "Stella Artois",
                "Stella Artois was first brewed as a Christmas beer in leuven. It was named Stella from the star of Christmas, and Artois after Sebastian Artois, founder of the brewery. It's brewed to perfection using the original Stella Artois yeast and the celebrated Saaz hops. It's the optimum premium lager, with it's full flavour and clean crisp taste.",
                5.0,
                "Belgium",
                "International Pilsener"
            )
        )
        beers.add(
            Beer(
                "Jupiler",
                "Jupiler is the second most famous and second most popular beer in Belgium, after Stella Artois. This delicious lager is brewed with the finest ingredients (malt, maize, water, hop, yeast), using undisputed craftsmanship, ensuring an outstanding beer quality. Jupiler offers refreshment on a wide variety of occasions, thanks to its digestibility and accessible taste.",
                5.2,
                "Belgium",
                "German Pilsener"
            )
        )
        beers.add(
            Beer(
                "Duvel",
                "Duvel is a natural beer with a subtle bitterness, a refined flavour and a distinctive hop character. The unique brewing process, which takes about 90 days, guarantees a pure character, delicate effervescence and a pleasant sweet taste of alcohol.",
                8.5,
                "Belgium",
                "Belgian Tripel"
            )
        )
        beers.add(
            Beer(
                "Primus",
                "Primus, the genuine pilsner! How so? Every sip releases its characteristic bitter taste in your mouth. It lives its own life, the way it wants to. It stays true to itself and believes in its own strength. Primus. The toughest of beers.",
                5.2,
                "Belgium",
                "Belgian Blonde"
            )
        )
        beers.add(
            Beer(
                "Heineken Lager",
                "Heineken is brewed using the same method developed in 1873 and takes the better part of a month to brew - around twice as long as regular beer. Theat extended \"lagering\" gives Heineken its distinctive flavor and clarity.",
                5.0,
                "Netherlands",
                "American Premium Lager"
            )
        )
        beers.add(
            Beer(
                "Stella Artois Solstice Lager",
                "Introducing Stella Artois Solstice Lager, a premium golden lager triple-filtered for refreshment & smoothness. Crafted in celebration of Summer’s longest day, its citrus notes and crisp clean finish make it perfect for your favorite summertime get togethers.",
                4.5,
                "Belgium",
                "American Amber Lager"
            )
        )
    }

    fun beers(): ArrayList<Beer> {
        return beers
    }


}