package com.jgames.apps.pokedex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.poke_page.*
import kotlinx.coroutines.*
import kotlin.collections.*
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
import android.graphics.BitmapFactory
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {
    private val pManager = PokeManager(this)
    private var imageApi: ImageApi? = null
    private var pokeApi = pManager.getPokeApi()
    private var pokeData: ArrayList<PokemonInfo>  = ArrayList()
    private var pokeDataAdapter: PokeButtonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //build retrofit with pokeApi
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        //instantiate image api and adapters
        imageApi = retrofit.create(ImageApi::class.java)
        pokeDataAdapter = PokeButtonAdapter(this, pokeData)
        listview.adapter = pokeDataAdapter

        pManager.importPokeNames(pokeData, progressBar)
    }

    //informs listView to update
    fun refreshListView () {
        pokeDataAdapter!!.notifyDataSetChanged()
    }

    //makes call for info from pokeApi based on the button pressed
    //the info is then displayed in the poke_page.xml
    fun displayPokePage(pokeInformation : PokemonInfo) {
        setContentView(R.layout.poke_page)
        //pull and set numerical and string data
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val pokeRequest = pokeApi.getPokeAsync(pokeInformation.url)
                val pokeResponse = pokeRequest.await()
                val poke : Pokemon? = pokeResponse.body()
                pokeName.text = poke?.name
                pokeHeight.text = getString(R.string.poke_height_str, poke?.height)
                pokeWeight.text = getString(R.string.poke_weight_str, poke?.weight)
                if (poke?.types?.size == 1)
                {
                    pokeType.text = poke.types[0].type.name
                    pokeTypeTitle2.visibility = View.INVISIBLE
                    pokeType2.visibility = View.INVISIBLE
                }
                else if (poke?.types?.size == 2)
                {
                    pokeType.text = poke.types[1].type.name
                    pokeType2.text = poke.types[0].type.name
                }
                //pull and set image data
                val imageRequest = imageApi?.getImageAsync(poke?.sprites?.front_default)
                val imageResponse = imageRequest?.await()
                val bmp = BitmapFactory.decodeStream(imageResponse?.body()?.byteStream())
                pokeImage.setImageBitmap(bmp)
            } catch (e: HttpException) {
                pManager.toast("Error connecting to internet")
            } catch (e: NullPointerException ) {
                pManager.toast("Image not available")
            } catch (e: Throwable) {
                pManager.toast("An error occurred")
            }
        }
    }

    //returns app to the opening screen
    fun displayPokedexPage(view: View) {
        setContentView(R.layout.activity_main)
        pokeDataAdapter?.clear()
        pokeDataAdapter?.addAll(pokeData)
        refreshListView()
        listview.setSelection(1)
    }
}

