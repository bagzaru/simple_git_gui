package jgitmanager;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import jgitmanager.JGitManager.FileStatus;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class JGitTester {
    //test용 객체입니다.
    //public static String testPathDotGit = "C:\\Users\\BAEKSE~1\\AppData\\Local\\Temp\\TestGitRepo7066676561043817265\\.git";
    //public static String testPath = "C:\\Users\\BAEKSE~1\\AppData\\Local\\Temp\\TestGitRepo7066676561043817265";
    //public static String testFile = testPath+"\\src\\src\\a.txt";
    //public static String testPath = "C:\\oss_test\\a\\b";
    //public static String testFile = testPath+"\\k.txt";
// >>>>>>> Git_min-jp_part
      public static String testPathDotGit = "D:\\open_prj1\\git_test\\.git";
      public static String testPath = "D:\\open_prj1\\git_test";
      public static String testFile = testPath+"\\b.txt";
    static JGitManager jGitManager;
    public static void main(String args[]) {
        System.out.println("Hello World from JGitTester");
        JGitTester tester = new JGitTester();
        jGitManager = new JGitManager();
        //tester.gitInitTest(jGitManager);
        //tester.gitRepoOpenTest();
        //tester.gitRmTest(jGitManager);
        //tester.gitRestoreStagedTest(jGitManager);
        //tester.gitRestoreTest(jGitManager);
        //tester.gitMvTest(jGitManager);
        tester.gitTestMinJp(jGitManager);
        //tester.gitRmCachedTest(jGitManager);
    }

    public void gitRepoOpenTest(){
        try{
            try(Repository repo = jGitManager.openRepositoryFromFile(new File(testPath))){
                if(repo==null){
                    System.out.println("repo.dir: null");
                }
                else{
                    System.out.println("repo.dir: "+repo.getDirectory().getAbsolutePath());
                }
            }

        }catch(IOException e){
            System.out.println("왜!!!!!");
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void gitInitTest() {
        File tempPath;
        try {
            tempPath = jGitManager.createTempPath();
            jGitManager.gitInit(new File(testPath));
        } catch (IOException e) {
            System.out.println("Cannot create temp path: " + e.toString());
            return;
        }catch (GitAPIException e){
            System.out.println("Cannot git init temp path: " + e.toString());
        }
    }

    public void gitRestoreTest(){
        try {
            //jGitManager.gitRestore(new File(testPath+"\\test.txt"),new File(testPathDotGit));
            jGitManager.gitRestore(new File(testFile));

        } catch (Exception e) {
            System.out.println("An error ocurred: " + e.toString());
        }
    }

    public void gitRestoreStagedTest(){
        try {
            //git restore --staged
            jGitManager.gitRestoreStaged(new File(testFile));

        } catch (Exception e) {
            System.out.println("An error ocurred: " + e.toString());
        }
    }

    public void gitMvTest(){
        try {
            //git restore --staged
            //jGitManager.gitMv(new File(testPath+"\\test.txt"),"k.txt",new File(testPathDotGit));
            jGitManager.gitMv(new File(testFile),"b.txt");

        } catch (Exception e) {
            System.out.println("An error ocurred: " + e.toString());
        }
    }

    public void gitRmTest() {
        try {
            //git restore --staged
            //jGitManager.gitMv(new File(testPath+"\\test.txt"),"k.txt",new File(testPathDotGit));
            jGitManager.gitRm(new File(testFile));

        } catch (Exception e) {
            System.out.println("An error ocurred: " + e.toString());
        }
    }

    public void gitRmCachedTest(JGitManager jGitManager) {
        try {
            //git restore --staged
            //jGitManager.gitMv(new File(testPath+"\\test.txt"),"k.txt",new File(testPathDotGit));
            jGitManager.gitRmCached(new File(testFile));

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
    	try {
    		FileStatus statusNum;
            statusNum = jGitManager.gitCheckFileStatus(new File(testFile));
            System.out.println(statusNum);
    	} catch (Exception e) {
    		System.out.println("An error ocurred: " + e.toString());
    	}
        

        // findGitRepository
        //int isGit;
        //isGit = jGitManager.findGitRepository(new File(testPath));
        //System.out.println(isGit);
    }
}
