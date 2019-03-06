package com.jgames.apps.pokedex

import android.widget.*
import android.view.*
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class PokeManager(context : MainActivity){
    private val pokeApi: PokeApi
    private val appContext : MainActivity = context
    private var importProgress : Int = 0

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://pokeapi.co/api/v2/pokemon/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        pokeApi = retrofit.create(PokeApi::class.java)
    }

    fun importPokeNames(pokeList : ArrayList<PokemonInfo>, progBar : ProgressBar) {
        GlobalScope.launch(Dispatchers.Main) {
            var pokeCount = 0
            var numPoke : Int? = 0
            var url : String? = "http://pokeapi.co/api/v2/pokemon/"
            var firstFetch = true

            do {
                val request = pokeApi.getPokeInfoAsync(url)
                try {
                    val response = request.await()
                    val currPokeBatch = response.body()
                    if (firstFetch) {
                        numPoke = currPokeBatch?.count
                        firstFetch = false
                    }
                    url = currPokeBatch?.next
                    for (counter in 0..(currPokeBatch!!.results.size - 1)) {
                        pokeList.add(currPokeBatch.results[counter])
                    }
                    appContext.refreshListView()
                    importProgress += currPokeBatch.results.size
                    pokeCount += currPokeBatch.results.size

                    progBar.setProgress(importProgress * 100 / numPoke!!, true)
                } catch (e: HttpException) {
                    toast("Error connecting to internet")
                } catch (e: Throwable) {
                    toast("Sorry, there appears to be an error")
                }
            } while (pokeCount <= numPoke!! && url != null)
            progBar.visibility = View.GONE
        }
    }

    fun toast(message: String?) =
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()

    fun getPokeApi() : PokeApi {
        return pokeApi
    }
}