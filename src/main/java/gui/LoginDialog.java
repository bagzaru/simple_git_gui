package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JTextField inputLoginId;
    private JTextField inputLoginToken;
    private JButton loginButton;

    public LoginDialog() {
        this.setTitle("LOGIN");
        this.setLayout(new GridLayout(3,2));

        JLabel IdLabel = new JLabel("ID");
        inputLoginId = new JTextField("ID를 입력하세요");
        JLabel TokenLabel = new JLabel("TOKEN");
        inputLoginToken = new JTextField("TOKEN을 입력하세요");
        loginButton = new JButton("Log In");
        this.add(IdLabel);
        this.add(inputLoginId);
        this.add(TokenLabel);
        this.add(inputLoginToken);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginID = inputLoginId.getText();
                String token = inputLoginToken.getText();
                dispose();
            }
        });

        this.add(loginButton);
        this.setSize(300,200);
        this.setModal(true);
        this.setVisible(true);

    }
}
