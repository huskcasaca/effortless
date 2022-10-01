import io.github.themrmilchmann.gradle.publish.curseforge.*
import org.gradle.jvm.tasks.Jar
import java.util.Properties
import java.io.*

@Suppress("DSL_SCOPE_VIOLATION")
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

        projectID.set(getLocalPropertyOrNull("curseforge.id")?.toInt()) // The CurseForge project ID (required)

        includeGameVersions { type, version -> type == "modloader" && version == "fabric" }

        val java = project.extensions.getByType(JavaPluginExtension::class)
        val targetVersion = java.targetCompatibility.majorVersion

        includeGameVersions { type, version -> type == "java" && version == "java-$targetVersion" }

        val mcVersion = libs.versions.minecraft.get()
        val matchGroups = mcVersion.split(".")
        val mcDependencySlug = "minecraft-${matchGroups.take(2).joinToString("-")}"
        val mcVersionSlug = matchGroups.take(3).joinToString("-")

        includeGameVersions { type, version -> type == mcDependencySlug && version == mcVersionSlug }

        artifact {
            changelog = Changelog("Changelog...", ChangelogType.TEXT) // The changelog (required)
            releaseType = ReleaseType.RELEASE // The release type (required)
            displayName = "${base.archivesName.get()}-${version}-${libs.versions.minecraft.get()}.jar"
        }
    }
}

tasks {
    withType(JavaCompile::class) {
        options.release.set(JavaVersion.VERSION_17.toString().toInt())
    }
    withType(ProcessResources::class) {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from("src/main/resources")
        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }
    withType(Jar::class) {
        archiveVersion.set("${project.version}-${libs.versions.minecraft.get()}")
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
