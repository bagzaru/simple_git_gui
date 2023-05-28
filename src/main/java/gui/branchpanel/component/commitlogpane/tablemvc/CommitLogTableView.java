package gui.branchpanel.component.commitlogpane.tablemvc;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class CommitLogTableView extends JTable {
    //EventListner 추가하기
    //모델 연결하기
    CommitLogTableModel model;

    public CommitLogTableView(CommitLogTableModel model) {
        //table UI의 속성을 지정합니다.
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);
        setShowVerticalLines(false);

        //모델을 등록하여 줍니다.
        this.model = model;
        setModel(model);

        //cell size를 지정해줍니다.

        //각 행의 길이를 정합니다.
        setColumnWidth(0, 8);
        setColumnWidth(1, 50);
        setColumnWidth(2, 20);
        setColumnWidth(3, 16);

    }

    public void setColumnWidth(int column, int textLength) {
        int width = 14;
        TableColumn tableColumn = getColumnModel().getColumn(column);
        String temp = "";
        for (int i = 0; i < textLength; i++) temp = temp + "A";
        // use the preferred width of the header..
        JLabel label = new JLabel(temp);
        Dimension preferred = label.getPreferredSize();
        // altered 10->14 as per camickr comment.
        width = (int) preferred.getWidth() + 14;

        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }
}
