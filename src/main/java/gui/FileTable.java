package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class FileTable extends JScrollPane {
    FileTable(FileDetail fileDetail) {
        GitGUI.table = new JTable();
        GitGUI.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GitGUI.table.setAutoCreateRowSorter(true);
        GitGUI.table.setShowVerticalLines(false);

        GitGUI.listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int row = GitGUI.table.getSelectionModel().getLeadSelectionIndex();
                fileDetail.setFileDetails( ((FileTableModel)GitGUI.table.getModel()).getFile(row) );
            }
        };
        GitGUI.table.getSelectionModel().addListSelectionListener(GitGUI.listSelectionListener);

        this.setViewportView(GitGUI.table);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight()/2));
    }
}