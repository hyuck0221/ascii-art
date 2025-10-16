# ASCII Art Generator

A web application that converts images into ASCII art.

**🌐 [Try it now at the demo site!](https://hspace.site/)**

[한국어 문서](README.ko.md)

## Features

- **Real-time Rendering**: Live ASCII art generation via SSE (Server-Sent Events)
- **Batch Rendering**: Get the complete result at once
- **Multiple Detail Options**: Binary, Simple, and Detail modes
- **Multi-threaded Processing**: Choose between single or multi-threaded rendering
- **Reverse Option**: Invert brightness levels
- **Font Size Control**: Fine-tune from 1px to 20px
- **Drag & Drop**: Easy image upload

## Tech Stack

- **Backend**: Kotlin, Spring Boot 3.5.6
- **Frontend**: Vanilla JavaScript, HTML5, CSS3
- **Build Tool**: Gradle 8.5
- **Java Version**: 21

## Getting Started

### Prerequisites

- Docker
- Docker Compose

### Running with Docker

1. Clone the repository

```bash
git clone <repository-url>
cd asciiart
```

2. Run with Docker Compose

```bash
docker-compose up -d
```

3. Open in browser

```
http://localhost:8080
```

### Running Locally

#### Prerequisites

- JDK 21
- Gradle 8.5 or higher

#### How to Run

1. Clone the repository

```bash
git clone <repository-url>
cd asciiart
```

2. Build the application

```bash
./gradlew build
```

3. Run the application

```bash
./gradlew bootRun
```

4. Open in browser

```
http://localhost:8080
```

## How to Use

1. **Select Render Mode**
   - Realtime (SSE): Watch the conversion process in real-time
   - Batch (Complete): Display the complete result at once

2. **Select Detail Type**
   - Binary: 2-level brightness ( ░)
   - Simple: 4-level brightness ( .:░█)
   - Detail: 11-level brightness ( .'`^",:;Il!i><~+_-?][}{1)(|/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$)

3. **Select Thread Type**
   - Single: Single-threaded processing
   - Multi: Multi-threaded parallel processing

4. **Options**
   - Reverse: Invert brightness

5. **Adjust Font Size**
   - Use slider to adjust from 1.0px to 20.0px

6. **Upload Image**
   - Click upload area or drag and drop

## Docker Commands

### Build Image

```bash
docker-compose build
```

### Start Container

```bash
docker-compose up -d
```

### Stop Container

```bash
docker-compose down
```

### View Logs

```bash
docker-compose logs -f
```

### Restart Container

```bash
docker-compose restart
```

## Project Structure

```
asciiart/
├── src/
│   └── main/
│       ├── kotlin/
│       │   └── com/hshim/asciiart/
│       │       ├── AsciiartApplication.kt
│       │       ├── controller/
│       │       ├── service/
│       │       └── model/
│       └── resources/
│           └── static/
│               ├── index.html
│               ├── script.js
│               └── style.css
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── build.gradle.kts
└── settings.gradle.kts
```