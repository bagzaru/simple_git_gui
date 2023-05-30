package jgitmanager;

import java.io.File;
import java.io.IOException;

public class JGitImprvTester {
    public static String testPath = "/Users/minjeong-in/민정인/open_prj/testPath";

    static JGitManagerImprv jGitManagerImprv;

    public static void main(String args[]) {
        System.out.println("Hello World from JGitTester");
        JGitImprvTester tester = new JGitImprvTester();

        tester.gitCloneTest2(testPath);
    }

    public void gitCloneTest(String filePath){
        try{
            // "min-jp" "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"
            jGitManagerImprv.gitClone(new File(filePath), "https://github.com/min-jp/testRepo.git");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitCloneTest2(String filePath){
        try{
            jGitManagerImprv.gitClone(new File(filePath+"2"), "https://github.com/min-jp/simple_git_gui.git");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
