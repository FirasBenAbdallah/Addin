package addinn.dev.team.utils.di

import addinn.dev.team.viewModel.SharedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedDataModule {

    @Provides
    @Singleton
    fun provideSharedViewModel(): SharedViewModel {
        return SharedViewModel()
    }
}