package com.jgames.apps.pokedex

import android.view.*

class PokeOnClickListener(appContext: MainActivity, poke: PokemonInfo) : View.OnClickListener {
    val appContext: MainActivity = appContext
    val poke      : PokemonInfo = poke

    override fun onClick(view: View?) {
        appContext.displayPokePage(poke)
    }
}