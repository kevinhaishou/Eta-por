package io.github.mangi.eta.data.model

import kotlinx.serialization.Serializable

const val MIN_INTERFACE_SCALE = 0.8f
const val MAX_INTERFACE_SCALE = 1.1f
const val DEFAULT_INTERFACE_SCALE = 1f

const val MIN_WALLPAPER_DIM = 0f
const val MAX_WALLPAPER_DIM = 1f
const val MIN_WALLPAPER_BLUR = 0f
const val MAX_WALLPAPER_BLUR = 24f

@Serializable
data class AppearanceSettings(
    val themeMode: AppearanceThemeMode = AppearanceThemeMode.SYSTEM,
    val monetEnabled: Boolean = false,
    val paletteStyle: AppearancePaletteStyle = AppearancePaletteStyle.FRUIT_SALAD,
    val accentColor: AppearanceAccentColor = AppearanceAccentColor.SYSTEM,
    val pureBlackEnabled: Boolean = true,
    val blurEnabled: Boolean = true,
    val topBarBlurStyle: AppearanceTopBarBlurStyle = AppearanceTopBarBlurStyle.GAUSSIAN,
    val swipeDismissEnabled: Boolean = true,
    val predictiveBackEnabled: Boolean = true,
    val interfaceScale: Float = DEFAULT_INTERFACE_SCALE,
    // ---- 图片功能：自定义壁纸 ----
    val wallpaperEnabled: Boolean = false,
    val wallpaperPath: String = "",
    val wallpaperDim: Float = 0f,
    val wallpaperBlur: Float = 0f,
    // ---- 图片功能：聊天双方头像 ----
    val avatarEnabled: Boolean = false,
    val avatarMinePath: String = "",
    val avatarAiPath: String = "",
) {
    fun normalized(): AppearanceSettings = copy(
        interfaceScale = normalizeInterfaceScale(interfaceScale),
        wallpaperDim = normalizeWallpaperDim(wallpaperDim),
        wallpaperBlur = normalizeWallpaperBlur(wallpaperBlur),
    )
}

@Serializable
enum class AppearanceThemeMode(val persistedValue: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromPersistedValue(value: String?): AppearanceThemeMode =
            entries.firstOrNull { it.persistedValue == value } ?: SYSTEM
    }
}

@Serializable
enum class AppearancePaletteStyle(val persistedValue: String) {
    TONAL_SPOT("tonal_spot"),
    NEUTRAL("neutral"),
    VIBRANT("vibrant"),
    EXPRESSIVE("expressive"),
    RAINBOW("rainbow"),
    FRUIT_SALAD("fruit_salad"),
    MONOCHROME("monochrome"),
    FIDELITY("fidelity"),
    CONTENT("content");

    companion object {
        fun fromPersistedValue(value: String?): AppearancePaletteStyle =
            entries.firstOrNull { it.persistedValue == value } ?: TONAL_SPOT
    }
}

@Serializable
enum class AppearanceAccentColor(val persistedValue: String) {
    SYSTEM("system"),
    BLUE("blue"),
    PURPLE("purple"),
    PINK("pink"),
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    GREEN("green"),
    TEAL("teal");

    companion object {
        fun fromPersistedValue(value: String?): AppearanceAccentColor =
            entries.firstOrNull { it.persistedValue == value } ?: SYSTEM
    }
}

@Serializable
enum class AppearanceTopBarBlurStyle(val persistedValue: String) {
    GAUSSIAN("gaussian"),
    PROGRESSIVE("progressive");

    companion object {
        fun fromPersistedValue(value: String?): AppearanceTopBarBlurStyle =
            entries.firstOrNull { it.persistedValue == value } ?: GAUSSIAN
    }
}

fun normalizeInterfaceScale(value: Float): Float =
    if (value.isFinite()) {
        value.coerceIn(MIN_INTERFACE_SCALE, MAX_INTERFACE_SCALE)
    } else {
        DEFAULT_INTERFACE_SCALE
    }

fun normalizeWallpaperDim(value: Float): Float =
    if (value.isFinite()) value.coerceIn(MIN_WALLPAPER_DIM, MAX_WALLPAPER_DIM) else 0f

fun normalizeWallpaperBlur(value: Float): Float =
    if (value.isFinite()) value.coerceIn(MIN_WALLPAPER_BLUR, MAX_WALLPAPER_BLUR) else 0f
