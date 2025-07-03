plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.ksp)
    // Google이 권장하는 App Architecture에서는 presentor -> domain -> data 순으로 의존하는 것을 권장하고 있다.
    // 하지만 이를 그대로 구현하니 마주한 문제는
    // No matching variant of project :data was found. The consumer was configured to find a library for use during compile-time, compatible wit
    // Android 의존성을 가지라는 것인데, domain은 순수 코틀린 비즈니스 로직을 포함하는 것이 맞지 않나?
}

android {
    namespace = "com.example.domain"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":data"))

    // javax
    implementation(libs.javax)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
