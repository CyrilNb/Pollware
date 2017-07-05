package cm3033.CyrilNiobe;

/**
 * Class which represents a Nominated in the Big Brother Game
 * @author Cyril Niobé - 2016
 * Module: Concurrent Programming Coursework - Robert Gordon University
 */
public class Nominated {

	/**
	 * int which represents the number of vote this nominated received.
	 * Initaliased at 0.
	 */
	private int NumberOfVote = 0;
	
	/**
	 * string which represents the name of the nominated
	 */
	private String name;
	
	/**
	 * Constructor of a Nominated
	 * @param n : name of the person nominated
	 */
	public Nominated(String n){
		this.setName(n);
	}

	//GETTER AND SETTER OF PRIVATE MEMBERS
	public synchronized int getNumberOfVote() {
		return NumberOfVote;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

        /**
         * method to increment the number of vote of this nominated person
         */
	public synchronized void incrementVote()
	{
		this.NumberOfVote ++;
	}
	
	/**
	 * equals() method which defined how to compare 2 Nominated
	 */
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof Nominated)) //if the given parameter is not a Nominated
			return false;
		Nominated nominated = (Nominated) obj;
		return this.getName() == nominated.getName();
		
	}
	
	/**
	 * toString() method which defined how a Nominated is displayed
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	
}
