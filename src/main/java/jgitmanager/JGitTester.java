package jgitmanager;

import java.io.File;
import java.io.IOException;

public class JGitTester {
    public static String testPathDotGit = "C:\\Users\\BAEKSE~1\\AppData\\Local\\Temp\\TestGitRepo7066676561043817265\\.git";
    public static String testPath = "C:\\Users\\BAEKSE~1\\AppData\\Local\\Temp\\TestGitRepo7066676561043817265";

    public static void main(String args[]) {
        System.out.println("Hello World from JGitTester");
        JGitTester tester = new JGitTester();
        JGitManager jGitManager = new JGitManager();
        //tester.gitInitTest(jGitManager);
        tester.gitRestoreStagedTest(jGitManager);
    }


    public void gitInitTest(JGitManager jGitManager) {
        File tempPath;
        try {
            tempPath = jGitManager.createTempPath();
        } catch (IOException e) {
            System.out.println("Cannot create temp path: " + e.toString());
            return;
        }
        jGitManager.gitInit(tempPath);
    }

    public void gitRestoreStagedTest(JGitManager jGitManager){
        try {
            //git restore --staged
            jGitManager.gitRestoreStaged(new File(testPath+"\\test.txt"),new File(testPathDotGit));

        } catch (Exception e) {
            System.out.println("An error ocurred: " + e.toString());
        }

    }
}
