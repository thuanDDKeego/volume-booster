package dev.keego.volume.booster.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.keego.volume.booster.repositories.NotificationPlaybackRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationPlaybackModule {
    @Singleton
    @Provides
    fun providesNotificationPlaybackActionRepository() = NotificationPlaybackRepository()
}
