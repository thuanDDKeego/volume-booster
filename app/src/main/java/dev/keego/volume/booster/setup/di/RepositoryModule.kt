package dev.keego.volume.booster.setup.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.keego.volume.booster.section.local.preset.PresetDao
import dev.keego.volume.booster.section.repositories.AddOnFeaturesRepository
import dev.keego.volume.booster.section.repositories.PresetRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesPresetRepository(presetDao: PresetDao) = PresetRepository(presetDao)

    @Provides
    @Singleton
    fun providesAddOnFeatureRepository(@ApplicationContext context: Context) =
        AddOnFeaturesRepository(context)
}
