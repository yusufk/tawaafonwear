# TawaafOnWear

TawaafOnWear is an Android Wear OS application designed to help pilgrims track their Tawaaf rounds around the Holy Kaabah in Makkah.

## Features

*   **Tawaaf Counter**: Easily track the number of rounds (shauts) completed during Tawaaf.
*   **Total Tawaaf Tracker**: Keep track of the total number of completed Tawaafs (sets of 7 rounds).
*   **Simple Interface**: Designed for ease of use while performing rituals.
*   **Wear OS Optimized**: Native support for circular and square watch faces.

## Usage

*   Tap the screen to increment the round count.
*   After 7 rounds, a completion animation is shown, and the total Tawaaf count increments.
*   Use the reset button to start over if needed.
*   Use the minus button to correct accidental increments.

## Development

The project is divided into two modules:
*   `:mobile`: A companion Android app (currently showing a web view).
*   `:wear`: The main Wear OS application where the counting logic resides.

### Technical Details

*   Built using AndroidX and modern Android development practices.
*   Uses `WearableActivity` for ambient mode support to save battery.
