package dev.keego.volume.booster.section.repositories

import dev.keego.volume.booster.section.local.preset.Preset
import dev.keego.volume.booster.section.local.preset.PresetDao

class PresetRepository(private val presetDao: PresetDao) {
    fun getAll() = presetDao.getAll()
    suspend fun insert(preset: Preset) = presetDao.insert(preset)
    suspend fun delete(preset: Preset) = presetDao.delete(preset)
}