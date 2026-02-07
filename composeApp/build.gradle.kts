import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidMultiplatformLibrary)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.kotlinSerialization)
}

kotlin {
  compilerOptions { freeCompilerArgs.add("-Xexpect-actual-classes") }

  androidLibrary {
    namespace = "com.trm.daysaway"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    minSdk = libs.versions.android.minSdk.get().toInt()
    compilerOptions.jvmTarget = JvmTarget.JVM_11
    androidResources { enable = true }
  }

  listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "ComposeApp"
      isStatic = true
    }
  }

  sourceSets {
    androidMain.dependencies {
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
