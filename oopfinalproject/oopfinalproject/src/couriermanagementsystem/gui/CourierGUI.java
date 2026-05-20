package couriermanagementsystem.gui;

import couriermanagementsystem.entity.Parcel;

import couriermanagementsystem.fileio.ParcelFileIO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import java.awt.*;

import java.io.IOException;

public class CourierGUI extends JFrame {

    private JTextField idField;            
    private JTextField senderField;    
    private JTextField receiverField;     
    private JTextField weightField;        
    private JTextField destinationField;  
    private JTextField courierStatusField; 
    private JTextField searchField;

    
    private JTable table;                 
    private DefaultTableModel tableModel; 

    
    public CourierGUI() {
        setTitle("Courier Management System");
        setSize(950, 620);                              
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 242, 245));

        
        JLabel titleLabel = new JLabel("Parcel Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(14, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

       
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(new Color(240, 242, 245));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 6));
        leftPanel.setPreferredSize(new Dimension(310, 0));

       
        JPanel fieldPanel = new JPanel(new GridLayout(6, 2, 8, 10));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 203, 210), 1, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        
        fieldPanel.add(makeLabel("Parcel ID (8 digits):"));
        idField = makeTextField();
        fieldPanel.add(idField);

        fieldPanel.add(makeLabel("Sender:"));
        senderField = makeTextField();
        fieldPanel.add(senderField);

        
        fieldPanel.add(makeLabel("Receiver:"));
        receiverField = makeTextField();
        fieldPanel.add(receiverField);

        fieldPanel.add(makeLabel("Weight (kg):"));
        weightField = makeTextField();
        fieldPanel.add(weightField);

        
        fieldPanel.add(makeLabel("Destination:"));
        destinationField = makeTextField();
        fieldPanel.add(destinationField);

        
        fieldPanel.add(makeLabel("Courier Status:"));
        courierStatusField = makeTextField();
        fieldPanel.add(courierStatusField);
       
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBackground(new Color(240, 242, 245));

        JButton addBtn = makeButton("Add",      new Color(40, 167, 69));   
        JButton updateBtn = makeButton("Update",   new Color(0, 123, 255));   
        JButton deleteBtn = makeButton("Delete",   new Color(220, 53, 69));   
        JButton clearBtn = makeButton("Clear",    new Color(108, 117, 125)); 
        JButton viewAllBtn = makeButton("View All", new Color(52, 58, 64));    

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(viewAllBtn);

        leftPanel.add(fieldPanel,  BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        JPanel rightPanel= new JPanel(new BorderLayout(0, 8));
        rightPanel.setBackground(new Color(240, 242, 245));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 12, 12));

       
        JPanel searchPanel= new JPanel(new BorderLayout(6, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 203, 210), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JButton searchBtn= makeButton("Search", new Color(52, 58, 64));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn,   BorderLayout.EAST);

       
        String[] columns ={ "ID", "Sender", "Receiver", "Weight (kg)", "Destination", "Courier Status" };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 58, 64));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 214, 255));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 203, 210), 1, true));

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane,  BorderLayout.CENTER);

        
        add(leftPanel,  BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        
        addBtn.addActionListener(e -> addParcel());
        updateBtn.addActionListener(e -> updateParcel());
        deleteBtn.addActionListener(e -> deleteParcel());
        clearBtn.addActionListener(e -> clearFields());
        searchBtn.addActionListener(e -> searchParcel());

       
        viewAllBtn.addActionListener(e -> viewAll());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));            
                senderField.setText(String.valueOf(tableModel.getValueAt(row, 1)));        
                receiverField.setText(String.valueOf(tableModel.getValueAt(row, 2)));      
                weightField.setText(String.valueOf(tableModel.getValueAt(row, 3)));        
                destinationField.setText(String.valueOf(tableModel.getValueAt(row, 4)));   
                courierStatusField.setText(String.valueOf(tableModel.getValueAt(row, 5))); 
            }
        });

    
        try {
            ParcelFileIO.createFileIfNotExists();
        } catch (IOException ex) {
            showError("Error creating file: " + ex.getMessage());
        }

        viewAll(); 

        setLocationRelativeTo(null);
        setVisible(true);
    }
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(33, 37, 41));
        return label;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 185, 195), 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return tf;
    }
    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private boolean isValidId(String id) {
        if (id.isEmpty()) {
            showError("Parcel ID is required!");
            return false;
        }
        if (!id.matches("\\d{8}")) {
            showError("ID must be exactly 8 digits (numbers only).\n"
                    + "Minimum: 8 digits, Maximum: 8 digits.");
            return false;
        }
        return true;
    }
    private boolean isValidAllFields(String id, String sender, String receiver,
                                     String weight, String destination, String courierStatus) {
        if (sender.isEmpty() || receiver.isEmpty() || weight.isEmpty()
                || destination.isEmpty() || courierStatus.isEmpty()) {
            showError("All fields are required!");
            return false;
        }
        if (!isValidId(id))
            return false;

        if (sender.contains(",") || receiver.contains(",") || weight.contains(",")
                || destination.contains(",") || courierStatus.contains(",")) {
            showError("Commas are not allowed in any field!");
            return false;
        }
        try {
            double w = Double.parseDouble(weight);
            if (w <= 0) {
                showError("Weight must be a positive number!");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Weight must be a number (e.g. 2.5)!");
            return false;
        }
        return true;
    }

    private void addParcel() {
        String id = idField.getText().trim();
        String sender = senderField.getText().trim();
        String receiver  = receiverField.getText().trim();
        String weight = weightField.getText().trim();
        String destination= destinationField.getText().trim();
        String courierStatus= courierStatusField.getText().trim();

        if (!isValidAllFields(id, sender, receiver, weight, destination, courierStatus))
            return;

        if (ParcelFileIO.idExists(id)) {
            showError("Duplicate ID! A parcel with ID " + id + " already exists.");
            return;
        }
        try {
            ParcelFileIO.addParcel(new Parcel(id, sender, receiver, weight, destination, courierStatus));
            showInfo("Parcel added successfully!");
            clearFields();
            viewAll();
        } catch (IOException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    
    private void updateParcel() {
        String id = idField.getText().trim();
        String sender = senderField.getText().trim();
        String receiver= receiverField.getText().trim();
        String weight = weightField.getText().trim();
        String destination = destinationField.getText().trim();
        String courierStatus= courierStatusField.getText().trim();

        if (!isValidAllFields(id, sender, receiver, weight, destination, courierStatus))
            return;

        try {
            boolean updated = ParcelFileIO.updateParcel(
                    new Parcel(id, sender, receiver, weight, destination, courierStatus));

            if (updated) {
                showInfo("Parcel updated successfully!");
                clearFields();
                viewAll();
            } else {
                showError("Parcel ID not found!");
            }
        } catch (IOException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void deleteParcel() {
        String id = idField.getText().trim();

        if (!isValidId(id))
            return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete parcel ID: " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION)
            return;

        try {
            boolean deleted = ParcelFileIO.deleteParcel(id);
            if (deleted) {
                showInfo("Parcel deleted successfully!");
                clearFields();
                viewAll();
            } else {
                showError("Parcel ID not found!");
            }
        } catch (IOException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void searchParcel() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            showError("Enter an ID or Sender name to search!");
            return;
        }

        Object[][] results = ParcelFileIO.searchParcels(keyword);

        tableModel.setRowCount(0);

        for (int i = 0; i < results.length; i++) {
            tableModel.addRow(results[i]); 
        }

        if (results.length == 0)
            showInfo("No matching parcel found.");
    }

   
    private void viewAll() {
        Object[][] rows = ParcelFileIO.getAllParcels();

        tableModel.setRowCount(0); 
        for (int i = 0; i < rows.length; i++) {
            if (rows[i][0] != null)
                tableModel.addRow(rows[i]); 
        }
    }
    private void clearFields() {
        idField.setText("");
        senderField.setText("");
        receiverField.setText("");
        weightField.setText("");
        destinationField.setText("");
        courierStatusField.setText(""); 
        searchField.setText("");
        table.clearSelection();
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
