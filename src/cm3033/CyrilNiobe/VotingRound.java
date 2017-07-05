package cm3033.CyrilNiobe;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Class which represents a voting round
 * @author Cyril Niobé - 2016
 * Module: Concurrent Programming Coursework - Robert Gordon University
 */
public class VotingRound {
	
	/**
	 * Date of opening of the voting round
	 */
	private Date dateOpen;
	
	/**
	 *  Date of ending of the voting round
	 */
	private Date dateClose = null;
	
	/**
	 * String which represent the vote written by the user.
	 */
	public String vote;
	
	/**
	 * boolean which represents if the voting round is open or not.
	 */
	private boolean open = false;
	
	/**
	 * The list of candidate nominated
	 */
	public List<Nominated> listNominated;
	
	/**
	 * The list of IP Addresses already known
	 */
	public List<String> listIPAddress;
	
	/**
	 * The list of logs which will be used in the GUI for the administrator
	 */
	public List<String> listLogs;	
	
	/**
	 * Constructor of a VotingRound
	 * @param listNom : the list of Nominated in this round as a given parameter here as the administrator set it up in the GUI
	 */
	public VotingRound(List<Nominated> listNom){
		this.listNominated = new ArrayList<Nominated>(listNom);
		this.listIPAddress = new ArrayList<String>();
		this.listLogs = new ArrayList<String>();
	}
	
	
	/**
	 * Getter on the boolean member 'opened'
	 * @return true if the voting round is opened, else false.
	 */
	public boolean isOpen(){
		return this.open;
	}
	
	/**
	 * Setter to change the value of the boolean 'opened'
	 * I.e. open or close the voting round
	 * @param status : the boolean for closing or opening the voting round.
	 */
	public void setOpen(boolean status){
		this.open = status;
	}
	
	/**
	 * Getter on the opening date of the round
	 * @return Date opening
	 */
	public Date getDateOpening(){
		return this.dateOpen;
	}
	
	/**
	 * Getter on the ending date of the round
	 * @return Date ending
	 */
	public Date getDateEnding(){
		return this.dateClose;
	}
	
	/**
	 * Setter on the opening date of the round
	 * @param d date
	 */
	public void setDateOpening(Date d){
		this.dateOpen = d;
	}
	
	/**
	 * Setter on the ending date of the round
	 * @param d date
	 */
	public void setDateEnding(Date d){
		this.dateClose = d;
	}
	
	
	/**
	 * Method which add a vote to a given Nominated name
	 * @param nominated : the name of the nominated to vote for
	 */
	public synchronized void addVote(String nominatedName){
		for (Nominated nominated : listNominated){
			if((nominated.getName().compareToIgnoreCase(nominatedName)) == 0){
				nominated.incrementVote();
			}
		}
	}
	
	/**
	 * Method which return a Stringbuilder to display the list of Nominated
	 */
	public String showListNominated()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Nominated for this round are: \n");
		for (Nominated nominated : listNominated) {
			sb.append(" - " + nominated + "\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Method which ask the client to vote and check some requirements
	 * @param in
	 * @param out
	 * @return boolean : to respond with the ClientRequest when we need to end the connection with the server
	 */
	public boolean askForVote(BufferedReader in, PrintWriter out, ClientRequest clientRequest){
		out.println("Enter the name of the nominated to vote for or 'Exit' to quit the server: ");
        try{
        	vote = in.readLine();        	
        	while(!vote.isEmpty()){
            	if(vote.trim().equals("Exit")){
            		this.vote="Exit";
            		return true;
            	}
            	else{
            		if(verifyVote(vote)){
            			out.println("Confirm your vote by enter it again: ");
            			String voteConfirm = in.readLine();
            			if(confirmVote(vote, voteConfirm)){
            				if(verifyIP(clientRequest.getIp())){ // this line to comment if you don't want to verify IP
	            				this.addVote(vote);
	            				this.addIP(clientRequest.getIp());
	            				out.println("Your nomination is for: " + vote + "\n");
	            				Date currentDate = new Date();
	            				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            				this.addLogs("Nomination received from " + clientRequest.getName() + ": '" + vote + "' at " + dateFormat.format(currentDate));
	            				out.println("Highest vote cast: " + this.getHighestVote() + " for: " + this.getNameOfHighestVote() + "at " + dateFormat.format(currentDate) + "\n");
            				} // this too
            				else{ // and this block too.
                                            out.println("Your vote is rejected, you have already voted in this round ! \n");
            				}
        				}
            			else{
            				out.println("You wrote a different vote !");
            				return false;
            			}
            		}
            		else{
            			out.println("Your vost is not in the list of householders ! \n");
            			return false;
            		}
            		return true;	
				}
            		
        	}
        	return this.askForVote(in, out, clientRequest);  	

        }
        
        catch (Exception ex)
        {
        	System.out.println(ex);
        }
		return false;
	}
	
	/**
	 * Method to verify that the Nominated whom the name is given in parameter, is in the list of Nominated of this round.
	 * @param nominatedName
	 * @return
	 */
	private boolean verifyVote(String nominatedName){
		return this.listNominated.stream().anyMatch(nominated -> (nominated.getName().compareToIgnoreCase(nominatedName)) == 0);
	}
	
	/**
	 * Method to confirm the vote of the client
	 * @param vote1
	 * @param vote2
	 * @return
	 */
	private boolean confirmVote(String vote1, String vote2){
		if(vote1.compareToIgnoreCase(vote2) == 0)
			return true;
		else
			return false;
	}
	
        /**
         * Getter to return the number of the highest vote
         * @return 
         */
	public synchronized int getHighestVote(){
		List<Integer> listVotes = new ArrayList<Integer>();
		
		for (Nominated nominated : listNominated) {
			listVotes.add(nominated.getNumberOfVote());
		}
		return Collections.max(listVotes);
		
		
	}
	
        /**
         * Getter to return the name of the nominated person which has the highest vote
         * @return 
         */
	public synchronized String getNameOfHighestVote(){
		String result = "";
		for (Nominated nominated : listNominated) {
			if(nominated.getNumberOfVote() != 0)
			{if(nominated.getNumberOfVote() == this.getHighestVote()){
				result += nominated.getName();
				result += " ";
			}}
		}
		return result;
	}
	
        /**
         * Method to add an IP address in the list of IP addresses
         * @param ip to add in the list
         */
	public synchronized void addIP(String ip){
		this.listIPAddress.add(ip);
	}
	
	/**
	 * Method to check if an IP is already in the IPAddresses known
	 * @param ip to check
	 * @return true if it's a unknown IP, else false;
	 */
	public synchronized boolean verifyIP(String ip){
		for (String  ipKnown : listIPAddress) {
			if(ipKnown.compareTo(ip) == 0)
				return false;
		}
		return true;
	}
	
	/**
	 * Getter on the list of Logs to display it
	 * @return logs contained in the list as a String.
	 */
	public String getLogs(){
		StringBuilder sb = new StringBuilder();
		if(!listLogs.isEmpty()){
			for (String string : listLogs) {
				sb.append(string);
				sb.append("\n");
			}
		}
		listLogs.clear();
		return sb.toString();
	}
	
	/**
	 * Method to add string in list of logs
	 * @param string
	 */
	public void addLogs(String string){
		this.listLogs.add(string);
	}
}
