package jgitmanager;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class JGitTester {
    public static String testPathDotGit = "D:\\open_prj1\\git_test\\.git";
    public static String testPath = "D:\\open_prj1\\git_test";
    public static String testFile = testPath+"\\b.txt";

    public static void main(String args[]) {
        System.out.println("Hello World from JGitTester");
        JGitTester tester = new JGitTester();
        JGitManager jGitManager = new JGitManager();
        //tester.gitInitTest(jGitManager);
        //tester.gitRestoreStagedTest(jGitManager);
        //tester.gitRestoreTest(jGitManager);
        //tester.gitMvTest(jGitManager);
        tester.gitTestMinJp(jGitManager);
    }

    public void gitInitTest(JGitManager jGitManager) {
        File tempPath;
        try {
            tempPath = jGitManager.createTempPath();
            jGitManager.gitInit(tempPath);
        } catch (IOException e) {
            System.out.println("Cannot create temp path: " + e.toString());
            return;
        }catch (GitAPIException e){
            System.out.println("Cannot git init temp path: " + e.toString());
        }
    }

    public void gitRestoreTest(JGitManager jGitManager){
        try {
            //jGitManager.gitRestore(new File(testPath+"\\test.txt"),new File(testPathDotGit));
            jGitManager.gitRestore(new File(testFile),new File(testPathDotGit));

        } catch (Exception e) {
            System.out.println("An error ocurred: " + e.toString());
        }
    }

    public void gitRestoreStagedTest(JGitManager jGitManager){
        try {
            //git restore --staged
            jGitManager.gitRestoreStaged(new File(testFile),new File(testPathDotGit));

        } catch (Exception e) {
            System.out.println("An error ocurred: " + e.toString());
        }
    }

    public void gitMvTest(JGitManager jGitManager){
        try {
            //git restore --staged
            //jGitManager.gitMv(new File(testPath+"\\test.txt"),"k.txt",new File(testPathDotGit));
            jGitManager.gitMv(new File(testFile),"b.txt",new File(testPathDotGit));

        } catch (Exception e) {
            System.out.println("An error ocurred: " + e.toString());
        }
    }

    /*
     * ------------------------------------------------------------------------------
     * ------------------------------min-jp_part-------------------------------------
     * ------------------------------------------------------------------------------
     * */

    public void gitTestMinJp(JGitManager jGitManager) {
        // gitAdd
        //jGitManager.gitAdd(new File(testFile),new File(testPathDotGit));

        // gitDoCommit
        //jGitManager.gitDoCommit(new File(testFile),new File(testPathDotGit),"cm_test1");

        // gitStagedList
        //Set<String> stagedFiles = jGitManager.gitStagedList(new File(testPathDotGit));
        //System.out.println("Staged file: " + stagedFiles);

        // gitCheckFileStatus
        int statusNum;
        statusNum = jGitManager.gitCheckFileStatus(new File(testFile),new File(testPathDotGit));
        System.out.println(statusNum);

        // findGitRepository
        //int isGit;
        //isGit = jGitManager.findGitRepository(new File(testPath));
        //System.out.println(isGit);
    }
}
