import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Suppress("Dsl_Scope_Violation")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget: KotlinNativeTargetWithHostTests = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    with(nativeTarget) {
        binaries {
            executable(listOf(NativeBuildType.RELEASE))
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}
