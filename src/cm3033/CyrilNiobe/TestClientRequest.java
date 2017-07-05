package cm3033.CyrilNiobe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CLASS NOT USED IN THE COURSEWORK PROGRAM BUT ONLY USED FOR TESTS
 * @author Cyril Niobé - 2016
 * Module: Concurrent Programming Coursework - Robert Gordon University
 */
public class TestClientRequest implements Runnable{
    Socket socket;
    private String name;
    VotingRound votingRound;
    private String ip;
    final String VOTE_FOR ="Ben";
    
    public String getIp(){
    	return this.ip;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public TestClientRequest(String name,Socket sock,VotingRound votinground,String ip)
    {
        this.name = name;
        this.socket = sock;
        this.votingRound = votinground;
        this.ip = ip;
    }

    @Override
    public void run() {
       
    try{
    	Socket socket = new Socket(InetAddress.getLocalHost(), 8080);        
        System.out.println(VOTE_FOR);
        System.out.println(this.votingRound.showListNominated());
        this.votingRound.addVote(VOTE_FOR);
        Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		this.votingRound.addLogs("Nomination received from " + this.getName() + ": '" + VOTE_FOR + "' at " + dateFormat.format(currentDate));
        System.out.println("Voted successfully");
    }
    catch(Exception e){
    	System.out.println(e);
    }
    
    }
    
    
    
}
