package com.tyshi00.astrolight.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.thelightphone.sdk.LightScreen
import com.thelightphone.sdk.LightViewModel
import com.thelightphone.sdk.SealedLightActivity
import com.thelightphone.sdk.SimpleLightScreen
import com.thelightphone.sdk.ui.LightBarButton
import com.thelightphone.sdk.ui.LightBottomBar
import com.thelightphone.sdk.ui.LightIcon
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsState(
    val dobDisplay: String = "Not set",
    val invertColors: Boolean = false,
    val showDaily: Boolean = true,
    val showWeekly: Boolean = true,
    val showMonthly: Boolean = true,
    val showWestern: Boolean = true,
    val showChinese: Boolean = true,
)

class SettingsViewModel(private val repo: AstroLightRepository) : LightViewModel<Unit>() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    override fun onScreenShow(screen: SimpleLightScreen<Unit>) {
        super.onScreenShow(screen)
        reload()
    }

    fun reload() {
        viewModelScope.launch(Dispatchers.IO) {
            val hasDOB = repo.hasDOB()
            val dobDisplay = if (hasDOB) {
                val label = repo.getActiveLabel()
                val entry = repo.getDOBEntry(repo.getActiveDOBIndex())
                val count = repo.getDOBCount()
                if (entry != null) "$label (${entry.dateDisplay}) - $count of 5" else "Not set"
            } else "Not set"

            _state.value = SettingsState(
                dobDisplay = dobDisplay,
                invertColors = repo.getInvertColors(),
                showDaily = repo.getShowDaily(),
                showWeekly = repo.getShowWeekly(),
                showMonthly = repo.getShowMonthly(),
                showWestern = repo.getShowWestern(),
                showChinese = repo.getShowChinese(),
            )
        }
    }

    fun toggleInvert() {
        viewModelScope.launch(Dispatchers.IO) {
            val newValue = !_state.value.invertColors
            repo.setInvertColors(newValue)
            _state.value = _state.value.copy(invertColors = newValue)
            if (newValue) LightThemeController.setLightTheme() else LightThemeController.setDarkTheme()
        }
    }

    private fun toggle(
        getter: suspend () -> Boolean,
        setter: suspend (Boolean) -> Unit,
        stateUpdater: (Boolean) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newValue = !getter()
            setter(newValue)
            stateUpdater(newValue)
        }
    }

    fun toggleDaily() = toggle({ repo.getShowDaily() }, { repo.setShowDaily(it) }) { _state.value = _state.value.copy(showDaily = it) }
    fun toggleWeekly() = toggle({ repo.getShowWeekly() }, { repo.setShowWeekly(it) }) { _state.value = _state.value.copy(showWeekly = it) }
    fun toggleMonthly() = toggle({ repo.getShowMonthly() }, { repo.setShowMonthly(it) }) { _state.value = _state.value.copy(showMonthly = it) }
    fun toggleWestern() = toggle({ repo.getShowWestern() }, { repo.setShowWestern(it) }) { _state.value = _state.value.copy(showWestern = it) }
    fun toggleChinese() = toggle({ repo.getShowChinese() }, { repo.setShowChinese(it) }) { _state.value = _state.value.copy(showChinese = it) }
}

class SettingsScreen(
    sealedActivity: SealedLightActivity,
    private val repo: AstroLightRepository,
) : LightScreen<Unit, SettingsViewModel>(sealedActivity) {

    override val viewModelClass: Class<SettingsViewModel>
        get() = SettingsViewModel::class.java

    override fun createViewModel() = SettingsViewModel(repo)

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
                    center = LightTopBarCenter.Text("Settings"),
                    modifier = Modifier.padding(bottom = 1f.gridUnitsAsDp()),
                )

                LightScrollView(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 1f.gridUnitsAsDp()),
                ) {
                    // Date of birth
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .lightClickable {
                                haptic()
                                navigateTo(
                                    screenFactory = { DOBEntryScreen(it, repo) },
                                    resultCallback = { result ->
                                        if (result == true) viewModel.reload()
                                    },
                                )
                            }
                            .padding(top = 1.3f.gridUnitsAsDp(), bottom = 1.3f.gridUnitsAsDp()),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            LightText(text = "Date of birth", variant = LightTextVariant.Heading)
                            LightText(text = state.dobDisplay, variant = LightTextVariant.Fine, lighten = true)
                        }
                    }

                    // Invert colors -- knob LEFT = dark (default), knob RIGHT = light
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .lightClickable { haptic(); viewModel.toggleInvert() }
                            .padding(top = 1.3f.gridUnitsAsDp(), bottom = 1.3f.gridUnitsAsDp()),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LightIcon(
                            icon = if (state.invertColors) LightIcons.TOGGLE_OFF else LightIcons.TOGGLE_ON,
                            modifier = Modifier.padding(end = 0.75f.gridUnitsAsDp()),
                        )
                        LightText(text = "Invert colors", variant = LightTextVariant.Heading)
                    }

                    // Feature toggles -- knob LEFT = enabled (default)
                    ToggleRow("Disable Daily horoscope", state.showDaily) { viewModel.toggleDaily() }
                    ToggleRow("Disable Weekly outlook", state.showWeekly) { viewModel.toggleWeekly() }
                    ToggleRow("Disable Monthly outlook", state.showMonthly) { viewModel.toggleMonthly() }
                    ToggleRow("Disable Western zodiac", state.showWestern) { viewModel.toggleWestern() }
                    ToggleRow("Disable Chinese zodiac", state.showChinese) { viewModel.toggleChinese() }

                    // Version
                    LightText(
                        text = "AstroLight v0.1.0",
                        variant = LightTextVariant.Fine,
                        lighten = true,
                        modifier = Modifier.padding(top = 1f.gridUnitsAsDp()),
                    )
                }

                LightBottomBar(items = listOf())
            }
        }
    }
}

@Composable
fun ToggleRow(label: String, isOn: Boolean, onToggle: () -> Unit) {
    val haptic = rememberHaptic()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .lightClickable { haptic(); onToggle() }
            .padding(top = 1.3f.gridUnitsAsDp(), bottom = 1.3f.gridUnitsAsDp()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LightIcon(
            icon = if (isOn) LightIcons.TOGGLE_ON else LightIcons.TOGGLE_OFF,
            modifier = Modifier.padding(end = 0.75f.gridUnitsAsDp()),
        )
        LightText(text = label, variant = LightTextVariant.Heading)
    }
}
