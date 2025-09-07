# SimpleMediaPlayer
A simple media player for windows. Thats it. Plays videos or displays images.

Video player has simple hotkey for pausing (space) and fullscreen can be exited by pressing ESCAPE. Image viewer is a bit more rudimentary but does have zooming and panning (you can double-click an image to recenter it).

This is a lightweight media program written in java 24. The purpose is mainly to help my understanding of javafx/javafxml design as well as making a, well, simple media player. This is more of a hobby project than anything else. I just wanted a very simple way to view media, without any fancy settings or telemetry tacked on.

Should run fine on both windows 10 and 11. I only own windows 10 computers at the moment so feel free to report any win11 issues. 


### Usage

You can find a latest release for the installer [from the releases page](https://github.com/H0rologium/SimpleMediaPlayer/releases) to fit your needs. Its an offline installer and will install the program under Program Files.

You set this as the default application for file extension of choice by right clicking a file with a desired extension and going to Open With -> Choose Another App (Win10) and browsing to the executable of this program to do so, and letting windows handle it from there. I'm not currently planning on adding regsitry functionality to do this automatically.

### Building from source

If you want to follow the exact process I used, I recommend starting by cleaning the project and running 'mvn install package' in the console in the project. You will then need to use jlink to put everything together. 
MOST IMPORTANTLY javafx libraries need to be manually included in the jlink command with --add-modules (you can run jdeps with a --module-path pointing to your javafx sdk with --print-module-deps in front of your target for the modules needed)

because I used the method for the installer, jpackage will be next in the process. when running jpackage you need to make sure that the jar we originally made with jlink is in an empty folder or jpackage will try to throw everything into the same archive. The jpackage arguments I consider important are "--runtime-image runtime","--type exe" and "--main-class horo.smp.main.Main" of course.
