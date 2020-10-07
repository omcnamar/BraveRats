package com.olegmcnamara.braverats.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.olegmcnamara.braverats.R
import com.olegmcnamara.braverats.model.Card
import kotlinx.android.synthetic.main.card.view.*
import kotlin.math.sign

class CardsAdapter constructor() : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    private var cardSelectedListener: ((Card) -> Unit) = {}

    private var cards: ArrayList<Card> = ArrayList()

    constructor(cards: ArrayList<Card>, cardSelectedListener: (Card) -> Unit) : this() {
        this.cards = cards
        this.cardSelectedListener = cardSelectedListener
    }

    fun setCards(cards: ArrayList<Card>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card, parent, false)

        view.layoutParams = LinearLayout.LayoutParams(parent.measuredWidth / 3, parent.measuredHeight)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cards[position]
        holder.tvCardName.text = item.cardName
        holder.tvCardNumber.text = "${item.cardNumber}"
        holder.tvCardPower.text = item.cardPower

        with(holder.mView) {
            tag = item
            this.btnPlayCard.setOnClickListener{
                cards.remove(item)
                cardSelectedListener(item)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = cards.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val tvCardName: TextView = mView.tvCardName
        val tvCardNumber: TextView = mView.tvCardNumber
        val tvCardPower: TextView = mView.tvCardPower
        val btnPlayCard: Button = mView.btnPlayCard


    }
}
