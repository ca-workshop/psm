plugins {
    id("com.android.application") version "8.1.0"
}

android {
    namespace = "com.ca.passwordmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ca.passwordmanager"   // must match manifest package
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // ✅ single buildFeatures block
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Navigation (KTX)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Media (for Notification + MediaSession compat)
    implementation("androidx.media:media:1.6.0")

    // ✅ Lifecycle (KTX) – 2.7.0 works well with your older ViewModelFactory signature
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:2.7.0")

    // ❌ REMOVE this line – it pulls a mismatched databinding version (8.13.1):
    // implementation("androidx.databinding:databinding-runtime:8.13.1")
    // Data Binding runtime is automatically matched to your AGP (8.1.0) when you do buildFeatures.dataBinding = true

    // Room (KTX)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // Google Play services
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.2.0")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}




/*dependencies {

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
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.8.4")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.8.4")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.8.4")
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
}*/

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