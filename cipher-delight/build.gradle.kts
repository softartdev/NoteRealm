plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}
group = "com.softartdev"
version = "0.1"

kotlin {
    jvm()
    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api("com.squareup.okio:okio-multiplatform:2.9.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
    cocoapods {
        frameworkName = "CipherDelight"
        summary = "Cipher library for the apps with SQLDelight"
        homepage = "https://github.com/softartdev/NoteDelight"
        ios.deploymentTarget = "13.5"
        pod("SQLCipher", "~> 4.0")
    }
}