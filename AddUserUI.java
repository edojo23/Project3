import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AddUserUI {
    public JFrame view;

    public JButton btnAdd = new JButton("Add");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtUsername = new JTextField(20);
    public JTextField txtPassword = new JTextField(20);
    public JTextField txtFullName = new JTextField(20);
    public JTextField txtUserType = new JTextField(20);
    public JTextField txtCustomerID = new JTextField(20);


    public AddUserUI()   {
        this.view = new JFrame();

        view.setTitle("Add User");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("Username "));
        line1.add(txtUsername);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Password "));
        line2.add(txtPassword);
        view.getContentPane().add(line2);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("FullName "));
        line4.add(txtFullName);
        view.getContentPane().add(line4);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("UserType "));
        line3.add(txtUserType);
        view.getContentPane().add(line3);

        JPanel line5 = new JPanel(new FlowLayout());
        line5.add(new JLabel("CustomerID "));
        line5.add(txtCustomerID);
        view.getContentPane().add(line5);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAdd);
        panelButtons.add(btnCancel);
        view.getContentPane().add(panelButtons);

        btnAdd.addActionListener(new AddUserUI.AddButtonListerner());

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                view.dispose();
            }
        });
    }

    public void run() {
        view.setVisible(true);
    }

    class AddButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel user = new UserModel();

            String username = txtUsername.getText();

            if (username.length() == 0) {
                JOptionPane.showMessageDialog(null, "Username cannot be null!");
                return;
            }

            user.mUsername = username;

            String password = txtPassword.getText();
            if (password.length() == 0) {
                JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                return;
            }

            user.mPassword = password;

            String fullname = txtFullName.getText();
            if (fullname.length() == 0) {
                JOptionPane.showMessageDialog(null, "FullName cannot be null!");
                return;
            }

            user.mFullname = fullname;

            String usertype = txtUserType.getText();
            if (usertype.length() == 0) {
                JOptionPane.showMessageDialog(null, "UserType cannot be null!");
                return;
            }

            try {
                user.mUserType = Integer.parseInt(usertype);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "UserType is invalid!");
                return;
            }

            String customerId = txtCustomerID.getText();
            if (customerId.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!");
                return;
            }

            try {
                user.mCustomerID = Integer.parseInt(customerId);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }

            try {
                Gson gson = new Gson();
                Socket link = new Socket("localhost", 1024);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.PUT_USER;
                msg.data = gson.toJson(user);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class); // receive from Server

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "USER is NOT saved successfully!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "USER is SAVED successfully!");
                    view.dispose();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}