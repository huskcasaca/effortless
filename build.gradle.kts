@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.loom)
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
