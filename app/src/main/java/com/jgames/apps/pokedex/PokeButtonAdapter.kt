package com.jgames.apps.pokedex

import android.widget.*
import android.view.*
import kotlin.*

class PokeButtonAdapter(appContext: MainActivity, users: ArrayList<PokemonInfo>) : ArrayAdapter<PokemonInfo>(appContext, 0, users) {
    private val appContext = appContext

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val poke = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.poke_button , parent, false)
        }

        val button: Button = convertView!!.findViewById(R.id.pButton)
        button.text = poke.name
        button.setOnClickListener( PokeOnClickListener(appContext, poke))

        return convertView
    }
}