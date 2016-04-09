package edu.ncsu.csc216.flix_2.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.*;

import edu.ncsu.csc216.flix_2.customer.CustomerAccountManager;
import edu.ncsu.csc216.flix_2.customer.MovieCustomerAccountSystem;
import edu.ncsu.csc216.flix_2.rental_system.DVDRentalSystem;
import edu.ncsu.csc216.flix_2.rental_system.RentalManager;

/**
 * A user interface for a DVD rental system.
 * 
 * @author Jo Perry
 */
public class MovieSystemGUI extends JFrame implements ActionListener {
	/** ID for serialization */
	private static final long serialVersionUID = 1L;
	// ----------------------------------------------
	//
	// Class Storage
	//
	// Parameters for component sizes and spacings
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 600;
	private static final int NAME_WIDTH = 25;
	private static final int INVENTORY_LENGTH = 450;
	private static final int QUEUE_LENGTH = 300;
	private static final int CHECKED_OUT_LENGTH = 120;
	private static final int VERTICAL_SPACER = 5;
	private static final int TOP_PAD = 5;
	private static final int LEFT_PAD = 10;
	private static final int RIGHT_PAD = 10;
	private static final int BOTTOM_PAD = 10;

	// Panel and window titles
	private static final String INVENTORY_TITLE = "Movie Inventory";
	private static final String RESERVE_QUEUE_TITLE = "My Queue";
	private static final String CHECKED_OUT_TITLE = "Movies At Home";
	private static final String WINDOW_TITLE = "DVD Rental System";

	// Button and text field strings
	private static final String BROWSE = "Browse";
	private static final String LOGOUT = "Logout";
	private static final String ADMIN = "Admin";
	private static final String QUEUE = "Show My Queue";
	private static final String MOVE = "Move Selected Movie Up";
	private static final String RETURN = "Return Selected Movie";
	private static final String REMOVE = "Remove Selected Movie";
	private static final String LOGIN = "Login";
	private static final String QUIT = "Quit";
	private static final String ADD = "Reserve Selected Movie";
	private static final String ADD_CUSTOMER = "Add New Customer";
	private static final String CANCEL = "Cancel Account";

	// Buttons and combo box
	private JButton btnBrowse = new JButton(BROWSE);
	private JButton btnShowQueue = new JButton(QUEUE);
	private JButton btnAddToQueue = new JButton(ADD);
	private JButton btnMove = new JButton(MOVE);
	private JButton btnRemove = new JButton(REMOVE);
	private JButton btnReturn = new JButton(RETURN);
	private JButton btnLogin = new JButton(LOGIN);
	private JButton btnQuit = new JButton(QUIT);
	private JButton btnLogout = new JButton(LOGOUT);
	private JButton btnAddNewCustomer = new JButton(ADD_CUSTOMER);
	private JButton btnCancelAcct = new JButton(CANCEL);

	// Labels and separator
	private JLabel lblAddedToQueue = new JLabel(" ");
	private JLabel lblUserName = new JLabel("Username: ");
	private JLabel lblPassword = new JLabel("Password: ");

	// Text fields;
	private JTextField txtUserName = new JTextField(NAME_WIDTH);
	private JPasswordField pwdPassword = new JPasswordField(NAME_WIDTH);

	// Default list models for the scrollable lists
	private DefaultListModel dlmInventory = new DefaultListModel();
	private DefaultListModel dlmReserveQueue = new DefaultListModel();
	private DefaultListModel dlmAtHome = new DefaultListModel();

	// Scrollable lists
	private JList lstInventory = new JList(dlmInventory);
	private JList lstReserveQueue = new JList(dlmReserveQueue);
	private JList lstAtHome = new JList(dlmAtHome);
	private JScrollPane scpInventory = new JScrollPane(lstInventory);
	private JScrollPane scpReserveQueue = new JScrollPane(lstReserveQueue);
	private JScrollPane scpAtHome = new JScrollPane(lstAtHome);

