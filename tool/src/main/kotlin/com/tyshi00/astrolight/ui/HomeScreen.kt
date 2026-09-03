package com.tyshi00.astrolight.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewModelScope
import com.thelightphone.sdk.InitialScreen
import com.thelightphone.sdk.LightScreen
import com.thelightphone.sdk.LightViewModel
import com.thelightphone.sdk.SealedLightActivity
import com.thelightphone.sdk.SimpleLightScreen
import com.thelightphone.sdk.buildDatabase
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
import com.tyshi00.astrolight.api.HoroscopeApi
import com.tyshi00.astrolight.R
import com.tyshi00.astrolight.data.AstroLightDatabase
import com.tyshi00.astrolight.data.AstroLightRepository
import com.tyshi00.astrolight.data.ZodiacCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private const val TAG = "AstroLight"

data class HomeState(
    val hasDOB: Boolean = false,
    val activeLabel: String = "",
    val dobCount: Int = 0,
    val currentDate: String = "",

    // Western sign
    val showWestern: Boolean = true,
    val signName: String = "",
    val signSymbol: String = "",
    val signDateRange: String = "",
    val signElement: String = "",
    val signModality: String = "",
    val signRulingPlanet: String = "",
    val signTraits: String = "",
    val signLuckyNumbers: String = "",
    val signLuckyColor: String = "",
    val signLuckyDay: String = "",

    // Chinese zodiac
    val showChinese: Boolean = true,
    val chineseFullName: String = "",
    val chineseEmoji: String = "",
    val chineseTraits: String = "",
    val chineseElementDesc: String = "",
    val chineseLuckyNumbers: String = "",
    val chineseLuckyColors: String = "",

    // Horoscopes
    val showDaily: Boolean = true,
    val dailyHoroscope: String = "",
    val dailyLoading: Boolean = false,
    val dailyLuckyNumber: String = "",
    val dailyLuckyColor: String = "",
    val dailySupportingSign: String = "",

    val showWeekly: Boolean = true,
    val weeklyHoroscope: String = "",
    val weeklyLoading: Boolean = false,

    val showMonthly: Boolean = true,
    val monthlyHeader: String = "",
    val monthlyHoroscope: String = "",
    val monthlyLoading: Boolean = false,
)

