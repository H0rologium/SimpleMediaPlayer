# SimpleMediaPlayer
A simple media player for windows. Thats it. Plays videos or displays images.

This is a lightweight media program written in java 24. The purpose is mainly to help my understanding of javafx/javafxml design as well as making a, well, simple media player. This is more of a hobby project than anything else. I just wanted a very simple way to view media, without any fancy settings or telemetry tacked on.

Should run fine on both windows 10 and 11. I only own windows 10 computers at the moment so feel free to report any win11 issues. 

### What it does/does not

This program does:
- Play videos
    - Includes standard playback buttons such as stop/pause, skip to beginning/end
    - Videos can be put on repeat
- View images
    - Images can be rotated in the view (does not save rotation, just for viewing)
- Include a settings menu to configure how the program operates


This program does not:
- Support subtitles
- Automatically check for updates (or make any sort of attempt at connecting to the internet)
- Playback videos at varying speeds
- Save timestamps or continuation points when viewing video

### Usage

You can find a latest release for the installer from the releases page to fit your needs. Its an offline installer and will install the program under Program Files.

Originally there was going to be logic for modifying the registry so you would be able to easily assign this program as the default program for common media filetypes. Unfortunately with how undocumented this is for a javafx program bundled using jpackage it was at best a risky implementation (plus I would like to avoid something going wrong and breaking the registry since this is just a hobby project). You can still set this as the default by right clicking a file with a desired extension and going to Open With -> Choose Another App (Win10) and browsing to the executable of this program to do so, and letting windows handle it from there.