	// Organizational and alignment boxes and panels
	private JPanel pnlMovies = new JPanel(); // = new JPanel(new
												// CardLayout(1,1));
	private JPanel pnlButtons = new JPanel(new FlowLayout());
	private JPanel pnlInventoryTop = new JPanel();
	private JPanel pnlAddButton = new JPanel(new FlowLayout());
	private JPanel pnlAddMessage = new JPanel(new FlowLayout());
	private JPanel pnlReturnButton = new JPanel(new FlowLayout());
	private JPanel pnlQueueInstructions = new JPanel(new FlowLayout());
	private JPanel pnlQueue = new JPanel(new BorderLayout());
	private JPanel pnlSeparator = new JPanel();
	private JPanel pnlAdmin = new JPanel();
	private JPanel pnlAdminButtons = new JPanel(new FlowLayout());
	private JPanel pnlLoginInfo = new JPanel(new FlowLayout());
	private JPanel pnlUserName = new JPanel(new FlowLayout());
	private JPanel pnlUserPassword = new JPanel(new FlowLayout());
	private Box boxInventory = Box.createVerticalBox();
	private Box boxQueue = Box.createVerticalBox();
	private CardLayout cardLayout = new CardLayout(1, 1);

	// Main window
	private Container mainWindow = getContentPane();

	// Backend model
	private transient CustomerAccountManager accountManager;
	private transient RentalManager rentals;

