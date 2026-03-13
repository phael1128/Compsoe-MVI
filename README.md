# Compose MVI

> Jetpack Compose, MVI, Clean Architecture를 바탕으로  
> 이미지와 동영상 검색 결과를 한 흐름으로 탐색하고, 저장한 항목을 다시 열어보는 Android 샘플 프로젝트입니다.

## Overview

이 프로젝트는 `검색`, `상세 보기`, `저장 문서 관리`라는 비교적 단순한 흐름 안에서  
상태 관리, 레이어 분리, 데이터 매핑, 로컬 저장, 적응형 레이아웃을 어떻게 조합할지에 초점을 둡니다.

현재 구조는 다음 방향을 지향합니다.

- `app`: Compose UI, Navigation, ViewModel, MVI 상태 관리, 적응형 레이아웃
- `domain`: 순수 비즈니스 로직, Entity, Repository Interface, UseCase
- `data`: 네트워크/DB 구현, Repository 구현체, Hilt DI

---

## What It Does

| 기능 | 설명 |
| --- | --- |
| 통합 검색 | 이미지 검색과 동영상 검색 결과를 함께 조회하고 하나의 피드로 병합합니다. |
| 무한 스크롤 | 스크롤 하단 도달 시 다음 페이지를 불러옵니다. |
| 상세 보기 | 선택한 결과를 WebView로 열어 원문/상세 내용을 확인합니다. |
| 저장 문서 | 사용자가 저장한 문서를 Room DB에 보관하고 다시 조회합니다. |
| 저장 액션 | 저장 탭에서 상세 보기, 저장 해제, 링크 공유, 링크 복사를 지원합니다. |
| 저장 상태 반영 | 검색 결과에서 저장된 항목은 `isSaved` 상태로 즉시 반영되어 별 아이콘 톤이 바뀝니다. |
| 다국어 지원 | 기본 언어는 영어이며, 디바이스 언어가 한국어면 한국어 문자열을 사용합니다. |
| 적응형 내비게이션 | 넓은 화면에서는 하단 탭 대신 좌측 내비게이션 레일로 전환됩니다. |

---

## Preview Flow

```text
검색어 입력
   ->
이미지 + 동영상 결과 조회
   ->
Document 모델로 매핑
   ->
datetime 기준 정렬 후 리스트 표시
   ->
상세 보기 또는 로컬 저장
   ->
저장 탭에서 다시 열기 / 공유 / 복사 / 제거
```

---

## Architecture

### Layer Diagram

```mermaid
flowchart LR
    UI[Compose UI] --> VM[ViewModel]
    VM --> UC[UseCase]
    UC --> RI[Repository Interface]
    RI --> RImpl[Repository Impl]
    RImpl --> Remote[HttpURLConnection + Gson]
    RImpl --> Local[Room Database]
```

### Module Diagram

```text
app
 -> Presentation layer
 -> Compose / Navigation / ViewModel / Adaptive layout

domain
 -> Entity / UseCase / Repository Interface
 -> Android 의존성 없는 순수 Kotlin 모듈

data
 -> Repository Impl / DataSource / DI
 -> HttpURLConnection / Gson / Room
```

### Search Flow

```text
SearchingScreen
 -> SearchingViewModel
 -> MediaSearchResultUseCase
 -> MediaSearchingRepository
 -> MediaSearchingRepositoryImpl
 -> MediaSearchingRemoteDataSource
 -> HttpUrlConnectionClient
 -> Kakao Search API
```

### Save Flow

```text
SavedDocumentScreen
 -> SavedDocumentViewModel
 -> SavedDocumentResultUseCase
 -> SavedDocumentRepository
 -> SavedDocumentRepositoryImpl
 -> DocumentLocalDataSource
 -> Room DAO
```

---

## Tech Stack

| Category | Stack |
| --- | --- |
| UI | Jetpack Compose, Material 3 |
| State | MVI-style Contract + ViewModel |
| DI | Hilt |
| Local Storage | Room |
| Network | HttpURLConnection + Gson |
| Async | Kotlin Coroutines |
| Image Loading | Coil |
| Detail Viewer | Accompanist WebView |
| Localization | Android string resources, `values-ko` |

---

## Project Structure

