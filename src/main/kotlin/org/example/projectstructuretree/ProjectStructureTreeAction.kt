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
        val builder = StringBuilder()

        val srcDir = File(dir, "src") //tree start
        if (srcDir.exists() && srcDir.isDirectory) {
            builder.append(prefix).append("├── ").append("src").append("\n")
            builder.append(buildTreeRecursively(srcDir, "$prefix│   "))
        }

        return builder.toString()
    }

    private fun buildTreeRecursively(dir: File, prefix: String): String {
        val builder = StringBuilder()

        dir.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))?.forEach { file ->
            if (file.isDirectory) {
                //wyświetl
                builder.append(prefix).append("├── ").append(file.name).append("\n")
                builder.append(buildTreeRecursively(file, "$prefix│   "))
            } else {
                builder.append(prefix).append("├── ").append(file.name).append("\n")
            }
        }

        return builder.toString()
    }
}
