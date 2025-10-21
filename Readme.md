## 📖 Overview

**Remote Car** is an IoT-based real-time control system that allows users to control a **DC motor car** using an **Android app** through **WebSocket communication**.  
The system consists of two main controllers — **ESP32** for wireless communication and **STM32** for low-level motor and sensor control.

The communication pipeline ensures **low latency and continuous control** between the mobile app and the car hardware.

---

## ⚙️ Key Features

- 📱 **Android App Control** – Real-time control of car movement and direction.  
- 🌐 **WebSocket Communication** – Enables continuous, low-latency data streaming between app and ESP32.  
- 🔁 **Dual MCU Architecture** – ESP32 handles networking, STM32 manages motor control and sensors.  
- ⚡ **Motor Driver (L298)** – Drives DC motors for forward, reverse, left, and right movements.  
- 📡 **Ultrasonic Sensor (HC-SR04)** – Detects obstacles; car automatically reverses when an obstacle is too close.  
- 🔄 **Automatic Safety Response** – STM32 monitors sensor data and prevents collisions.  

---

## 🛠️ Hardware Components

| Component | Description |
|------------|-------------|
| **ESP32** | Handles Wi-Fi connection and WebSocket communication with the Android app |
| **STM32** | Receives control commands via UART and executes motor + sensor logic |
| **L298 Motor Driver** | Controls DC motors (forward, reverse, left, right) |
| **DC Motors** | Drive the car |
| **Ultrasonic Sensor (HC-SR04)** | Measures distance to detect obstacles |
| **Battery Pack** | Powers the system |
| **Chassis + Wheels** | Car frame and movement system |

---

## 💻 Software Components

| Component | Description |
|------------|-------------|
| **Android App** | Sends control commands (e.g., forward, reverse, left, right) through WebSocket |
| **ESP32 Firmware** | Acts as a WebSocket server/client, relays commands to STM32 via UART |
| **STM32 Firmware** | Parses UART commands and controls motors via L298; monitors HC-SR04 |
| **WebSocket Protocol** | Provides full-duplex, low-latency communication for real-time control |

---

## 🔄 System Workflow

1. The **Android app** connects to the **ESP32 WebSocket server**.  
2. When the user presses a control button (e.g., Forward, Left, Stop),  
   → the app sends a **WebSocket command** (e.g., `"MOVE_FORWARD"`) to ESP32.  
3. ESP32 forwards that command via **UART** to STM32.  
4. STM32 decodes the command and drives the motors through **L298** accordingly.  
5. STM32 continuously reads data from the **HC-SR04 ultrasonic sensor**.  
   - If an obstacle is detected within a safety distance → STM32 automatically sends a `"BACKWARD"` command to reverse the car.  
6. ESP32 and app remain synchronized in real time, showing the latest car status.

---

## 📱 Android App

- Real-time WebSocket control (Forward / Backward / Left / Right / Stop)  
- Displays live car status  
- Connects via Wi-Fi to ESP32  
- Sends continuous commands for smooth movement (no delay) 
