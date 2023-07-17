package addinn.dev.data.remote

import addinn.dev.data.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

const val FCM_SERVER_KEY =
    "AAAACPCvCho:APA91bHbTKcRQdW51hb_GUswhav7U2D_cyKCWDwhJycSJ27DJ-tnJAZnrN3T325ELhewQWlz21DpfVZoLNYTGEjwEIKbZ4veDhs7WSF1XnKqaFhFlEt8W7T9EWCzogo7Pmp9-Bza8wsG"

interface MessageNotificationApi {

    @Headers(
        "Authorization: key=$FCM_SERVER_KEY",
        "Content-Type:application/json"
    )
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}