package edu.ncsu.csc216.flix_2.rental_system;

import edu.ncsu.csc216.flix_2.customer.Customer;

/**
 * Interface for a rental system where the items for rent are stored in an
 * inventory and where there are different customers. Items can be reserved,
 * checked out for home, and returned to the inventory. Items in the the
 * inventory, reserves, and at home can be located by position.
 * 
 * @author Jo Perry
 */
public interface RentalManager {

	/**
	 * Traverse all items in the inventory.
	 * 
	 * @return the string representing the items in the inventory
	 */
	public String showInventory();

	/**
	 * Set the customer for the current context to a given value.
	 * 
	 * @param c
	 *            the new current customer
	 */
	public void setCustomer(Customer c);

	/**
	 * Reserve the selected item for the reserve queue.
	 * 
	 * @param position
	 *            position of the selected item in the inventory
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 * @throws IllegalArgumentException
	 *             if position is out of bounds
	 */
	public void addToCustomerQueue(int position);

	/**
	 * Move the item in the given position up 1 in the reserve queue.
	 * 
	 * @param position
	 *            current position of item to move up one
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	public void reserveMoveAheadOne(int position);

	/**
	 * Remove the item in the given position from the reserve queue.
	 * 
	 * @param position
	 *            position of the item in the queue
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 * @throws IllegalArgumentException
	 *             if position is out of bounds
	 */
	public void removeSelectedFromReserves(int position);

	/**
	 * Traverse all items in the reserve queue.
	 * 
	 * @return string representation of items in the queue
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	public String traverseReserveQueue();

	/**
	 * Traverse all items in the reserve queue.
	 * 
	 * @return string representation of items at home
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	public String traverseAtHomeQueue();

	/**
	 * Return the selected item to the inventory.
	 * 
	 * @param position
	 *            location in the list of items at home of the item to return
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 * @throws IllegalArgumentException
	 *             if position is out of bounds
	 */
	public void returnItemToInventory(int position);

}
