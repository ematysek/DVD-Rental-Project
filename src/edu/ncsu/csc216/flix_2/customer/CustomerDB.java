package edu.ncsu.csc216.flix_2.customer;

import edu.ncsu.csc216.flix_2.list_util.MultiPurposeList;

/**
 * This class keeps a running list of customers in the system. Allows for
 * functionalities including adding new customers, canceling accounts, and
 * verifying customer accounts.
 * 
 * @author Eric Matysek, Nick Brust
 * 
 */
public class CustomerDB {

	/**
	 * Max number of customers allowed in the database
	 */
	public static final int MAX_SIZE = 20;

	/**
	 * The number of customers currently in the system.
	 */
	private int size;

	/**
	 * The database of customer objects.
	 */
	private MultiPurposeList<Customer> list;

	/**
	 * Constructs a new CustomerDB, initializing the list and setting the inital
	 * size to 0.
	 */
	public CustomerDB() {
		this.list = new MultiPurposeList<Customer>();
		this.size = 0;
	}

	/**
	 * Returns the customer in the list whose username and password match the
	 * ones that are given. If either the given username or given password is
	 * null of if there are no customers in the list that match completely, then
	 * an IllegalArgumentException is thrown.
	 * 
	 * @param id
	 *            The username to find within the list.
	 * @param password
	 *            The password to match with the customer found.
	 * @throws IllegalArgumentException
	 *             if the id or password given is null, customer doesn't exist,
	 *             or if password is incorrect.
	 * @return The customer with matching credentials.
	 */
	public Customer verifyCustomer(String id, String password) {
		if (id == null || password == null || id.equals("") || password.equals("")) {
			throw new IllegalArgumentException("The account doesn't exist.");
		}
		this.list.resetIterator();
		Customer c;
		while (this.list.hasNext()) {
			c = this.list.next();
			if (c.getId().equals(id) && c.verifyPassword(password)) {
				if (c.verifyPassword(password)) {
					return c;
				} else {
					throw new IllegalArgumentException("Incorrect Password");
				}
			}
		}
		throw new IllegalArgumentException("The account doesn't exist.");
	}

	/**
	 * List of usernames in the database.
	 * 
	 * @return A list of all customer usernames.
	 */
	public String listAccounts() {
		this.list.resetIterator();
		String s = "";
		while (this.list.hasNext()) {
			Customer c = this.list.next();
			s += c.getId();
			s += "\n";
		}
		return s;
	}

	/**
	 * Adds a new customer to the database given an username, password, and max
	 * movies at home. throws an IllegalStateException if the database is full.
	 * Throws and IllegalArgumentException if there is whitespace in either
	 * strings, if either strings are empty, or if another customer with that
	 * username already exists.
	 * 
	 * @param id
	 *            Username for the new customer.
	 * @param password
	 *            Password for the new customer.
	 * @param maxAtHome
	 *            maximum number of movies the new customer is allowed at home.
	 * @throws IllegalStateException
	 *             if the max number of customers has already been reached.
	 * @throws IllegalArgumentException
	 *             if the customer already exists or the username/password is
	 *             invalid.
	 */
	public void addNewCustomer(String id, String password, int maxAtHome) {
		if (this.size >= MAX_SIZE) {
			throw new IllegalStateException("There is no room for additional customers.");
		}
		if (id.contains(" ") || password.contains(" ") || id.equals("") || password.equals("")) {
			throw new IllegalArgumentException("Username and password must have non-whitespace characters.");
		}
		if (!this.isNewCustomer(id)) {
			throw new IllegalArgumentException("Customer already has an account.");
		}
		this.insert(new Customer(id, password, maxAtHome));
		size++;
	}

	/**
	 * Removes the customer with the given username from the database and
	 * returns all movies the customer has at home. Throws an
	 * IllegalArgumentException if the account does not exist.
	 * 
	 * @param id
	 *            Username of the customer to be cancelled.
	 * @throws IllegalArgumentException
	 *             if the customer is not found.
	 */
	public void cancelAccount(String id) {
		if (this.isNewCustomer(id)) {
			throw new IllegalArgumentException("No matching customer account found.");
		}
		this.list.remove(findMatchingAccount(id)).closeAccount();
	}

	/**
	 * Returns true if there are no customers in the database that match the id
	 * given.
	 * 
	 * @param id
	 *            username of the customer to check
	 * @return True if there are no customers with the same username within the
	 *         database.
	 */
	private boolean isNewCustomer(String id) {
		if (findMatchingAccount(id) > -1) {
			return false;
		}
		return true;
	}

	/**
	 * Adds the given customer to the end of the database.
	 * 
	 * @param customer
	 *            Customer to be added.
	 * @throws IllegalArgumentException
	 *             if the customer already has an account.
	 */
	private void insert(Customer customer) {
		if (this.size >= MAX_SIZE) {
			throw new IllegalStateException("There is no room for additional customers.");
		}
		if (list.isEmpty()) {
			list.addItem(0, customer);
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			Customer c = list.lookAtItemN(i);
			int comparator = c.compareToByName(customer);
			if (comparator == 0) {
				throw new IllegalArgumentException("Customer already has an account.");
			}
			if (comparator > 0) {
				list.addItem(i, customer);
				return;
			}
		}
		list.addToRear(customer);
	}

	/**
	 * Sees if there is a customer within the database whose username matches
	 * the id given. if there is, the index of the matching customer is given.
	 * 
	 * @param id
	 *            Username of the customer to find within the database.
	 * @return Index of the matching customer, or -1 if the customer does not
	 *         exis within the database.
	 */
	private int findMatchingAccount(String id) {
		this.list.resetIterator();
		Customer c;
		int index = 0;
		while (this.list.hasNext()) {
			c = this.list.next();
			if (c.getId().equalsIgnoreCase(id)) {
				return index;
			}
			index++;
		}
		return -1;
	}
}
