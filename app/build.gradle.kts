plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.dagger.hilt.android)
    jacoco
}


jacoco {
    toolVersion = "0.8.10"
}

project.afterEvaluate {
    setupAndroidReporting()
}

fun setupAndroidReporting() {
    val buildTypes = listOf("debug")

    buildTypes.forEach { buildTypeName ->
        val sourceName = buildTypeName
        val testTaskName = "test${sourceName.capitalize()}UnitTest"
        println("Task -> $testTaskName")

        tasks.register<JacocoReport>("${testTaskName}Coverage") {
            dependsOn(tasks.findByName(testTaskName))

            group = "Reporting"
            description = "Generate Jacoco coverage reports on the ${sourceName.capitalize()} build."

            reports {
                xml.required.set(true)
                csv.required.set(false)
                html.required.set(true)
            }

            val fileFilter = listOf(
                    // android
                    "**/R.class",
                    "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*Test*.*",
                    "android/**/*.*",
                    // kotlin
                    "**/*MapperImpl*.*",
                    "**/*\$ViewInjector*.*",
                    "**/*\$ViewBinder*.*",
                    "**/BuildConfig.*",
                    "**/*Component*.*",
                    "**/*BR*.*",
                    "**/Manifest*.*",
                    "**/*\$Lambda$*.*",
                    "**/*Companion*.*",
                    "**/*Module*.*",
                    "**/*Dagger*.*",
                    "**/*Hilt*.*",
                    "**/*MembersInjector*.*",
                    "**/*_MembersInjector.class",
                    "**/*_Factory*.*",
                    "**/*_Provide*Factory*.*",
                    "**/*Extensions*.*",
                    // sealed and data classes
                    "**/*\$Result.*",
                    "**/*\$Result$*.*",
                    // adapters generated by moshi
                    "**/*JsonAdapter.*",
                    "**/*Activity*",
                    "**/di/**",
                    "**/hilt*/**",
                    "**/entrypoint/**",
                    "**/theme/**",
                    "**/*Screen*.*"
            )

            val javaTree = fileTree("${project.buildDir}/intermediates/javac/$sourceName/classes"){
                exclude(fileFilter)
            }
            val kotlinTree = fileTree("${project.buildDir}/tmp/kotlin-classes/$sourceName"){
                exclude(fileFilter)
            }
            classDirectories.setFrom(files(javaTree, kotlinTree))

            executionData.setFrom(files("${project.buildDir}/jacoco/${testTaskName}.exec"))
            val coverageSourceDirs = listOf(
                    "${project.projectDir}/src/main/java",
                    "${project.projectDir}/src/$buildTypeName/java"
            )

            sourceDirectories.setFrom(files(coverageSourceDirs))
            additionalSourceDirs.setFrom(files(coverageSourceDirs))
        }
    }
}

android {
    namespace = "io.mcnulty.avwx"
    compileSdk = 33

    defaultConfig {
        applicationId = "io.mcnulty.avwx"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn" + listOf(
            "-P",
            // See https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir}/reports/kotlin-compile/compose"
        )
    }
    
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation (platform("androidx.compose:compose-bom:2022.10.00"))
    implementation ("androidx.compose.ui:ui")
    implementation ("androidx.compose.ui:ui-graphics")
    implementation ("androidx.compose.ui:ui-tooling-preview")
    implementation ("androidx.compose.material3:material3")
    implementation (libs.org.jacoco.core)

    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Data and Async
    implementation(libs.retrofit)

    testImplementation (libs.junit)
    androidTestImplementation (libs.ext.junit)
    androidTestImplementation (libs.espresso.core)
    androidTestImplementation (platform("androidx.compose:compose-bom:2022.10.00"))
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation (libs.runner)
    androidTestImplementation (libs.rules)
    debugImplementation ("androidx.compose.ui:ui-tooling")
    debugImplementation ("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}