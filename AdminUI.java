import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminUI {
    public JFrame view;

    public JButton btnSystemConfig = new JButton("Setup System Config");
    public JButton btnAddUser = new JButton("Add User");
    public JButton btnDeleteUser = new JButton("Delete User");
    public JButton btnManageUserType = new JButton("Change User Type");
    public JButton btnManageUserInfo = new JButton("Change User Info");

    UserModel user;

    public AdminUI(UserModel u) {
        this.view = new JFrame();
        user = u;

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Admin View");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System");

        title.setFont (title.getFont ().deriveFont (24.0f));
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnSystemConfig);
        panelButtons.add(btnAddUser);
        panelButtons.add(btnDeleteUser);
        panelButtons.add(btnManageUserType);
        panelButtons.add(btnManageUserInfo);

        view.getContentPane().add(panelButtons);

        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddUserUI ui = new AddUserUI();
                ui.run();
            }
        } );

        btnManageUserType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageUserTypeUI ui = new ManageUserTypeUI();
                ui.run();
            }
        } );

        btnManageUserInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageUserInfoUI ui = new ManageUserInfoUI(user);
                ui.run();
            }
        } );
    }
}

