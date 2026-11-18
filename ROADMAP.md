# ROADMAP.md — ChadMode Development Plan 🗺️

This roadmap outlines the phased development of **ChadMode**, from initial MVP to a feature-complete, gamified habit-recovery powerhouse.

---

## 📍 Phase 1: Foundation & The Streak Engine (MVP)
> **Goal:** Create a rock-solid, battery-efficient streak counter with real-time ticker and reset mechanism.

- [x] **1.1 Project Setup & Theme Configuration**
  - Enable `viewBinding` in `app/build.gradle.kts`.
  - Configure Material Design 3 Dark/Night Theme (`Theme.Material3.DayNight.NoActionBar`).
  - Set up `AppExecutors` utility for thread management.
- [x] **1.2 Data Persistence (Streak State)**
  - Implement `PreferenceManager` to store `streakStartTimeMillis` and `longestStreakMillis`.
  - Provide helper methods: `getStreakStartTime()`, `setStreakStartTime()`, `resetStreak()`.
- [x] **1.3 Real-Time Ticker Engine**
  - Implement `TimeFormatter` utility to convert milliseconds elapsed into:
    - Days, Hours, Minutes, Seconds.
  - Implement dynamic UI update loop using `Handler` + `postDelayed(1000)` running only while Activity is in `RESUMED` state.
- [x] **1.4 Main Dashboard UI**
  - Circular progress indicator reflecting daily cycle or current milestone goal.
  - Prominent Days counter, followed by Hours : Minutes : Seconds.
  - Current Rank Title badge (starts at *Day 0: Dopamine Reset*).
  - "Reset / Relapse" button with a two-step confirmation dialog.

---

## 🛡️ Phase 2: Emergency Panic Shield & Urge Surfing
> **Goal:** Provide instant, high-friction tools to stop cravings in their tracks when urges spike.

- [x] **2.1 Emergency Screen / Dialog**
  - Prominent, high-contrast "🚨 PANIC / SHIELD" button on the main dashboard.
  - Fullscreen distraction view that blocks access to the trigger.
- [x] **2.2 Urge Surfing 15-Minute Countdown**
  - Cravings typically peak and subside within 15–20 minutes.
  - Add a dedicated 15-minute countdown timer with encouraging milestone checkpoints.
- [x] **2.3 Interactive Box Breathing (4-4-4-4)**
  - Custom expanding/contracting circle animation using XML `ValueAnimator`.
  - Guides the user through: Inhale (4s) ➔ Hold (4s) ➔ Exhale (4s) ➔ Hold (4s) to reset the nervous system.
- [x] **2.4 Reality-Check Motivational Quotes**
  - Rotating pool of aggressive, stoic, and scientific reality-check cards (e.g., *"10 seconds of cheap pleasure isn't worth throwing away 7 days of discipline"*).

---

## 📊 Phase 3: Relapse Journaling & Trigger Analytics
> **Goal:** Shift from guilt/shame to scientific self-reflection by logging and studying relapses.

- [x] **3.1 Room Database Setup**
  - Add Room dependencies to `build.gradle.kts`.
  - Create `RelapseEntity`:
    - `id`, `timestamp`, `streakDurationMillis`, `triggerCategory`, `moodRating`, `journalNote`.
  - Create `RelapseDao` with queries: `getAllRelapses()`, `getLongestStreak()`, `getRelapsesByTrigger()`.
- [x] **3.2 Post-Relapse Questionnaire Flow**
  - When resetting the streak, replace the generic prompt with a quick reflection form:
    - **Trigger Multi-choice:** Stress, Boredom, Late Night, Social Media/Peeking, Loneliness, Fatigue.
    - **Location/Context:** Bedroom, Bathroom, Alone at PC, Bed with Phone.
    - **Optional Note:** What lie did your brain tell you right before?
- [x] **3.3 Analytics & Heatmap Dashboard**
  - Danger zone chart: Days of the week & hours of the day where relapses most frequently occur.
  - Streak history list showing dates, durations, and top triggers.

---

## 🗿 Phase 4: GigaChad Progression & Gamification
> **Goal:** Make self-mastery feel like an epic RPG level-up journey.

- [ ] **4.1 Milestone & Rank Hierarchy**
  - Define rank engine based on active streak:
    * **Day 0:** *Dopamine Reset*
    * **Day 3:** *Awakening* (Physical withdrawal fading)
    * **Day 7:** *The Spark* (Testosterone and energy surge)
    * **Day 14:** *Iron Discipline* (Eye contact restored, brain fog lifting)
    * **Day 30:** *Sigma Grindset* (Dopamine baseline stabilizing)
    * **Day 60:** *Titan* (Emotional resilience, habit formation)
    * **Day 90+:** *Ascended GigaChad* (Complete neural rewiring)
- [ ] **4.2 Milestone Celebration Dialog**
  - Visual celebration screen when a new rank is unlocked.
  - Shareable/saveable achievement card.

---

## 🔒 Phase 5: Privacy, Security & Discretion
> **Goal:** Ensure complete stealth and privacy on the user's phone.

- [ ] **5.1 App Lock (Biometrics & PIN)**
  - Integration with `androidx.biometric:biometric` (`BiometricPrompt`).
  - Fallback 4-digit PIN stored securely in `EncryptedSharedPreferences`.
  - Auto-lock on app minimize / background.
- [ ] **5.2 Stealth Launcher Alias (Optional)**
  - Ability to toggle app launcher label & icon between **ChadMode** and discreet covers (e.g., **"Calculator"** or **"Daily Log"**).

---

## 📱 Phase 6: Widgets & Background Support
> **Goal:** Passive reminders and quick access directly from the Android Home Screen.

- [ ] **6.1 Home Screen App Widget**
  - XML `AppWidgetProvider` showing current streak days and current rank title.
- [ ] **6.2 Daily Check-In WorkManager**
  - Offline daily evening check-in notification: *"Day X complete. Stand strong."*
