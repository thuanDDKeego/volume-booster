package dev.keego.volume.booster.section.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.keego.volume.booster.section.local.converter.ListIntConverters
import dev.keego.volume.booster.section.local.preset.DefaultPreset
import dev.keego.volume.booster.section.local.preset.Preset
import dev.keego.volume.booster.section.local.preset.PresetDao

@Database(
    entities = [
        Preset::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListIntConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun presetDao(): PresetDao

    companion object {
        val Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL(
                    DefaultPreset.sqlStatementPrepopulateDB()
                )
            }
        }
    }
}
