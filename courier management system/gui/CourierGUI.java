package couriermanagementsystem.gui;

// Import the Parcel data model
import couriermanagementsystem.entity.Parcel;

// Import the file I/O helper that reads/writes parcel records
import couriermanagementsystem.fileio.ParcelFileIO;

// Swing imports — javax.swing provides all GUI components (buttons, labels, tables, etc.)
import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Used to manage JTable data programmatically

// AWT imports — java.awt provides layout managers and the Color/Font APIs
import java.awt.*;

// IOException is thrown when file operations fail
import java.io.IOException;

/**
 * CourierGUI - The main application window for the Courier Management System.
 *
 * This class extends JFrame (a top-level Swing window) and builds the entire
 * user interface: input fields, a search bar, action buttons, and a data table.
 *
 * Layout overview (left / right split):
 *
 *   LEFT PANEL (fixed width)
 *     UPPER — fieldPanel  : five label + text-field rows (ID, Sender, Receiver,
 *                           Weight, Destination)
 *     LOWER — buttonPanel : four action buttons (Add, Update, Delete, Clear)
 *
 *   RIGHT PANEL (fills remaining space)
 *     TOP    — searchPanel : keyword search bar + Search button
 *     CENTER — scrollPane  : JTable that displays parcel records
 *
 * The GUI communicates with ParcelFileIO for all data persistence.
 */
public class CourierGUI extends JFrame {

    // --- Input text fields (one per parcel attribute) ---
    private JTextField idField;          // Field where the user types the parcel ID
    private JTextField senderField;      // Field where the user types the sender name
    private JTextField receiverField;    // Field where the user types the receiver name
    private JTextField weightField;      // Field where the user types the parcel weight
    private JTextField destinationField; // Field where the user types the destination
    private JTextField searchField;      // Field where the user types a search keyword

    // --- Table components ---
    private JTable table;                // The visual table widget displayed in the window
    private DefaultTableModel tableModel; // The data model that backs the JTable

    // =========================================================================
    // CONSTRUCTOR — builds and displays the entire GUI
    // =========================================================================

