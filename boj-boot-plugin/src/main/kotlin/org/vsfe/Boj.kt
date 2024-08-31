package org.vsfe

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.TaskAction

class BojBootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("boj", BojTask::class.java) { task ->
            task.group = "boj"
            task.description = "test"
        }
    }
}

abstract class BojTask : DefaultTask() {
    @TaskAction
    fun generateFiles() {
        val problemNumber = if (project.hasProperty("problemNumber")) project.properties["problemNumber"].toString().toInt() else 0
        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        val mainSourceSet = javaExtension.sourceSets.getByName("main")
        val testSourceSet = javaExtension.sourceSets.getByName("test")
        val mainSrcDir = "${mainSourceSet.allJava.srcDirs.first().path}/${project.group.toString().replace(".", "/")}/p$problemNumber"
        val testSrcDir = "${testSourceSet.allJava.srcDirs.first().path}/${project.group.toString().replace(".", "/")}/p$problemNumber"
        val testResourceDir = "${testSourceSet.resources.srcDirs.first().path}/testcase/p$problemNumber"

        try {
            val problemInfo = ProblemFinder.getProblemInfo(problemNumber)

            FileWriter.writeMainFile(project, problemNumber, mainSrcDir)
            FileWriter.writeTestFile(project, problemNumber, problemInfo.problemTimeout, testSrcDir)
            FileWriter.writeTestcase(testResourceDir, problemInfo)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}