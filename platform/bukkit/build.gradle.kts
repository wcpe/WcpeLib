repositories {
    maven("https://repo.opencollab.dev/snapshot")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://libraries.minecraft.net/")
    maven("https://maven.aliyun.com/repository/spring")
    maven("https://repo1.maven.org/maven2")
}
dependencies {
    implementation(project(":common"))
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("org.black_ixx:PlayerPoints:2.1.3")
    compileOnly("me.clip:placeholderapi:2.10.4")
}


