package gui.branchpanel.component.commitlogpane.tablemvc;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class CommitLogTableView extends JTable {
    CommitLogTableModel model;

    public CommitLogTableView(CommitLogTableModel model) {
        //table UI의 속성을 지정합니다.
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);
        setShowVerticalLines(false);

        //모델을 등록하여 줍니다.
        this.model = model;
        setModel(model);

        //각 행의 길이를 정합니다.
        setColumnWidth(0, 8);
        setColumnWidth(1, 24);
        setColumnWidth(2, 12);
        setColumnWidth(3, 16);

    }

    //Table의 열의 길이를 지정해 줍니다. 기기마다 글꼴이 달라 preferred를 이용합니다.
    //column: 길이를 지정할 열, textLength: 영어 텍스트 길이
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

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // 점 그리기
        g.setColor(Color.RED);
        g.fillRect(10, 10, 5, 5);

        // 선 그리기
        g.setColor(Color.BLUE);
        g.drawLine(20, 20, 50, 50);

        // 면 그리기
        g.setColor(Color.GREEN);
        g.fillRect(30, 30, 50, 50);
    }
}
