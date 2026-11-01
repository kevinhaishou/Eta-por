package io.github.mangi.eta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import io.github.mangi.eta.data.model.AppearanceTopBarBlurStyle
import io.github.mangi.eta.ui.app.LocalAppearanceSettings
import io.github.mangi.eta.ui.app.LocalBlurEnabled
import io.github.mangi.eta.ui.app.LocalTopBarBlurStyle
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.ProgressiveBlur
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.progressiveTextureBlur
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun rememberTopBarBackdrop(): LayerBackdrop? {
    if (!LocalBlurEnabled.current || !isRuntimeShaderSupported()) return null
    val surfaceColor = MiuixTheme.colorScheme.surface
    // 毛玻璃底必须跟随顶栏透明度：这里若填不透明色，无论 blend alpha 调到多低，
    // 模糊采样到的都是那块实色，表现就是「透明度调到 15% 顶栏仍然是黑的」。
    val topBarAlpha = LocalAppearanceSettings.current.topBarAlpha
    return rememberLayerBackdrop {
        drawRect(surfaceColor.copy(alpha = topBarAlpha))
        drawContent()
    }
}

@Composable
internal fun TopBarBackdrop(
    backdrop: LayerBackdrop?,
    content: @Composable () -> Unit,
) {
    val surfaceColor = MiuixTheme.colorScheme.surface
    val topBarAlpha = LocalAppearanceSettings.current.topBarAlpha
    val modifier = when {
        backdrop == null -> Modifier.background(surfaceColor.copy(alpha = topBarAlpha))
        LocalTopBarBlurStyle.current == AppearanceTopBarBlurStyle.PROGRESSIVE -> {
            Modifier.progressiveTextureBlur(
                backdrop = backdrop,
                shape = RectangleShape,
                gradient = ProgressiveBlur.Top.copy(curve = 2.2f),
                blurRadius = ProgressiveTopBarBlurRadius,
                colors = BlurColors(
                    blendColors = listOf(
                        BlendColorEntry(surfaceColor.copy(alpha = topBarAlpha)),
                    ),
                ),
            )
        }
        else -> {
            Modifier.textureBlur(
                backdrop = backdrop,
                shape = RectangleShape,
                blurRadius = TopBarBlurRadius,
                colors = BlurColors(
                    blendColors = listOf(
                        BlendColorEntry(surfaceColor.copy(alpha = topBarAlpha)),
                    ),
                ),
            )
        }
    }
    Box(modifier = modifier) { content() }
}

internal fun Modifier.captureForTopBar(backdrop: LayerBackdrop?): Modifier =
    if (backdrop == null) this else layerBackdrop(backdrop)

@Composable
internal fun topBarContainerColor(backdrop: LayerBackdrop?): Color =
    if (backdrop == null) MiuixTheme.colorScheme.surface else Color.Transparent

private const val TopBarBlurRadius = 25f
private const val TopBarSurfaceAlpha = 0.8f
private const val ProgressiveTopBarBlurRadius = 10f
private const val ProgressiveTopBarSurfaceAlpha = 0.3f
