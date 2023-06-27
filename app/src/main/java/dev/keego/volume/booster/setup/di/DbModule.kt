package dev.keego.volume.booster.setup.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.keego.volume.booster.section.local.AppDatabase
import dev.keego.volume.booster.section.local.preset.PresetDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "volumeboost_v8.db"
        ).addCallback(AppDatabase.Callback)
            .build()
    }

    @Provides
    @Singleton
    fun providePresetDao(database: AppDatabase): PresetDao {
        return database.presetDao()
    }
}