class HomeViewModel(private val repo: AstroLightRepository) : LightViewModel<Unit>() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val api = HoroscopeApi()

    override fun onScreenShow(screen: SimpleLightScreen<Unit>) {
        super.onScreenShow(screen)
        viewModelScope.launch(Dispatchers.IO) {
            if (repo.getInvertColors()) LightThemeController.setLightTheme() else LightThemeController.setDarkTheme()
        }
        refresh()
    }

    override fun onCleared() {
        super.onCleared()
        api.close()
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val hasDOB = repo.hasDOB()
            val profile = repo.getProfile()
            val activeLabel = repo.getActiveLabel()
            val dobCount = repo.getDOBCount()

            val tz = TimeZone.getDefault()
            val now = Calendar.getInstance(tz)
            val dateFmt = SimpleDateFormat("EEE, MMM d, yyyy", Locale.US)
            dateFmt.timeZone = tz
            val monthYearFmt = SimpleDateFormat("MMMM yyyy", Locale.US)
            monthYearFmt.timeZone = tz

            val showWestern = repo.getShowWestern()
            val showChinese = repo.getShowChinese()
            val showDaily = repo.getShowDaily()
            val showWeekly = repo.getShowWeekly()
            val showMonthly = repo.getShowMonthly()

            if (profile != null) {
                val sign = profile.westernSign
                val animal = profile.chineseAnimal

                _state.value = HomeState(
                    hasDOB = true,
                    activeLabel = activeLabel,
                    dobCount = dobCount,
                    currentDate = dateFmt.format(now.time),
                    showWestern = showWestern,
                    signName = "${sign.symbol} ${sign.name}",
                    signSymbol = sign.symbol,
                    signDateRange = sign.dateRange,
                    signElement = "${sign.element.label} | ${sign.modality.label}",
                    signModality = sign.modality.label,
                    signRulingPlanet = sign.rulingPlanet,
                    signTraits = sign.traits.joinToString(", "),
                    signLuckyNumbers = sign.luckyNumbers.joinToString(", "),
                    signLuckyColor = sign.luckyColor,
                    signLuckyDay = sign.luckyDay,
                    showChinese = showChinese,
                    chineseFullName = "${animal.emoji} ${profile.chineseFullName}",
                    chineseEmoji = animal.emoji,
                    chineseTraits = animal.traits.joinToString(", "),
                    chineseElementDesc = profile.chineseElementDescription,
                    chineseLuckyNumbers = animal.luckyNumbers.joinToString(", "),
                    chineseLuckyColors = animal.luckyColors.joinToString(", "),
                    showDaily = showDaily,
                    dailyLoading = showDaily,
                    showWeekly = showWeekly,
                    weeklyLoading = showWeekly,
                    showMonthly = showMonthly,
                    monthlyHeader = monthYearFmt.format(now.time),
                    monthlyLoading = showMonthly,
                )

                // Always fetch fresh from the API
                if (showDaily) {
                    fetchHoroscope("daily", sign.apiName)
                }

                // Compute today's lucky data (offline, instant)
                val daily = com.tyshi00.astrolight.data.DailyLucky.forToday(sign)
                _state.value = _state.value.copy(
                    dailyLuckyNumber = daily.luckyNumber.toString(),
                    dailyLuckyColor = daily.luckyColor,
                    dailySupportingSign = daily.supportingSign,
                )
                if (showWeekly) fetchHoroscope("weekly", sign.apiName)
                if (showMonthly) fetchHoroscope("monthly", sign.apiName)
            } else {
                _state.value = HomeState(
                    hasDOB = false,
                    dobCount = dobCount,
                    currentDate = dateFmt.format(now.time),
                    showWestern = showWestern,
                    showChinese = showChinese,
                    showDaily = showDaily,
                    showWeekly = showWeekly,
                    showMonthly = showMonthly,
                )
            }
        }
    }

    /** Advance to the next saved date of birth and reload the screen for it. */
    fun cycleProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val count = repo.getDOBCount()
            if (count <= 1) return@launch
            val next = (repo.getActiveDOBIndex() + 1) % count
            repo.setActiveDOBIndex(next)
            refresh()
        }
    }

    private fun fetchHoroscope(period: String, sign: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = when (period) {
                "daily" -> api.fetchDaily(sign)
                "weekly" -> api.fetchWeekly(sign)
                "monthly" -> api.fetchMonthly(sign)
                else -> return@launch
            }

            result.onSuccess { data ->
                when (period) {
                    "daily" -> _state.value = _state.value.copy(dailyHoroscope = data.text, dailyLoading = false)
                    "weekly" -> _state.value = _state.value.copy(weeklyHoroscope = data.text, weeklyLoading = false)
                    "monthly" -> _state.value = _state.value.copy(monthlyHoroscope = data.text, monthlyLoading = false)
                }
            }
            result.onFailure { error ->
                Log.w(TAG, "Failed to fetch $period horoscope: ${error.message}")
                val fallback = "Unable to load. Check connection."
                when (period) {
                    "daily" -> _state.value = _state.value.copy(dailyHoroscope = fallback, dailyLoading = false)
                    "weekly" -> _state.value = _state.value.copy(weeklyHoroscope = fallback, weeklyLoading = false)
                    "monthly" -> _state.value = _state.value.copy(monthlyHoroscope = fallback, monthlyLoading = false)
                }
            }
        }
    }

}

