package jgitmanager;

import java.io.File;
import java.io.IOException;

public class JGitImprvTester {
    public static String testPath = "/Users/minjeong-in/민정인/open_prj/testPath";

    static JGitManagerImprv jGitManagerImprv;

    public static void main(String args[]) {
        System.out.println("Hello World from JGitTester");
        JGitImprvTester tester = new JGitImprvTester();

        tester.gitDeleteBranchTest(testPath);
    }

    public void gitCloneTest(String filePath){
        try{
            jGitManagerImprv.gitClone(new File(filePath), "https://github.com/min-jp/testRepo.git", "min-jp", "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");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitCreateBranchTest(String filePath){
        try{
            jGitManagerImprv.gitCreateBranch(new File(filePath), "branch1");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitDeleteBranchTest(String filePath){
        try{
            jGitManagerImprv.gitDeleteBranch(new File(filePath), "branch1");
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
