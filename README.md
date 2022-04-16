![myPiCamsLogo](/assets/img/pi_cams_logo.png)
***
# My PiCams
An android application that lets you monitor from all your Raspberry Pi Cameras.

## Features
This surveillance setup with just under &#8377;2000 and some Python coding is a perfect cost-effective, easy-to-set-up and easy-to-use home surveillance solution. Along with *My PiCams* app which provides a single pane of glass to monitor all the cameras from a single android application, it becomes a go-to solution for all IoT enthusiasts.

## Applications
* **Knock Knock** :door: - Watch who's in the lobby or at the door without even attending it!
* **Baby Monitor** :baby: - Ensure safety of your little ones in the other room!
* **Plant Monitor** :seedling: - Watch for uninvited birds trying to snatch your little seedlings!
* **Pet Monitor** :paw_prints: - Keep an eye on your furry members for their mischieves!
* **Parking Monitor** :car: - Ensure safety of your ride parked in the garage!
* **and many more...** :repeat: - Basically setup a PiCam wherever you need to keep an eye on!

## Usage
### 1. Setting up Raspberry Pi

Getting your Pi ready is a two step process:
  1. Get the hardware working by installing the camera using [this link](https://picamera.readthedocs.io/en/release-1.13/quickstart.html#getting-started).
  2. And secondly configure your Pi Camera to stream the video output over the web using [this link](https://picamera.readthedocs.io/en/release-1.13/recipes2.html#web-streaming).

Once the setup is ready, test from a web browser before accessing it directly from the app. Please make sure the python code for web streaming is saved in the `home` folder with the name `camerastream.py` as this is where the app will try to find the code to execute.
Make sure the code is executable with the command `python3 camerastream.py` in the home directory of the username used to SSH to Pi.
### 2. Configure My PiCams app
Configuring the app is as easy as it can be. Just fill in the details with proper IP Address, Web Port number, SSH Port number, Username and Password. 
If you are unsure about the port numbers, then web port number is the one we used in `camerastream.py` and SSH port number is usually 22 if you haven't changed it. 
The Alias name of the Pi Camera can be anything as it is for your reference.

***
The following demo shows a working example of the app. You'll see my 6yo nephew riding his bike. :bike:

![](/assets/img/demo_screen.gif)
***

This is my first work getting published and I have used a lot of open source resources. Following are few to mention:

## Credits & many thanks to 
- Raspberry Pi documentation for Pi Camera [web streaming code](https://picamera.readthedocs.io/en/release-1.13/recipes2.html#web-streaming) example.
- Start BootStrap for the [New Age](https://startbootstrap.com/theme/new-age) web template
- And countless online tutorials for coding the android application.

***
