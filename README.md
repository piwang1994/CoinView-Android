# CoinView - Cryptocurrency Tracker

Android application for tracking cryptocurrency prices and managing portfolios.

## About

**Course:** ITMD 555 - Mobile Application Development  
**Author:** WANGQINGBIN (A20478885)  
**Email:** qwang93@hawk.illinoistech.edu  
**Semester:** Fall 2025

## Features

- User authentication (Email/Password, Google Sign-In)
- Real-time cryptocurrency data from CoinGecko API
- Favorites management
- Search functionality
- Offline support with local caching
- Material Design 3 UI

## Technologies

- **Language:** Java
- **Architecture:** MVVM
- **Database:** Room
- **API:** Retrofit + CoinGecko
- **Auth:** Firebase
- **UI:** Material Design 3

## Setup

1. Clone repository
2. Open in Android Studio
3. Add `google-services.json` to `app/` directory
4. Build and run

## Project Structure


app/src/main/java/com/example/coinview/
├── ui/              # Activities & Fragments
├── viewmodel/       # ViewModels
├── repository/      # Data repositories
├── database/        # Room database
├── api/             # Retrofit services
├── model/           # Data models
└── adapter/         # RecyclerView adapters

## Author

WANGQINGBIN  
Student ID: A20478885  
Illinois Institute of Technology
