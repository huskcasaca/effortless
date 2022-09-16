plugins {
    id("fabric-loom")
//    alias(libs.plugins.loom)
//    kotlin("jvm").version(System.getProperty("kotlinVersion"))
}

val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val modmenuVersion: String by project
val clothConfigVersion: String by project

val modVersion: String by project
val mavenGroup: String by project
val archivesBaseName: String by project

base {
    archivesName.set(project.property("archivesBaseName") as String)
}

version = modVersion
group = mavenGroup

repositories {
    maven("https://maven.shedaniel.me/") { name = "shedaniel" }
    maven("https://maven.terraformersmc.com/releases/") { name = "TerraformersMC" }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modApi("com.terraformersmc:modmenu:$modmenuVersion") { isTransitive = false }
    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion") { isTransitive = false }

    implementation("com.google.code.findbugs:jsr305:3.0.2")

//    minecraft(libs.minecraft)
//    mappings(loom.officialMojangMappings())
//    modImplementation(libs.fabric.loader)
//    modImplementation(libs.fabric.api)
//    modImplementation(libs.modmenu) {
//        exclude(group = "net.fabricmc.fabric-api")
//    }
//    modImplementation(libs.cloth.config) {
//        exclude(group = "net.fabricmc.fabric-api")
//        exclude(module = "modmenu")
//    }
//    implementation("com.google.code.findbugs:jsr305:3.0.2")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        if (JavaVersion.current().isJava9Compatible) {
            options.release.set(JavaVersion.VERSION_17.toString().toInt())
        }
    }
//    withType<KotlinCompile> {
//        kotlinOptions {
//            jvmTarget = javaVersion.toString()
//        }
//    }

    jar {
        this.exclude("effortless.accesswidener")
    }

    processResources {
        inputs.property("version", project.version)
    }
}




java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

loom {
    accessWidenerPath.value {
        file("src/main/resources/effortless.accesswidener")
    }
}