package com.tyshi00.astrolight.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.tyshi00.astrolight.data.DOBEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DOBListState(
    val entries: List<DOBEntry> = emptyList(),
    val activeIndex: Int = 0,
    val count: Int = 0,
    val changed: Boolean = false,
)

class DOBListViewModel(private val repo: AstroLightRepository) : LightViewModel<Boolean>() {

    private val _state = MutableStateFlow(DOBListState())
    val state: StateFlow<DOBListState> = _state.asStateFlow()

    override fun onScreenShow(screen: SimpleLightScreen<Boolean>) {
        super.onScreenShow(screen)
        reload()
    }

    fun reload() {
        viewModelScope.launch(Dispatchers.IO) {
            val entries = repo.getAllDOBEntries()
            val active = repo.getActiveDOBIndex()
            _state.value = DOBListState(
                entries = entries,
                activeIndex = active,
                count = entries.size,
                changed = _state.value.changed,
            )
        }
    }

    fun setActive(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setActiveDOBIndex(index)
            _state.value = _state.value.copy(activeIndex = index, changed = true)
        }
    }

    fun addEntry(year: Int, month: Int, day: Int, label: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addDOB(year, month, day, label)
            reload()
            _state.value = _state.value.copy(changed = true)
        }
    }

    fun deleteEntry(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteDOB(index)
            reload()
            _state.value = _state.value.copy(changed = true)
        }
    }
}

class DOBEntryScreen(
    sealedActivity: SealedLightActivity,
    private val repo: AstroLightRepository,
) : LightScreen<Boolean, DOBListViewModel>(sealedActivity) {

    override val viewModelClass: Class<DOBListViewModel>
        get() = DOBListViewModel::class.java

    override fun createViewModel() = DOBListViewModel(repo)

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
                    leftButton = LightBarButton.LightIcon(
                        icon = LightIcons.BACK,
                        onClick = { haptic(); goBack(state.changed) },
                    ),
                    center = LightTopBarCenter.Text("Date of Birth"),
                    modifier = Modifier.padding(bottom = 1f.gridUnitsAsDp()),
                )

                LightScrollView(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 1f.gridUnitsAsDp()),
                ) {
                    // List of saved entries
                    state.entries.forEach { entry ->
                        val isActive = entry.index == state.activeIndex

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .lightClickable {
                                    haptic()
                                    if (!isActive) viewModel.setActive(entry.index)
                                }
                                .padding(vertical = 0.5f.gridUnitsAsDp()),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (isActive) {
                                LightText(
                                    text = "\u25CF",
                                    variant = LightTextVariant.Fine,
                                    modifier = Modifier.padding(end = 0.4f.gridUnitsAsDp()),
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                LightText(
                                    text = entry.label,
                                    variant = LightTextVariant.Copy,
                                )
                                LightText(
                                    text = entry.dateDisplay,
                                    variant = LightTextVariant.Fine,
                                    lighten = true,
                                )
                            }
                        }

                        // Delete option (dimmed, below the entry)
                        LightText(
                            text = "Remove",
                            variant = LightTextVariant.Fine,
                            lighten = true,
                            modifier = Modifier
                                .lightClickable { haptic(); viewModel.deleteEntry(entry.index) }
                                .padding(bottom = 0.75f.gridUnitsAsDp()),
                        )
                    }

                    // Add new entry (if under max)
                    if (state.count < AstroLightRepository.MAX_DOBS) {
                        Spacer(modifier = Modifier.height(0.5f.gridUnitsAsDp()))

                        LightText(
                            text = "Add date of birth",
                            variant = LightTextVariant.Copy,
                            modifier = Modifier
                                .fillMaxWidth()
                                .lightClickable {
                                    // Step 1: Month
                                    navigateTo(
                                        screenFactory = { NumberEditorScreen(it, title = "Birth month", hint = "1-12") },
                                        resultCallback = { monthStr ->
                                            val month = monthStr?.toIntOrNull()
                                            if (month != null && month in 1..12) {
                                                // Step 2: Day
                                                navigateTo(
                                                    screenFactory = { NumberEditorScreen(it, title = "Birth day", hint = "1-31") },
                                                    resultCallback = { dayStr ->
                                                        val day = dayStr?.toIntOrNull()
                                                        if (day != null && day in 1..31) {
                                                            // Step 3: Year
                                                            navigateTo(
                                                                screenFactory = { NumberEditorScreen(it, title = "Birth year", hint = "e.g. 1990") },
                                                                resultCallback = { yearStr ->
                                                                    val year = yearStr?.toIntOrNull()
                                                                    if (year != null && year in 1900..2026) {
                                                                        // Step 4: Label
                                                                        val defaultLabel = if (state.count == 0) "Main" else "Additional"
                                                                        navigateTo(
                                                                            screenFactory = { TextEditorScreen(it, title = "Label") },
                                                                            resultCallback = { label ->
                                                                                val finalLabel = label ?: defaultLabel
                                                                                viewModel.addEntry(year, month, day, finalLabel)
                                                                            },
                                                                        )
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
                        if (state.count > 1) {
                            LightText(
                                text = "Tap the profile label under the date on the home screen to cycle through profiles.",
                                variant = LightTextVariant.Fine,
                                lighten = true,
                                modifier = Modifier.padding(bottom = 0.5f.gridUnitsAsDp()),
                            )
                        }
                        LightText(
                            text = "You can add up to 5 dates of birth (${state.count} of 5).",
                            variant = LightTextVariant.Fine,
                            lighten = true,
                        )
                    }
                }

                LightBottomBar(items = listOf())
            }
        }
    }
}
