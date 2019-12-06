group = "hu.suppoze.pupperbot.bootloader"
version = "0.5.0"

application {
    mainClassName = "hu.suppoze.pupperbot.bootloader.BootLoader"
}

tasks {
    register("initConfig", Copy::class) {
        from("bootloader.json")
        into("build/install/pupperbot-bootloader-shadow/bin")
    }
}
