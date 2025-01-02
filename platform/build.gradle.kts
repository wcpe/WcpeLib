subprojects {
    tasks.withType<ProcessResources> {
        filesMatching(listOf("plugin.yml", "bungee.yml")) {
            expand(project.properties)
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    tasks {
        jar {
            // 构件名
            archiveBaseName.set("${rootProject.name}-${project.name}")
            // 打包子项目源代码

            for (project in listOf(project(":common"))) {
                from(project.sourceSets["main"].output)
            }
        }
    }
}