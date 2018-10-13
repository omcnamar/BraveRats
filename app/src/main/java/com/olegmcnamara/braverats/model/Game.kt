package com.olegmcnamara.braverats.model

import com.olegmcnamara.braverats.model.Player

class Game {
    var player: Player? = null
    var player2: Player? = null

    constructor()
    constructor(player: Player,
                player2: Player) {
        this.player = player
        this.player2 = player2
    }
}