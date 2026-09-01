package com.tyshi00.astrolight.ui

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import com.thelightphone.lp3Keyboard.ui.NumberLayout
import com.thelightphone.sdk.SealedLightActivity
import com.thelightphone.sdk.SimpleLightScreen
import com.thelightphone.sdk.rememberKeyboardOptions
import com.thelightphone.sdk.ui.LightTextInputEditor
import com.thelightphone.sdk.ui.LightTheme
import com.thelightphone.sdk.ui.LightThemeController

/**
 * Number input editor. Starts with an empty field and a number keyboard.
 */
class NumberEditorScreen(
    sealedActivity: SealedLightActivity,
    private val title: String = "Enter",
    private val hint: String = "",
) : SimpleLightScreen<String>(sealedActivity) {

    private val editorKey = System.nanoTime()

    @Composable
    override fun Content() = key(editorKey) {
        val themeColors by LightThemeController.colors.collectAsState()
        val state = rememberTextFieldState("")
        val keyboardOptionsFlow = rememberKeyboardOptions()

        LightTheme(colors = themeColors) {
            LightTextInputEditor(
                title = if (hint.isNotEmpty()) "$title ($hint)" else title,
                editorKey = editorKey,
                state = state,
                keyboardOptionsFlow = keyboardOptionsFlow,
                onSubmit = { text ->
                    val trimmed = text.toString().trim()
                    if (trimmed.isNotEmpty()) goBack(trimmed)
                },
                onBack = { goBack(null) },
                submitLabel = "CONFIRM",
                singleLine = true,
                initialLayout = NumberLayout,
            )
        }
    }
}

/**
 * Text input editor with QWERTY keyboard.
 */
class TextEditorScreen(
    sealedActivity: SealedLightActivity,
    private val title: String = "Enter",
) : SimpleLightScreen<String>(sealedActivity) {

    private val editorKey = System.nanoTime()

    @Composable
    override fun Content() = key(editorKey) {
        val themeColors by LightThemeController.colors.collectAsState()
        val state = rememberTextFieldState("")
        val keyboardOptionsFlow = rememberKeyboardOptions()

        LightTheme(colors = themeColors) {
            LightTextInputEditor(
                title = title,
                editorKey = editorKey,
                state = state,
                keyboardOptionsFlow = keyboardOptionsFlow,
                onSubmit = { text ->
                    val trimmed = text.toString().trim()
                    if (trimmed.isNotEmpty()) goBack(trimmed)
                },
                onBack = { goBack(null) },
                submitLabel = "CONFIRM",
                singleLine = true,
            )
        }
    }
}
