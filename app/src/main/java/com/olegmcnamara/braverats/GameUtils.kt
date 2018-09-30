package com.olegmcnamara.braverats

class GameUtils {

    companion object {
        fun playRound(playerOne: Player, playerTwo: Player): Int {
            var result: String = when {
                playerOne.currentPlayedCard == playerTwo.currentPlayedCard -> return 0 //tie
                playerOne.currentPlayedCard > playerTwo.currentPlayedCard -> return 1 //player one wins
                else -> return -1 //player two wins
            }

        }
    }
}