package com.example.hw12.data.network

import com.example.hw12.model.imdb.IMDBResponse
import com.example.hw12.model.imdb.properties.IMDBGenres
import com.example.hw12.model.imdb.properties.IMDBLanguages
import com.example.hw12.model.imdb.properties.IMDBTitleType
import com.example.hw12.model.imdb.properties.SearchMethod
import com.example.hw12.model.imdb.search.SearchResponse
import com.example.hw12.model.imdb.trailer.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMDBService {
    companion object {
        const val TOKEN = "k_ob4vv6yh"
    }

    @GET("AdvancedSearch/$TOKEN")
    fun search(
        @Query("title_type") titleType: IMDBTitleType = IMDBTitleType.VIDEO,
        @Query("genres") genres: IMDBGenres = IMDBGenres.ACTION,
        @Query("languages") language: IMDBLanguages = IMDBLanguages.ENGLISH,
        @Query("count") per_page: Int = 30,
    ) : Call<SearchResponse>

    @GET("{method}/$TOKEN")
    fun searchMethod(
        @Path("method") method: SearchMethod
    ) : Call<IMDBResponse>

    @GET("Trailer/$TOKEN/{id}")
    fun getTrailer(
        @Path("id") id: String
    ) : Call<TrailerResponse>
}
