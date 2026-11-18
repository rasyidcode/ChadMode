# ChadMode 🗿⚡
> **Unfuck your brain, overcome PMO, and ascend to peak discipline.**

ChadMode is a privacy-first, local-only Android sobriety and habit tracker designed to help you break free from PMO (Porn, Masturbation, Orgasm) addiction, reset dopamine receptors, and build unbreakable self-discipline.

Built with **Android Native (Java) + XML Views**, engineered for lightning-fast performance, zero battery drain, and complete offline privacy.

---

## 🚀 Key Features

* **Real-Time Streak Ticker:** Displays exact sobriety progress in Days, Hours, Minutes, and Seconds without draining battery (timestamp-based calculation).
* **The GigaChad Rank System:** Gamified progression leveling your mindset up from *Day 0 (Dopamine Reset)* to *Day 90+ (Ascended Chad)*.
* **Emergency Panic Button:** Instant urge-surfing mechanism with an interactive 4-4-4-4 Box Breathing visualizer and motivational reality checks.
* **Smart Relapse Journaling:** When a reset occurs, log triggers (Stress, Boredom, Late Night, Social Media) and mood to identify danger patterns.
* **Trigger Heatmap & Analytics:** Local visual breakdown of your most vulnerable hours and days.
* **Zero-Leak Privacy:** 100% offline. No internet permission, no telemetry, no third-party analytics. Supports Biometric (Fingerprint) & PIN lock.

---

## 🛠️ Tech Stack & Architecture

* **Language:** Java 11 (strict OOP, fast compilation)
* **UI Framework:** Android XML Views + Material Design 3 (`MaterialCardView`, `CircularProgressIndicator`, `ConstraintLayout`)
* **Architecture:** MVVM (Model-View-ViewModel) + ViewBinding
* **Persistence:**
  * **Room Database:** For relapse logs, trigger entries, and journal history.
  * **SharedPreferences:** For streak start timestamp, settings, and lock credentials.
* **Async & Concurrency:** Java `ExecutorService` and `Handler` (Main Looper)
* **Background Tasks:** Android Jetpack `WorkManager` (for daily check-ins & milestone alerts)
* **Target SDK:** Android 14+ (minSdk: 23, targetSdk: 37)

---

## 📁 Project Structure

```
com.rasyidcode.chadmode/
├── data/
│   ├── local/              # Room database, DAOs, and entities
│   │   ├── AppDatabase.java
│   │   ├── RelapseDao.java
│   │   └── entity/         # RelapseRecord, TriggerLog
│   └── preference/         # SharedPreferences manager (StreakManager)
├── ui/
│   ├── main/               # Main Dashboard & Streak Ticker
│   ├── emergency/          # Panic Button, Box Breathing, Urge Surfer
│   ├── relapse/            # Reset dialog, Trigger logging form
│   ├── analytics/          # Streak history, charts & stats
│   ├── rank/               # GigaChad milestone tiers & badges
│   └── security/           # Biometric / PIN Lock Screen
├── util/                   # Time formatting, Constants, View extensions
└── receiver/               # Daily check-in notification receivers
```

---

## ⚡ Getting Started & Building

### Prerequisites
* **Android Studio** (Ladybug / Koala or newer)
* **Android SDK** (API 34+ installed)
* **JDK 11 / 17**

### Building via Terminal
```bash
# Clone or navigate to the directory
git clone git@github.com:rasyidcode/ChadMode.git && cd ~/ChadMode

# Build debug APK
./gradlew assembleDebug

# Install directly to connected device (via USB / adb)
./gradlew installDebug
```

> **Tip for screen mirroring:** Run `scrcpy` in your terminal to view and interact with your physical test device directly from Ubuntu.

---

## 🛡️ Privacy Guarantee

ChadMode believes recovery is deeply personal. This app does not request the `android.permission.INTERNET` permission in its manifest. Your logs, your thoughts, and your streaks never leave your physical storage.
