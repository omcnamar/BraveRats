package com.olegmcnamara.braverats.model


enum class GameSessionStatus {
    notStarted, inProgress, finished
}

class GameSession {
    var id: String = ""
    var game: Game? = null
    var numberOfPlayers: Int = 0
    var status: GameSessionStatus = GameSessionStatus.notStarted

    constructor()
    constructor(game: Game?) {
        this.game = game
        with(game){
            game?.let {
                it.player?.let {
                    numberOfPlayers += 1
                }
                it.player2?.let {
                    numberOfPlayers += 1
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is GameSession) {
            other.id == this.id
        } else {
            false
        }
    }
}