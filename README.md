# Sahyadri-Siri (ಸಹ್ಯಾದ್ರಿ-ಸಿರಿ)

**Guardian of the Ghats' Lifeblood**

Sahyadri-Siri is a community-driven conservation initiative and environmental monitoring project focused on protecting the crystalline streams and river systems of the Western Ghats.

## 🌟 Features (MVP)

| Feature | Description | Priority |
| :--- | :--- | :--- |
| **Stream Report Form** | Report on Clarity, Flow, Smell, and Visible Pollution. | Must-Have |
| **Water Health Score** | Weighted composite score (0-100) recalculated per report. | Must-Have |
| **Health Map** | Google Maps overlay with real-time blue-to-brown polyline coloring. | Must-Have |
| **Alert Feed** | In-app feed and push notifications on major water quality drops. | Must-Have |
| **Anonymous Reporting** | Zero-PII toggle for safe reporting of environmental concerns. | Must-Have |
| **GPS Verification** | Validates report origin is within 500m of the selected stream. | Must-Have |
| **Educational Wiki** | Per-stream ecological info page with WHS trend charts and search. | Must-Have |
| **Multilingual Support** | Full UI localization in English and Kannada. | Must-Have |
| **Serene Blue UI Theme** | Calm blue-water color palette with accessible contrast. | Must-Have |

## 🛠 Tech Stack

*   **Language:** Kotlin
*   **UI:** Jetpack Compose (Modern Android UI)
*   **Navigation:** Compose Navigation
*   **Backend:** Firebase Realtime Database & Firebase Auth (Phone/OTP)
*   **Maps:** Google Maps SDK for Android (Compose Library)
*   **Image Loading:** Coil 3
*   **Architecture:** MVVM (Model-View-ViewModel)

## 🚀 Getting Started

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/YOUR_USERNAME/SahyadriSiriMain.git
    ```
2.  **Firebase Setup:**
    *   Create a project in the [Firebase Console](https://console.firebase.google.com/).
    *   Add an Android App with the package name `com.example.sahyadrisirimain`.
    *   Download `google-services.json` and place it in the `app/` directory.
    *   Enable Phone Authentication and Realtime Database.
3.  **Maps API Key:**
    *   Obtain a Google Maps API Key from the [Google Cloud Console](https://console.cloud.google.com/).
    *   Place the key in `AndroidManifest.xml` under the meta-data tag `com.google.android.geo.API_KEY`.
4.  **Build & Run:**
    *   Open the project in Android Studio.
    *   Sync Gradle and click **Run**.

## 📷 Screenshots

*(Add your screenshots here later)*

---
**SAHYADRI-SIRI ECOSYSTEM MONITORING PROJECT © 2026**
