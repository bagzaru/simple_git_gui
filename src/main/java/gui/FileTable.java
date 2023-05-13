package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import file.FileTableModel;
import file.SelectedFile;

public class FileTable extends JScrollPane {
    Table table;
    SelectedFile selectedFile;

    public FileTable() {
        table = Table.getInstance();
        selectedFile = SelectedFile.getInstance();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);

        GitGUI.listSelectionListener = new ListSelectionListener() {
            //중앙 파일 테이블에서 파일 클릭 시 발생하는 이벤트
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int row = table.getSelectionModel().getLeadSelectionIndex();
                selectedFile.setFile(((FileTableModel)table.getModel()).getFile(row));

                //우측 git 패널을 업데이트합니다.
                GitFilePanel.getInstance().UpdatePanel();
            }
        };
        table.getSelectionModel().addListSelectionListener(GitGUI.listSelectionListener);

        this.setViewportView(table);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight()/2));
    }
}

class Table extends JTable {
    private static Table instance = null;

    public Table() {
        super();
    }

    public static Table getInstance() {
        if(instance == null) {
            instance = new Table();
        }
        return instance;
    }
}