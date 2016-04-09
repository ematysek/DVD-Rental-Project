package edu.ncsu.csc216.flix_2.inventory;

import java.util.Scanner;

/**
 * Represents a movie in the system.
 * 
 * @author Nick Brust, Eric Matysek
 */
public class Movie {

	/**
	 * Title of movie.
	 */
	private String name;

	/**
	 * Number of movies in the inventory.
	 */
	private int inStock;

	/**
	 * Constructor, creating Movie objects from a string.
	 * 
	 * @throws IllegalArgumentException
	 *             if the string is invalid.
	 * @param line
	 *            String containing title and quantity information, read from
	 *            file.
	 */
	public Movie(String line) {
		int numInStock;
		String movieTitle;
		Scanner console = new Scanner(line);

		if (!console.hasNextInt()) {
			console.close();
			throw new IllegalArgumentException();
		}

		numInStock = console.nextInt();

		if (!console.hasNext()) {
			console.close();
			throw new IllegalArgumentException();
		}
		movieTitle = console.next();

		if (console.hasNextLine()) {
			movieTitle += console.nextLine();
			if (movieTitle.startsWith("A ") || movieTitle.startsWith("a ")) {
				movieTitle = movieTitle.substring(2);
			} else if (movieTitle.startsWith("An ") || movieTitle.startsWith("an ")){
				movieTitle = movieTitle.substring(3);
			} else if (movieTitle.startsWith("The ") || movieTitle.startsWith("the ")) {
				movieTitle = movieTitle.substring(4);
			}
		}

		if (console.hasNext()) {
			console.close();
			throw new IllegalArgumentException();
		} else {
			console.close();
			inStock = numInStock;
			name = movieTitle;
		}
	}

	/**
	 * Returns the movie title.
	 * 
	 * @return movie title.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the movie title and denotes whether it is out of stock.
	 * 
	 * @return movie title and denotes whether it is out of stock.
	 */
	public String getDisplayName() {
		if (!isAvailable()) {
			return name + " (currently unavailable)";
		} else {
			return name;
		}
	}

	/**
	 * Compares movie titles lexicographically.
	 * 
	 * @param movie
	 *            Movie object whose title is used for comparison.
	 * @return integer denoting lexicographic order.
	 */
	public int compareToByName(Movie movie) {
		return this.getName().compareToIgnoreCase(movie.getName());
	}

	/**
	 * Determines whether or not the movie is in inventory.
	 * 
	 * @return boolean value denoting if the movie is in inventory.
	 */
	public boolean isAvailable() {
		return inStock > 0;
	}

	/**
	 * Places a copy of a movie into inventory.
	 */
	public void backToInventory() {
		inStock++;
	}

	/**
	 * Removes a copy of a movie from inventory.
	 * 
	 * @throws IllegalArgumentException
	 *             if attempting to remove a movie with no copies in inventory.
	 */
	public void removeOneCopyFromInventory() {
		if (inStock <= 0) {
			throw new IllegalStateException("No copy of this movie currently available.");
		} else {
			inStock--;
		}
	}
}
