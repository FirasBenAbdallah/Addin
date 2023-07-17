package addinn.dev.data.network

import addinn.dev.data.remote.MessageNotificationApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://fcm.googleapis.com"

    fun createNetworkService(
        client: OkHttpClient
    ): MessageNotificationApi {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MessageNotificationApi::class.java)
    }
}