```text
ComposeMVI
├── app
│   ├── screen
│   ├── viewmodels
│   ├── contract
│   ├── model
│   └── ui
├── domain
│   ├── entity
│   ├── repository
│   └── usecase
└── data
    ├── datasource
    │   ├── local
    │   └── remote
    ├── mapping
    ├── repository
    └── usecase/di
```

---

## Key Implementation Points

### 1. Search Result Merge

이미지 결과와 동영상 결과를 각각 조회한 뒤 `Document` 모델로 매핑하고,  
하나의 리스트로 합쳐 `datetime` 기준 내림차순으로 정렬해 표시합니다.

### 2. Pagination per Content Type

이미지와 동영상은 각각 별도의 페이지 상태를 관리합니다.

- 이미지: `imagePageCount`
- 동영상: `videoPageCount`
- 새 검색어 입력 시 각 페이지는 다시 `1`부터 시작합니다.

### 3. Saved State Reflection

검색 결과를 그리기 전에 저장된 문서 목록을 조회하고,  
이미 저장된 항목은 `Document.isSaved`로 표시합니다.

- 저장 전: 기본 톤의 별 아이콘
- 저장 후: 강조된 별 아이콘
- 저장된 항목은 다시 눌러도 중복 저장하지 않도록 막습니다.

### 4. Saved Item Actions

저장 탭의 항목은 단순 조회만 하지 않고 다시 활용할 수 있습니다.

- 카드 탭: 상세 보기
- 메뉴 액션: 저장 해제
- 메뉴 액션: 링크 공유
- 메뉴 액션: 링크 복사

### 5. Correct Type Restoration in Local DB

저장된 문서를 다시 불러올 때 이미지/동영상 타입을 추론하지 않고,  
`searchingViewType` 값을 DB에 직접 저장하고 복원합니다.

### 6. Pure Domain

`domain` 모듈은 Android Framework에 의존하지 않도록 분리했습니다.  
UseCase 생성은 `data` 모듈의 Hilt Module에서 담당합니다.

### 7. Adaptive Navigation for Wide Screens

일반 화면에서는 하단 탭을 사용하고,  
폭이 넓은 화면에서는 좌측 `NavigationRail`로 전환합니다.

- 기본 기준: `screenWidthDp >= 600`
- 대상 예시: 펼친 폴더블, 태블릿, 넓은 분할 화면

### 8. Localization

문자열은 기본적으로 영어 리소스를 사용하고,  
디바이스 locale이 한국어인 경우 `values-ko` 리소스로 전환됩니다.

---

## Getting Started

### Requirements

- Android Studio
- JDK 11
- Android SDK 35

### Build

```bash
./gradlew build
```

### Run Debug Build

```bash
./gradlew :app:assembleDebug
```

---

## Main Entry Points

| 위치 | 역할 |
| --- | --- |
| `app/src/main/java/com/example/myapplication/MainActivity.kt` | 앱 시작점 |
| `app/src/main/java/com/example/myapplication/screen/main/MainScreen.kt` | 메인 네비게이션과 적응형 레이아웃 분기 |
| `app/src/main/java/com/example/myapplication/screen/main/SearchingNavigationRail.kt` | 넓은 화면용 좌측 내비게이션 레일 |
| `app/src/main/java/com/example/myapplication/viewmodels/SearchingViewModel.kt` | 검색 상태와 이벤트 처리 |
| `app/src/main/java/com/example/myapplication/viewmodels/SavedDocumentViewModel.kt` | 저장 문서 액션과 effect 처리 |
| `domain/src/main/java/com/example/domain/usecase/MediaSearchResultUseCase.kt` | 검색 결과 병합과 페이지 처리 |
| `data/src/main/java/com/example/data/datasource/remote/network/HttpUrlConnectionClient.kt` | HTTP 요청 처리 |
| `data/src/main/java/com/example/data/datasource/local/database/SearchingResultDataBase.kt` | Room DB 진입점 |

---

## Notes

- 이 프로젝트는 학습과 구조 이해를 목적으로 한 샘플 성격이 강합니다.
- 현재 네트워크 계층은 `Retrofit` 대신 `HttpURLConnection`을 사용합니다.
- 넓은 화면 적응형 UI는 `화면 폭` 기준 분기이며, 아직 실제 fold posture API를 직접 사용하지는 않습니다.
- 운영 환경이라면 API Key 관리, 에러 처리, 로깅, 테스트 커버리지를 추가로 강화하는 편이 적절합니다.
