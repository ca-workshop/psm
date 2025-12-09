plugins {
    id("com.android.application")
}

android {
    namespace = "com.ca.passwordmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ca.unforgotten"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment:2.9.6")
    implementation("androidx.navigation:navigation-ui:2.9.6")


    // NotificationCompat
    implementation ("androidx.core:core-ktx:1.13.1")

    // MediaSessionCompat (optional but recommended for media controls)
    implementation ("androidx.media:media:1.6.0")

    // For backward-compatible vector drawables (optional)
    //implementation 'androidx.vectordrawable:vectordrawable:1.1.0'

    // Material Design (optional, for nicer buttons etc)
    //implementation ("com.google.android.material:material:1.11.0")


    // Lifecycle: ViewModel & LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.10.0")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.10.0")
    annotationProcessor ("androidx.lifecycle:lifecycle-compiler:2.8.4")

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    //google
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation ("com.google.android.gms:play-services-maps:19.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}

/*dependencies {


    // Lifecycle: ViewModel & LiveData
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.8.4'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.8.4'
    implementation 'androidx.lifecycle:lifecycle-runtime:2.8.4'
    annotationProcessor 'androidx.lifecycle:lifecycle-compiler:2.8.4'

    // Room
    implementation 'androidx.room:room-runtime:2.6.1'
    annotationProcessor 'androidx.room:room-compiler:2.6.1'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.2.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.6.1'
}*/