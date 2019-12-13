import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ManagePurchaseUI {
    public JFrame view;

    public JButton btnLoad = new JButton("Load Purchase");
    public JButton btnSave = new JButton("Save Purchase");

    public JTextField txtPurchaseID = new JTextField(20);
    public JTextField txtProductID = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);

    PurchaseModel purchase = null;


    public ManagePurchaseUI() {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Update Purchase Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        view.getContentPane().add(panelButtons);

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("PurchaseID "));
        line1.add(txtPurchaseID);
        view.getContentPane().add(line1);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("ProductID "));
        line3.add(txtProductID);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Quantity "));
        line4.add(txtQuantity);
        view.getContentPane().add(line4);


        btnLoad.addActionListener(new ManagePurchaseUI.LoadButtonListerner());

        btnSave.addActionListener(new ManagePurchaseUI.SaveButtonListener());

    }

    public void run() {
        view.setVisible(true);
    }

    class LoadButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Gson gson = new Gson();
            String id = txtPurchaseID.getText();
            int purchaseID;

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!");
                return;
            }

            try {
                purchaseID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "PurchaseID is invalid!");
                return;
            }

            // do client/server

            try {
                Socket link = new Socket("localhost", 1024);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_PURCHASE;
                msg.data = Integer.toString(purchaseID);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "Purchase NOT exists!");
                }
                else {
                    purchase = new PurchaseModel();
                    purchase = gson.fromJson(msg.data, PurchaseModel.class);
                    txtProductID.setText(Integer.toString(purchase.mProductID));
                    txtQuantity.setText(Integer.toString(purchase.mQuantity));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Gson gson = new Gson();
            String id = txtPurchaseID.getText();

            if (purchase == null) {
                JOptionPane.showMessageDialog(null, "A Purchase must be loaded in first!");
                return;
            }

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!");
                return;
            }

            try {
                purchase.mPurchaseID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "PurchaseID is invalid!");
                return;
            }

            String productID = txtProductID.getText();
            if (productID.length() == 0) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be empty!");
                return;
            }

            try {
                purchase.mProductID = Integer.parseInt(productID);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid!");
                return;
            }

            String quantity = txtQuantity.getText();
            if (quantity.length() == 0) {
                JOptionPane.showMessageDialog(null, "Quantity cannot be empty!");
                return;
            }

            try {
                purchase.mQuantity = Integer.parseInt(quantity);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is invalid!");
                return;
            }

            // Load the selected product to get its price
            ProductModel product = new ProductModel();
            try {
                Socket link = new Socket("localhost", 1024);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_PRODUCT;
                msg.data = Integer.toString(purchase.mProductID);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null,
                            "Error: No product with id = " + purchase.mProductID + " in store!", "Error Message",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }
                else {
                    product = gson.fromJson(msg.data, ProductModel.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            purchase.mCost = product.mPrice * purchase.mQuantity;
            purchase.mTax = purchase.mCost * purchase.TAX_RATE;
            purchase.mTotal = purchase.mCost + purchase.mTax;


            // Load customer info for the receipt
            CustomerModel customer = new CustomerModel();
            try {
                Socket link = new Socket("localhost", 1024);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_CUSTOMER;
                msg.data = Integer.toString(purchase.mCustomerID);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null,
                            "Error: No customer with id = " + purchase.mCustomerID + " in store!", "Error Message",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }
                else {
                    customer = gson.fromJson(msg.data, CustomerModel.class);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            // all product info is ready! Send to Server!


            try {
                Socket link = new Socket("localhost", 1024);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.PUT_PURCHASE;
                msg.data = gson.toJson(purchase);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class); // receive from Server

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "Purchase is NOT saved successfully!");
                }
                else {
                    TXTReceiptBuilder receipt = new TXTReceiptBuilder();
                    receipt.appendHeader("Purchase added successfully!");
                    receipt.appendCustomer(customer);
                    receipt.appendProduct(product);
                    receipt.appendPurchase(purchase);
                    receipt.appendFooter("");
                    JOptionPane.showMessageDialog(null, receipt.sb);
                    view.dispose();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
