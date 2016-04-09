package edu.ncsu.csc216.flix_2.inventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc216.flix_2.list_util.MultiPurposeList;

/**
 * Represents an internal database of movies.
 * 
 * @author Nick Brust, Eric Matysek
 */
public class MovieDB {

	/**
	 * List of movies in the inventory.
	 */
	private MultiPurposeList<Movie> movies;

	/**
	 * Constructor, creates a movie database from a file.
	 * 
	 * @throws IllegalArgumentException
	 *             if the file cannot be read.
	 * @param fileName
	 *            name of the file containing the movie information.
	 */

	public MovieDB(String fileName) {
		Scanner console;
		try {
			File file = new File(fileName);
			console = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException();
		}
		Movie movie;
		this.movies = new MultiPurposeList<Movie>();

		if (!console.hasNext()) {
			console.close();
			throw new IllegalArgumentException();
		} else {
			while (console.hasNextLine()) {
				String line = console.nextLine();
				movie = new Movie(line);
				if (movies.size() == 0) {
					movies.addItem(0, movie);
				} else {
					int placeHolder = 0;
					int size = movies.size();
					movies.resetIterator();
					while (movies.hasNext()) {
						Movie next = movies.next();
						int difference = movie.compareToByName(next);
						if (difference < 0) {
							movies.addItem(placeHolder, movie);
							break;
						} else {
							placeHolder++;
							if (placeHolder == size) {
								movies.addToRear(movie);
							}
						}
					}
				}
			}
			console.close();
		}
	}

	/**
	 * Creates a String containing all of the movies using their display name,
	 * separated by new lines.
	 * 
	 * @return String containing all of the movies using their display name,
	 *         separated by new lines.
	 */
	public String traverse() {
		String list = "";
		movies.resetIterator();
		while (movies.hasNext()) {
			list += movies.next().getDisplayName() + "\n";
		}
		return list;
	}

	/**
	 * Returns the movie at the given position.
	 * 
	 * @throws IllegalArgumentException
	 *             if the index is out of range.
	 * @param index
	 *            position of the movie in the list.
	 * @return Movie at the given position.
	 */
	public Movie findItemAt(int index) {
		if (index < 0 || index >= movies.size()) {
			throw new IllegalArgumentException();
		}
		return movies.lookAtItemN(index);
	}
}