@InitialScreen
class HomeScreen(sealedActivity: SealedLightActivity) :
    LightScreen<Unit, HomeViewModel>(sealedActivity) {

    private val repo = AstroLightRepository.getInstance {
        lightContext.buildDatabase(AstroLightDatabase::class.java, "astrolight.db")
    }

    override val viewModelClass: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    override fun createViewModel() = HomeViewModel(repo)

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
                LightTopBar(
                    center = LightTopBarCenter.Text("AstroLight"),
                    modifier = Modifier.padding(bottom = 0.5f.gridUnitsAsDp()),
                )

                if (!state.hasDOB) {
                    // No DOB set, prompt user
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 1f.gridUnitsAsDp()),
                    ) {
                        Spacer(modifier = Modifier.height(2f.gridUnitsAsDp()))
                        LightText(
                            text = "Welcome to AstroLight",
                            variant = LightTextVariant.Heading,
                            align = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(1f.gridUnitsAsDp()))
                        LightText(
                            text = "Set your date of birth in Settings to see your zodiac profile, horoscopes, and more.",
                            variant = LightTextVariant.Copy,
                            align = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                } else {
                    // Date + profile header, kept above the scroll area so it
                    // stays centered under the "AstroLight" top-bar title (the
                    // scroll view reserves a scrollbar gutter on the right) and
                    // so the profile toggle is always visible.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1f.gridUnitsAsDp()),
                    ) {
                        LightText(
                            text = state.currentDate,
                            variant = LightTextVariant.Detail,
                            lighten = true,
                            align = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        if (state.activeLabel.isNotEmpty()) {
                            val canCycle = state.dobCount > 1
                            LightText(
                                text = if (canCycle) "‹ ${state.activeLabel} ›" else state.activeLabel,
                                variant = LightTextVariant.Fine,
                                // Full-contrast + underlined when tappable so it
                                // reads as interactive; dimmed like before otherwise.
                                lighten = !canCycle,
                                underline = canCycle,
                                align = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (canCycle) {
                                            Modifier.lightClickable(onClickLabel = "Switch profile") {
                                                viewModel.cycleProfile()
                                            }
                                        } else {
                                            Modifier
                                        },
                                    )
                                    .padding(bottom = 1f.gridUnitsAsDp()),
                            )
                        } else {
                            Spacer(modifier = Modifier.height(1f.gridUnitsAsDp()))
                        }
                    }

                    LightScrollView(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 1f.gridUnitsAsDp()),
                    ) {
                        // Daily horoscope
                        if (state.showDaily) {
                            SectionHeader("Today")
                            if (state.dailyLoading) {
                                LightText(text = "Loading...", variant = LightTextVariant.Fine, lighten = true)
                            } else {
                                LightText(
                                    text = state.dailyHoroscope,
                                    variant = LightTextVariant.Fine,
                                )
                            }
                            // Today's Lucky
                            if (state.dailyLuckyNumber.isNotEmpty()) {
                                LightText(
                                    text = "Today's Luck",
                                    variant = LightTextVariant.Detail,
                                    lighten = true,
                                    modifier = Modifier.padding(top = 0.5f.gridUnitsAsDp()),
                                )
                                DataRow("Lucky number", state.dailyLuckyNumber)
                                DataRow("Lucky color", state.dailyLuckyColor)
                                DataRow("Supporting sign", state.dailySupportingSign)
                            }
                            SectionSpacer()
                        }

                        // Weekly outlook
                        if (state.showWeekly) {
                            SectionHeader("This Week")
                            if (state.weeklyLoading) {
                                LightText(text = "Loading...", variant = LightTextVariant.Fine, lighten = true)
                            } else {
                                LightText(
                                    text = state.weeklyHoroscope,
                                    variant = LightTextVariant.Fine,
                                )
                            }
                            SectionSpacer()
                        }

                        // Monthly outlook
                        if (state.showMonthly) {
                            SectionHeader(state.monthlyHeader)
                            if (state.monthlyLoading) {
                                LightText(text = "Loading...", variant = LightTextVariant.Fine, lighten = true)
                            } else {
                                LightText(
                                    text = state.monthlyHoroscope,
                                    variant = LightTextVariant.Fine,
                                )
                            }
                            SectionSpacer()
                        }

                        // Western sign profile
                        if (state.showWestern) {
                            SectionHeader(state.signName)
                            LightText(
                                text = state.signDateRange,
                                variant = LightTextVariant.Fine,
                                lighten = true,
                                modifier = Modifier.padding(bottom = 0.25f.gridUnitsAsDp()),
                            )
                            DataRow("Element", state.signElement)
                            DataRow("Ruling planet", state.signRulingPlanet)
                            DataRow("Traits", state.signTraits)
                            DataRow("Lucky numbers", state.signLuckyNumbers)
                            DataRow("Lucky color", state.signLuckyColor)
                            DataRow("Lucky day", state.signLuckyDay)
                            SectionSpacer()
                        }

                        // Chinese zodiac profile
                        if (state.showChinese) {
                            SectionHeader(state.chineseFullName)
                            DataRow("Traits", state.chineseTraits)
                            LightText(
                                text = state.chineseElementDesc,
                                variant = LightTextVariant.Fine,
                                lighten = true,
                                modifier = Modifier.padding(vertical = 0.25f.gridUnitsAsDp()),
                            )
                            DataRow("Lucky numbers", state.chineseLuckyNumbers)
                            DataRow("Lucky colors", state.chineseLuckyColors)
                            SectionSpacer()
                        }

                        Spacer(modifier = Modifier.height(1f.gridUnitsAsDp()))
                    }
                }

                LightBottomBar(
                    items = listOf(
                        LightBarButton.LightIcon(
                            icon = LightIcons.SETTINGS,
                            onClick = { navigateTo(screenFactory = { SettingsScreen(it, repo) }) },
                            contentDescription = "Settings",
                        ),
                        LightBarButton.Icon(
                            painter = painterResource(R.drawable.ic_heart_compat),
                            onClick = { navigateTo(screenFactory = { CompatibilityScreen(it, repo) }) },
                            contentDescription = "Compatibility",
                            sizeUnits = 2.5f,
                        ),
                    ),
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    LightText(
        text = title,
        variant = LightTextVariant.Subheading,
        modifier = Modifier.padding(bottom = 0.4f.gridUnitsAsDp()),
    )
}

@Composable
fun DataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.2f.gridUnitsAsDp()),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        LightText(text = label, variant = LightTextVariant.Fine, lighten = true)
        LightText(
            text = value,
            variant = LightTextVariant.Fine,
            modifier = Modifier.weight(1f).padding(start = 0.5f.gridUnitsAsDp()),
            align = TextAlign.End,
        )
    }
}

@Composable
fun SectionSpacer() {
    Spacer(modifier = Modifier.height(1.25f.gridUnitsAsDp()))
}
