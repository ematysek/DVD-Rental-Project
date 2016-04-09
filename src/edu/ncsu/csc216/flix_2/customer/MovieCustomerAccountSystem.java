package edu.ncsu.csc216.flix_2.customer;

import edu.ncsu.csc216.flix_2.rental_system.RentalManager;

/**
 * This class controlls the flow of the system and delegates tasks to their
 * appropriate places. Keeps track of the state of the currently logged in
 * customer or admin and holds the movie inventory system along with a running
 * list of customers.
 * 
 * @author Eric Matysek, Nick Brust
 * 
 */
public class MovieCustomerAccountSystem implements CustomerAccountManager {

	/**
	 * True if the admin is logged into the system.
	 */
	private boolean adminLoggedIn;

	/**
	 * True if and only if a customer is logged into the system.
	 */
	private boolean customerLoggedIn;

	/**
	 * username of the admin account.
	 */
	public static final String ADMIN = "admin";

	/**
	 * The rental inventory of the system.
	 */
	private RentalManager inventorySystem;

	/**
	 * Database of customers in the system.
	 */
	private CustomerDB customerList;

	/**
	 * Constructs a new MovieCustomerAccountSystem given the inventory system.
	 * 
	 * @param inventorySystem
	 *            The rental inventory for the system.
	 */
	public MovieCustomerAccountSystem(RentalManager inventorySystem) {
		this.inventorySystem = inventorySystem;
		this.adminLoggedIn = false;
		this.customerLoggedIn = false;
		this.customerList = new CustomerDB();
	}

	/**
	 * Logs a user into the system.
	 * 
	 * @param id
	 *            id/username of the user
	 * @param password
	 *            user's password
	 * @throws IllegalStateException
	 *             if a customer or the administrator is already logged in
	 * @throws IllegalArgumentException
	 *             if the customer account does not exist
	 */
	public void login(String id, String password) {
		if (this.customerLoggedIn || this.adminLoggedIn) {
			throw new IllegalStateException("Current customer or admin must first log out.");
		}
		if (id.equals(ADMIN) && password.equals(ADMIN)) {
			this.adminLoggedIn = true;
		} else {
			Customer c = this.customerList.verifyCustomer(id, password);
			this.inventorySystem.setCustomer(c);
			this.customerLoggedIn = true;
		}
	}

	/**
	 * Logs the current customer or administrator out of the system.
	 */
	public void logout() {
		this.inventorySystem.setCustomer(null);
		this.adminLoggedIn = false;
		this.customerLoggedIn = false;
	}

	/**
	 * Is an administrator logged into the system?
	 * 
	 * @return true if yes, false if no
	 */
	public boolean isAdminLoggedIn() {
		return this.adminLoggedIn;
	}

	/**
	 * Is a customer logged into the system?
	 * 
	 * @return true if yes, false if no
	 */
	public boolean isCustomerLoggedIn() {
		return this.customerLoggedIn;
	}

	/**
	 * Add a new customer to the customer database. The administrator must be
	 * logged in.
	 * 
	 * @param id
	 *            id/email for new customer
	 * @param password
	 *            new customer's password
	 * @param maxAtHome
	 *            number associated with this customer
	 * @throws IllegalStateException
	 *             if the database is full or the administrator is not logged in
	 * @throws IllegalArgumentException
	 *             if customer with given id is already in the database
	 */
	public void addNewCustomer(String id, String password, int maxAtHome) {
		if(this.adminLoggedIn){
			this.customerList.addNewCustomer(id, password, maxAtHome);
		} else if(!this.adminLoggedIn){
			throw new IllegalStateException("Access denied.");
		}
	}

	/**
	 * Cancel a customer account.
	 * 
	 * @param id
	 *            id/username of the customer to cancel
	 * @throws IllegalStateException
	 *             if the administrator is not logged in
	 * @throws IllegalArgumentException
	 *             if no matching account is found
	 */
	public void cancelAccount(String id) {
		this.customerList.cancelAccount(id);
	}

	/**
	 * List all customer accounts.
	 * 
	 * @return string of customer usernames separated by newlines
	 */
	public String listAcounts() {
		return this.customerList.listAccounts();
	}

}
