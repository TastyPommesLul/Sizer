plugins {
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
    id("maven-publish")
}

version = "${property("mod.version")}+${sc.current.version}"
group = property("maven_group").toString()
base.archivesName = property("mod.id").toString()

val minecraftVersion = property("deps.minecraft").toString()
val modVersion = property("mod.version").toString()
val loaderVersion = property("deps.loader").toString()


repositories {
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    implementation("net.fabricmc:fabric-loader:${property("deps.loader")}")

    implementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
    include(implementation("com.moulberry:lattice:2.2.0")!!)
    implementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
}

tasks.named<ProcessResources>("processResources") {
    inputs.property("version", modVersion)
    inputs.property("minecraft_version", minecraftVersion)
    inputs.property("loader_version", loaderVersion)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to modVersion,
            "minecraft_version" to minecraftVersion,
            "loader_version" to loaderVersion
        )
    }
}

val targetJavaVersion = 25
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    withSourcesJar()
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.named<Jar>("jar") {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

stonecutter {
    replacements.string(current.parsed >= "26.3") {
        replace("KEYSYM", "KEYBOARD")
    }
}