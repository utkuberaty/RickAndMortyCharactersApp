plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.utku.rickandmortycharacters"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.utku.rickandmortycharacters"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "BASE_URL", "\"https://rickandmortyapi.com/graphql\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

apollo {
    service("service") {
        packageName.set("com.utku.rickandmortycharacters")
        introspection {
            endpointUrl.set("https://rickandmortyapi.com/graphql")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //Apollo
    implementation(libs.apollo.runtime)
    implementation(libs.apollo.http.cahce)
    implementation(libs.apollo.normalized.cache)
    implementation(libs.apollo.normalized.cache.sqlite)
    implementation(libs.apollo.android.compose.paging)

    //Okhttp
    implementation(libs.okhttp)

    //Coil
    implementation(libs.coil)

    //Koin
    implementation(libs.koin.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}