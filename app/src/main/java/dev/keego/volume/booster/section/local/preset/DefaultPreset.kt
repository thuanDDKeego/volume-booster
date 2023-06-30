package dev.keego.volume.booster.section.local.preset

import dev.keego.volume.booster.R

object DefaultPreset {
    fun get(): List<Preset> {
        return listOf(
            Preset(
                name = "Classical",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(300, -200, 100, 50, 200),
                isDefault = true
            ),
            Preset(
                name = "Dance",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(500, 0, -200, 300, 400),
                isDefault = true
            ),
            Preset(
                name = "Flat",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(100, 0, 0, 0, 100),
                isDefault = true
            ),
            Preset(
                name = "Folk",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(-200, 300, -100, 150, 0),
                isDefault = true
            ),
            Preset(
                name = "Heavy Metal",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(300, 100, 500, 400, -200),
                isDefault = true
            ),
            Preset(
                name = "Hip Hop",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(800, -100, 200, 100, -300),
                isDefault = true
            ),
            Preset(
                name = "Jazz",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(0, 200, -100, 300, 400),
                isDefault = true
            ),
            Preset(
                name = "Pop",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(-200, 100, 300, 200, 500),
                isDefault = true
            ),
            Preset(
                name = "Rock",
                thumb = R.drawable.ic_music,
                bandLevels = listOf(400, 200, 100, 500, -100),
                isDefault = true
            ),
            Preset(
                name = "ACG",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(100, 0, 200, 300, 400)
            ),
            Preset(
                name = "Plate",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(0, 100, 200, 100, 0)
            ),
            Preset(
                name = "Blues",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(200, -100, 300, 100, 400)
            ),
            Preset(
                name = "Electronic",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(600, 0, -200, 300, 500)
            ),
            Preset(
                name = "Slow",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(-200, 100, 0, 200, 300)
            ),
            Preset(
                name = "Flow Bass",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(1000, -300, 100, -200, -500)
            ),
            Preset(
                name = "Elec Bass",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(1200, -400, 0, -100, -400)
            ),
            Preset(
                name = "DJ",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(500, 0, 200, 300, 400)
            ),
            Preset(
                name = "Live",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(-100, 200, 0, 300, 400)
            ),
            Preset(
                name = "Vocals",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(-400, 200, 300, 800, 200)
            ),
            Preset(
                name = "Country",
                isDefault = true,
                thumb = R.drawable.ic_music,
                bandLevels = listOf(0, 300, -200, 100, 300)
            )
        )
    }

//    fun sqlStatementPrepopulateDB(): String {
//        var sqlStatement =
//            "INSERT INTO preset (name, isDefault, thumb, f60, f230, f910, f3k, f14k) VALUES "
//        val defaultPresets = get()
//        defaultPresets.forEachIndexed { index, preset ->
//            sqlStatement += "('${preset.name}', ${preset.isDefault}, ${preset.thumb}, ${preset.f60}, ${preset.f230}, ${preset.f910}, ${preset.f3k}, ${preset.f14k})"
//            if (index != defaultPresets.size - 1) sqlStatement += ", "
//        }
//        return sqlStatement
//    }

    fun sqlStatementPrepopulateDB(): String {
        var sqlStatement =
            "INSERT INTO preset (name, isDefault, thumb, band_levels, time_created) VALUES "
        val defaultPresets = get()
        defaultPresets.forEachIndexed { index, preset ->
            val gainsAsString =
                preset.bandLevels.joinToString(prefix = "[", postfix = "]", separator = ",")
            sqlStatement += "('${preset.name}', ${preset.isDefault}, ${preset.thumb}, '$gainsAsString', 0)"
            if (index != defaultPresets.size - 1) sqlStatement += ", "
        }
        return sqlStatement
    }
}
