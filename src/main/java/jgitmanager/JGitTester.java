package jgitmanager;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class JGitTester {
    //test용 객체입니다.
    public static String testPathDotGit = "C:\\Users\\BAEKSE~1\\AppData\\Local\\Temp\\TestGitRepo7066676561043817265\\.git";
    //public static String testPath = "C:\\Users\\BAEKSE~1\\AppData\\Local\\Temp\\TestGitRepo7066676561043817265";
    //public static String testFile = testPath+"\\src\\src\\a.txt";
// >>>>>>> Git_min-jp_part
//    public static String testPathDotGit = "D:\\open_prj1\\git_test\\.git";
//    public static String testPath = "D:\\open_prj1\\git_test";
//    public static String testFile = testPath+"\\b.txt";
    static JGitManager jGitManager;
    public static void main(String args[]) {
        System.out.println("Hello World from JGitTester");
        JGitTester tester = new JGitTester();
        jGitManager = new JGitManager();
        //tester.gitInitTest(jGitManager);
        tester.gitRepoOpenTest();
        //tester.gitRmTest(jGitManager);
        //tester.gitRestoreStagedTest(jGitManager);
        //tester.gitRestoreTest(jGitManager);
        //tester.gitMvTest(jGitManager);
        //tester.gitTestMinJp(jGitManager);
    }

    public static String testPath = "C:\\oss_test\\a\\b";
    public static String testFile = testPath+"\\k.txt";
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

    public void gitRmTest(JGitManager jGitManager) {
        try {
            //git restore --staged
            //jGitManager.gitMv(new File(testPath+"\\test.txt"),"k.txt",new File(testPathDotGit));
            jGitManager.gitRm(new File(testFile), new File(testPathDotGit));

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
