package gui.branchpanel;

import file.GitBranchData;
import gui.branchpanel.component.*;

import javax.swing.*;
import java.awt.*;

public class BranchPanel extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData = new GitBranchData(); //컴포넌트 생성할 때 인자로 넣어서 의존성 주입

    public BranchPanel() {
        setLayout(new BorderLayout(3, 3));

        JPanel status = new JPanel(new BorderLayout(3, 3));
        //status.add(new GitCommit(gitBranchData), BorderLayout.CENTER);
        status.add(new GitGraph(gitBranchData), BorderLayout.SOUTH);

        add(status, BorderLayout.CENTER);
        add(new GitBranch(gitBranchData), BorderLayout.WEST);
    }
}