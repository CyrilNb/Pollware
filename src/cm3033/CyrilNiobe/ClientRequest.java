package cm3033.CyrilNiobe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Cyril Niobé - 2016
 * Module: Concurrent Programming Coursework - Robert Gordon University
 */
public class ClientRequest implements Runnable{
    
    /**
     * Socket member
     */
    Socket socket;
    
    /**
     * name of the ClientRequest
     */
    private String name;
    
    /**
     * Voting round member
     */
    VotingRound votingRound;
    
    /**
     * ip of the ClientRequest
     */
    private String ip;
    
    /**
     * Getter to return the IP
     * @return 
     */
    public String getIp(){
    	return this.ip;
    }
    
    /**
     * Getter to return the name
     * @return 
     */
    public String getName(){
    	return this.name;
    }
    
    /**
     * Constructor
     * @param name of the client request
     * @param sock socket of the client request
     * @param votinground voting round of the client request
     * @param ip of the client request
     */
    public ClientRequest(String name,Socket sock,VotingRound votinground,String ip)
    {
        this.name = name;
        this.socket = sock;
        this.votingRound = votinground;
        this.ip = ip;
    }

    /**
     * run method which will be called by this class
     */
    @Override
    public void run() {
       
        try{
            //set up streams for bidirectionnal transfer across connection socket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            out.println("\nYou are connected to the Big Brother Vote Counter\n");
            out.println("Type Exit to quit the server\n");
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            while(!this.votingRound.isOpen()){
            	if(this.votingRound.getDateEnding() == null){
            		out.println("There is no currently voting round open. You will quit the server. \n");
            	}
            	else{
            		out.println(this.votingRound.showListNominated() + "\n" );
            		out.println("The voting round closed at: " + dateFormat.format(this.votingRound.getDateEnding()) + "\n");
            		out.println("Sorry, you can't vote anymore. You will quit the server. \n");
            	}
            	socket.close();	
            }
            out.println("The voting round opened at: " + dateFormat.format(this.votingRound.getDateOpening()) + "\n");
            boolean done = false;
            out.println(votingRound.showListNominated());
            while(!done){
				done = votingRound.askForVote(in, out,this);
            }
            if(votingRound.vote.compareToIgnoreCase("Exit")!=0)
            {
            	out.println("'Exit' to quit the server");
	            String str = in.readLine();
	            while((str.compareToIgnoreCase("Exit")) != 0)
	            {
	            	out.println("'Exit' to quit the server");
	            	str = in.readLine();
	            }
	            socket.close();
            }
            socket.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }
    
    
    
}
