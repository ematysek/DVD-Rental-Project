package edu.ncsu.csc216.flix_2.rental_system;

import edu.ncsu.csc216.flix_2.customer.Customer;
import edu.ncsu.csc216.flix_2.inventory.MovieDB;

/**
 * Represents the rental system where the movies for rent are stored in an
 * inventory and where there are different customers. Movies can be reserved,
 * checked out for home, and returned to the inventory. Movies in the the
 * inventory, reserves, and at home can be located by position.
 * 
 * @author Nick Brust, Eric Matysek
 */
public class DVDRentalSystem implements RentalManager {

	private MovieDB inventory;

	private Customer currentCustomer;

	/**
	 * Constructor, creating the system by filing out the inventory from a file.
	 * 
	 * @throws IllegalArgumentException
	 *             if the file cannot be read.
	 * @param file
	 *            String that contains the MovieDB information.
	 */
	public DVDRentalSystem(String file) {
		inventory = new MovieDB(file);
	}

	/**
	 * Set the customer for the current context to a given value.
	 * 
	 * @param customer
	 *            the new current customer.
	 */
	public void setCustomer(Customer customer) {
		currentCustomer = customer;
	}

	/**
	 * Traverses all movies in the inventory.
	 * 
	 * @return the string representing the movies in the inventory.
	 */
	public String showInventory() {
		return inventory.traverse();
	}

	/**
	 * Reserve the selected movie for the reserve queue.
	 * 
	 * @param num
	 *            position of the selected movie in the inventory.
	 * @throws IllegalStateException
	 *             if no customer is logged in.
	 * @throws IllegalArgumentException
	 *             if position is out of bounds.
	 */
	public void addToCustomerQueue(int num) {
		if (currentCustomer == null) {
			throw new IllegalStateException("No customer is logged in.");
		}
		currentCustomer.reserve(inventory.findItemAt(num));
	}

	/**
	 * Move the movie in the given position up 1 in the reserve queue.
	 * 
	 * @param num
	 *            current position of item to move up one
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	public void reserveMoveAheadOne(int num) {
		if (currentCustomer == null) {
			throw new IllegalStateException("No customer is logged in.");
		}
		currentCustomer.moveAheadOneInReserves(num);
	}

	/**
	 * Remove the movie in the given position from the reserve queue.
	 * 
	 * @param num
	 *            position of the item in the queue
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 * @throws IllegalArgumentException
	 *             if position is out of bounds
	 */
	public void removeSelectedFromReserves(int num) {
		if (currentCustomer == null) {
			throw new IllegalStateException("No customer is logged in.");
		}
		currentCustomer.unReserve(num);
	}

	/**
	 * Traverse all movies in the reserve queue.
	 * 
	 * @return string representation of movie in the queue
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	public String traverseReserveQueue() {
		if (currentCustomer == null) {
			throw new IllegalStateException("No customer is logged in.");
		}
		return currentCustomer.traverseReserveQueue();
	}

	/**
	 * Traverse all movies in the atHome queue.
	 * 
	 * @return string representation of movies at home
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	public String traverseAtHomeQueue() {
		if (currentCustomer == null) {
			throw new IllegalStateException("No customer is logged in.");
		}
		return currentCustomer.traverseAtHomeQueue();
	}

	/**
	 * Return the selected movie to the inventory.
	 * 
	 * @param num
	 *            location in the list of movie at home of the item to return
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 * @throws IllegalArgumentException
	 *             if position is out of bounds
	 */
	public void returnItemToInventory(int num) {
		if (currentCustomer == null) {
			throw new IllegalStateException("No customer is logged in.");
		}
		currentCustomer.returnDVD(num);
	}
}
