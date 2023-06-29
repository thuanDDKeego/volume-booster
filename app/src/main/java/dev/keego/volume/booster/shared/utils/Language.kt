package dev.keego.volume.booster.shared.utils

import dev.keego.volume.booster.R
import dev.keego.volume.booster.section.model.Language
import dev.keego.volume.booster.setup.preference.LanguagePreferences

object LanguageUtils {
    fun getAll(): List<Language> {
        return listOf(
            Language(
                "English",
                "English",
                R.drawable.img_uk,
                "en"
            ),
            Language("Spanish", "Español", R.drawable.spain, "es"),
            Language("Portuguese", "Português", R.drawable.portugal, "pt"),
            Language("Italian", "Italiano", R.drawable.italy, "it"),
            Language("Malay", "Bahasa Melayu", R.drawable.malaysia, "ms"),
            Language("Filipino", "Filipino", R.drawable.philippines, "fil"),
            Language("Thai", "ภาษาไทย", R.drawable.thailand, "th"),
            Language("Korean", "한국어", R.drawable.korea, "ko"),
            Language("Japanese", "日本語", R.drawable.japan, "ja"),
            Language("India", "हिन्दी", R.drawable.india, "hi"),
            Language("Indonesia", "Bahasa Indonesia", R.drawable.indonesia, "id"),
            Language("Brazil", "Português do Brasil", R.drawable.brazil, "pt"),
            Language("Vietnamese", "Tiếng Việt", R.drawable.vietnam, "vi"),
            Language("Mexico", "Español de México", R.drawable.mexico, "es"),
            Language("Russia", "Русский", R.drawable.russia, "ru"),
            Language("Argentina", "Español de Argentina", R.drawable.argentina, "es"),
            Language("Ukraine", "Українська", R.drawable.ukraine, "uk"),
            Language("Turkey", "Türkçe", R.drawable.turkey, "tr"),
            Language("Algeria ", "لُغة الجزائر", R.drawable.algeria, "dz"),
            Language("Egypt ", "اللغة المصرية", R.drawable.egypt, "ar")
        )
    }

    fun getCurrent() =
        getAll().run { firstOrNull { it.name == LanguagePreferences.language } ?: first() }

    fun updateCurrent(language: Language) {
        LanguagePreferences.language = language.name
    }
}
