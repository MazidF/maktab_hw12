package com.example.hw12.data.remote

import com.example.hw12.data.network.NetworkManager
import com.example.hw12.model.imdb.IMDBResponse
import com.example.hw12.model.imdb.properties.SearchMethod
import com.example.hw12.model.imdb.search.SearchResponse
import com.example.hw12.setCallback
import com.example.hw12.utils.MyCallback

class IMDBDataSource {
    private val service = NetworkManager.imdbService

    fun search(searchMethod: SearchMethod, myCallback: MyCallback<IMDBResponse>? = null): IMDBResponse? {
        val call = service.searchMethod(searchMethod)
        if (myCallback != null) {
            myCallback.call(call)
            return null
        }
        return call.execute().body()
    }

    fun advanceSearch(myCallback: MyCallback<SearchResponse>? = null): SearchResponse? {
        val call = service.search()
        return call.setCallback(myCallback)
    }
}
