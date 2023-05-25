package gui.filepanel.component;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import gui.filepanel.PanelRefreshUtil;
import gui.filepanel.model.FileTableModel;
import file.SelectedFile;

public class FileTable extends JScrollPane {
    /** singleton instance variable */
    private static FileTable instance = null;

    /** singleton Lazy initialize */
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private SelectedFile selectedFile = SelectedFile.getInstance();

    /** instance variable */
    private JTable table;
    private FileTableModel fileTableModel;
    private ListSelectionListener listSelectionListener;
    private boolean cellSizesSet = false;
    private int rowIconPadding = 6;

    public FileTable() {
        table = new JTable();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);

        listSelectionListener = new ListSelectionListener() {
            //중앙 파일 테이블에서 파일 클릭 시 발생하는 이벤트
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int row = table.getSelectionModel().getLeadSelectionIndex();
                selectedFile.setFile(((FileTableModel) table.getModel()).getFile(row));

                //우측 git 패널을 업데이트합니다.
                PanelRefreshUtil.refreshGitFilePanel();
                PanelRefreshUtil.refreshGitRepoStatusPanel();
            }
        };

        //중앙 파일테이블의 더블클릭을 통해 다른 폴더로 이동을 구현하였습니다.
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        //1. selectedFile을 갱신해줍니다.
                        int row = table.getSelectionModel().getLeadSelectionIndex();
                        File selected = ((FileTableModel) table.getModel()).getFile(row);
                        selectedFile.setFile(selected);
                        //2. 좌측 Tree에서 내가 클릭한 파일을 찾습니다.
                        //  일단 찾으면 Tree쪽 EventListener에 의해 폴더가 이동됩니다.
                        int treeCount = PanelRefreshUtil.lastTreeNode.getChildCount();
                        for (int i = 0; i < treeCount; i++) {
                            TreeNode treeNode = PanelRefreshUtil.lastTreeNode.getChildAt(i);
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode;
                            if (((File) node.getUserObject()).getName().equals(selected.getName())) {
                                FileTree.getInstance().getTree().setExpandsSelectedPaths(true);
                                FileTree.getInstance().getTree().setSelectionPath(new TreePath(node.getPath()));
                                break;
                            }
                        }

                    } catch (Exception exception) {
                        System.out.println("Something error occurred while mouseDoubleClick Event:" + exception.getMessage());
                    }

                }
            }
        });


        table.getSelectionModel().addListSelectionListener(listSelectionListener);

        this.setViewportView(table);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int) d.getWidth(), (int) d.getHeight() / 2));
    }

    public static FileTable getInstance() {
        if(instance == null) {
            instance = new FileTable();
        }
        return instance;
    }

    public void setTableData(final File[] files) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (fileTableModel == null) {
                    fileTableModel = new FileTableModel();
                    table.setModel(fileTableModel);
                }
                table.getSelectionModel().removeListSelectionListener(listSelectionListener);
                fileTableModel.setFiles(files);
                table.getSelectionModel().addListSelectionListener(listSelectionListener);
                if (!cellSizesSet) {
                    Icon icon = fileSystemView.getSystemIcon(files[0]);

                    // size adjustment to better account for icons
                    table.setRowHeight(icon.getIconHeight() + rowIconPadding);

                    setColumnWidth(0, -1);
                    setColumnWidth(2, -1);
                    setColumnWidth(3, -1);
                    setColumnWidth(4, table.getRowHeight() * 3 + 5);

                    cellSizesSet = true;
                }
            }
        });
    }

    public void setColumnWidth(int column, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        if (width < 0) {
            // use the preferred width of the header..
            JLabel label = new JLabel((String) tableColumn.getHeaderValue());
            Dimension preferred = label.getPreferredSize();
            // altered 10->14 as per camickr comment.
            width = (int) preferred.getWidth() + 14;
        }
        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }
}