	/**
	 * Constructor for FlixBUI. Creates the DVDRentalSystem model and
	 * administrative user. The GUI is initialized and set visible.
	 * 
	 * @param filename
	 *            name of file that initializes the inventory
	 * @throws FileNotFoundException
	 *             if no file of the given filename exists
	 */
	public MovieSystemGUI(String filename) throws FileNotFoundException {
		if (filename == null) {
			String userPickFilename = null;
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				userPickFilename = fc.getSelectedFile().getName();
			}
			rentals = new DVDRentalSystem(userPickFilename);
		} else {
			rentals = new DVDRentalSystem(filename);
		}
		accountManager = new MovieCustomerAccountSystem(rentals);
		if (filename != null)
			populateCustomerAccounts();
		initializeUI();
		this.setVisible(true);
	}

	/**
	 * Makes the GUI respond to button clicks.
	 * 
	 * @param ae
	 *            the button click
	 */
	public void actionPerformed(ActionEvent ae) {

		// Browse titles button
		if (ae.getSource().equals(btnBrowse)) {
			cardLayout.show(pnlMovies, BROWSE);
			lblAddedToQueue.setText(" ");
			refreshInventoryList();
		}
		// Show my queue button
		if (ae.getSource().equals(btnShowQueue)) {
			cardLayout.show(pnlMovies, QUEUE);
			this.refreshQueueAndAtHomeLists();
		}
		// Add to my queue button
		if (ae.getSource().equals(btnAddToQueue)) {
			int k = lstInventory.getSelectedIndex();
			if (k >= 0) {
				rentals.addToCustomerQueue(k);
				lblAddedToQueue.setText("Added: " + (String) dlmInventory.get(k));
				refreshQueueAndAtHomeLists();
				refreshInventoryList();
			}
		}
		// Move to top button
		if (ae.getSource().equals(btnMove)) {
			int k = lstReserveQueue.getSelectedIndex();
			if (k >= 0) {
				rentals.reserveMoveAheadOne(k);
				refreshQueueAndAtHomeLists();
			}
		}
		// Remove selected item button
		if (ae.getSource().equals(btnRemove)) {
			int k = lstReserveQueue.getSelectedIndex();
			if (k >= 0)
				rentals.removeSelectedFromReserves(k);
			refreshQueueAndAtHomeLists();
		}
		// Return selected item button
		if (ae.getSource().equals(btnReturn)) {
			int k = lstAtHome.getSelectedIndex();
			if (k >= 0) {
				rentals.returnItemToInventory(k);
				refreshQueueAndAtHomeLists();
				refreshInventoryList();
			}
		}
		// Administrative functions
		// Quit button
		if (ae.getSource().equals(btnQuit)) {
			stopExecution();
		}
		// Logout
		if (ae.getSource().equals(btnLogout)) {
			cardLayout.show(pnlMovies, ADMIN);
			accountManager.logout();
			enableDisable();
		}
		// Login
		if (ae.getSource().equals(btnLogin)) {
			try {
				accountManager.login(txtUserName.getText(), new String(pwdPassword.getPassword()));
				enableDisable();
				txtUserName.setText("");
				pwdPassword.setText("");
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}

		}

		// Add new customer
		if (ae.getSource().equals(btnAddNewCustomer)) {
			addNewCustomer();
		}

		// Cancel Account
		if (ae.getSource().equals(btnCancelAcct)) {
			try {
				accountManager.cancelAccount(cancelInfo());
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		}
	}

	// ---------------------------------------------------
	//
	// Private methods

	/**
	 * Private Method - populates the customer accounts with two dummy
	 * customers. It is useful for testing.
	 */
	private void populateCustomerAccounts() {
		accountManager.login("admin", "admin");
		accountManager.addNewCustomer("customer1@ncsu.edu", "pw1", 3);
		accountManager.addNewCustomer("customer2@ncsu.edu", "pw2", 2);
		accountManager.logout();
		accountManager.login("customer1@ncsu.edu", "pw1");
		rentals.addToCustomerQueue(86);
		rentals.addToCustomerQueue(77);
		rentals.addToCustomerQueue(73);
		rentals.addToCustomerQueue(87);
		rentals.addToCustomerQueue(77);
		accountManager.logout();
	}

	/**
	 * Private Method - creates the user interface.
	 */
	private void initializeUI() {
		// Initialize the main frame parameters.
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setTitle(WINDOW_TITLE);

		// Set the list contents and behaviors.
		loadModel(lstInventory, dlmInventory, rentals.showInventory());
		lstInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstReserveQueue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstAtHome.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Include all visual components.
		setBordersAndPanels();
		mainWindow.add(pnlButtons, BorderLayout.NORTH);
		mainWindow.add(pnlMovies, BorderLayout.CENTER);

		// Enable buttons to respond to events.
		addListeners();

		enableDisable();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				stopExecution();
			}
		});
	}

	/**
	 * Private method -- Adds action listeners to all the buttons.
	 */
	private void addListeners() {
		btnQuit.addActionListener(this);
		btnBrowse.addActionListener(this);
		btnShowQueue.addActionListener(this);
		btnAddToQueue.addActionListener(this);
		btnMove.addActionListener(this);
		btnReturn.addActionListener(this);
		btnRemove.addActionListener(this);
		btnLogout.addActionListener(this);
		btnLogin.addActionListener(this);
		btnCancelAcct.addActionListener(this);
		btnAddNewCustomer.addActionListener(this);
	}

	/**
	 * Private Method - adds most of the components to the interface and formats
	 * them appropriately.
	 */
	private void setBordersAndPanels() {
		// Set up panel of buttons at the top.
		pnlButtons.add(btnBrowse);
		pnlButtons.add(btnShowQueue);
		pnlButtons.add(btnLogout);

		// Set the borders for scrolling lists
		Font fontTitles = new Font(mainWindow.getFont().getName(), Font.BOLD, mainWindow.getFont().getSize());
		scpInventory.setBorder(BorderFactory.createTitledBorder(scpInventory.getBorder(), INVENTORY_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, fontTitles));
		scpAtHome.setBorder(BorderFactory.createTitledBorder(scpAtHome.getBorder(), CHECKED_OUT_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, fontTitles));
		scpReserveQueue.setBorder(BorderFactory.createTitledBorder(scpReserveQueue.getBorder(), RESERVE_QUEUE_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, fontTitles));
		scpInventory.setPreferredSize(new Dimension(FRAME_WIDTH - LEFT_PAD - RIGHT_PAD, INVENTORY_LENGTH));
		scpAtHome.setPreferredSize(new Dimension(FRAME_WIDTH - LEFT_PAD - RIGHT_PAD, CHECKED_OUT_LENGTH));
		scpReserveQueue.setPreferredSize(new Dimension(FRAME_WIDTH - LEFT_PAD - RIGHT_PAD, QUEUE_LENGTH));

		// Set up the browsing, queue, and admin panels.
		setUpBrowsingPanel();
		setUpQueuePanel();
		setUpAdminPanel();

		// Add browsing, queue, and admin panels to the UI.
		pnlMovies.setLayout(cardLayout);
		pnlMovies.add(pnlAdmin, ADMIN);
		pnlMovies.add(boxInventory, BROWSE);
		pnlMovies.add(boxQueue, QUEUE);
	}

	/**
	 * Private method -- Sets up administrative face.
	 */
	private void setUpAdminPanel() {
		// The top of the Admin panel has a subpanel for login information.
		pnlLoginInfo.setLayout(new BoxLayout(pnlLoginInfo, BoxLayout.PAGE_AXIS));
		// The login info panel has two subpanels, one for username and the
		// other for password.
		pnlUserName.setLayout(new FlowLayout());
		pnlUserPassword.setLayout(new FlowLayout());
		pnlUserName.add(lblUserName);
		pnlUserName.add(txtUserName);
		pnlUserPassword.add(lblPassword);
		pnlUserPassword.add(pwdPassword);
		pnlLoginInfo.add(Box.createVerticalStrut(40));
		pnlLoginInfo.add(pnlUserName);
		pnlLoginInfo.add(pnlUserPassword);
		pnlLoginInfo.add(btnLogin);
		pnlLoginInfo.setBorder(BorderFactory.createCompoundBorder((EmptyBorder) BorderFactory.createEmptyBorder(TOP_PAD, LEFT_PAD, BOTTOM_PAD, RIGHT_PAD),
				BorderFactory.createLineBorder(Color.red)));
		pnlAdmin.setLayout(new BorderLayout());
		pnlAdmin.add(pnlLoginInfo, BorderLayout.NORTH);
		btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

		pnlAdminButtons.add(btnAddNewCustomer);
		pnlAdminButtons.add(btnCancelAcct);
		pnlAdminButtons.add(btnQuit);

		pnlAdmin.add(pnlAdminButtons, BorderLayout.SOUTH);
	}

	/**
	 * Private method -- Sets up queue face.
	 */
	private void setUpQueuePanel() {
		// Add components for the queue and home list.
		boxQueue.setBorder((EmptyBorder) BorderFactory.createEmptyBorder(TOP_PAD, LEFT_PAD, BOTTOM_PAD, RIGHT_PAD));
		boxQueue.add(scpAtHome);
		boxQueue.add(Box.createVerticalStrut(VERTICAL_SPACER));
		boxQueue.add(pnlReturnButton);
		boxQueue.add(pnlSeparator);
		boxQueue.add(Box.createVerticalStrut(VERTICAL_SPACER));
		boxQueue.add(scpReserveQueue);
		boxQueue.add(pnlQueueInstructions);
		pnlQueue.setBorder((EmptyBorder) BorderFactory.createEmptyBorder(TOP_PAD, LEFT_PAD, BOTTOM_PAD, RIGHT_PAD));
		pnlSeparator.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
		pnlQueueInstructions.add(btnMove);
		pnlQueueInstructions.add(btnRemove);
	}

	/**
	 * Private method -- Sets up browsing face.
	 */
	private void setUpBrowsingPanel() {
		// Add the browsing components.
		boxInventory.setBorder((EmptyBorder) BorderFactory.createEmptyBorder(TOP_PAD, LEFT_PAD, BOTTOM_PAD, RIGHT_PAD));
		pnlAddButton.add(btnAddToQueue);
		pnlAddMessage.add(lblAddedToQueue);
		pnlReturnButton.add(btnReturn);
		boxInventory.add(pnlInventoryTop);
		boxInventory.add(scpInventory);
		boxInventory.add(Box.createVerticalStrut(VERTICAL_SPACER));
		boxInventory.add(pnlAddButton);
		boxInventory.add(Box.createVerticalStrut(VERTICAL_SPACER));
		boxInventory.add(pnlAddMessage);
		boxInventory.add(Box.createVerticalStrut(VERTICAL_SPACER * 2));
	}

	/**
	 * Private method -- adds a new customer to the CustomerAccountSystem.
	 */
	private void addNewCustomer() {
		String[] info = newCustomerInfo();
		if (info != null) {
			try {
				accountManager.addNewCustomer(info[0], info[1], Integer.parseInt(info[2]));
			} catch (Exception e) {
				// Note that the message displayed in the JOptionPane is the
				// message passed
				// through the exception object.
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		}
	}

	/**
	 * Private method -- opens a dialog box for entry of new customer username
	 * and password.
	 * 
	 * @return An array of 2 strings, the first is the username and the second
	 *         is the password. The strings are null if the user cancels out.
	 */
	private String[] newCustomerInfo() {
		String[] info = null;
		JPanel pnlCustomerInfo = new JPanel();
		JPanel pnlCustomerName = new JPanel(new FlowLayout());
		JPanel pnlCustomerPassword = new JPanel(new FlowLayout());
		JPanel pnlCustomerLimit = new JPanel(new FlowLayout());
		JTextField txtCustomerUserName = new JTextField(NAME_WIDTH);
		JPasswordField pwdCustomerPassword = new JPasswordField(NAME_WIDTH);
		String[] limits = { "1", "2", "3", "4", "5" };
		JComboBox cboLimit = new JComboBox(limits);

		pnlCustomerInfo.setLayout(new BoxLayout(pnlCustomerInfo, BoxLayout.PAGE_AXIS));
		pnlCustomerName.add(new JLabel("Username: "));
		pnlCustomerName.add(txtCustomerUserName);
		pnlCustomerPassword.add(new JLabel("Password: "));
		pnlCustomerPassword.add(pwdCustomerPassword);
		pnlCustomerLimit.add(new JLabel("Movie Limit: "));
		pnlCustomerLimit.add(cboLimit);
		pnlCustomerInfo.add(pnlCustomerName);
		pnlCustomerInfo.add(pnlCustomerPassword);
		pnlCustomerInfo.add(pnlCustomerLimit);
		int result = JOptionPane.showConfirmDialog(null, pnlCustomerInfo, "Enter the new customer's username and password", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			info = new String[3];
			info[0] = txtCustomerUserName.getText();
			info[1] = new String(pwdCustomerPassword.getPassword());
			info[2] = (String) cboLimit.getSelectedItem();
		}
		return info;
	}

	/**
	 * Private method -- opens a dialog box for entry of account cancellation
	 * info.
	 * 
	 * @return username of account to cancel, or null if the user cancels out.
	 */
	private String cancelInfo() {
		String acctResult = null;
		JPanel pnlCancel = new JPanel(new FlowLayout());
		pnlCancel.add(new JLabel("Username: "));
		JTextField txtCancelUserName = new JTextField(NAME_WIDTH);
		pnlCancel.add(txtCancelUserName);
		int result = JOptionPane.showConfirmDialog(null, pnlCancel, "Enter the username for the account to be cancelled.", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION)
			acctResult = txtCancelUserName.getText().trim();
		return acctResult;
	}

	/**
	 * Private Method -- enables and disables administrative actions.
	 */
	private void enableDisable() {
		boolean adminLoggedIn = accountManager.isAdminLoggedIn();
		boolean customerLoggedIn = accountManager.isCustomerLoggedIn();
		btnQuit.setEnabled(adminLoggedIn);
		btnAddNewCustomer.setEnabled(adminLoggedIn);
		btnCancelAcct.setEnabled(adminLoggedIn);
		btnQuit.setEnabled(adminLoggedIn);
		btnLogin.setEnabled(!adminLoggedIn && !customerLoggedIn);
		btnLogout.setEnabled(adminLoggedIn || customerLoggedIn);

		btnBrowse.setEnabled(customerLoggedIn);
		btnShowQueue.setEnabled(customerLoggedIn);
		btnAddToQueue.setEnabled(customerLoggedIn);
		btnMove.setEnabled(customerLoggedIn);
		btnRemove.setEnabled(customerLoggedIn);
		btnReturn.setEnabled(customerLoggedIn);
	}

	/**
	 * Private Method - loads a list model from a string, using newline
	 * separators.
	 * 
	 * @param j
	 *            the JList to refresh
	 * @param m
	 *            the default list model associated with j
	 * @param info
	 *            the String whose tokens initialize the default list model
	 */
	private void loadModel(JList j, DefaultListModel m, String info) {
		Scanner s = new Scanner(info);
		while (s.hasNext()) {
			m.addElement(s.nextLine());
		}
		j.ensureIndexIsVisible(0);
	}

	/**
	 * Private Method - refreshes lists on the QUEUE card.
	 */
	private void refreshQueueAndAtHomeLists() {
		dlmAtHome.clear();
		dlmReserveQueue.clear();
		try { // Can load these lists only if a customer is logged in
			loadModel(lstAtHome, dlmAtHome, rentals.traverseAtHomeQueue());
			loadModel(lstReserveQueue, dlmReserveQueue, rentals.traverseReserveQueue());
		} catch (IllegalStateException e) {
			// Customer is not logged in
		}
	}

	/**
	 * Private Method - refreshes list on the BROWSE card.
	 */
	private void refreshInventoryList() {
		dlmInventory.clear();
		loadModel(lstInventory, dlmInventory, rentals.showInventory());
	}

	/**
	 * Private Method - exits the program.
	 */
	private static void stopExecution() {
		System.exit(0);
	}

	// ------------------------------------------------
	//
	// Main Method
	//

	/**
	 * Starts the program.
	 * 
	 * @param args
	 *            command line args
	 */
	public static void main(String[] args) {
		try {
			if (args.length > 0)
				new MovieSystemGUI(args[0]);
			else
				new MovieSystemGUI(null);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Incorrect Inventory File Specified");
			stopExecution();
		}
	}

}

// ********** End of FlixGUI.java **********/*

