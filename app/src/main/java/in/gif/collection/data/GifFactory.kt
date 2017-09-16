package `in`.gif.collection.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by vivek on 15/09/17.
 */
class GifFactory {

    companion object {

        fun create(): GifService {
            val retrofit = Retrofit.Builder().baseUrl("https://api.giphy.com/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(GifService::class.java)
        }
    }
}