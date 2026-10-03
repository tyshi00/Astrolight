package com.tyshi00.astrolight.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewModelScope
import com.thelightphone.sdk.LightScreen
import com.thelightphone.sdk.LightViewModel
import com.thelightphone.sdk.SealedLightActivity
import com.thelightphone.sdk.SimpleLightScreen
import com.thelightphone.sdk.ui.LightBarButton
import com.thelightphone.sdk.ui.LightBottomBar
import com.thelightphone.sdk.ui.LightIcons
import com.thelightphone.sdk.ui.LightScrollView
import com.thelightphone.sdk.ui.LightText
import com.thelightphone.sdk.ui.LightTextVariant
import com.thelightphone.sdk.ui.LightTheme
import com.thelightphone.sdk.ui.LightThemeController
import com.thelightphone.sdk.ui.LightThemeTokens
import com.thelightphone.sdk.ui.LightTopBar
import com.thelightphone.sdk.ui.LightTopBarCenter
import com.thelightphone.sdk.ui.gridUnitsAsDp
import com.thelightphone.sdk.ui.lightClickable
import com.tyshi00.astrolight.data.AstroLightRepository
import com.tyshi00.astrolight.data.ZodiacCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CompatState(
    val hasResult: Boolean = false,
    val yourSign: String = "",
    val theirSign: String = "",
    val yourChinese: String = "",
    val theirChinese: String = "",
    val westernCompat: String = "",
    val elementCompat: String = "",
    val chineseCompat: String = "",
    val overallVibes: String = "",
)

class CompatViewModel(private val repo: AstroLightRepository) : LightViewModel<Unit>() {

    private val _state = MutableStateFlow(CompatState())
    val state: StateFlow<CompatState> = _state.asStateFlow()

    fun checkCompatibility(theirYear: Int, theirMonth: Int, theirDay: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val myProfile = repo.getProfile() ?: return@launch
            val theirProfile = ZodiacCalculator.profile(theirYear, theirMonth, theirDay)
            val report = ZodiacCalculator.compatibility(myProfile, theirProfile)

            _state.value = CompatState(
                hasResult = true,
                yourSign = "${myProfile.westernSign.symbol} ${myProfile.westernSign.name}",
                theirSign = "${theirProfile.westernSign.symbol} ${theirProfile.westernSign.name}",
                yourChinese = "${myProfile.chineseAnimal.emoji} ${myProfile.chineseFullName}",
                theirChinese = "${theirProfile.chineseAnimal.emoji} ${theirProfile.chineseFullName}",
                westernCompat = report.westernCompatibility,
                elementCompat = report.elementCompatibility,
                chineseCompat = report.chineseCompatibility,
                overallVibes = report.overallVibes,
            )
        }
    }
}

class CompatibilityScreen(
    sealedActivity: SealedLightActivity,
    private val repo: AstroLightRepository,
) : LightScreen<Unit, CompatViewModel>(sealedActivity) {

    override val viewModelClass: Class<CompatViewModel>
        get() = CompatViewModel::class.java

    override fun createViewModel() = CompatViewModel(repo)

    @Composable
    override fun Content() {
        val themeColors by LightThemeController.colors.collectAsState()
        val state by viewModel.state.collectAsState()

        LightTheme(colors = themeColors) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightThemeTokens.colors.background),
            ) {
                val haptic = rememberHaptic()

                LightTopBar(
                    leftButton = LightBarButton.LightIcon(icon = LightIcons.BACK, onClick = { haptic(); goBack() }),
                    center = LightTopBarCenter.Text("Compatibility"),
                    modifier = Modifier.padding(bottom = 1f.gridUnitsAsDp()),
                )

                LightScrollView(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 1f.gridUnitsAsDp()),
                ) {
                    // Enter their birthday
                    LightText(
                        text = "Check compatibility",
                        variant = LightTextVariant.Copy,
                        modifier = Modifier
                            .fillMaxWidth()
                            .lightClickable {
                                haptic()
                                navigateTo(
                                    screenFactory = { NumberEditorScreen(it, title = "Their birth month", hint = "1-12") },
                                    resultCallback = { monthStr ->
                                        val month = monthStr?.toIntOrNull()
                                        if (month != null && month in 1..12) {
                                            navigateTo(
                                                screenFactory = { NumberEditorScreen(it, title = "Their birth day", hint = "1-31") },
                                                resultCallback = { dayStr ->
                                                    val day = dayStr?.toIntOrNull()
                                                    if (day != null && day in 1..31) {
                                                        navigateTo(
                                                            screenFactory = { NumberEditorScreen(it, title = "Their birth year", hint = "e.g. 1992") },
                                                            resultCallback = { yearStr ->
                                                                val year = yearStr?.toIntOrNull()
                                                                if (year != null && year in 1900..2026) {
                                                                    viewModel.checkCompatibility(year, month, day)
                                                                }
                                                            },
                                                        )
                                                    }
                                                },
                                            )
                                        }
                                    },
                                )
                            }
                            .padding(vertical = 0.75f.gridUnitsAsDp()),
                    )
                    LightText(
                        text = "Enter their month, day, then year",
                        variant = LightTextVariant.Fine,
                        lighten = true,
                        modifier = Modifier.padding(bottom = 1f.gridUnitsAsDp()),
                    )

                    // Results
                    if (state.hasResult) {
                        SectionHeader("You and Them")
                        DataRow("You", state.yourSign)
                        DataRow("Them", state.theirSign)
                        Spacer(modifier = Modifier.height(0.5f.gridUnitsAsDp()))

                        SectionHeader("Western Compatibility")
                        LightText(
                            text = state.westernCompat,
                            variant = LightTextVariant.Fine,
                            modifier = Modifier.padding(bottom = 0.15f.gridUnitsAsDp()),
                        )
                        LightText(
                            text = "Elements: ${state.elementCompat}",
                            variant = LightTextVariant.Fine,
                            lighten = true,
                        )
                        SectionSpacer()

                        SectionHeader("Chinese Compatibility")
                        DataRow("You", state.yourChinese)
                        DataRow("Them", state.theirChinese)
                        LightText(
                            text = state.chineseCompat,
                            variant = LightTextVariant.Fine,
                            modifier = Modifier.padding(top = 0.25f.gridUnitsAsDp()),
                        )
                        SectionSpacer()

                        SectionHeader("Overall")
                        LightText(
                            text = state.overallVibes,
                            variant = LightTextVariant.Copy,
                            modifier = Modifier.padding(bottom = 0.5f.gridUnitsAsDp()),
                        )
                    }

                    Spacer(modifier = Modifier.height(1f.gridUnitsAsDp()))
                }

                LightBottomBar(items = listOf())
            }
        }
    }
}
