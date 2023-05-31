package gui;

import javax.swing.*;

public class LoginDialog extends JDialog {
    private JTextField inputLoginId;
    private JTextField inputLoginToken;
    private JButton loginButton;

    public LoginDialog() {
        JPanel loginPanel = new JPanel();
        JLabel IdLabel = new JLabel("ID");
        inputLoginId = new JTextField("ID를 입력하세요");
        JLabel TokenLabel = new JLabel("TOKEN");
        inputLoginToken = new JTextField("TOKEN을 입력하세요");
        loginButton = new JButton("Log In");

        loginButton.addActionListener(e -> {
            dispose();
        });
        loginPanel.add(IdLabel);
        loginPanel.add(inputLoginId);
        loginPanel.add(TokenLabel);
        loginPanel.add(inputLoginToken);

        loginPanel.add(loginButton);

    }
}
