/*
Name: <Osman Ali>
Course: CNT 4714 Fall 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 29, 2023
Class: <MainAppGUI.java>
*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;


//Gui For application begins here
public class MainAppGUI extends JFrame {
    // GUI components
    private JLabel connectionStatusLabel;
    private JComboBox<String> dbURLChoice;
    private JComboBox<String> dbUserChoice;
    private JPasswordField passwordField;
    private JTextArea queryBox;
    private JTable tableView;
    private JTextField usernameField;
    private JLabel queryLabel;
    private JLabel dbUrlPropertiesLabel;
    private JLabel userPropertiesLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JScrollPane scrollPane;

    // Database connection and query related variables
    private String dbUsername;
    private String dbPassword;
    private String sqlCommand;
    private String dbURL;
    private ResultSet resultSet;

    private String tmpUser;
    private String tmpPwd;
    
      //Constructing the main GUI for the database client application.
     
    public MainAppGUI() {
        setTitle("SQL Client App- (Osman M Ali- CNT4714-Fall2023-Project 3)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1024, 768));
        setLayout(null);

        initComponents();
        addComponents();
        setupListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        queryLabel = new JLabel("Enter your query:");
        queryLabel.setBounds(20, 20, 150, 30);

        dbUrlPropertiesLabel = new JLabel("DB URL PROPERTIES");
        dbUrlPropertiesLabel.setBounds(40, 69, 180, 30);
        dbUrlPropertiesLabel.setOpaque(true);
        dbUrlPropertiesLabel.setBackground(new Color(192, 192, 192));
        dbUrlPropertiesLabel.setForeground(new Color(31, 31, 31));
        dbUrlPropertiesLabel.setFont(new Font("System Bold", Font.BOLD, 15));

        userPropertiesLabel = new JLabel("USER PROPERTIES");
        userPropertiesLabel.setBounds(40, 116, 180, 30);
        userPropertiesLabel.setOpaque(true);
        userPropertiesLabel.setBackground(new Color(192, 192, 192));
        userPropertiesLabel.setForeground(new Color(31, 31, 31));
        userPropertiesLabel.setFont(new Font("System Bold", Font.BOLD, 15));

        usernameLabel = new JLabel("USERNAME");
        usernameLabel.setBounds(40, 161, 180, 30);
        usernameLabel.setOpaque(true);
        usernameLabel.setBackground(new Color(192, 192, 192));
        usernameLabel.setForeground(new Color(31, 31, 31));
        usernameLabel.setFont(new Font("System Bold", Font.BOLD, 15));

        passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setBounds(40, 208, 180, 30);
        passwordLabel.setOpaque(true);
        passwordLabel.setBackground(new Color(192, 192, 192));
        passwordLabel.setForeground(new Color(31, 31, 31));
        passwordLabel.setFont(new Font("System Bold", Font.BOLD, 15));

        connectionStatusLabel = new JLabel("No Connection Now");
        connectionStatusLabel.setBounds(40, 330, 950, 37);
        connectionStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        connectionStatusLabel.setForeground(Color.RED);
        connectionStatusLabel.setOpaque(true);
        connectionStatusLabel.setBackground(Color.BLACK);
        connectionStatusLabel.setFont(connectionStatusLabel.getFont().deriveFont(Font.BOLD, 17));

        dbURLChoice = new JComboBox<>();
        dbURLChoice.setBounds(230, 69, 280, 30);
        dbURLChoice.addItem("project3.properties");
        dbURLChoice.addItem("bikedb.properties");


        dbUserChoice = new JComboBox<>();
        dbUserChoice.setBounds(230, 116, 280, 30);
        dbUserChoice.addItem("root.properties");
        dbUserChoice.addItem("client1.properties");
        dbUserChoice.addItem("client2.properties");

        passwordField = new JPasswordField();
        passwordField.setBounds(230, 208, 280, 30);

        queryBox = new JTextArea();
        queryBox.setBounds(540, 77, 450, 170);
        queryBox.setLineWrap(true);

        tableView = new JTable();
        tableView.setBounds(39, 373, 952, 282);

        usernameField = new JTextField();
        usernameField.setBounds(230, 161, 280, 30);
    }

    private void addComponents() {
        Container contentPane = getContentPane();
        contentPane.add(connectionStatusLabel);
        contentPane.add(dbURLChoice);
        contentPane.add(dbUserChoice);
        contentPane.add(passwordField);
        contentPane.add(queryBox);
        contentPane.add(usernameField);
        contentPane.add(queryLabel);

        getContentPane().add(dbUrlPropertiesLabel);
        getContentPane().add(userPropertiesLabel);
        getContentPane().add(usernameLabel);
        getContentPane().add(passwordLabel);
    }

    private void setupListeners() {
        Font font = new Font("System Bold", Font.BOLD, 15);
        JButton connectToDBButton = createButton("Connect to Database");
        connectToDBButton.setBounds(40, 270, 200, 40);
        connectToDBButton.setBackground(new Color(60, 140, 129));
        connectToDBButton.setForeground(Color.WHITE);
        connectToDBButton.setBorder(null);
        connectToDBButton.setFont(font);
        connectToDBButton.addActionListener(e -> connectToDB());
        getContentPane().add(connectToDBButton);

        JButton clearQueryBoxButton = createButton("Clear SQL Command");
        clearQueryBoxButton.setBackground(new Color(194, 145, 133));
        clearQueryBoxButton.setForeground(Color.BLACK);
        clearQueryBoxButton.setBounds(546, 270, 200, 40);
        clearQueryBoxButton.setBorder(null);
        clearQueryBoxButton.setFont(font);
        clearQueryBoxButton.addActionListener(e -> clearSQLCommand());
        getContentPane().add(clearQueryBoxButton);

        JButton executeSQLCommandButton = createButton("Execute SQL Command");
        executeSQLCommandButton.setBounds(770, 270, 200, 40);
        executeSQLCommandButton.setBackground(new Color(152, 193, 140));
        executeSQLCommandButton.setForeground(Color.BLACK);
        executeSQLCommandButton.setBorder(null);
        executeSQLCommandButton.setFont(font);
        executeSQLCommandButton.addActionListener(e -> executeSQLCommand());

        getContentPane().add(executeSQLCommandButton);
        JButton clearResultWindowButton = createButton("Clear Result Window");
        clearResultWindowButton.setBounds(40, 670, 200, 40);
        clearResultWindowButton.setBackground(new Color(204, 191, 99));
        clearResultWindowButton.setForeground(Color.BLACK);
        clearResultWindowButton.setBorder(null);
        clearResultWindowButton.setFont(font);
        clearResultWindowButton.addActionListener(e -> clearResultWindow());
        getContentPane().add(clearResultWindowButton);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 15));
        return button;
    }

    private void connectToDB() {
        dbUsername = usernameField.getText().trim();
        dbPassword = new String(passwordField.getPassword());


        if (dbURLChoice.getSelectedItem() != null && dbURLChoice.getSelectedItem().equals("project3.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("project3.properties")).getPath().replace("%20", " ")))) {
                properties.load(fis);
                dbURL = properties.getProperty("url");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (dbURLChoice.getSelectedItem() != null && dbURLChoice.getSelectedItem().equals("bikedb.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("bikedb.properties")).getPath().replace("%20", " ")))) {
                properties.load(fis);
                dbURL = properties.getProperty("url");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        if (dbUserChoice.getSelectedItem() != null && dbUserChoice.getSelectedItem().equals("root.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("root.properties")).getPath().replace("%20", " ")))) {
                properties.load(fis);
                this.setTmpUser(properties.getProperty("username"));
                this.setTmpPwd(properties.getProperty("password"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (dbUserChoice.getSelectedItem() != null && dbUserChoice.getSelectedItem().equals("client1.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("client1.properties")).getPath().replace("%20", " ")))) {
                properties.load(fis);
                this.setTmpUser(properties.getProperty("username"));
                this.setTmpPwd(properties.getProperty("password"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (dbUserChoice.getSelectedItem() != null && dbUserChoice.getSelectedItem().equals("client2.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("client2.properties")).getPath().replace("%20", " ")))) {
                properties.load(fis);
                this.setTmpUser(properties.getProperty("username"));
                this.setTmpPwd(properties.getProperty("password"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        if (dbUsername != null && dbPassword != null && !dbUsername.equals(tmpUser) && !dbPassword.equals(tmpPwd)) {
            connectionStatusLabel.setText("Invalid Credentials");
        } else {
            DatabaseConnection.connect(dbURL, dbUsername, dbPassword);
            connectionStatusLabel.setText("Connected to: " + dbURL);
        }
    }

    private void clearSQLCommand() {
        queryBox.setText("");
    }

    void executeSQLCommand() {
        sqlCommand = queryBox.getText().trim();
        char firstLetter = sqlCommand.charAt(0);
        int response1;
        try {
            if (Objects.equals(dbUserChoice.getSelectedItem(), "root.properties")) {

                if (firstLetter == 'S' || firstLetter == 's') {
                    resultSet = DatabaseConnection.executeQuery(sqlCommand);
                    DatabaseLogger.totalOperations("root@localhost");
                } else if (firstLetter == 'U' || firstLetter == 'u' || firstLetter == 'D' || firstLetter == 'd' || firstLetter == 'i' || firstLetter == 'I') {
                    response1 = DatabaseConnection.executeQuery1(sqlCommand);
                    DatabaseLogger.totalUpdates("root@localhost");
                    JOptionPane.showMessageDialog(null, "SQL command executed successfully:\n" + response1 + " row(s) affected", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error executing SQL command:\n" + "Invalid SQL command", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (Objects.equals(dbUserChoice.getSelectedItem(), "client1.properties")) {
                if (firstLetter == 'U' || firstLetter == 'u') {
                    JOptionPane.showMessageDialog(null, "Update command Denied for client1@localhost", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (firstLetter == 'D' || firstLetter == 'd') {
                    JOptionPane.showMessageDialog(null, "Delete command Denied for client1@localhost", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (firstLetter == 'I' || firstLetter == 'i') {
                    JOptionPane.showMessageDialog(null, "Insert command Denied for client1@localhost", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (firstLetter == 'S' || firstLetter == 's') {
                    resultSet = DatabaseConnection.executeQuery(sqlCommand);
                    DatabaseLogger.totalOperations("client1@localhost");
                }
            }
            if (Objects.equals(dbUserChoice.getSelectedItem(), "client2.properties")) {
                if (firstLetter == 'S' || firstLetter == 's') {
                    resultSet = DatabaseConnection.executeQuery(sqlCommand);
                    DatabaseLogger.totalOperations("client2@localhost");
                } else if (firstLetter == 'U' || firstLetter == 'u') {
                    response1 = DatabaseConnection.executeQuery1(sqlCommand);
                    DatabaseLogger.totalUpdates("client2@localhost");
                    JOptionPane.showMessageDialog(null, "SQL command executed successfully:\n" + response1 + " row(s) affected", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (firstLetter == 'D' || firstLetter == 'd') {
                        JOptionPane.showMessageDialog(null, "Delete command Denied for client2@localhost", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (firstLetter == 'I' || firstLetter == 'i') {
                        JOptionPane.showMessageDialog(null, "Insert command Denied for client2@localhost", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error executing SQL command:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (resultSet != null) {
            try {
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                DefaultTableModel tableModel = new DefaultTableModel();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSetMetaData.getColumnName(i);
                    tableModel.addColumn(columnName);
                }
                tableView = new JTable();
                tableView.setModel(tableModel);

                JTableHeader tableHeader = tableView.getTableHeader();
                TableColumnModel tableColumnModel = tableHeader.getColumnModel();
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    TableColumn tableColumn = tableColumnModel.getColumn(columnIndex);
                    String columnName = resultSetMetaData.getColumnName(columnIndex + 1);
                    tableColumn.setHeaderValue(columnName);
                }
                tableHeader.repaint();

                DefaultTableModel model = (DefaultTableModel) tableView.getModel();
                model.setRowCount(0); // Clear existing rows

                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        rowData[i - 1] = resultSet.getString(i);
                    }
                    model.addRow(rowData);
                }

                scrollPane = new JScrollPane(tableView);
                scrollPane.setBounds(39, 373, 952, 282);
                getContentPane().add(scrollPane);

            } catch (SQLException e) {
                System.out.println("Invalid SQL command");
            }
        }
    }
    private void clearResultWindow() {
        scrollPane.setVisible(false);
    }
    
     //Setting the temporary user property.
     
    public void setTmpUser(String tmpUser) {
        this.tmpUser = tmpUser;
    }
    
      //Setting the temporary password property.
     
    public void setTmpPwd(String tmpPwd) {
        this.tmpPwd = tmpPwd;
    }
}

