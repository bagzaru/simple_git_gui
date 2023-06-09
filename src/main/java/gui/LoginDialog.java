package gui;

import jgitmanager.JGitManagerImprv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginDialog extends JDialog {
    private JOptionPane ConflictBox;
    private String Id;
    private String Token;
    private JTextField inputLoginId;
    private JTextField inputLoginToken;
    private JButton loginButton;

    public LoginDialog() {
        this.setTitle("LOGIN");
        this.setLayout(new GridLayout(3,2));
        ConflictBox =new JOptionPane();

        try {
            Id = JGitManagerImprv.getID();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        try{
            Token=JGitManagerImprv.getAccessToken();
        }catch (Exception e){
            System.out.println(e.toString());
        }

        JLabel IdLabel = new JLabel("ID",JLabel.CENTER);
        inputLoginId = new JTextField(Id);
        JLabel TokenLabel = new JLabel("TOKEN",JLabel.CENTER);
        inputLoginToken = new JTextField(Token);
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
                try {
                    JGitManagerImprv.setIdToken(loginID,token);
                } catch (IOException ex) {
                    ConflictBox.showMessageDialog(null,ex.toString());
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        this.add(loginButton);
        this.setSize(300,200);
        this.setModal(true);
        this.setVisible(true);

    }
}
