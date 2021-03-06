package com.olegmcnamara.braverats.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.olegmcnamara.braverats.R
import com.olegmcnamara.braverats.model.Card
import kotlinx.android.synthetic.main.card.*


class CardFragment : Fragment() {

    private lateinit var card: Card
    private var listener: OnFragmentInteractionListener? = null

    companion object {
        @JvmStatic
        fun newInstance(card: Card) =
                CardFragment().apply {
                    this.card = card
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCardName.text = card.cardName
        tvCardNumber.text = "${card.cardNumber}"
        tvCardPower.text = card.cardPower
        btnPlayCard.text = "Take Back"
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        //listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {

    }
}
