package io.github.mangi.eta.ui.app

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import java.io.File

/**
 * 自定义壁纸背景层：铺满(Crop)、按 wallpaperDim 压暗、按 wallpaperBlur 模糊。
 * 无第三方图片库，直接 BitmapFactory 解码 app 私有目录里的文件。
 */
@Composable
fun WallpaperBackground(modifier: Modifier = Modifier) {
    val appearance = LocalAppearanceSettings.current
    if (!appearance.wallpaperEnabled || appearance.wallpaperPath.isBlank()) return
    val bitmap by produceState<ImageBitmap?>(initialValue = null, appearance.wallpaperPath) {
        value = runCatching {
            val file = File(appearance.wallpaperPath)
            if (file.exists()) BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap() else null
        }.getOrNull()
    }
    val bmp = bitmap ?: return
    Box(modifier.fillMaxSize()) {
        Image(
            bitmap = bmp,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().graphicsLayer {
                if (appearance.wallpaperBlur > 0f) {
                    renderEffect = RenderEffect.createBlurEffect(
                        appearance.wallpaperBlur,
                        appearance.wallpaperBlur,
                        TileMode.Clamp,
                    )
                }
            },
        )
        if (appearance.wallpaperDim > 0f) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = appearance.wallpaperDim)))
        }
    }
}
