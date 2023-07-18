package com.jozu.weatherforecast.presentation.compose

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.text.InternalFoundationTextApi
import androidx.compose.foundation.text.TextDelegate
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.lang.Float.max
import java.lang.Float.min

/**
 *
 * Created by jozuko on 2023/07/18.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    autoSizeMaxTextSize: Float = LocalTextStyle.current.fontSize.value,
    autoSizeMinTextSize: Float = LocalTextStyle.current.fontSize.value,
    autoSizeStepGranularity: Float = 1.0f,
    autoSizePresetSizes: List<Float> = emptyList(),
    ignoreFontScale: Boolean = false,
) {
    BoxWithConstraints(modifier = modifier) {
        val mergedStyle = style.merge(
            TextStyle(
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
            )
        )

        val adjustedFontSizeStyle = getTextStyle(
            text,
            maxLines,
            autoSizeMaxTextSize,
            autoSizeMinTextSize,
            autoSizeStepGranularity,
            autoSizePresetSizes,
            ignoreFontScale,
            mergedStyle,
        )

        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontSize = TextUnit.Unspecified,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            style = adjustedFontSizeStyle,
            onTextLayout = onTextLayout,
        )
    }
}

@OptIn(InternalFoundationTextApi::class)
@Composable
private fun BoxWithConstraintsScope.getTextStyle(
    text: String,
    maxLines: Int,
    autoSizeMaxTextSize: Float,
    autoSizeMinTextSize: Float,
    autoSizeStepGranularity: Float,
    autoSizePresetSizes: List<Float>,
    ignoreFontScale: Boolean,
    style: TextStyle,
): TextStyle {
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val layoutDirection = LocalLayoutDirection.current

    val sizePresets = getFontSizes(
        autoSizeMaxTextSize,
        autoSizeMinTextSize,
        autoSizeStepGranularity,
        autoSizePresetSizes,
    )

    var combinedTextStyle = style
    for (fontSize in sizePresets) {
        val tempFontSize = if (ignoreFontScale) {
            with(density) {
                (fontSize / fontScale).sp
            }
        } else {
            fontSize.sp
        }
        combinedTextStyle = combinedTextStyle.copy(fontSize = tempFontSize)

        val textDelegate = TextDelegate(
            text = AnnotatedString(text),
            style = combinedTextStyle,
            maxLines = maxLines,
            minLines = 1,
            softWrap = true,
            overflow = TextOverflow.Clip,
            density = density,
            fontFamilyResolver = fontFamilyResolver,
        )
        val textLayoutResult = textDelegate.layout(
            constraints,
            layoutDirection = layoutDirection,
        )

        if (!textLayoutResult.hasVisualOverflow) {
            break
        }
    }

    return combinedTextStyle
}

private fun getFontSizes(
    autoSizeMaxTextSize: Float,
    autoSizeMinTextSize: Float,
    autoSizeStepGranularity: Float,
    autoSizePresetSizes: List<Float>,
): List<Float> {
    if (autoSizePresetSizes.isNotEmpty()) {
        return autoSizePresetSizes.sortedDescending()
    }

    val min = min(autoSizeMinTextSize, autoSizeMaxTextSize)
    val max = max(autoSizeMinTextSize, autoSizeMaxTextSize)

    val fontSizes = mutableListOf<Float>()
    while (fontSizes.isEmpty() || fontSizes.last() < max) {
        fontSizes.add(min + (autoSizeStepGranularity * fontSizes.count()))
    }

    return fontSizes.reversed()
}
