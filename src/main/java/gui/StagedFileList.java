package gui;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import file.SelectedFile;
import jgitmanager.FileStatus;
import jgitmanager.JGitManager;
import org.eclipse.jgit.api.errors.GitAPIException;

public class StagedFileList extends JScrollPane {
    private static StagedFileList instance = null;

    private JTable table;
    private StagedFileTableModel stagedFileTableModel;
    boolean cellSizesSet = false;

    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    public StagedFileList() {
        table = new JTable();

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);

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
                    table.setRowHeight(30);
                    setColumnWidth(0,100);
                    setColumnWidth(1,100);
                    //setColumnWidth(2,60);

                    cellSizesSet = true;
                }

                GitGUI.gui.repaint();
            }
        });
    }

    private File[] getStagedFile() {
        Set<String> stagedFileSet;
        File[] stagedFiles;

        try {
            File selectedFile = SelectedFile.getInstance().getFile();
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
            stagedFiles[i] = new File(filePath);
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
            "File",
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
                return gitStatus(file);
            case 2:
                return file.getPath();
            default:
                System.err.println("Logic Error");
        }
        return "";
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Class<?> getColumnClass(int column) {
        return String.class;
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

    public String gitStatus(File file) {
        FileStatus fileStatus;

        try {
            if(JGitManager.findGitRepository(file) == 1) {
                fileStatus = JGitManager.gitCheckFileStatus(file);
            }
            else {
                fileStatus = FileStatus.UNTRACKED;
            }
            return "Staged";
            /*
            switch (fileStatus) {
                case FOLDER:
                    return "";
                case UNTRACKED:
                    return "Untracked";
                case MODIFIED:
                    return "Modified";
                case STAGED_MODIFIED:
                    return "Staged & Modified";
                case DELETED:
                    return "Deleted";
                case STAGED:
                    return "Staged";
                case UNMODIFIED:
                    return "Unmodified(committed)";
                default:
                    return "UNKNOWN FILE";
            }
            */
        } catch(IOException | GitAPIException e) {
        };

        return "UNKNOWN FILE";
    }
}