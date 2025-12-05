import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.kotlinSerialization)
}

kotlin {
  androidTarget { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }

  listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "ComposeApp"
      isStatic = true
    }
  }

  sourceSets {
    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.core.splashscreen)
      implementation(libs.androidx.glance.appwidget)
      implementation(libs.androidx.glance.material3)
    }

    commonMain.dependencies {
      implementation(libs.common.compose.components.resources)
      implementation(libs.common.compose.ui.toolingPreview)
      implementation(libs.common.compose.foundation)
      implementation(libs.common.compose.material.icons)
      implementation(libs.common.compose.material3)
      implementation(libs.common.compose.material3.windowSizeClass)
      implementation(libs.common.compose.runtime)
      implementation(libs.common.compose.ui)

      implementation(libs.common.kermit)

      implementation(libs.common.lifecycle.runtimeCompose)
      implementation(libs.common.lifecycle.viewModelNavigation3)

      implementation(libs.common.navigation3.ui)

      implementation(libs.common.calendar)
      implementation(libs.common.kotlinx.datetime)
      implementation(libs.common.kotlinx.serializationJson)
    }

    commonTest.dependencies { implementation(libs.common.kotlin.test) }
  }
}

android {
  namespace = "com.trm.daysaway"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.trm.daysaway"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }

  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
  buildTypes { getByName("release") { isMinifyEnabled = false } }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

dependencies { debugImplementation(compose.uiTooling) }
