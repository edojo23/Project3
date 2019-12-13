import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CashierUI {
    public JFrame view;
    UserModel user;

    public JButton btnAddPurchase = new JButton("Add New Purchase");
    public JButton btnManagePurchase = new JButton("Manage Purchase");
    public JButton btnAddCustomer = new JButton("Add New Customer");
    public JButton btnManageCustomer = new JButton("Manage Customer");
    public JButton btnManageUserInfo = new JButton("Change User Info");

    public CashierUI(UserModel u) {
        this.view = new JFrame();
        user = u;

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Cashier View");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System");

        title.setFont (title.getFont ().deriveFont (24.0f));
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAddPurchase);
        panelButtons.add(btnManagePurchase);
        panelButtons.add(btnAddCustomer);
        panelButtons.add(btnManageCustomer);
        panelButtons.add(btnManageUserInfo);

        view.getContentPane().add(panelButtons);


        btnAddPurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddPurchaseUI ap = new AddPurchaseUI();
                ap.run();
            }
        });

        btnManagePurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManagePurchaseUI mp = new ManagePurchaseUI();
                mp.run();
            }
        });
        btnAddCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddCustomerUI ui = new AddCustomerUI();
                ui.run();
            }
        });

        btnManageCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageCustomerUI ui = new ManageCustomerUI();
                ui.run();
            }
        });

        btnManageUserInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageUserInfoUI ui = new ManageUserInfoUI(user);
                ui.run();
            }
        } );
    }
}