# 🎮 Jetris

A sleek, minimalist **Tetris clone** built in Java — complete with a GUI, keyboard controls, and a persistent leaderboard.

---

## 🚀 How It Works

At the core of Jetris is a **2D integer array** that represents the game board. Each tetromino piece is defined by a shape matrix and manipulated using standard transformation logic:

- Blocks fall over time (gravity)
- User controls: rotate, move, and drop
- Game over when the stack reaches the top
- Score tracking and persistent leaderboard (via JSON)

---

## 🖼️ Features

- **Swing GUI** with smooth graphics
- **Real-time keyboard input** — no need to press Enter
- **Classic Tetris mechanics**
- **Leaderboard system** using JSON (with sorting and save/load)
- Optional **ASCII mode** for console nostalgia
- Toggle between GUI and text mode via command input

---

## 🧰 Tech Stack

- Java (JDK 17+ recommended)
- Swing (GUI)
- GSON (for leaderboard JSON serialization)

---

## 🛠️ How to Run

### From an IDE (IntelliJ, VS Code, etc.)

1. Clone the repo  
2. Make sure `gson-*.jar` is included in your project libraries  
3. Run the `Jetris` main class  

### From the Command Line

```bash
javac -cp ".;libs/gson-2.10.1.jar" Jetris.java
java -cp ".;libs/gson-2.10.1.jar" Jetris
