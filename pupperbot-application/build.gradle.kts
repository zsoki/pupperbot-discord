group = "hu.suppoze.pupperbot"
version = "0.4.0"

application {
    mainClassName = "hu.suppoze.pupperbot.app.PupperBotApplication"
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.61")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")

    // Discord
    implementation("net.dv8tion:JDA:4.0.0_67")

    // Reflection
    implementation("org.reflections:reflections:0.9.11")

    // Networking
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.2.2")
    implementation("com.squareup.okhttp3:okhttp:4.2.2")

    // DI
    implementation("org.kodein.di:kodein-di-generic-jvm:6.4.1")

    // Scraper
    implementation("org.jsoup:jsoup:1.12.1")

    // Database
    implementation("org.jetbrains.exposed:exposed:0.11.2")
    implementation("org.xerial:sqlite-jdbc:3.21.0.1")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:5.5.2")
    testImplementation("io.mockk:mockk:1.9")
}

tasks {
    register("initToken", Copy::class) {
        from("../token_release.txt")
        into("build/libs")
        rename("token_release.txt", "token.txt")
    }
}

tasks.test {
    useJUnitPlatform()
}
