package org.example.projectstructuretree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.io.File

class ProjectTreeAction : AnAction() {
    private var filters: ProjectStructureTreeFilters = ProjectStructureTreeFilters()

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val projectPath = project.basePath ?: return

        val treeStructure = buildProjectTree(File(projectPath))
        println(treeStructure)
    }

    fun setFilters(filters: ProjectStructureTreeFilters) {
        this.filters = filters
    }

    fun buildProjectTree(dir: File, prefix: String = ""): String {
        val builder = StringBuilder()

        val srcDir = File(dir, "src")
        if (srcDir.exists() && srcDir.isDirectory) {
            builder.append(prefix).append("├── ").append("src").append("\n")
            builder.append(buildTreeRecursively(srcDir, "$prefix│   "))
        }

        return builder.toString()
    }

    private fun buildTreeRecursively(dir: File, prefix: String): String {
        val builder = StringBuilder()

        val filesAndDirs = dir.listFiles()?.sortedWith(compareBy({ it.isDirectory }, { it.name })) ?: return ""

        if (filters.showFilesFirst) {
            //first filers
            filesAndDirs.filter { it.isFile }.forEach { file ->
                builder.append(prefix).append("├── ").append(file.name).append("\n")
            }
            filesAndDirs.filter { it.isDirectory }.forEach { subDir ->
                builder.append(prefix).append("├── ").append(subDir.name).append("\n")
                builder.append(buildTreeRecursively(subDir, "$prefix│   "))
            }
        } else {
            //first dirs
            filesAndDirs.filter { it.isDirectory }.forEach { subDir ->
                builder.append(prefix).append("├── ").append(subDir.name).append("\n")
                builder.append(buildTreeRecursively(subDir, "$prefix│   "))
            }
            filesAndDirs.filter { it.isFile }.forEach { file ->
                builder.append(prefix).append("├── ").append(file.name).append("\n")
            }
        }

        return builder.toString()
    }
}
