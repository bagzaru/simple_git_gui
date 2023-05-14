package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import file.FileTableModel;
import file.SelectedFile;
import jgitmanager.FileStatus;
import jgitmanager.JGitManager;
import jgitmanager.JGitTester;
import jgitmanager.StagedFileStatus;
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
        table.setAutoCreateRowSorter(true);
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
                            int result = JOptionPane.showConfirmDialog(GitGUI.gui,confirmPanel,
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

                GitGUI.gui.repaint();
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

class StagedFileTableModel extends AbstractTableModel {
    private File[] stagedFiles;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private String[] columns = {
            "Staged File",
            "File Status",
            "Path"
    };

    public StagedFileTableModel() {
        this(new File[0]);
    }

    StagedFileTableModel(File[] files) {
        this.stagedFiles = files;
    }

    public Object getValueAt(int row, int column) {
        File file = stagedFiles[row];
        switch (column) {
            case 0:
                return fileSystemView.getSystemDisplayName(file);
            case 1:
                String gitStatusImagePath = getGitStatusImagePath(file);
                if (gitStatusImagePath != null) {
                    URL url = getClass().getResource(gitStatusImagePath);
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        int cellHeight = 23;
                        int cellWidth = cellHeight * 3;
                        Image image = icon.getImage();
                        Image scaledImage = image.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImage);
                        return scaledIcon;
                    }
                }
                return null;
            case 2:
                String path = "/";
                path += JGitManager.findGitRepositoryName(file);
                path += "/";
                path += JGitManager.findGitRepositoryRelativePath(file);
                return path;
            default:
                System.err.println("Logic Error");
        }
        return "";
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Class<?> getColumnClass(int column) {
        switch(column) {
            case 1:
                return ImageIcon.class;
            default:
                return String.class;
        }
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return stagedFiles.length;
    }

    public File getFile(int row) {
        return stagedFiles[row];
    }

    public void setFiles(File[] files) {
        this.stagedFiles = files;
        fireTableDataChanged();
    }

    public String getGitStatusImagePath(File file) {
        StagedFileStatus stagedFileStatus;
        String imagePath;

        try {
            stagedFileStatus = JGitManager.gitCheckStagedFileStatus(file);

            switch (stagedFileStatus) {
                case STAGED_MODIFIED:
                    return "/git_status_icons/Staged_Modified.png";
                case STAGED:
                    return "/git_status_icons/Staged.png";
                case REMOVED:
                    return "/git_status_icons/Removed.png";
                default:
                    imagePath = null;

            }
        } catch (IOException | GitAPIException e) {
            imagePath = null;
        }
        return imagePath;
    }
}