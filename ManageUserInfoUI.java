import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ManageUserInfoUI {
    public JFrame view;

    public JButton btnSave = new JButton("Save");

    public JTextField txtFullName = new JTextField(20);
    public JTextField txtPassword = new JTextField(20);
    public UserModel user;


    public ManageUserInfoUI(UserModel userIn) {
        this.view = new JFrame();
        user = userIn;

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Update User Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Full Name "));
        line2.add(txtFullName);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Password "));
        line3.add(txtPassword);
        view.getContentPane().add(line3);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnSave);
        view.getContentPane().add(panelButtons);

        btnSave.addActionListener(new ManageUserInfoUI.SaveButtonListener());

    }

    public void run() {
        view.setVisible(true);
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Gson gson = new Gson();

            String name = txtFullName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty!");
                return;
            }

            user.mFullname = name;

            String password = txtPassword.getText();
            if (password.length() == 0) {
                JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                return;
            }

            user.mPassword = password;
            // all user info is ready! Send to Server!

            try {
                Socket link = new Socket("localhost", 1024);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.PUT_USER;
                msg.data = gson.toJson(user);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class); // receive from Server

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "User is NOT saved successfully!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "User is SAVED successfully!");
                    view.dispose();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}