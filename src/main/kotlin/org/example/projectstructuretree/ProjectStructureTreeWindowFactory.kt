package org.example.projectstructuretree

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextArea
import java.io.File
import java.awt.datatransfer.StringSelection
import java.awt.Toolkit

class ProjectStructureTreeWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(createProjectTreePanel(project), "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun createProjectTreePanel(project: Project): JPanel {
        val panel = JPanel(BorderLayout())
        val textArea = JTextArea()
        val projectPath = project.basePath ?: ""
        textArea.text = ProjectTreeAction().buildProjectTree(File(projectPath))
        textArea.isEditable = false

        val regenerateButton = JButton("Generate again")
        regenerateButton.addActionListener {
            textArea.text = ProjectTreeAction().buildProjectTree(File(projectPath))
        }

        val copyButton = JButton("Copy")
        copyButton.addActionListener {
            val stringSelection = StringSelection(textArea.text)
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(stringSelection, null)
        }

        val buttonPanel = JPanel()
        buttonPanel.add(regenerateButton)
        buttonPanel.add(copyButton)

        panel.add(JBScrollPane(textArea), BorderLayout.CENTER)
        panel.add(buttonPanel, BorderLayout.SOUTH)
        return panel
    }
}

