package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Set;

import file.SelectedFile;

public class StagedFileList extends JScrollPane {
    Set<String> stagedFileSet;
    File[] files;
    JTable table;

    StagedFileList() {
        /*
        table = new JTable();
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);


        //stagedFileSet = gitStagedList(SelectedFile.getInstance().getFile());
        files = new File[stagedFileSet.size()];

        int i = 0;
        for(String filePath : stagedFileSet) {
            files[i] = new File(filePath);
            i++;
        }
        */
        setPreferredSize(new Dimension(100, 150));
    }
}