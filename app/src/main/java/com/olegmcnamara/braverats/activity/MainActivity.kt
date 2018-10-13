package com.olegmcnamara.braverats.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.olegmcnamara.braverats.R
import com.olegmcnamara.braverats.adapters.CardsAdapter
import com.olegmcnamara.braverats.fragments.CardFragment
import com.olegmcnamara.braverats.model.*
import com.olegmcnamara.braverats.remote.auth.Authentication
import com.olegmcnamara.braverats.remote.database.EventType
import com.olegmcnamara.braverats.remote.database.FirebaseDatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*

enum class GameSessionType {
    creator, joiner
}

class MainActivity : AppCompatActivity(), CardFragment.OnFragmentInteractionListener {
    lateinit var playerOne: Player
    lateinit var playerTwo: Player
    var globalGameSession: GameSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpCurrentPlayerRecycler()

        btnCreateGame.setOnClickListener { createGameSession() }
        btnJoinGame.setOnClickListener { joinWaitingGameSession() }
        btnCancel.setOnClickListener {
            globalGameSession = null
            sessionInProgress = false
            enterNormalMode()
        }

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

    // when creating game session the joining player is player1
    private var sessionInProgress = false
    private fun createGameSession() {

        if (!sessionInProgress) {
            val myPlayer = Player(Authentication.instance.firebaseUser?.uid.toString())
            val game = Game(myPlayer, null)
            globalGameSession = GameSession(game)

            val gameSessionTable = FirebaseDatabaseHelper("GameSession", GameSession::class.java)

            globalGameSession?.let { gameS ->
                gameSessionTable.insert(
                        row = gameS,
                        success = { id ->
                            gameS.id = id
                            sessionInProgress = true
                            enterWaitingMode()
                        }, failure = {})

                // add listener
                gameSessionTable.addDataChangedListener { joinGameSession, eventType ->
                    when (eventType) {
                        EventType.childAdded -> {

                        }
                        EventType.childDeleted -> {

                        }
                        EventType.childChanged -> {

                            // some one joined your session
                            if (gameS.equals(joinGameSession)) {
                                if (joinGameSession.numberOfPlayers == 2) {
                                    globalGameSession = joinGameSession
                                    enterPlayingMode(gameSessionType = GameSessionType.creator)
                                }
                            }
                        }
                        EventType.childMoved -> {

                        }
                        else -> {

                        }
                    }
                }
            }

        }
    }

    // when joining game session the joining player is player2
    private fun joinWaitingGameSession() {
        val myPlayer = Player(Authentication.instance.firebaseUser?.uid.toString())
        var gameSessionTable: FirebaseDatabaseHelper<GameSession>? = FirebaseDatabaseHelper("GameSession", GameSession::class.java)

        // add listener
        var haveJoined = false
        gameSessionTable?.addDataChangedListener { gameSession, eventType ->
            when(eventType) {
                EventType.childAdded -> {
                    when(gameSession.status) {
                        GameSessionStatus.notStarted -> {

                            var myOwnSession = false
                            gameSession.let { gs -> gs.game?.let { game -> game.player?.let { currentSessionPlayer ->
                                if (currentSessionPlayer.equals(myPlayer)) {
                                    myOwnSession = true
                                }
                            } } }
                            if (!haveJoined && !myOwnSession) {
                                gameSession.game?.player2 = myPlayer
                                gameSession.numberOfPlayers += 1
                                gameSession.status = GameSessionStatus.inProgress
                                gameSessionTable?.update(arrayListOf(gameSession.id), gameSession, {}, {})
                                haveJoined = true
                                globalGameSession = gameSession
                                enterPlayingMode(gameSessionType = GameSessionType.joiner)
                                // after joining a session don't listen anymore, to avoid joining all open sessions
                                gameSessionTable = null
                            }
                        }
                    }
                }
                EventType.childDeleted -> {
                }
                EventType.childChanged -> {
                }
                EventType.childMoved -> {
                }
                else -> {

                }
            }
        }

    }

    private fun disabaleCreateandJoinButtons(){
        btnCreateGame.isEnabled = false
        btnJoinGame.isEnabled = false
    }

    private fun enableCreateandJoinButtons(){
        btnCreateGame.isEnabled = true
        btnJoinGame.isEnabled = true
    }

    private fun enterPlayingMode(gameSessionType: GameSessionType) {
        var userNameOfPlayerThatCurrentUserIsPlaying = "Fake"
        globalGameSession?.let {
            it.game?.let { game ->
                when(gameSessionType) {
                    GameSessionType.creator -> {
                        game.player2?.let { player ->
                            userNameOfPlayerThatCurrentUserIsPlaying = player.id
                        }
                    }
                    GameSessionType.joiner -> {
                        game.player?.let { player ->
                            userNameOfPlayerThatCurrentUserIsPlaying = player.id
                        }
                    }
                }
            }
        }
        btnCreateGame.text = "You are Playing $userNameOfPlayerThatCurrentUserIsPlaying"
        btnJoinGame.text = "You are Playing $userNameOfPlayerThatCurrentUserIsPlaying"
        disabaleCreateandJoinButtons()
    }

    private fun enterWaitingMode() {
        btnCreateGame.text = "Waiting for someone to Join"
        btnJoinGame.text = "Waiting for someone to Join"
        disabaleCreateandJoinButtons()
    }

    private fun enterNormalMode() {
        btnCreateGame.text = "Create Game"
        btnJoinGame.text = "Join Game"
        enableCreateandJoinButtons()
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
