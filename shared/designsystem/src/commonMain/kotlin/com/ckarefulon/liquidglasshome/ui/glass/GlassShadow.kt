package com.ckarefulon.liquidglasshome.ui.glass

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.shadow.Shadow

/**
 * 悬浮玻璃（胶囊底栏、悬浮条这类真浮在内容上的东西）的阴影。
 *
 * **不吃主题色**：曾经是 `lerp(Black, accent, 0.4f)`，主题色偏亮时（比如亮青 `#24F0EA`）
 * 阴影会变成一层发亮的彩色偏移，在胶囊下面看着像内容被复制了一份（重影）。
 * 阴影只表达「这里有高度」，跟主题色无关，固定用几乎中性黑。
 */
fun floatingGlassShadow(
    accent: Color,
    isDark: Boolean,
): Shadow {
    // 只保留一点点主题色的环境反光：上限压到 0.12，亮色主题色不再能把阴影提亮成第二层轮廓
    val ambient = lerp(Color.Black, accent, 0.12f)
    return Shadow(
        radius = 28.dp,
        offset = DpOffset(0.dp, 10.dp),
        color = ambient.copy(alpha = if (isDark) 0.45f else 0.18f),
    )
}
