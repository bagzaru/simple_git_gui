package gui.branchmanager.component.branch.GitBranchListMVC;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class GitBranchListTableView extends JTable {
    public GitBranchListTableView() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setShowVerticalLines(false);
    }

    public void setTableModel(GitBranchListTableModel model) {
        setModel(model);
        setColumnWidth(0, 20);
    }

    private void setColumnWidth(int column, int width) {
        TableColumn tableColumn = getColumnModel().getColumn(column);

        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }
}