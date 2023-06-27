package dev.keego.volume.booster.section.local.preset

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PresetDao {
    @Query("select * from preset")
    fun getAll(): Flow<List<Preset>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preset: Preset): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(preset: Preset)

    @Delete
    suspend fun delete(preset: Preset)
}