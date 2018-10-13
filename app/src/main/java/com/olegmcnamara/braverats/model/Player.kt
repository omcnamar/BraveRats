package com.olegmcnamara.braverats.model

class Player {
    var id: String = ""
    var score: Int = 0
    var currentPlayedCard: Int = -1
    var lastPlayedCard: Int = -1

    constructor()
    constructor(id: String) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Player) {
            other.id == this.id
        } else {
            false
        }
    }
}