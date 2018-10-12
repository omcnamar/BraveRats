package com.olegmcnamara.braverats

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.olegmcnamara.braverats.remote.database.EventType
import com.olegmcnamara.braverats.remote.database.FirebaseDatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var playerOne:Player
    lateinit var playerTwo:Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerOne = Player()
        playerTwo = Player()

        button_play.setOnClickListener {
            playerOne.currentPlayedCard = Integer.parseInt(et_player_one.text.toString())
            playerTwo.currentPlayedCard = Integer.parseInt(et_player_two.text.toString())
            playRound()
        }
        updateUI()

        val gameTable = FirebaseDatabaseHelper("Game",Game::class.java)
        val game = Game(playerOne, playerTwo)
        gameTable.insert(game, {

        }, {

        })
        setUpGameListener(gameTable)
    }

    private fun playRound() {
        var scoreChange = GameUtils.playRound(playerOne, playerTwo)
        when {
            scoreChange == 1 -> playerOne.score += 1
            scoreChange == -1 -> playerTwo.score += 1
            scoreChange == 0 -> 1==1 //how to handle a tie
        }
        playerOne.lastPlayedCard = playerOne.currentPlayedCard
        playerTwo.lastPlayedCard = playerTwo.currentPlayedCard


        if (playerOne.score == 4 || playerTwo.score ==4) {
            Toast.makeText(this, "Game over", Toast.LENGTH_SHORT).show()
            playerOne.score = 0
            playerTwo.score = 0
        }
        updateUI()
    }

    private fun updateUI() {
        score_player_one.text = playerOne.score.toString()
        score_player_two.text = playerTwo.score.toString()

        et_player_one.setText("")
        et_player_two.setText("")
    }

    private fun createGame(player: Player, player2: Player) {


    }

    private fun setUpGameListener(table: FirebaseDatabaseHelper<Game>) {
        table.addDataChangedListener{ game, eventType ->
            when(eventType) {
                EventType.childAdded -> {

                }
                EventType.childDeleted -> {

                }
                EventType.childChanged -> {
                    et_player_one.text = Editable.Factory.getInstance().newEditable("${game.player?.currentPlayedCard}")
                }
                EventType.childMoved -> {

                }
                else -> {

                }
            }
        }
    }
}
