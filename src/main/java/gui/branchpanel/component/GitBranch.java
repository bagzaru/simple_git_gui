package gui.branchpanel.component;

import gui.branchpanel.component.branch.GitBranchMenu;

import javax.swing.*;
import java.awt.*;

public class GitBranch extends JPanel {
    public GitBranch() {
        add(GitBranchMenu.getInstance(), BorderLayout.NORTH);

    }
}
