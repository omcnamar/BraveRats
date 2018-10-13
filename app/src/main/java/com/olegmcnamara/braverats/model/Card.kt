package com.olegmcnamara.braverats.model

class Card {
    var cardNumber: Int = 0
    var cardName: String = ""
    var cardPower: String = ""
    constructor()
    constructor(cardNumber: Int, cardName: String, cardPower: String) {
        this.cardNumber = cardNumber
        this.cardName = cardName
        this.cardPower = cardPower
    }

}