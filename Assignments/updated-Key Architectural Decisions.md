# Key Architectural Decisions

## Table of Contents

- [Directions](#directions)
- [1. Introduction](#1-introduction)
  - [1.1. Purpose](#11-purpose)
- [2. System Overview](#2-system-overview)
- [3. Technical Choices](#3-technical-choices)
  - [3.1. Frontend Framework](#31-frontend-framework)
  - [3.2. Backend Framework](#32-backend-framework)
  - [3.3. Database System](#33-database-system)
- [4. Other Considerations](#4-other-considerations)
  - [4.1. Team Skills and Learning](#41-team-skills-and-learning)
  - [4.2. Community and Support](#42-community-and-support)
  - [4.3. Future Adaptability](#43-future-adaptability)
- [5. Decision Log](#5-decision-log)


## 1. Introduction

### 1.1. Purpose

This document provides insights into the technical decisions that were made while building the "Tweety Bird" game. By documenting these decisions and their reasons, it provides an overview of the project's technological landscape.

---

## 2. System Overview

The "Tweety Bird" game is developed using Java and utilizes the Java Swing library for GUI components. It features an endless runner gameplay where the player controls a bird character in order to navigate through obstacles while also aiming to achieve the highest score possible.

---

## 3. Technical Choices

### 3.1. Frontend Framework

#### 3.1.1. Frontend Framework: Java Swing

We have chosen to employ the Java Swing library for developing the frontend of the "Tweety Bird" game. Java Swing offers a wide range of GUI components and robust event-handling mechanisms, making it well-suited for creating interactive graphical applications.

#### 3.1.2. Rationale:

- **Platform Independence:** Java Swing facilitates the development of platform-independent applications, ensuring compatibility across various operating systems.
- **Rich GUI Components:** Swing provides a various amount of customizable GUI components, allowing us to design a visually appealing user interface for the game.
- **Event-Driven Programming:** The event-driven programming model of Swing simplifies the implementation of interactive features such as user input handling and animation.

---

### 3.2. Backend Framework

#### 3.2.1. No Backend Framework

Due to the standalone nature of the "Tweety Bird" game as a desktop application, we have decided not to unclude a backend framework. The game operates locally on the user's device without requiring server-side processing or data storage.

#### 3.2.2. Rationale:

- **Standalone Application:** "Tweety Bird" is designed as a single-player desktop game, eliminating the need for server-side processing or backend infrastructure.
- **Local Data Storage:** Game progress and high scores are managed locally within the application, obviating the necessity for backend services for data storage or retrieval.

---

### 3.3. Database System

#### 3.3.1. No Database System

As a standalone desktop game, "Tweety Bird" does not include a database system for data storage or management. All of the game-related data, including high scores, is stored within the application.

#### 3.3.2. Rationale:

- **Local Data Storage:** Game data, such as high scores and user progress, is stored locally on the user's device, eliminating the need for a centralized database system.
- **Simplicity and Performance:** By eschewing a database system, we simplify the application architecture and enhance performance by reducing data access latency and overhead.

---

## 4. Other Considerations

### 4.1. Team Skills and Learning

The decision to utilize Java Swing as the frontend framework aligns with the team's proficiency in Java programming and familiarity with Swing GUI development. Leveraging existing skills speeds up the development process and ensures the code's quality.

### 4.2. Community and Support

There is an ample amount of documentation and community support available for Java Swing and has made the development process smoother by providing valuable resources, tutorials, and troubleshooting assistance.

### 4.3. Future Adaptability

While "Tweety Bird" currently operates as a standalone desktop game, the architecture allows for future adaptations or expansions, such as porting to other platforms or integrating online features, if the project requirements progress.

---

## 5. Decision Log

| Date       | Decision                                          | Reasoning                                                                                                                                    |
|------------|---------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| 2024-04-06 | Selected Java Swing as the frontend framework.    | Java Swing's platform independence, GUI components, and event-driven programming model line up with the requirements of the project.         |
| 2024-04-18 | Omitted a backend framework and database system.  | The standalone nature of "Tweety Bird" as a desktop game removes the need for server-side processing, or centralized data storage.              |