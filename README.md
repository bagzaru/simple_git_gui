# Simple-git-gui

![./img/v1.1/1_main.png](./img/v1.1/1_main.png)

Simple-git-gui is a GUI file browser that allows you to explore and manage files using Git integration.

# Key Features

1. File Exploration: Simple-git-gui provides the ability to explore files and directories managed by Git. You can navigate through files and directories by clicking on them in the left panel.

2. File Management with Git: You can use Simple-git-gui to manage files through Git. The status of files can be easily identified using icons. You can create a directory as a Git repository, stage or unstage changes, create commits, delete files, and rename files.

3. Branch Management: Simple-git-gui offers features to manage Git branches. You can add, delete, and rename branches, as well as perform merge operations between branches. Commit logs and graphs provide a visual representation of branch relationships.

4. Commit Information: Simple-git-gui allows you to view information about commits. You can see commit messages, authors, time, and a detailed list of changed files and their modifications.

5. Login Feature: Users can register and store their ID and access tokens. This information can be used for cloning repositories.

# Supported Platforms

- Windows 10 x64 or higher

# Requirements

- Java Development Kit (JDK) 17 installed
- Java Runtime Environment (JRE) version 17 installed
- Git installed

# How to Run

[https://github.com/bagzaru/simple_git_gui/releases/latest](https://github.com/bagzaru/simple_git_gui/releases/latest)

Download the simple_git_gui-2.0-all.jar file from the above link.

Navigate to the location of the file in the command prompt.

```bash
java -jar simple_git_gui-2.0-all.jar

```

Run the simple_git_gui-2.0-all.jar file using the above command.
(It needs to be executed with Java version 17.)

When performing a clone or login, a credentials.txt file will be created to store the ID/Access Token information.

# Program Description

## Mode Selection

{Image}

### Top Toolbar
- Choose between File Manager, Branch Manager, or Login.
- If the current directory is not a Git repository, the Branch Manager button is disabled.

## File Exploration Guide (File Manager)

![./img/v1.1/9_main.png](./img/v1.1/9_main.png)

### Left Panel

- Use the left panel to navigate through files and directories by clicking on them.

### Top Center Panel (Directory)

- In the Current Directory, you can explore files and folders within the current folder.
- The Git status of each file can be identified using icons.

### Center Toolbar

- Perform file execution, creation, and deletion.
- Click the StageAll button to stage all changes in the current Git repository (git add .).
- Click the Refresh button to refresh any external changes that occurred outside the file browser.

### Bottom Center Panel (Staged List)

- The Staged List displays the currently staged files in Git.

### Right Panel

- If it is not a Git repository, you can make the selected directory a Git repository.

### Right Panel - File Selection

- If it is a Git repository, you can manage the selected file using Git commands.

## File Management with Git Guide (File Manager)

You can select a file and manage it using Git.

### File Status - Directory

![./img/v1.1/2_directory.png](./img/v1.1/2_directory.png)

In the top center table, you can see the status of files.
You can execute commands corresponding to each status using the panel on the top right.

- Committed: Indicates that the file is in the same state as the last commit.
- Modified: Indicates that the file differs from the content managed by Git.
- Staged: Indicates that the file's changes have been staged, and the content of the staged file is the same as the local file.
- Untracked: Indicates that the file is not managed by Git.

### File Status - Staged List

![./img/v1.1/3_StagedList.png](./img/v1.1/3_StagedList.png)

The bottom center table represents the files in the staged state.
You can unstage a file in the staged state by double-clicking on it.

- Added: The file has been newly added to Git and staged.
- Staged: The file has been modified in a previous commit and staged.
- Deleted: The file has been deleted using git rm or similar commands and is in a staged state.

### Version Control with Git

![./img/v1.1/4_Untracked.png](./img/v1.1/4_Untracked.png)

[Untracked]

- Add: Adds the selected file to the Staged List.

![./img/v1.1/5_Modified.png](./img/v1.1/5_Modified.png)

[Modified]

- Add: Adds the selected file to the Staged List.
- Undo: Reverts the selected file to the state of the last commit.

![./img/v1.1/6_Staged.png](./img/v1.1/6_Staged.png)

[Staged]

- Unstage: Removes the selected file from the Staged List (file content remains unchanged).

![./img/v1.1/7_Committed.png](./img/v1.1/7_Committed.png)

[Committed]

- Delete: Deletes the file and reflects the change in Git.
- Untrack: Changes the file to an untracked mode.
- Rename: Renames the file and reflects the change in Git.

### Commit

![./img/v1.1/8_commit_msg.png](./img/v1.1/8_commit_msg.png)

- Commit: Commits the current content in the Staged List to Git.

### Clone

{Image}

- Clones the repository using the stored ID, Access Token, and the entered URL to the current directory.
- Cloning may take some time, and once it is completed, a message window will be displayed.

## Branch / Commit Management Guide (Branch Manager)

{Image}

### Top Left Buttons

{Image}

- [+] Add a new branch.
- [-] Delete the selected branch.
- [R] Rename the selected branch.
- [M] Merge the selected branch into the currently checkout branch. If conflicts occur, an error message will be displayed.

### Left Center Panel

{Image}

- Displays all branches in the Git repository.
- The currently checkout branch is marked with a checkmark on the left.
- When a branch is clicked once, its log is displayed in the top right panel.
- Double-clicking a branch checks out that branch.

### Bottom Left Panel

{Image}

- Displays the name of the currently checkout branch.
- Displays the name of the branch clicked in the branch list (left center panel).

### Top Right Panel

{Image}

- Displays a graph of commit logs.
- Displays commit messages, authors, and checksums.
- Clicking a commit displays detailed

information about the commit in the bottom center panel.

### Bottom Center Panel

{Image}

Top Panel:
- Displays information about the clicked commit from the top right panel.
- Shows the checksum, commit time, commit message, author's name, and author's email.

Bottom Panel:
- It displays the list of files that have been changed in the selected commit.
- When a file is selected, it displays the changes made to that file.

### Bottom Right Panel

{Image}

- Displays the changes made to the selected file.

## Login Guide

{Image}

- Displays the pre-saved ID and token values.
- If no information has been previously registered, it displays an empty string.
- Clicking the "Log In" button saves the entered (or modified) ID and token values.

# Code of Conduct

Refer to the file:

[CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md)

# Contribution Guidelines

If you are interested in contributing, please send an email to <bagzaru3690@gmail.com>.

# License

[MIT License](./LICENSE.txt)

# Open Source Credits

Refer to the file:

[OpensourceCredit.txt](./OpensourceCredit.txt)