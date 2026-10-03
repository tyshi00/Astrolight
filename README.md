# AstroLight

A minimalist astrology companion for the Light Phone III: daily, weekly, and monthly horoscopes alongside your Western and Chinese zodiac profiles, plus a compatibility checker. Horoscopes are fetched online when connected; zodiac profiles and compatibility work fully offline.

## Features

**Daily Horoscope**: Today's reading for your sun sign, pulled fresh from horoscope.com.

**Weekly Outlook**: The week ahead at a glance.

**Monthly Outlook**: Broader themes and energy for the current month.

**Today's Luck**: Daily-changing lucky number, lucky color, and supporting sign based on your zodiac profile.

**Western Zodiac Profile**: Your sun sign with element, modality, ruling planet, personality traits, strengths, lucky numbers, colors, and day.

**Chinese Zodiac Profile**: Your animal sign and elemental modifier (Wood, Fire, Earth, Metal, Water) with traits, strengths, lucky numbers, and colors. Determined by birth year using the traditional 60-year Sexagenary cycle.

**Multiple Profiles**: Save up to 5 dates of birth for yourself and loved ones. When more than one is saved, the label under the date on the home screen becomes a toggle. Tap it to cycle through profiles; the whole screen updates with that person's sign and readings.

**Compatibility Checker**: Enter another person's birthday to compare your Western and Chinese zodiac compatibility side by side: element harmony, trine groups, clash pairs, and an overall read.

**Settings**: Toggle each section on or off. Invert colors (dark mode is the default, matching LightOS). Manage your saved dates of birth and pick which one is active.

## Screenshots

<p align="center">
  <img src="docs/screenshots/Astrolight_homescreen_1.png" width="180" />
  <img src="docs/screenshots/Astrolight_homescreen_2.png" width="180" />
  <img src="docs/screenshots/Astrolight_homescreen_3.png" width="180" />
</p>
<p align="center">
  <img src="docs/screenshots/Astrolight_compatability_1.png" width="180" />
  <img src="docs/screenshots/Astrolight_compatability_2.png" width="180" />
  <img src="docs/screenshots/Astrolight_settings_1.png" width="180" />
  <img src="docs/screenshots/Astrolight_settings_2.png" width="180" />
</p>

## Installation

### From Releases

Download the latest APK from [Releases](https://github.com/tyshi00/Astrolight/releases) and install it over ADB:

```
adb install astrolight-vX.Y.Z.apk
```

### From Source

Requires a GitHub token with the `read:packages` scope for the Light SDK packages. Add your credentials to `local.properties` in the project root:

```
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_TOKEN
```

Build and install:

```
./gradlew :tool:assembleRelease
adb install tool/build/outputs/apk/release/tool-release.apk
```

## Data Sources

**Horoscopes**: Daily, weekly, and monthly readings from [horoscope.com](https://www.horoscope.com/).

**Western Zodiac**: Sign attributes, elements, modalities, ruling planets, and compatibility frameworks follow established astrological tradition as documented in standard references.

**Chinese Zodiac**: The 12-animal cycle, 5-element cycle, trine compatibility groups, and clash pairs follow traditional Chinese astrology.

## Credits

Built on the [Light SDK](https://github.com/lightphone/light-sdk) by The Light Phone (MIT). The original copyright notice is kept in [LICENSE](LICENSE), and the SDK's own README is kept in [README.light-sdk.md](README.light-sdk.md).

Horoscope content sourced from [horoscope.com](https://www.horoscope.com/).

## License

MIT. See [LICENSE](LICENSE).

## Disclaimer

Unofficial, independent open-source project — not affiliated with or endorsed by The Light Phone, Inc. Light Phone and Light OS are trademarks of The Light Phone, Inc.
