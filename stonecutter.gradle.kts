plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter.active("26.3")

tasks.register<Copy>("collectJars") {
    group = "build"
    stonecutter.versions.forEach { ver ->
        from(project(":${ver.project}").layout.buildDirectory.dir("libs"))
    }
    into(layout.buildDirectory.dir("libs"))
    include("*.jar")
    exclude("*-dev.jar", "*-sources.jar")
}

tasks.register("build") {
    group = "build"
    stonecutter.versions.forEach { ver ->
        dependsOn(":${ver.project}:build")
    }
    finalizedBy("collectJars")
}