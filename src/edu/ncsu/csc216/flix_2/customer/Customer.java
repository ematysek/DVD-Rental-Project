package edu.ncsu.csc216.flix_2.customer;

import edu.ncsu.csc216.flix_2.inventory.Movie;
import edu.ncsu.csc216.flix_2.list_util.MultiPurposeList;

/**
 * This class is the customer object. It holds the current state of any given
 * customer including username, password, max number of movies at home, number
 * of movies currently at home, movies in this customer's reserve queue, and the
 * movies in this customer's queue at home.
 * 
 * @author Eric Matysek, Nick Brust
 * 
 */
public class Customer {

	/**
	 * The customer's username.
	 */
	private String id;

	/**
	 * The customer's password.
	 */
	private String password;

	/**
	 * The maximum number of movies this customer is allowed to have at home.
	 */
	private int maxAtHome;

	/**
	 * The number of rental movies this customer currently has at home.
	 */
	private int nowAtHome;

	/**
	 * A list of movie objects the customer currently has at home.
	 */
	private MultiPurposeList<Movie> atHomeQueue;

	/**
	 * A list of movie objects the customer currently has on reserve.
	 */
	private MultiPurposeList<Movie> reserveQueue;

	/**
	 * Constructs a new Customer object given the username, passworde, and max
	 * allowed movies at home. This constructor checks for empty strings, and
	 * incorrect values of maxAtHome. If the value passed for maxAtHome is
	 * negative, it is re-set to zero. If an empty string or null is passed for
	 * the username or password, an IllegalArgumentException is thrown.
	 * 
	 * @param id
	 *            The username for the customer.
	 * @param password
	 *            The password for the customer.
	 * @param maxAtHome
	 *            The maximum allowed movies this customer can have at home at
	 *            any given time.
	 */
	public Customer(String id, String password, int maxAtHome) {

		if (id == null || password == null) {
			throw new IllegalArgumentException();
		}

		String trimmedId = id.trim();
		String trimmedPassword = password.trim();

		if (trimmedId.length() == 0 || trimmedPassword.length() == 0) {
			throw new IllegalArgumentException();
		}

		int checkedMax = maxAtHome;
		if (maxAtHome < 0) {
			checkedMax = 0;
		}

		this.id = trimmedId;
		this.password = trimmedPassword;
		this.maxAtHome = checkedMax;
		this.nowAtHome = 0;
		this.atHomeQueue = new MultiPurposeList<Movie>();
		this.reserveQueue = new MultiPurposeList<Movie>();
	}

	/**
	 * Checks if the password provided matches the password of this customer.
	 * 
	 * @param password
	 *            The entered password.
	 * @return Returns true if the password matches, false otherwise.
	 */
	public boolean verifyPassword(String password) {
		return password.trim().equals(this.password.trim());
	}

	/**
	 * Returns the username of the customer.
	 * 
	 * @return Returns the username of the customer.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Compares the given customer's username to this customer's username. If
	 * the two usernames are the same, 0 is returned; a value less than 0 if
	 * this customer's username is lexicographically less than the given
	 * customer's username; a value greater than 0 if this customer's username
	 * is lexicographically greater than the give customer's username.
	 * 
	 * @param customer
	 *            The customer whose username to compare to.
	 * @return Returns 0 if the two usernames are the same; a value less than 0
	 *         if this customer's username is lexicographically less than the
	 *         given customer's username; a value greater than 0 if this
	 *         customer's username is lexicographically greater than the give
	 *         customer's username.
	 */
	public int compareToByName(Customer customer) {
		return this.id.compareToIgnoreCase(customer.getId());
	}

	/**
	 * Gets the list of movies in this customer's reserve queue. Each movie name
	 * is on it's own line.
	 * 
	 * @return list of movie names in this customer's reserve queue.
	 */
	public String traverseReserveQueue() {
		return this.traverseQueue(this.reserveQueue);
	}

	/**
	 * Gets the list of movies that this customer currently has at home. Each
	 * movie name is on its own line.
	 * 
	 * @return list of movie names that this customer currently has at home.
	 */
	public String traverseAtHomeQueue() {
		return this.traverseQueue(this.atHomeQueue);
	}

