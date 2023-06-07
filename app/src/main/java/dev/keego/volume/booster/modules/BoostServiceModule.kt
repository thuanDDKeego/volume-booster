package dev.keego.volume.booster.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.keego.volume.booster.repositories.BoostServiceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BoostServiceModule {
    @Singleton
    @Provides
    fun providesBoostServiceRepository() = BoostServiceRepository()
}
