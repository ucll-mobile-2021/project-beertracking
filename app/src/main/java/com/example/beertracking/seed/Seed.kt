package com.example.beertracking.seed

import com.example.beertracking.model.Beer
import com.example.beertracking.model.CheckIn
import com.example.beertracking.model.User
import java.time.Instant.now
import java.util.*
import kotlin.collections.ArrayList

class Seed() {

    val beers = ArrayList<Beer>()
    val users = ArrayList<User>()
    val checkIns = ArrayList<CheckIn>()

    init {
        seedBeers()
        seedUsers()
        seedCheckIn()
    }

    fun seedBeers(){
        beers.add(Beer("Stella Artois", "Stella Artois was first brewed as a Christmas beer in leuven. It was named Stella from the star of Christmas, and Artois after Sebastian Artois, founder of the brewery. It's brewed to perfection using the original Stella Artois yeast and the celebrated Saaz hops. It's the optimum premium lager, with it's full flavour and clean crisp taste.",
            5.0, "Belgium", "International Pilsener"))
        beers.add(Beer("Jupiler", "Jupiler is the second most famous and second most popular beer in Belgium, after Stella Artois. This delicious lager is brewed with the finest ingredients (malt, maize, water, hop, yeast), using undisputed craftsmanship, ensuring an outstanding beer quality. Jupiler offers refreshment on a wide variety of occasions, thanks to its digestibility and accessible taste.",
            5.2, "Belgium", "German Pilsener"))
        beers.add(Beer("Duvel", "Duvel is a natural beer with a subtle bitterness, a refined flavour and a distinctive hop character. The unique brewing process, which takes about 90 days, guarantees a pure character, delicate effervescence and a pleasant sweet taste of alcohol.",
            8.5,"Belgium", "Belgian Tripel"))
        beers.add(Beer("Primus", "Primus, the genuine pilsner! How so? Every sip releases its characteristic bitter taste in your mouth. It lives its own life, the way it wants to. It stays true to itself and believes in its own strength. Primus. The toughest of beers.",
            5.2,"Belgium","Belgian Blonde"))
        beers.add(Beer("Heineken Lager", "Heineken is brewed using the same method developed in 1873 and takes the better part of a month to brew - around twice as long as regular beer. Theat extended \"lagering\" gives Heineken its distinctive flavor and clarity.",
            5.0, "Netherlands", "American Premium Lager"))
        beers.add(Beer("Stella Artois Solstice Lager", "Introducing Stella Artois Solstice Lager, a premium golden lager triple-filtered for refreshment & smoothness. Crafted in celebration of Summer’s longest day, its citrus notes and crisp clean finish make it perfect for your favorite summertime get togethers.",
            4.5, "Belgium", "American Amber Lager"))
    }

    fun beers(): ArrayList<Beer>{
        return beers
    }

    fun seedUsers(){
        users.add(User("Admin", "t"))
        users.add(User("User", "t"))
    }

    fun users(): ArrayList<User>{
        return users;
    }

    fun seedCheckIn(){
        val reviews = listOf("This beer works really well. It wildly improves my baseball by a lot.",
                "It only works when I'm Bahrain.",
                "My baboon loves to play with it.",
                "It only works when I'm Bolivia.",
                "this beer is standard.",
                "It only works when I'm Bolivia.",
                "It only works when I'm Bahrain.",
                "This beer works too well. It buoyantly improves my football by a lot.",
                "heard about this on smooth jazz radio, decided to give it a try.",
                "It only works when I'm Martinique.",
                "i use it never again when i'm in my station.",
                "It only works when I'm Malaysia.",
                "My neighbor Isabela has one of these. She works as a taxidermist and she says it looks monochromatic.",
                "It only works when I'm Bahrain.",
                "one of my hobbies is piano. and when i'm playing piano this works great.",
                "heard about this on original pilipino music radio, decided to give it a try.",
                "My neighbor Ardeth has one of these. She works as a gasman and she says it looks fuzzy.",
                "heard about this on hip-hop music radio, decided to give it a try.",
                "talk about shame.",
                "My neighbor Aldona has one of these. She works as a butler and she says it looks humongous.",
                "This beer works certainly well. It perfectly improves my tennis by a lot.",
                "I tried to scratch it but got cheeseburger all over it.",
                "My co-worker Erick has one of these. He says it looks fluffy.",
                "i use it once in a while when i'm in my ring.",
                "i use it never when i'm in my nightclub.",
                "My co-worker Mitchell has one of these. He says it looks dry.",
                "i use it never again when i'm in my station.",
                "heard about this on folktronica radio, decided to give it a try.",
                "i use it biweekly when i'm in my greenhouse.",
                "My goldfinch loves to play with it.",
                "talk about anticipation!",
                "one of my hobbies is spearfishing. and when i'm spearfishing this works great.",
                "My neighbor Krista has one of these. She works as a salesman and she says it looks soapy.",
                "I saw one of these in Kazakhstan and I bought one.",
                "one of my hobbies is piano. and when i'm playing piano this works great.",
                "one of my hobbies is skateboarding. and when i'm skateboarding this works great.",
                "I tried to impale it but got fudge all over it.",
                "I saw one of these in The Gambia and I bought one.",
                "This beer works quite well. It pointedly improves my golf by a lot.",
                "My neighbor Victoria has one of these. She works as a professor and she says it looks menthol.",
                "i use it profusely when i'm in my garage.",
                "heard about this on jump-up radio, decided to give it a try.",
                "My co-worker Matthew has one of these. He says it looks gigantic.",
                "I saw one of these in Canada and I bought one.",
                "talk about hatred!!!",
                "one of my hobbies is web-browsing. and when i'm browsing the web this works great.",
                "heard about this on gypsy jazz radio, decided to give it a try.",
                "this beer is mellow.",
                "this beer is tasty.",
                "This beer works quite well. It professionally improves my soccer by a lot.")
        checkIns.add(CheckIn(users.random(), beers.random(), reviews.random(), Calendar.getInstance().time))
        checkIns.add(CheckIn(users.random(), beers.random(), reviews.random(), Calendar.getInstance().time))
        checkIns.add(CheckIn(users.random(), beers.random(), reviews.random(), Calendar.getInstance().time))
        checkIns.add(CheckIn(users.random(), beers.random(), reviews.random(), Calendar.getInstance().time))
        checkIns.add(CheckIn(users.random(), beers.random(), reviews.random(), Calendar.getInstance().time))
    }

    fun checkIns(): ArrayList<CheckIn>{
        return checkIns
    }
}