package addinn.dev.team.utils.di

import addinn.dev.team.utils.connectivity.ConnectivityObserver
import addinn.dev.team.utils.connectivity.ConnectivityObserverImp
import android.app.Application
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(application: Application): ConnectivityManager =
        application.getSystemService(ConnectivityManager::class.java)

    @Provides
    @Singleton
    fun provideConnectivityObserver(connectivityManager: ConnectivityManager): ConnectivityObserver =
        ConnectivityObserverImp(connectivityManager)
}