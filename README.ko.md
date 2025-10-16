# ASCII Art Generator

이미지를 아스키 아트로 변환하는 웹 애플리케이션입니다.

**🌐 [데모 사이트에서 바로 사용해보세요!](https://hspace.site/)**

## 기능

- **실시간 렌더링**: SSE(Server-Sent Events)를 통한 실시간 아스키 아트 생성
- **배치 렌더링**: 완성된 결과를 한 번에 받아보기
- **다양한 디테일 옵션**: Binary, Simple, Detail 모드 지원
- **멀티스레드 처리**: 싱글/멀티 스레드 선택 가능
- **반전 옵션**: 명암 반전 기능
- **폰트 크기 조절**: 1px ~ 20px까지 세밀한 조절
- **드래그 앤 드롭**: 간편한 이미지 업로드

## 기술 스택

- **Backend**: Kotlin, Spring Boot 3.5.6
- **Frontend**: Vanilla JavaScript, HTML5, CSS3
- **Build Tool**: Gradle 8.5
- **Java Version**: 21

## 시작하기

### 사전 요구사항

- Docker
- Docker Compose

### Docker로 실행하기

1. 저장소 클론

```bash
git clone <repository-url>
cd asciiart
```

2. Docker Compose로 실행

```bash
docker-compose up -d
```

3. 브라우저에서 접속

```
http://localhost:8080
```

### 로컬에서 직접 실행하기

#### 사전 요구사항

- JDK 21
- Gradle 8.5 이상

#### 실행 방법

1. 저장소 클론

```bash
git clone <repository-url>
cd asciiart
```

2. 애플리케이션 빌드

```bash
./gradlew build
```

3. 애플리케이션 실행

```bash
./gradlew bootRun
```

4. 브라우저에서 접속

```
http://localhost:8080
```

## 사용 방법

1. **렌더 모드 선택**
   - Realtime (SSE): 실시간으로 변환 과정 확인
   - Batch (Complete): 완성된 결과를 한 번에 표시

2. **디테일 타입 선택**
   - Binary: 2단계 명암 ( ░)
   - Simple: 4단계 명암 ( .:░█)
   - Detail: 11단계 명암 ( .'`^",:;Il!i><~+_-?][}{1)(|/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$)

3. **스레드 타입 선택**
   - Single: 단일 스레드 처리
   - Multi: 멀티 스레드 병렬 처리

4. **옵션 설정**
   - Reverse: 명암 반전

5. **폰트 크기 조절**
   - 슬라이더로 1.0px ~ 20.0px 조절

6. **이미지 업로드**
   - 업로드 영역 클릭 또는 드래그 앤 드롭

## Docker 명령어

### 이미지 빌드

```bash
docker-compose build
```

### 컨테이너 시작

```bash
docker-compose up -d
```

### 컨테이너 중지

```bash
docker-compose down
```

### 로그 확인

```bash
docker-compose logs -f
```

### 컨테이너 재시작

```bash
docker-compose restart
```

## 프로젝트 구조

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