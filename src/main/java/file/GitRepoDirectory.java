package file;

import java.io.File;

public class GitRepoDirectory {
    private static GitRepoDirectory instance = null;
    private File repoDirectory;

    GitRepoDirectory() {
    }

    public static GitRepoDirectory getInstance() {
        if(instance == null) {
            instance = new GitRepoDirectory();
        }
        return instance;
    }

    public File getRepoDirectory() {
        return repoDirectory;
    }

    public void setRepoDirectory() {
        //repoDirectory = (function name)
    }
}