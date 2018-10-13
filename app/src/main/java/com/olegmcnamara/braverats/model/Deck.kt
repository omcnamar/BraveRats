package com.olegmcnamara.braverats.model

class Deck {
    companion object {
        private var cards = ArrayList<Card>()
        fun getAllCards() : ArrayList<Card> {
            val princess   = Card(1,"Princess","If opponent plays Prince, you win the Game")
            val spy        = Card(2, "Spy", "Next Round your opponent shows his card before you choose yours")
            val assassin   = Card(3, "Assassin","Lowest strength win!")
            val ambassador = Card(4, "Ambassador", "If you win with this card, Two Victories!")
            val wizard     = Card(5, "Wizard", "Nullifies the special power of your opponent!")
            val general    = Card(6, "General", "Card in the next Round +2 strength!")
            val prince     = Card(7, "Prince", "You win the round!")

            cards.add(princess)
            cards.add(spy)
            cards.add(assassin)
            cards.add(ambassador)
            cards.add(wizard)
            cards.add(general)
            cards.add(prince)
            return cards
        }
    }
}