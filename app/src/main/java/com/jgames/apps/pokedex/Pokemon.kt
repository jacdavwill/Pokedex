package com.jgames.apps.pokedex

class PokemonBatch (
    val next     : String = "http://pokeapi.co/api/v2/pokemon/",
    val count    : Int = 0,
    val results  : Array<PokemonInfo> = arrayOf()
)

class PokemonInfo (
    val name : String = "",
    val url  : String = ""
)

class Pokemon(
    val name     : String = "",
    val height   : Int = 0,
    val weight   : Int = 0,
    val types    : Array<PokeType> = arrayOf(),
    val sprites  : Sprites = Sprites()
    )

class PokeType (
    val type: TypeInfo = TypeInfo()
)

class TypeInfo (
    val name : String = ""
)

class Sprites (
    val front_default : String = ""
)