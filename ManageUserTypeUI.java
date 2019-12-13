import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ManageUserTypeUI {
    public JFrame view;

    public JButton btnSave = new JButton("Save");

    public JTextField txtUsername = new JTextField(20);
    public JTextField txtUserType = new JTextField(20);

    public ManageUserTypeUI() {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Update User Type");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Username "));
        line2.add(txtUsername);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("UserType "));
        line3.add(txtUserType);
        view.getContentPane().add(line3);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnSave);
        view.getContentPane().add(panelButtons);

        btnSave.addActionListener(new ManageUserTypeUI.SaveButtonListener());
    }

    public void run() {
        view.setVisible(true);
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Gson gson = new Gson();
            UserModel user = new UserModel();

            String username = txtUsername.getText();
            if (username.length() == 0) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty!");
                return;
            }

            user.mUsername = username;

            // Get the user
            try {
                Socket link = new Socket("localhost", 1024);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_USER;
                msg.data = user.mUsername;
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class); // receive from Server

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "User NOT found!");
                }
                else {
                    user = gson.fromJson(msg.data, UserModel.class);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            String usertype = txtUserType.getText();
            if (usertype.length() == 0) {
                JOptionPane.showMessageDialog(null, "UserType cannot be empty!");
                return;
            }

            try {
                user.mUserType = Integer.parseInt(usertype);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "UserType is invalid!");
                return;
            }

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