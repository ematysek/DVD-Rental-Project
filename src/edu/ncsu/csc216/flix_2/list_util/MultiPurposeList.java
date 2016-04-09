package edu.ncsu.csc216.flix_2.list_util;

/**
 * Generic list class.
 * 
 * @param <T>
 *            Object type that the list is made up of.
 * @author Nick Brust, Eric Matysek
 */
public class MultiPurposeList<T> {

	/**
	 * Used as a cursor, to remember position.
	 */
	private Node iterator;

	/**
	 * Beginning node of the list.
	 */
	private Node head;

	/**
	 * Constructor, creates an empty list.
	 */
	public MultiPurposeList() {
		iterator = head;
	}

	/**
	 * Resets the iterator by making it point back to the beginning of the list.
	 */
	public void resetIterator() {
		iterator = head;
	}

	/**
	 * Determines whether or not the list has another element.
	 * 
	 * @return boolean value denoting whether or not the list has another
	 *         element.
	 */
	public boolean hasNext() {
		return iterator != null;
	}

	/**
	 * Returns the element the iterator is pointing to and moves the iterator to
	 * point to the next element in the list.
	 * 
	 * @throws NullPointerException
	 *             if the iterator is not pointing to an element.
	 * @return element the iterator is pointing to.
	 */
	public T next() {
		if (!this.hasNext()) {
			return null;
		}
		T element = iterator.data;
		iterator = iterator.next;
		return element;
	}

	/**
	 * Inserts an element at the given position.
	 * 
	 * @param num
	 *            position to insert the element.
	 * @param t
	 *            element to be added.
	 */
	public void addItem(int num, T t) {
		Node holder = new Node(null, null);
		this.resetIterator();
		if (this.isEmpty()) {
			head = new Node(t, null);
			return;
		}
		if (num <= 0) {
			head = new Node(t, head);
		} else if (num >= this.size()) {
			this.resetIterator();
			while (this.hasNext()) {
				holder = iterator;
				this.next();
			}
			holder.next = new Node(t, null);
		} else {
			this.resetIterator();
			for (int i = 0; i < num - 1; i++) {
				this.next();
			}
			iterator.next = new Node(t, iterator.next);
		}
	}

	/**
	 * Determines whether or not the list is empty.
	 * 
	 * @return boolean value denoting whether or not the list is empty.
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Returns the element at the given position, or null is the position is out
	 * of range.
	 * 
	 * @param num
	 *            the position in the list.
	 * @return element at the given position, or null is the position is out of
	 *         range.
	 */
	public T lookAtItemN(int num) {
		if (num < 0 || num >= this.size()) {
			return null;
		} else {
			this.resetIterator();
			int i = 0;
			while (this.hasNext() && i < num) {
				this.next();
				i++;
			}
			return iterator.data;
		}
	}

	/**
	 * Adds an element to the rear of the list.
	 * 
	 * @param t
	 *            element to be added.
	 */
	public void addToRear(T t) {
		if (this.size() == 0) {
			head = new Node(t, null);
		} else {
			Node holder = new Node(null, null);
			this.resetIterator();
			while (this.hasNext()) {
				holder = iterator;
				this.next();
			}
			holder.next = new Node(t, null);
		}
	}

	/**
	 * Removes and returns the element in the given position or null if the
	 * position is out of range.
	 * 
	 * @param num
	 *            the position in the list.
	 * @return element in the given position or null if the position is out of
	 *         range.
	 */
	public T remove(int num) {
		this.resetIterator();
		Node holder;
		if (num < 0 || num >= this.size()) {
			return null;
		}
		if (num == 0) {
			holder = head;
			head = head.next;
			return holder.data;
		} else {
			this.resetIterator();
			for (int i = 0; i < num - 1; i++) {
				this.next();
			}
			holder = iterator.next;
			iterator.next = iterator.next.next;
			return holder.data;
		}
	}

	/**
	 * Moves the element at the given position ahead one position in the list.
	 * Does nothing if the element is already at the front of the list or the
	 * position is out of range.
	 * 
	 * @param num
	 *            the position in the list.
	 */
	public void moveAheadOne(int num) {
		this.resetIterator();
		Node holder1;
		Node holder2;
		Node holder3;
		if (num <= 0 || num >= this.size()) {
			return;
		}
		if (num == 1) {
			holder1 = head.next;
			head.next = holder1.next;
			holder1.next = head;
			head = holder1;
		} else {
			this.resetIterator();
			for (int i = 0; i < num - 2; i++) {
				this.next();
			}
			holder1 = iterator;
			this.next();
			holder2 = iterator;
			this.next();
			holder3 = iterator;
			holder2.next = holder3.next;
			holder3.next = holder2;
			holder1.next = holder3;
		}
	}

	/**
	 * Returns the number of elements in the list.
	 * 
	 * @return number of elements in the list.
	 */
	public int size() {
		this.resetIterator();
		if (this.isEmpty()) {
			return 0;
		} else {
			int size = 0;
			while (this.hasNext()) {
				this.next();
				size++;
			}
			return size;
		}
	}

	/**
	 * Represents a position in the list, holding data and pointing to the next
	 * Node.
	 * 
	 * @author Nick Brust
	 */
	public class Node {

		/**
		 * Holds Object information.
		 */
		public T data;

		/**
		 * Pointer to the next Node in the list.
		 */
		public Node next;

		/**
		 * Constructor, creates a Node, holding Object information and pointing
		 * to the next Node.
		 * 
		 * @param t
		 *            Object to be stored in the Node.
		 * @param node
		 *            the next Node in the list.
		 */
		public Node(T t, Node node) {
			data = t;
			next = node;
		}
	}
}
