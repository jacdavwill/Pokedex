package com.jgames.apps.pokedex

import retrofit2.*
import retrofit2.http.*
import kotlinx.coroutines.*

interface  PokeApi {
    @GET //for pulling initial info
    fun getPokeInfoAsync(@Url url : String?) : Deferred<Response<PokemonBatch>>

    @GET //for pulling pokemon details
    fun getPokeAsync(@Url url : String?) : Deferred<Response<Pokemon>>
}