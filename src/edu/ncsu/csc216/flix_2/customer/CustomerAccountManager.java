package edu.ncsu.csc216.flix_2.customer;

/**
 * Describes behaviors of a customer management systems that permits user login.
 * The management system has an administrator.
 * 
 * @author Jo Perry
 */
public interface CustomerAccountManager {

	/**
	 * Logs a user into the system.
	 * 
	 * @param username
	 *            id/username of the user
	 * @param password
	 *            user's password
	 * @throws IllegalStateException
	 *             if a customer or the administrator is already logged in
	 * @throws IllegalArgumentException
	 *             if the customer account does not exist
	 */
	public void login(String username, String password);

	/**
	 * Logs the current customer or administrator out of the system.
	 */
	public void logout();

	/**
	 * Is an administrator logged into the system?
	 * 
	 * @return true if yes, false if no
	 */
	public boolean isAdminLoggedIn();

	/**
	 * Is a customer logged into the system?
	 * 
	 * @return true if yes, false if no
	 */
	public boolean isCustomerLoggedIn();

	/**
	 * Add a new customer to the customer database. The administrator must be
	 * logged in.
	 * 
	 * @param id
	 *            id/email for new customer
	 * @param password
	 *            new customer's password
	 * @param num
	 *            number associated with this customer
	 * @throws IllegalStateException
	 *             if the database is full or the administrator is not logged in
	 * @throws IllegalArgumentException
	 *             if customer with given id is already in the database
	 */
	public void addNewCustomer(String id, String password, int num);

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
	public void cancelAccount(String id);

	/**
	 * List all customer accounts.
	 * 
	 * @return string of customer usernames separated by newlines
	 */
	public String listAcounts();

}
