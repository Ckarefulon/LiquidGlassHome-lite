package com.ckarefulon.liquidglasshome.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 顶栏左侧那枚圆形头像（折叠顶栏展开时淡入的那一份），调用方给尺寸，内部裁成圆形。
 *
 * 模板给的是中性的占位图，接自己的产品时把 `androidMain/resources/placeholder_avatar.webp`
 * 换成自己的形象即可，代码不用动。
 */
@Composable
expect fun PlaceholderAvatar(modifier: Modifier = Modifier)
