package jgitmanager.testjgit;

import jgitmanager.JGitManager;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;

public class JGitTester {
    //test용 객체입니다.
    public static String testPath = "C:\\oss_test";
    public static String testFile = testPath + "\\asdf.txt";
    // >>>>>>> Git_min-jp_part
    //public static String testPathDotGit = "D:\\open_prj1\\git_test\\.git";
    //public static String testPath = "D:\\open_prj1\\git_test";
    //public static String testFile = testPath+"\\b.txt";
    static JGitManager jGitManager;

    public static void main(String args[]) {
        System.out.println("Hello World from JGitTester");
        JGitTester tester = new JGitTester();
        //tester.gitTestMinJp(jGitManager);
        tester.gitTestBagzaru();
    }

    public void gitTestBagzaru() {
        String test = "/Users/bagzaru/IdeaProjects/simple_git_gui";
        File file = new File(test);
        System.out.println("isValid: "+file.getAbsolutePath()+", "+file.exists());
        try{
            Iterable<RevCommit> logs = JGitManager.gitLog(file);
            for(RevCommit r : logs){
                System.out.println(r.getFullMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
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
//    	try {
//    		FileStatus statusNum;
//            statusNum = jGitManager.gitCheckFileStatus(new File(testFile));
//            System.out.println(statusNum);
//    	} catch (Exception e) {
//    		System.out.println("An error ocurred: " + e.toString());
//    	}


        // findGitRepository
        //int isGit;
        //isGit = jGitManager.findGitRepository(new File(testPath));
        //System.out.println(isGit);
    }


}
