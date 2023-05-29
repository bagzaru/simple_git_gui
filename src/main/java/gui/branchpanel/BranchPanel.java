package gui.branchpanel;

import file.GitBranchData;

import javax.swing.*;

public class BranchPanel extends JPanel {
    /** Branch 모드 공유 데이터 객체*/
    private GitBranchData gitBranchData = new GitBranchData(); //컴포넌트 생성할 때 인자로 넣어서 의존성 주입

    public BranchPanel() {
    }
}