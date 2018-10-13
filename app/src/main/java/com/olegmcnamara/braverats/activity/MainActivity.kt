package com.olegmcnamara.braverats.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.olegmcnamara.braverats.R
import com.olegmcnamara.braverats.adapters.CardsAdapter
import com.olegmcnamara.braverats.fragments.CardFragment
import com.olegmcnamara.braverats.model.Deck
import com.olegmcnamara.braverats.model.Player
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CardFragment.OnFragmentInteractionListener {
    lateinit var playerOne: Player
    lateinit var playerTwo: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpCurrentPlayerRecycler()

    }

    private fun setUpCurrentPlayerRecycler() {

        val cards = Deck.getAllCards()
        val adapter = CardsAdapter(cards) { card ->
            val cardFragment = CardFragment.newInstance(card)
            supportFragmentManager.beginTransaction().add(frameCardOneContainer.id, cardFragment).commit()

        }
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        rvCurrentPlayersCards.adapter = adapter
        rvCurrentPlayersCards.layoutManager = layoutManager
        rvCurrentPlayersCards.addItemDecoration(dividerItemDecoration)
        adapter.notifyDataSetChanged()
    }



//    private fun playRound() {
//        var scoreChange = GameUtils.playRound(playerOne, playerTwo)
//        when {
//            scoreChange == 1 -> playerOne.score += 1
//            scoreChange == -1 -> playerTwo.score += 1
//            scoreChange == 0 -> 1==1 //how to handle a tie
//        }
//        playerOne.lastPlayedCard = playerOne.currentPlayedCard
//        playerTwo.lastPlayedCard = playerTwo.currentPlayedCard
//
//
//        if (playerOne.score == 4 || playerTwo.score ==4) {
//            Toast.makeText(this, "Game over", Toast.LENGTH_SHORT).show()
//            playerOne.score = 0
//            playerTwo.score = 0
//        }
//        updateUI()
//    }
//
//    private fun updateUI() {
//        score_player_one.text = playerOne.score.toString()
//        score_player_two.text = playerTwo.score.toString()
//
//        et_player_one.setText("")
//        et_player_two.setText("")
//    }
//
//    private fun createGame(player: Player, player2: Player) {
//
//
//    }
//
//    private fun setUpGameListener(table: FirebaseDatabaseHelper<Game>) {
//        table.addDataChangedListener{ game, eventType ->
//            when(eventType) {
//                EventType.childAdded -> {
//
//                }
//                EventType.childDeleted -> {
//
//                }
//                EventType.childChanged -> {
//                    et_player_one.text = Editable.Factory.getInstance().newEditable("${game.player?.currentPlayedCard}")
//                    et_player_two.text = Editable.Factory.getInstance().newEditable("${game.player2?.currentPlayedCard}")
//                }
//                EventType.childMoved -> {
//
//                }
//                else -> {
//
//                }
//            }
//        }
//    }

//    playerOne = Player()
//    playerTwo = Player()
//
//    button_play.setOnClickListener {
//        playerOne.currentPlayedCard = Integer.parseInt(et_player_one.text.toString())
//        playerTwo.currentPlayedCard = Integer.parseInt(et_player_two.text.toString())
//        playRound()
//    }
//    updateUI()
//
//    val gameTable = FirebaseDatabaseHelper("Game",Game::class.java)
//    val game = Game(playerOne, playerTwo)
//    gameTable.insert(game, {
//
//    }, {
//
//    })
//    setUpGameListener(gameTable)
}
