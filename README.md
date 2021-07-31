### Run app

Open app folder using Android Studio

Inside `app/src/main/java/com/tencent/iot/speech/app/DemoConfig.java` (inside ASR-App-fork folder)

Change the following configurations

```java
public final static String SERVER_PATH = "http://35.215.150.91";
public final static String WS_PATH = "ws://35.215.150.91/ws?"; 
```

to

```java
public final static String SERVER_PATH = "http://192.168.1.124:8080";
public final static String WS_PATH = "ws://192.168.1.124:8080/ws?";
```

where 192.168.1.124  is your machine's local ip address

To find your machine's local address , please refer to the following link
[https://www.avg.com/en/signal/find-ip-address](https://www.avg.com/en/signal/find-ip-address)

Run the app in Android Studio using an emulator or a real device connected to the machine

### Support

If you need help using SimulRoom, or have a bug, please create an issue on [https://github.com/aribexxx/ASR-App](https://github.com/aribexxx/ASR-App)

### Code Reference
1. Tencent ASR SDK https://cloud.tencent.com/document/product/1093/35722
2. Material Design UI https://codelabs.developers.google.com/codelabs/mdc-101-java#0
