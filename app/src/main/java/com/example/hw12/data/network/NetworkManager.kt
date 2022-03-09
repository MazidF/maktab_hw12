package com.example.hw12.data.network

import com.example.hw12.model.user.User
import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory
import com.github.leonardoxh.livedatacalladapter.LiveDataResponseBodyConverterFactory
import com.google.gson.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private fun crateInterceptor(value: String): Interceptor {
        return Interceptor { chain ->
            val oldRequest = chain.request()
            val newRequest = oldRequest.newBuilder()
                .addHeader("Authorization", value)
                .build()
            chain.proceed(newRequest)
        }
    }

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(User::class.java, userAdapter())
        .setLenient()
        .create()

    private fun userAdapter() = JsonDeserializer { json, _, _ ->
        val jsonObject = json.asJsonObject
        User.fromJsonObject(jsonObject)
    }

    private val retrofitUser = Retrofit.Builder()
        .baseUrl("http://papp.ir/api/v1/")
        .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
        .addConverterFactory(LiveDataResponseBodyConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(
            client.newBuilder()
                .addInterceptor(crateInterceptor("Bearer Mazid_App_Token!!!"))
                .build()
        )
        .build()

    private val retrofitImage: Retrofit = retrofitUser.newBuilder()
        .baseUrl("http://51.195.19.222/")
        .client(client)
        .build()

    private val retrofitIMDB = Retrofit.Builder()
        .baseUrl("https://imdb-api.com/en/API/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private const val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmNWE1ZDdhZjU5Y2JhOTQxZjJkMDk2NDg4NjllYTRlMyIsInN1YiI6IjYyMjMzMDJhZTlkYTY5MDA0M2Q4YTExYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.0D6sjab3rWtARmajZKNWjrkwcJUTO4cCs9qIFGRiJ6Q"
    private val retrofitTMDB = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(
            client.newBuilder()
                .addInterceptor(crateInterceptor("Bearer $accessToken"))
                .build()
        ).build()

    val userService: UserService = retrofitUser.create(UserService::class.java)
    val imageService: ImageService = retrofitImage.create(ImageService::class.java)
    val imdbService: IMDBService = retrofitIMDB.create(IMDBService::class.java)
    val tmdbService: TMDBService = retrofitTMDB.create(TMDBService::class.java)
}

