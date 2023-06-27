package dev.keego.volume.booster.section.local.preset

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "preset")
@Immutable
data class Preset(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val isDefault: Boolean = false,
    @DrawableRes val thumb: Int,
    @ColumnInfo(name = "band_levels") val bandLevels: List<Int> = listOf(),
    @ColumnInfo(name = "time_created") val timeCreated: Long = 0
) : Parcelable
