# KeyKeep - Secure Credential Manager

![KeyKeep Banner](https://placehold.co/1200x400/0D47A1/FFFFFF/png?text=KeyKeep&font=arial) 

[![Platform](https://img.shields.io/badge/Platform-Android-green)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8+-blueviolet)](https://kotlinlang.org)
[![Room](https://img.shields.io/badge/Database-Room-orange)](https://developer.android.com/training/data-storage/room)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)
[![Downloads](https://img.shields.io/badge/Downloads-Coming%20Soon-brightgreen)](https://github.com/yourusername/keykeep/releases)

**KeyKeep** is a modern, secure, and user-friendly Android app for managing your login credentials. Built with privacy in mind, it stores everything locally with encryption, ensuring your data stays on your device. Say goodbye to forgetting passwords — KeyKeep keeps them organized, searchable, and accessible with just a few taps!

---

## Why KeyKeep? 🚀

* **Secure Storage**: All credentials are encrypted and saved in a local Room database — no cloud sync, no worries.
* **Intuitive UI**: Eye-catching Material Design with smooth transitions, icon pickers, and expandable fields.
* **Powerful Features**: Search, filter by category, copy details instantly, and more.
* **Open Source**: Built with Kotlin and best practices for easy contributions.

## Features ✨

* **Local Database Storage**: All credentials (usernames, passwords, URLs, notes) are securely stored in an encrypted Room database.
* **Flexible Addition Screen**: Add credentials with optional fields like URL, email, phone number, and notes. 
* **Details Page**: View credentials with passwords hidden initially; reveal them with a single tap.
* **One-Click Copy**: Easily copy usernames, passwords, or URLs to your clipboard.
* **Edit & Delete**: Seamlessly update existing credentials or remove them with a confirmation dialog.
* **Search & Filter**: Quick search across all fields and filter by categories (Social, Work, Banking, etc.).

## Screenshots 📸

| Add Credential | Credential List | Details View |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/d3e7640c-1e40-42ec-871a-8e60f12010fc" width="200"> | <img src="https://github.com/user-attachments/assets/9c710a65-8bae-4b1e-92e0-3c0cb2baf169" width="200"> | <img src="https://github.com/user-attachments/assets/8350793b-a862-4384-91c6-ced4a714cb0c" width="200"> |

| Empty List | Search View | Category View |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/12c33beb-1974-419a-852f-3ce9afa7c86b" width="200"> | <img src="https://github.com/user-attachments/assets/b02a8663-5a8d-4039-a314-a7bf8fd21635" width="200"> | <img src="https://github.com/user-attachments/assets/532c529f-4184-4c59-9ed9-100cd4094ccb" width="200"> |

## Installation 🛠️

1.  **Clone the repository**:
    ```bash
    git clone [https://github.com/yourusername/keykeep.git](https://github.com/PrateekSingh009/keykeep.git)
    cd keykeep
    ```

2.  **Open in Android Studio**:
    * Open the project in Android Studio (Ladybug or later recommended).
    * Sync Gradle and build the project.

3.  **Run the app**:
    * Connect an Android device or emulator (API 24+).
    * Click **Run** (Shift + F10).

## Tech Stack 🔧

* **Language**: [Kotlin](https://kotlinlang.org/)
* **Architecture**: MVVM (Model-View-ViewModel)
* **UI**: XML Layouts, Material Design 3
* **Database**: Room (with encryption)
* **Dependency Injection**: Hilt
* **Coroutines**: For asynchronous operations
* **Navigation**: Jetpack Navigation Component
* **Reactive UI**: LiveData / StateFlow

## Contributing 🤝

We welcome contributions! 

1.  Fork the repository.
2.  Create a feature branch (`git checkout -b feature/YourFeature`).
3.  Commit your changes (`git commit -m 'Add YourFeature'`).
4.  Push to the branch (`git push origin feature/YourFeature`).
5.  Open a **Pull Request**.

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact 📫

* **Developer**: Prateek Singh
* **GitHub**: [@PrateekSingh009](https://github.com/PrateekSingh009)
* **Issues**: [Report here](https://github.com/PrateekSingh009/keykeep/issues)

---
⭐ If you find **KeyKeep** useful, star the repo and share it! 🚀
