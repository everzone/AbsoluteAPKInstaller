# Absolute APK Installer


### Android APK installer using device owner privilege
(Only one application can be made as device owner)


## How to Install
> ### Oculus Quest2
> - Perform factory reset
> - Pair with your phone in Oculus App (don't follow instructions in Quest2)
> - Enable Developer Mode using your phone
> - Select 'Allow' button in Quest2
> - Install apk using adb (SideQuest)
> - Execute adb command below
>   ```
>   adb shell dpm set-device-owner net.everzone.AbsoluteAPKInstaller/.DeviceAdminReceiver
>   ---
>   (result)
>   Success: Device owner set to package ComponentInfo{net.everzone.AbsoluteAPKInstaller/net.everzone.AbsoluteAPKInstaller.DeviceAdminReceiver}
>   Active admin set to component {net.everzone.AbsoluteAPKInstaller/net.everzone.AbsoluteAPKInstaller.DeviceAdminReceiver}
>   ```
> - Follow instructions in Quest2
> - After reboot once, Enable Developer Mode again


---


## Using

https://user-images.githubusercontent.com/59465158/173155304-e2f752f1-ed98-468e-a86c-2c2f85a21e1b.mp4
