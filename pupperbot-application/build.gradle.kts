group = "hu.suppoze.pupperbot.app"
version = "0.5.0"

application {
    mainClassName = "hu.suppoze.pupperbot.app.PupperBotApplication"
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.61")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.0")

    // Discord
    implementation("com.jessecorbett:diskord:1.5.3")

    // Scraper
    implementation("org.jsoup:jsoup:1.12.2")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.0")
    testImplementation("io.mockk:mockk:1.9.3")
}

tasks {
    register("initToken", Copy::class) {
        from("../token_release.txt")
        into("build/libs")
        rename("token_release.txt", "token.txt")
    }
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
        }
    }
}
