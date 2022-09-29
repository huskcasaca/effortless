import io.github.themrmilchmann.gradle.publish.curseforge.*
import org.gradle.jvm.tasks.Jar
import java.util.Properties
import java.io.*

plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.curseforge)
}

version = "1.3.0"
group = "dev.huskcasaca"

base {
    archivesName.set("effortless-fabric")
}

repositories {
    maven("https://maven.shedaniel.me/") { name = "shedaniel" }
    maven("https://maven.terraformersmc.com/releases/") { name = "TerraformersMC" }
}

dependencies {
    minecraft(libs.minecraft)

    mappings(loom.officialMojangMappings())

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.modmenu) { isTransitive = false }
    modImplementation(libs.cloth.config) { isTransitive = false }

    implementation(libs.findbugs)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

loom {
    accessWidenerPath.value {
        file("src/main/resources/effortless.accesswidener")
    }
}

publishing {
    repositories {
        curseForge {
            apiKey.set(getLocalPropertyOrNull("curseforge.apikey"))
        }
    }

    publications.create<CurseForgePublication>("curseForge") {
        val minecraftVersion = libs.versions.minecraft.get()
        val minecraftVersionTypeName = minecraftVersion.split(".").take(2).joinToString("-")
        val minecraftVersionVersionName = minecraftVersion.split(".").take(3).joinToString("-")

        projectID.set(getLocalPropertyOrNull("curseforge.id")?.toInt()) // The CurseForge project ID (required)
        includeGameVersions { type, version -> type == "java" && version == "java-17" }
        includeGameVersions { type, version -> type == "modloader" && version == "fabric" }
        includeGameVersions { type, version -> type == "minecraft-$minecraftVersionTypeName" && version == minecraftVersionVersionName }

        artifact {
            changelog = Changelog("Changelog...", ChangelogType.TEXT) // The changelog (required)
            releaseType = ReleaseType.RELEASE // The release type (required)
            displayName = "${base.archivesName}-${version}-${minecraftVersion}.jar"
        }
    }
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from("src/main/resources")
        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }
    withType<Jar> {
        archiveVersion.value("${project.version}-${libs.versions.minecraft.get()}")
    }
}

fun getLocalPropertyOrNull(key: String): String? {
    val properties = Properties()
    val localProperties = File("local.properties")
    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else {
        println(localProperties.name + " is not found")
    }
    return properties.getProperty(key)
}