    /**
     * Constructs the CourierGUI window, wires up all components and event
     * listeners, ensures the data file exists, and loads all existing records
     * into the table.
     */
    public CourierGUI() {
        // Set the text shown in the window's title bar
        setTitle("Courier Management System");

        // Set the initial window size in pixels (width x height)
        setSize(950, 560);

        // Close the application completely when the user clicks the X button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use BorderLayout as the root layout with 10-pixel gaps between regions
        setLayout(new BorderLayout(10, 10));

        // Apply a light background colour to the content pane for a clean look
        getContentPane().setBackground(new Color(240, 242, 245));

        // -----------------------------------------------------------------
        // TITLE BANNER — shown at the very top of the window
        // -----------------------------------------------------------------
        JLabel titleLabel = new JLabel("Parcel Details", SwingConstants.CENTER); // Centred heading text
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));                // Large bold font
        titleLabel.setForeground(new Color(33, 37, 41));                         // Dark text colour
        titleLabel.setBorder(BorderFactory.createEmptyBorder(14, 10, 10, 10));   // Breathing room around it
        add(titleLabel, BorderLayout.NORTH);                                     // Place at the top of the window

        // =================================================================
        // LEFT PANEL — split vertically into UPPER (fields) and LOWER (buttons)
        // =================================================================
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10)); // Vertical gap between upper and lower
        leftPanel.setBackground(new Color(240, 242, 245));       // Match window background
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 6)); // Outer padding
        leftPanel.setPreferredSize(new Dimension(310, 0));       // Fix the left panel width

        // -----------------------------------------------------------------
        // UPPER LEFT — fieldPanel: five rows of label + text field
        // GridLayout(rows, cols, hgap, vgap) places components in a grid
        // -----------------------------------------------------------------
        JPanel fieldPanel = new JPanel(new GridLayout(5, 2, 8, 10)); // 5 rows, 2 cols
        fieldPanel.setBackground(Color.WHITE);                        // White card background
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 203, 210), 1, true), // Rounded outline
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));                 // Inner padding

        // Row 1: ID label and text field
        fieldPanel.add(makeLabel("Parcel ID (8 digits):")); // Descriptive label
        idField = makeTextField();                           // Empty text box for ID input
        fieldPanel.add(idField);

        // Row 2: Sender label and text field
        fieldPanel.add(makeLabel("Sender:"));
        senderField = makeTextField();
        fieldPanel.add(senderField);

        // Row 3: Receiver label and text field
        fieldPanel.add(makeLabel("Receiver:"));
        receiverField = makeTextField();
        fieldPanel.add(receiverField);

        // Row 4: Weight label and text field
        fieldPanel.add(makeLabel("Weight (kg):"));
        weightField = makeTextField();
        fieldPanel.add(weightField);

        // Row 5: Destination label and text field
        fieldPanel.add(makeLabel("Destination:"));
        destinationField = makeTextField();
        fieldPanel.add(destinationField);

        // -----------------------------------------------------------------
        // LOWER LEFT — buttonPanel: four action buttons in a 2×2 grid
        // GridLayout(rows, cols, hgap, vgap) keeps the buttons evenly spaced
        // -----------------------------------------------------------------
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 cols
        buttonPanel.setBackground(new Color(240, 242, 245));            // Match window background

        JButton addBtn    = makeButton("Add",    new Color(40, 167, 69));  // Green  — save new record
        JButton updateBtn = makeButton("Update", new Color(0, 123, 255));  // Blue   — overwrite record
        JButton deleteBtn = makeButton("Delete", new Color(220, 53, 69));  // Red    — remove record
        JButton clearBtn  = makeButton("Clear",  new Color(108, 117, 125)); // Grey  — reset fields

        // Add all four buttons to the panel in reading order
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Assemble the left panel: field card on top, buttons on the bottom
        leftPanel.add(fieldPanel,  BorderLayout.CENTER); // Fields fill available vertical space
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);  // Buttons sit at the bottom

        // =================================================================
        // RIGHT PANEL — search bar on top, scrollable table below
        // =================================================================
        JPanel rightPanel = new JPanel(new BorderLayout(0, 8)); // Vertical gap between search and table
        rightPanel.setBackground(new Color(240, 242, 245));      // Match window background
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 12, 12)); // Outer padding

        // -----------------------------------------------------------------
        // SEARCH PANEL — a text field with a Search button to its right
        // -----------------------------------------------------------------
        JPanel searchPanel = new JPanel(new BorderLayout(6, 0)); // Horizontal gap between field and button
        searchPanel.setBackground(Color.WHITE);                   // White card background
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 203, 210), 1, true), // Rounded outline
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));                   // Inner padding

        searchField = new JTextField();                   // Keyword entry box
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13)); // Readable font size
        searchField.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6)); // Inner text padding

        JButton searchBtn = makeButton("Search", new Color(52, 58, 64)); // Dark search button

        searchPanel.add(searchField, BorderLayout.CENTER); // Text field fills available width
        searchPanel.add(searchBtn,   BorderLayout.EAST);   // Button sits at the right edge

        // -----------------------------------------------------------------
        // TABLE — displays parcel records in a scrollable grid
        // -----------------------------------------------------------------
        // Column headers shown at the top of the table
        String[] columns = { "ID", "Sender", "Receiver", "Weight (kg)", "Destination" };

        // Create a custom DefaultTableModel that prevents the user from editing cells directly
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            // isCellEditable returns false for all cells → read-only table
            public boolean isCellEditable(int row, int column) {
                return false; // Editing must go through the input fields + Update button
            }
        };

        table = new JTable(tableModel);           // Build the visual table backed by tableModel
        table.setRowHeight(24);                   // Make each row 24 pixels tall for readability
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));             // Table body font
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13)); // Bold column headers
        table.getTableHeader().setBackground(new Color(52, 58, 64));      // Dark header background
        table.getTableHeader().setForeground(Color.WHITE);                // White header text
        table.setSelectionBackground(new Color(173, 214, 255));           // Light-blue row highlight
        table.setGridColor(new Color(220, 220, 220));                     // Subtle grid lines
        table.setShowGrid(true);                                          // Always show the grid

        // Wrap the table in a scroll pane so a scrollbar appears when there are many records
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 203, 210), 1, true)); // Outlined border

        // Assemble the right panel: search bar on top, table filling the rest
        rightPanel.add(searchPanel, BorderLayout.NORTH);  // Search sits at the top
        rightPanel.add(scrollPane,  BorderLayout.CENTER); // Table fills remaining space

        // =================================================================
        // ASSEMBLE — add the two major panels to the JFrame
        // =================================================================
        add(leftPanel,  BorderLayout.WEST);   // Left panel docked to the left side
        add(rightPanel, BorderLayout.CENTER); // Right panel fills the remaining space

        // =================================================================
        // EVENT LISTENERS — wire each button/interaction to its handler method
        // =================================================================

        // "Add" button → call addParcel() when clicked
        addBtn.addActionListener(e -> addParcel());

        // "Update" button → call updateParcel() when clicked
        updateBtn.addActionListener(e -> updateParcel());

        // "Delete" button → call deleteParcel() when clicked
        deleteBtn.addActionListener(e -> deleteParcel());

        // "Clear" button → reset all input fields and deselect the table row
        clearBtn.addActionListener(e -> clearFields());

        // "Search" button → call searchParcel() when clicked
        searchBtn.addActionListener(e -> searchParcel());

        // Table row click → auto-fill the input fields with the selected parcel's data
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow(); // -1 if nothing is selected

            if (row >= 0) { // A valid row was selected
                // Populate each text field from the corresponding table column
                idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));          // Column 0 = ID
                senderField.setText(String.valueOf(tableModel.getValueAt(row, 1)));      // Column 1 = Sender
                receiverField.setText(String.valueOf(tableModel.getValueAt(row, 2)));    // Column 2 = Receiver
                weightField.setText(String.valueOf(tableModel.getValueAt(row, 3)));      // Column 3 = Weight
                destinationField.setText(String.valueOf(tableModel.getValueAt(row, 4))); // Column 4 = Destination
            }
        });

        // -----------------------------------------------------------------
        // STARTUP — ensure the data file exists, then load existing records
        // -----------------------------------------------------------------
        try {
            ParcelFileIO.createFileIfNotExists(); // Create parcels.txt if it's the first run
        } catch (IOException ex) {
            showError("Error creating file: " + ex.getMessage()); // Alert user if creation fails
        }

        viewAll(); // Load all existing parcel records into the table on launch

        setLocationRelativeTo(null); // Centre the window on the screen
        setVisible(true);            // Make the window visible to the user
    }

    // =========================================================================
    // FACTORY HELPERS — produce consistently styled components
    // =========================================================================

    /**
     * Creates a right-aligned label with a consistent font used for field names.
     *
     * @param text The label text to display.
     * @return A styled JLabel instance.
     */
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT); // Right-align so it sits next to the field
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));  // Readable body font
        label.setForeground(new Color(33, 37, 41));            // Dark text colour
        return label; // Return the configured label
    }

    /**
     * Creates a plain text field with consistent padding and font.
     *
     * @return A styled JTextField instance.
     */
    private JTextField makeTextField() {
        JTextField tf = new JTextField();                       // Standard text input box
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));     // Match label font size
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 185, 195), 1), // Thin outline
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));               // Inner text padding
        return tf; // Return the configured text field
    }

    /**
     * Creates a styled button with a specific background colour and white text.
     *
     * @param text  The button label.
     * @param color The background colour to use.
     * @return A styled JButton instance.
     */
    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);                          // Standard button with label
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));       // Bold label for readability
        btn.setBackground(color);                                 // Colour-coded background
        btn.setForeground(Color.WHITE);                           // White text on coloured background
        btn.setFocusPainted(false);                               // Remove focus ring for cleaner look
        btn.setBorderPainted(false);                              // Remove default border
        btn.setOpaque(true);                                      // Ensure background colour is painted
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Pointer cursor on hover
        return btn; // Return the configured button
    }

    // =========================================================================
    // VALIDATION METHODS
    // =========================================================================

    /**
     * Validates that the given ID is exactly 8 numeric digits.
     *
     * Rules enforced:
     * - Must not be empty.
     * - Must match the regular expression \d{8} (exactly 8 digit characters).
     *
     * @param id The ID string entered by the user.
     * @return true if valid; false if invalid (also shows an error dialog).
     */
    private boolean isValidId(String id) {
        // Check for empty input first
        if (id.isEmpty()) {
            showError("Parcel ID is required!");
            return false;
        }

        // \d{8} means "exactly 8 digit characters (0-9)" — no letters or symbols allowed
        if (!id.matches("\\d{8}")) {
            showError("ID must be exactly 8 digits (numbers only).\n"
                    + "Minimum: 8 digits, Maximum: 8 digits.");
            return false;
        }
        return true; // ID passed all checks
    }

    /**
     * Validates all five input fields before an Add or Update operation.
     *
     * Rules enforced:
     * - Sender, receiver, weight, and destination must not be empty.
     * - ID must pass isValidId() (exactly 8 digits).
     * - No field may contain a comma (commas are the CSV delimiter in the data file).
     * - Weight must be parseable as a positive number.
     *
     * @param id          ID field value.
     * @param sender      Sender field value.
     * @param receiver    Receiver field value.
     * @param weight      Weight field value.
     * @param destination Destination field value.
     * @return true if all fields are valid; false if any validation fails.
     */
    private boolean isValidAllFields(String id, String sender, String receiver,
                                     String weight, String destination) {
        // Ensure none of the non-ID fields are blank
        if (sender.isEmpty() || receiver.isEmpty() || weight.isEmpty() || destination.isEmpty()) {
            showError("All fields are required!");
            return false;
        }

        // Validate the ID using the dedicated ID validation method
        if (!isValidId(id))
            return false;

        // Commas would break the CSV format in the data file — disallow them everywhere
        if (sender.contains(",") || receiver.contains(",")
                || weight.contains(",") || destination.contains(",")) {
            showError("Commas are not allowed in any field!");
            return false;
        }

        // Weight must be a valid positive number (e.g. "2.5", not "heavy" or "-1")
        try {
            double w = Double.parseDouble(weight); // Attempt to parse; exception means not a number
            if (w <= 0) {                          // Weight must be greater than zero
                showError("Weight must be a positive number!");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Weight must be a number (e.g. 2.5)!");
            return false;
        }
        return true; // All fields passed validation
    }

    // =========================================================================
    // CRUD ACTION METHODS (called by button listeners)
    // =========================================================================

    /**
     * Reads the input fields and adds a new parcel to the data file.
     *
     * Steps:
     * 1. Trim whitespace from all field values.
     * 2. Validate all fields.
     * 3. Check that the ID is not already in use.
     * 4. Save the new parcel.
     * 5. Clear the input fields and refresh the table.
     */
    private void addParcel() {
        // Read and trim each input field value (trim removes leading/trailing spaces)
        String id          = idField.getText().trim();
        String sender      = senderField.getText().trim();
        String receiver    = receiverField.getText().trim();
        String weight      = weightField.getText().trim();
        String destination = destinationField.getText().trim();

        // Stop immediately if any field fails validation
        if (!isValidAllFields(id, sender, receiver, weight, destination))
            return;

        // Prevent adding a second parcel with the same ID
        if (ParcelFileIO.idExists(id)) {
            showError("Duplicate ID! A parcel with ID " + id + " already exists.");
            return;
        }

        try {
            // Create a new Parcel object and save it to the data file
            ParcelFileIO.addParcel(new Parcel(id, sender, receiver, weight, destination));
            showInfo("Parcel added successfully!"); // Inform the user of success
            clearFields();                          // Reset the input form for the next entry
            viewAll();                              // Refresh the table to show the newly added record
        } catch (IOException ex) {
            showError("Error: " + ex.getMessage()); // Show any file I/O error to the user
        }
    }

    /**
     * Reads the input fields and updates the matching parcel record in the data file.
     *
     * Steps:
     * 1. Trim whitespace from all field values.
     * 2. Validate all fields.
     * 3. Attempt the update (returns false if the ID does not exist).
     * 4. Clear the input fields and refresh the table on success.
     */
    private void updateParcel() {
        // Read and trim each input field value
        String id          = idField.getText().trim();
        String sender      = senderField.getText().trim();
        String receiver    = receiverField.getText().trim();
        String weight      = weightField.getText().trim();
        String destination = destinationField.getText().trim();

        // Stop if any validation fails
        if (!isValidAllFields(id, sender, receiver, weight, destination))
            return;

        try {
            // updateParcel returns true if it found and replaced the record
            boolean updated = ParcelFileIO.updateParcel(
                    new Parcel(id, sender, receiver, weight, destination));

            if (updated) {
                showInfo("Parcel updated successfully!"); // Notify the user
                clearFields();                            // Reset the form
                viewAll();                                // Refresh the table to show the updated data
            } else {
                showError("Parcel ID not found!"); // No record with that ID exists
            }
        } catch (IOException ex) {
            showError("Error: " + ex.getMessage()); // Show any file I/O error
        }
    }

    /**
     * Reads the ID field and deletes the matching parcel record after user confirmation.
     *
     * Steps:
     * 1. Trim and validate the ID field.
     * 2. Ask the user to confirm the deletion (prevents accidental deletes).
     * 3. Attempt the delete (returns false if the ID does not exist).
     * 4. Clear the input fields and refresh the table on success.
     */
    private void deleteParcel() {
        String id = idField.getText().trim(); // Only the ID is needed to identify the record

        // Validate the ID before proceeding
        if (!isValidId(id))
            return;

        // Show a Yes/No confirmation dialog — safety net against accidental deletions
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete parcel ID: " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        // If the user chose anything other than "Yes", abort the delete
        if (confirm != JOptionPane.YES_OPTION)
            return;

        try {
            // deleteParcel returns true if it found and removed the record
            boolean deleted = ParcelFileIO.deleteParcel(id);

            if (deleted) {
                showInfo("Parcel deleted successfully!"); // Notify the user
                clearFields();                            // Reset the form
                viewAll();                                // Refresh the table (the deleted record is now gone)
            } else {
                showError("Parcel ID not found!"); // No record with that ID exists
            }
        } catch (IOException ex) {
            showError("Error: " + ex.getMessage()); // Show any file I/O error
        }
    }

    /**
     * Reads the search field keyword and displays only the matching parcel records.
     *
     * The search is case-insensitive and matches any ID or Sender name that contains
     * the keyword as a substring (partial match).
     */
    private void searchParcel() {
        String keyword = searchField.getText().trim(); // The keyword entered by the user

        // Require at least something to search for
        if (keyword.isEmpty()) {
            showError("Enter an ID or Sender name to search!");
            return;
        }

        // Retrieve matching rows from the file (2D array, one row per match)
        Object[][] results = ParcelFileIO.searchParcels(keyword);

        tableModel.setRowCount(0); // Clear the current table contents before loading results

        // Add each matching parcel row to the table
        for (int i = 0; i < results.length; i++) {
            tableModel.addRow(results[i]); // Each results[i] is a 5-element Object array
        }

        // Inform the user if no matches were found
        if (results.length == 0)
            showInfo("No matching parcel found.");
    }

    /**
     * Loads all parcel records from the data file and displays them in the table.
     *
     * Called at startup and after every Add, Update, or Delete operation to
     * keep the table in sync with the data file.
     */
    private void viewAll() {
        // Retrieve all records as a 2D array from the file
        Object[][] rows = ParcelFileIO.getAllParcels();

        tableModel.setRowCount(0); // Clear all existing rows from the table

        // Add each parcel row to the table model (which automatically updates the JTable)
        for (int i = 0; i < rows.length; i++) {
            // rows[i][0] is the ID — skip rows where it is null (safety guard for empty slots)
            if (rows[i][0] != null)
                tableModel.addRow(rows[i]);
        }
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    /**
     * Clears all input text fields and removes any row selection in the table.
     *
     * Called after a successful Add/Update/Delete and when the Clear button is clicked.
     */
    private void clearFields() {
        idField.setText("");          // Erase the ID field
        senderField.setText("");      // Erase the Sender field
        receiverField.setText("");    // Erase the Receiver field
        weightField.setText("");      // Erase the Weight field
        destinationField.setText(""); // Erase the Destination field
        searchField.setText("");      // Erase the Search field
        table.clearSelection();       // Deselect any highlighted row in the table
    }

    /**
     * Displays an informational pop-up dialog with the given message.
     *
     * Used to show success messages (e.g. "Parcel added successfully!").
     *
     * @param msg The message to show in the dialog body.
     */
    private void showInfo(String msg) {
        // showMessageDialog(parent, message, title, messageType)
        // INFORMATION_MESSAGE shows a blue "i" icon
        JOptionPane.showMessageDialog(this, msg, "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error pop-up dialog with the given message.
     *
     * Used to show validation errors and I/O failure messages.
     *
     * @param msg The error message to show in the dialog body.
     */
    private void showError(String msg) {
        // ERROR_MESSAGE shows a red "X" icon so errors are visually distinct from info
        JOptionPane.showMessageDialog(this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
