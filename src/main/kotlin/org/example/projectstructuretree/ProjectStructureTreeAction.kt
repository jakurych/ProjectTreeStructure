package org.example.projectstructuretree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.io.File

class ProjectTreeAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val projectPath = project.basePath ?: return

        val treeStructure = buildProjectTree(File(projectPath))
        println(treeStructure)
    }

    fun buildProjectTree(dir: File, prefix: String = ""): String {
        val ignoreDirs = listOf("build", ".gradle", ".idea")
        val builder = StringBuilder()

        dir.listFiles()?.filter { it.isDirectory && !ignoreDirs.contains(it.name) }?.forEach { subdir ->
            builder.append(prefix).append("├── ").append(subdir.name).append("\n")
            builder.append(buildProjectTree(subdir, "$prefix│   "))
        }

        dir.listFiles()?.filter { it.isFile }?.forEach { file ->
            builder.append(prefix).append("├── ").append(file.name).append("\n")
        }

        return builder.toString()
    }
}
