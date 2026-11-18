# AGENTS.md — Development Guidelines for AI Agents

Welcome to **ChadMode**. This document defines the engineering standards, architecture rules, and development constraints for AI agents interacting with this codebase.

---

## 🎯 Project Overview
* **Application:** ChadMode (PMO / Bad Habit Sobriety Tracker)
* **Package Name:** `com.rasyidcode.chadmode`
* **Root Location:** `/mnt/EYOTA/AndroidStudioProjects/ChadMode`
* **Primary Philosophy:** Local-first, 100% offline, privacy-critical, lightweight, highly gamified.

---

## 🚫 Critical Constraints & Rules

1. **Strict Language Constraint: Java 11 Only**
   * **Do NOT introduce Kotlin files** (`.kt`) or Kotlin plugins unless the user explicitly requests a migration.
   * All classes, models, ViewModels, and DAOs must be written in idiomatic, clean **Java 11**.

2. **Strict UI Framework: Android XML Views Only**
   * **Do NOT introduce Jetpack Compose**. Keep `buildFeatures { compose = false }`.
   * Use **ViewBinding** for view lookups. Do not use raw `findViewById()` in new code.
   * Maintain Edge-to-Edge compliance using `WindowInsetsCompat`.

3. **Strict Privacy Constraint: 100% Offline**
   * **Never add `android.permission.INTERNET`** to `AndroidManifest.xml`.
   * No analytics SDKs, no remote telemetry, no remote crash reporters. All data lives in local storage.

4. **Hardware & Build Considerations**
   * Host machine is an Intel Core i5-3470 (4 cores) running Ubuntu.
   * Keep dependencies minimal and lightweight. Avoid heavy annotation processors or multi-layer abstraction frameworks that bloat Gradle build times.

---

## 🏗️ Architecture & Coding Standards

### 1. Architectural Pattern: MVVM (Model-View-ViewModel)
* **View (Activity / Fragment):** Responsible *only* for rendering UI and forwarding user actions. Observes `LiveData` from ViewModel.
* **ViewModel (`androidx.lifecycle.ViewModel`):** Holds UI state, exposes data via `LiveData<T>` or `MutableLiveData<T>`. Survives configuration changes.
* **Repository:** Serves as the single source of truth, mediating between Room Database and SharedPreferences.

### 2. Concurrency & Asynchronous Operations
Since Kotlin Coroutines are not used:
* **Background Threads:** Use `java.util.concurrent.Executors.newSingleThreadExecutor()` or a shared `AppExecutors` utility class.
* **UI Dispatching:** Use `Handler(Looper.getMainLooper())` or `MutableLiveData.postValue(data)`.
* **Timers:** Use a lightweight `Handler` with `postDelayed()` for the real-time ticker to avoid battery drain.

### 3. Data Persistence
* **Key-Value Store:** Use `android.content.SharedPreferences` (or `EncryptedSharedPreferences` for PIN security) for:
  * Streak start timestamp (`long streakStartTimeMs`).
  * App lock preferences.
* **Database (Room):**
  * Use Room with Java annotations (`@Entity`, `@Dao`, `@Database`).
  * DAOs return `LiveData<List<T>>` for observable queries.
  * Write operations (insert, delete, update) must be executed off the main thread via an `Executor`.

### 4. Layout & Styling Guidelines
* Base theme: Material Design 3 (`Theme.Material3.DayNight.NoActionBar`).
* Use `ConstraintLayout` for flat view hierarchies.
* Extract all strings to `res/values/strings.xml` and colors to `res/values/colors.xml`.
* Support dark theme seamlessly via `res/values-night/`.

---

## 🛠️ Verification & Build Commands

Before marking any task as complete, always verify the build passes via terminal:

```bash
# Check compile & build health
cd /mnt/EYOTA/AndroidStudioProjects/ChadMode
./gradlew assembleDebug --no-daemon

# Run unit tests (if applicable)
./gradlew testDebugUnitTest
```

---

## 📂 Key Package Directory Map
```
com.rasyidcode.chadmode
├── data
│   ├── local (Room DB, DAOs, Entities)
│   └── pref (SharedPreferences wrapper)
├── ui
│   ├── main (DashboardActivity, MainViewModel)
│   ├── emergency (PanicActivity, BreathingView)
│   └── relapse (RelapseDialog, RelapseViewModel)
└── util (Constants, TimeFormatter, AppExecutors)
```
