package gui.filemanager.component;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import file.SelectedFile;
import gui.filemanager.FileManager;
import gui.filemanager.PanelRefreshUtil;
import gui.filemanager.model.StagedFileTableModel;
import jgitmanager.JGitManager;
import org.eclipse.jgit.api.errors.GitAPIException;

public class StagedFileList extends JScrollPane {
    private static StagedFileList instance = null;

    private JTable table;
    private StagedFileTableModel stagedFileTableModel;
    boolean cellSizesSet = false;

    public static File selectedStagedFile;
    public static JPanel confirmPanel;

    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    public StagedFileList() {
        table = new JTable();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);

        //더블클릭으로 Restore를 실행
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        //1. selectedFile을 갱신해줍니다.
                        int row = table.getSelectionModel().getLeadSelectionIndex();
                        selectedStagedFile=((StagedFileTableModel) table.getModel()).getFile(row);
                        //2. 확인창을 띄웁니다.
                        if(selectedStagedFile!=null){
                            if (confirmPanel == null) {
                            confirmPanel = new JPanel(new BorderLayout(3, 3));
                            confirmPanel.add(new JLabel("Do you really want to unstage this file?"), BorderLayout.WEST);
                        }
                            int result = JOptionPane.showConfirmDialog(FileManager.gui,confirmPanel,
                                    "Unstage file", JOptionPane.OK_CANCEL_OPTION);
                            if (result == JOptionPane.OK_OPTION) {
                                try {
                                    //해당 파일을 unstage
                                    JGitManager.gitRestoreStaged(selectedStagedFile);
                                    PanelRefreshUtil.refreshAll();
                                } catch (Throwable t) {
                                    System.out.println("error ocurred");
                                }
                            }
                        }

                    } catch (Exception exception) {
                        System.out.println("Something error occurred while mouseDoubleClick Event:" + exception.getMessage());
                    }

                }
            }
        });

        this.setViewportView(table);
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight()/2));
    }

    public static StagedFileList getInstance() {
        if(instance == null) {
            instance = new StagedFileList();
        }
        return instance;
    }

    public void setStagedFileTableData() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                File[] files = getStagedFile();

                if (stagedFileTableModel == null) {
                    stagedFileTableModel = new StagedFileTableModel();
                    table.setModel(stagedFileTableModel);
                }

                stagedFileTableModel.setFiles(files);
                if (!cellSizesSet) {
                    table.setRowHeight(23);

                    setColumnWidth(0, 100);
                    table.getColumnModel().getColumn(0).setMaxWidth(200);
                    setColumnWidth(1, table.getRowHeight() * 3 + 5);

                    cellSizesSet = true;
                }

                FileManager.gui.repaint();
            }
        });
    }

    private File[] getStagedFile() {
        Set<String> stagedFileSet;
        File[] stagedFiles;
        File selectedFile = SelectedFile.getInstance().getFile();

        try {
            if(selectedFile != null) {
                stagedFileSet = JGitManager.gitStagedList(selectedFile);
            } else {
                stagedFileSet = new HashSet<>();
            }
        } catch(NullPointerException n) {
            stagedFileSet = new HashSet<>();
        } catch(IOException | GitAPIException e) {
            stagedFileSet = new HashSet<>();
        }

        stagedFiles = new File[stagedFileSet.size()];

        int i = 0;
        for(String filePath : stagedFileSet) {
            //filePath가 repo 기준 상대경로이므로, 다시확인
            String dotGit = JGitManager.getRepositoryAbsolutePath(selectedFile);
            File repo = (new File(dotGit)).getParentFile();
            stagedFiles[i] = new File(repo,filePath);
            i++;
        }

        return stagedFiles;
    }

    private void setColumnWidth(int column, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        if (width < 0) {
            // use the preferred width of the header..
            JLabel label = new JLabel( (String)tableColumn.getHeaderValue() );
            Dimension preferred = label.getPreferredSize();
            // altered 10->14 as per camickr comment.
            width = (int)preferred.getWidth()+14;
        }
        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }
}