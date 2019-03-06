package com.jgames.apps.pokedex

import kotlinx.coroutines.Deferred
import retrofit2.*
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ImageApi {
    @GET //pulls image info from image website
    fun getImageAsync(@Url url: String?): Deferred<Response<ResponseBody>>
}