	/**
	 * Unreserves all movies in the reserve queue and returns all movies at
	 * home.
	 */
	public void closeAccount() {
		/*
		while (this.reserveQueue.size() > 0) {
			this.unReserve(0);
		}
		*/
		while (this.atHomeQueue.size() > 0) {
			this.atHomeQueue.remove(0);
		}

	}

	/**
	 * Returns the DVD at the specified index to the movie inventory and checks
	 * out the next movie in the reserve.
	 * 
	 * @param index
	 *            index of the DVD to return.
	 * @throws IllegalArgumentException
	 *             if the index given is out of bounds.
	 */
	public void returnDVD(int index) {
		if (index < 0 || index > (this.atHomeQueue.size() - 1)) {
			throw new IllegalArgumentException("Index given is out of bounds.");
		}
		Movie returnMovie = this.atHomeQueue.lookAtItemN(index);
		this.atHomeQueue.remove(index);
		returnMovie.backToInventory();
		this.nowAtHome--;
		this.checkOut();
	}

	/**
	 * Moves the movie at the specified index in the reserve queue forward one
	 * in the list.
	 * 
	 * @param index
	 *            Index of the movie to move.
	 * @throws IllegalArgumentException
	 *             if the index given is out of bounds.
	 */
	public void moveAheadOneInReserves(int index) {
		if (index < 0 || index > (this.reserveQueue.size() - 1)) {
			throw new IllegalArgumentException("Index given is out of bounds.");
		}
		if (index == 0) {
			return;
		}
		this.reserveQueue.moveAheadOne(index);

	}

	/**
	 * Removes the movie in the given position from the reserve queue. Throws an
	 * IllegalArgumentException if the index is out of bounds.
	 * 
	 * @param index
	 *            Index of the movie to be removed from the reserve queue.
	 * @throws IllegalArgumentException
	 *             if the index given is out of bounds.
	 */
	public void unReserve(int index) {
		if (index < 0 || index > (this.reserveQueue.size() - 1)) {
			throw new IllegalArgumentException("No movie selected.");
		}
		this.reserveQueue.remove(index);
	}

	/**
	 * Places the movie at the end of this customer's reserve queue. Throws an
	 * IllegalArgumentException if the index is out of bounds and the movie
	 * given is null.
	 * 
	 * @param movie
	 *            Movie to be reserved.
	 * @throws IllegalArgumentException
	 *             if the Movie object given is null.
	 */
	public void reserve(Movie movie) {
		if (movie == null) {
			throw new IllegalArgumentException("Movie not specified.");
		}
		this.reserveQueue.addToRear(movie);
		this.checkOut();
	}

	/**
	 * Traverses the list of movies creating a string that contains all the
	 * names of the movies in the list, each on their own line.
	 * 
	 * @param list
	 *            The list to be traversed.
	 * @return String of all movie names, each on its own line.
	 */
	private String traverseQueue(MultiPurposeList<Movie> list) {
		String movieList = "";
		list.resetIterator();
		while (list.hasNext()) {
			movieList += list.next().getName() + "\n";
		}

		return movieList;
	}

	/**
	 * Removes the first available movie from the reserve queue and adds it to
	 * the back of the at home queue. If the reserve queue is empty or no movies
	 * are available, nothing happens. If the number of movies at home is equal
	 * to the max at home, then nothing happens.
	 */
	private void checkOut() {
		if (this.nowAtHome == this.maxAtHome) {
			return;
		}
		Movie nextAvailable = this.removeFirstAvailable();
		if (nextAvailable == null) {
			return;
		}
		this.atHomeQueue.addToRear(nextAvailable);
		nextAvailable.removeOneCopyFromInventory();
		this.nowAtHome++;
	}

	/**
	 * Removes the first available movie from the reserve queue and returns it.
	 * If none are available or the queue is empty, null is returned.
	 * 
	 * @return The first available movie or null if none are available or if the
	 *         queue is empty.
	 */
	private Movie removeFirstAvailable() {
		Movie firstAvailableMovie = null;
		for (int i = 0; i < this.reserveQueue.size(); i++) {
			firstAvailableMovie = this.reserveQueue.lookAtItemN(i);
			if (firstAvailableMovie.isAvailable()) {
				return this.reserveQueue.remove(i);
			}
		}
		return null;
	}
}
