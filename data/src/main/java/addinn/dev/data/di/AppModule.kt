package addinn.dev.data.di

import addinn.dev.data.network.RetrofitInstance
import addinn.dev.data.remote.MessageNotificationApi
import addinn.dev.data.repository.auth.AuthRepoImpl
import addinn.dev.data.repository.poll.PollRepoImpl
import addinn.dev.domain.repository.auth.AuthRepo
import addinn.dev.domain.repository.poll.PollRepo
import addinn.dev.data.repository.chat.MessagesRepoImpl
import addinn.dev.data.repository.chat.UsersRepoImpl
import addinn.dev.domain.repository.chat.MessagesRepo
import addinn.dev.domain.repository.chat.UsersRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    fun provideNetworkService(
        client: OkHttpClient
    ): MessageNotificationApi = RetrofitInstance.createNetworkService(client)

    @Singleton
    @Provides
    fun provideAuthRepository(
        database: FirebaseFirestore,
        cloudStorage: FirebaseStorage,
        auth: FirebaseAuth
    ): AuthRepo {
        return AuthRepoImpl(auth = auth, database = database, cloudStorage = cloudStorage)
    }

    @Singleton
    @Provides
    fun provideUsersRepository(
        database: FirebaseFirestore,
    ): UsersRepo {
        return UsersRepoImpl(database = database)
    }

    @Singleton
    @Provides
    fun provideMessagesRepository(
        database: FirebaseFirestore,
    ): MessagesRepo {
        return MessagesRepoImpl(database = database)
    }

    @Singleton
    @Provides
    fun providePollRepository(
        database: FirebaseFirestore,
    ): PollRepo {
        return PollRepoImpl( database = database)
    }
}