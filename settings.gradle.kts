pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.polyfrost.org/releases")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.polyfrost.loom" -> useModule("org.polyfrost:architectury-loom:${requested.version}")
            }
        }
    }
}

rootProject.name = "HitDelayFix"
