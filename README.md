# Virtual Volume Buttons

A simple Android application that provides a floating, movable volume button on your screen, allowing you to control your device's volume without using the physical hardware buttons.

## Features

- **Floating Bubble:** A draggable bubble that stays on top of other apps, giving you quick access to volume controls.
- **Home Screen Widgets:** Add volume control widgets directly to your home screen for quick and easy access.
- **Customizable:** The bubble's appearance and behavior can be easily customized.
- **Dismissible:** Drag the bubble to the 'X' at the bottom of the screen to dismiss it.
- **Lightweight:** The service runs efficiently in the background without consuming significant resources.
- **Built with Modern Android:** The entire floating bubble is built using Jetpack Compose.

## How to Use

### Floating Bubble

1.  **Launch the App:** Open the Virtual Volume Buttons app.
2.  **Grant Permission:** The app will prompt you to grant the "Draw over other apps" permission. This is necessary for the floating bubble to appear.
3.  **Start Floating:** Once the permission is granted, the floating bubble will automatically appear on your screen.
4.  **Move the Bubble:** Tap and hold the bubble to drag it anywhere on the screen.
5.  **Dismiss the Bubble:** Drag the bubble towards the 'X' icon that appears at the bottom of the screen and release it to close the bubble.

### Home Screen Widgets

The app also provides several home screen widgets for quick access to volume controls without opening the app:

1.  **Go to your home screen** and long-press to add a widget.
2.  **Find "Virtual Volume Buttons"** in the widget list.
3.  **Choose your style:**
    *   **Classic:** A vertical layout with volume up and down buttons.
    *   **Reversed:** A vertical layout with the button order reversed.
    *   **Classic Row:** A horizontal layout with volume up and down buttons.
    *   **Reversed Row:** A horizontal layout with the button order reversed.
4.  **Place the widget** on your home screen.

## Permissions

- `SYSTEM_ALERT_WINDOW`: Required to display the floating bubble over other applications.

## Tech Stack

- **Kotlin:** The primary programming language for the app.
- **Jetpack Compose:** Used for building the entire UI, including the floating bubble.
- **Android Services:** The core component for running the floating bubble in the background.

## Contributing

Contributions are welcome! If you have any ideas, suggestions, or bug reports, please feel free to open an issue or submit a pull request.