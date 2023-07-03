package addinn.dev.data.di

import addinn.dev.data.repository.auth.AuthRepoImpl
import addinn.dev.domain.repository.auth.AuthRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
    ): AuthRepo {
        return AuthRepoImpl(auth = auth, database = database)
    }
}