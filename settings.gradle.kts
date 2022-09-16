pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net") { name = "Fabric" }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        val loomVersion: String by settings
        id("fabric-loom").version(loomVersion)
    }
//    resolutionStrategy {
//        eachPlugin {
//            if (requested.id.id == "net.minecraftforge.gradle") {
//                useModule("${requested.id}:ForgeGradle:${requested.version}")
//            }
//        }
//    }
}
