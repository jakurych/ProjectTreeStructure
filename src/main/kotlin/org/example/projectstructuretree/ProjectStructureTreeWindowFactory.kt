package org.example.projectstructuretree

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextArea
import java.io.File
import java.awt.datatransfer.StringSelection
import java.awt.Toolkit
import javax.swing.JCheckBox
import javax.swing.JOptionPane

class ProjectStructureTreeWindowFactory : ToolWindowFactory {
    private val filters = ProjectStructureTreeFilters()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(createProjectTreePanel(project), "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun createProjectTreePanel(project: Project): JPanel {
        val panel = JPanel(BorderLayout())
        val textArea = JTextArea()
        val projectPath = project.basePath ?: ""
        val projectTreeAction = ProjectTreeAction().apply { setFilters(filters) }
        textArea.text = projectTreeAction.buildProjectTree(File(projectPath))
        textArea.isEditable = false

        val regenerateButton = JButton("Generate again")
        regenerateButton.addActionListener {
            textArea.text = projectTreeAction.buildProjectTree(File(projectPath))
        }

        val copyButton = JButton("Copy")
        copyButton.addActionListener {
            val stringSelection = StringSelection(textArea.text)
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(stringSelection, null)
        }

        val filtersButton = JButton("Filters")
        filtersButton.addActionListener {
            showFiltersDialog()
            textArea.text = projectTreeAction.buildProjectTree(File(projectPath))
        }

        val buttonPanel = JPanel()
        buttonPanel.add(regenerateButton)
        buttonPanel.add(copyButton)
        buttonPanel.add(filtersButton)

        panel.add(JBScrollPane(textArea), BorderLayout.CENTER)
        panel.add(buttonPanel, BorderLayout.SOUTH)
        return panel
    }

    private fun showFiltersDialog() {
        val filesFirstCheckbox = JCheckBox("Show files first", filters.showFilesFirst)
        val options = arrayOf<Any>("OK", "Cancel")
        val result = JOptionPane.showOptionDialog(
            null,
            filesFirstCheckbox,
            "Filters",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        )

        if (result == 0) {
            filters.showFilesFirst = filesFirstCheckbox.isSelected
        }
    }
}
