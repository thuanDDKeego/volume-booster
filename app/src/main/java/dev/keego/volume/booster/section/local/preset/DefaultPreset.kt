package dev.keego.volume.booster.section.local.preset

import dev.keego.volume.booster.R

object DefaultPreset {
    fun get(): List<Preset> {
        return listOf(
            Preset(
                name = "Classical",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 300,
                f230 = -200,
                f910 = 100,
                f3k = 50,
                f14k = 200
            ),
            Preset(
                name = "Dance",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 500,
                f230 = 0,
                f910 = -200,
                f3k = 300,
                f14k = 400
            ),
            Preset(
                name = "Flat",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 100,
                f230 = 0,
                f910 = 0,
                f3k = 0,
                f14k = 100
            ),
            Preset(
                name = "Folk",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = -200,
                f230 = 300,
                f910 = -100,
                f3k = 150,
                f14k = 0
            ),
            Preset(
                name = "Heavy Metal",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 300,
                f230 = 100,
                f910 = 500,
                f3k = 400,
                f14k = -200
            ),
            Preset(
                name = "Hip Hop",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 800,
                f230 = -100,
                f910 = 200,
                f3k = 100,
                f14k = -300
            ),
            Preset(
                name = "Jazz",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 0,
                f230 = 200,
                f910 = -100,
                f3k = 300,
                f14k = 400
            ),
            Preset(
                name = "Pop",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = -200,
                f230 = 100,
                f910 = 300,
                f3k = 200,
                f14k = 500
            ),
            Preset(
                name = "Rock",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 400,
                f230 = 200,
                f910 = 100,
                f3k = 500,
                f14k = -100
            ),
            Preset(
                name = "ACG",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 100,
                f230 = 0,
                f910 = 200,
                f3k = 300,
                f14k = 400
            ),
            Preset(
                name = "Plate",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 0,
                f230 = 100,
                f910 = 200,
                f3k = 100,
                f14k = 0
            ),
            Preset(
                name = "Blues",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 200,
                f230 = -100,
                f910 = 300,
                f3k = 100,
                f14k = 400
            ),
            Preset(
                name = "Electronic",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 600,
                f230 = 0,
                f910 = -200,
                f3k = 300,
                f14k = 500
            ),
            Preset(
                name = "Slow",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = -200,
                f230 = 100,
                f910 = 0,
                f3k = 200,
                f14k = 300
            ),
            Preset(
                name = "Flow Bass",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 1000,
                f230 = -300,
                f910 = 100,
                f3k = -200,
                f14k = -500
            ),
            Preset(
                name = "Elec Bass",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 1200,
                f230 = -400,
                f910 = 0,
                f3k = -100,
                f14k = -400
            ),
            Preset(
                name = "DJ",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 500,
                f230 = 0,
                f910 = 200,
                f3k = 300,
                f14k = 400
            ),
            Preset(
                name = "Live",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = -100,
                f230 = 200,
                f910 = 0,
                f3k = 300,
                f14k = 400
            ),
            Preset(
                name = "Vocals",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = -400,
                f230 = 200,
                f910 = 300,
                f3k = 800,
                f14k = 200
            ),
            Preset(
                name = "Country",
                isDefault = true,
                thumb = R.drawable.ic_launcher_foreground,
                f60 = 0,
                f230 = 300,
                f910 = -200,
                f3k = 100,
                f14k = 300
            )
        )
    }

    fun sqlStatementPrepopulateDB(): String {
        var sqlStatement =
            "INSERT INTO preset (name, isDefault, thumb, f60, f230, f910, f3k, f14k) VALUES "
        val defaultPresets = get()
        defaultPresets.forEachIndexed { index, preset ->
            sqlStatement += "('${preset.name}', ${preset.isDefault}, ${preset.thumb}, ${preset.f60}, ${preset.f230}, ${preset.f910}, ${preset.f3k}, ${preset.f14k})"
            if (index != defaultPresets.size - 1) sqlStatement += ", "
        }
        return sqlStatement
    }
}
