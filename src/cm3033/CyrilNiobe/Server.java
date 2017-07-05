package cm3033.CyrilNiobe;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* @author Cyril Niobé - 2016
* Module: Concurrent Programming Coursework - Robert Gordon University
*/
public class Server implements Runnable {

	/**
	 * size of the thread pool
	 */
	private static final int NTHREADS = 4;
	
        /**
         * int using in the name of the ClientRequests
         */
	private static int i = 1;
	
	/**
	 * size of queue of waiting clientrequests
	 */
	private static final int QSIZE = 100;
	
	/**
	 * The voting round of this server.
	 */
	public VotingRound votingRound;
	
        /**
         * ThreadPoolExecutor of the server
         */
	private ThreadPoolExecutor pool;
	
	/**
	 * Constructor of a Server
	 * @param votingRound
	 */
	public Server(VotingRound votingRound) {
		this.votingRound = votingRound;
		this.pool = new ThreadPoolExecutor(NTHREADS, NTHREADS, 50000L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(QSIZE));
	}

        /**
         * run method which will be called by the Server
         */
	@Override
	public void run() {
		
		try {
			ServerSocket serv = new ServerSocket(8080);
			pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			for (;;) {
				Socket incomingConnectionSock = serv.accept();
				SocketAddress socketAddress = incomingConnectionSock.getRemoteSocketAddress();
				InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
				String ip = inetSocketAddress.getAddress().toString();
				ClientRequest clientRequest = new ClientRequest("Client" + String.valueOf(i),incomingConnectionSock,votingRound,ip);
				pool.submit(clientRequest);
				DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
				Date dateConnection = new Date();
				this.votingRound.addLogs(clientRequest.getName() + " (IP: '" + ip +"')" + " connected at " + dateFormat.format(dateConnection));
				i++;
			}
			

		} catch (Exception ex) {
			System.out.println(ex);
		}
		
	}
	
	/**
	 * Method to get the VotingRound object of this server.
	 * @return
	 */
	public VotingRound getVotingRound()
	{
		return this.votingRound;
	}
	
        /**
         * Getter to return the threadPoolExecutor
         * @return 
         */
	public ThreadPoolExecutor getPool(){
		return this.pool;
	}
}
