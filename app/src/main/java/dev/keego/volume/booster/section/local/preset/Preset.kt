package dev.keego.volume.booster.section.local.preset

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "preset")
data class Preset(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val isDefault: Boolean = false,
    @DrawableRes val thumb: Int,
    val f60: Short,
    val f230: Short,
    val f910: Short,
    val f3k: Short,
    val f14k: Short
) : Parcelable
