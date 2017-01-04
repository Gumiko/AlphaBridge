package fr.upmc.datacenter.dispatcher;

import java.net.InetAddress;
import java.net.UnknownHostException;

import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;

public class RequestDispatcherDynamicState implements RequestDispatcherDynamicStateI{
	
	private static final long serialVersionUID = 1L ;
	/** timestamp in Unix time format, local time of the timestamper.		*/
	protected final long timestamp;
	/** IP of the node that did the timestamping.							*/
	protected final String timestamperIP ;
	protected final long averageTime;
	
	
	public RequestDispatcherDynamicState(long averageTime) throws UnknownHostException{
		super() ;
		this.averageTime=averageTime;
		this.timestamp = System.currentTimeMillis() ;
		this.timestamperIP = InetAddress.getLocalHost().getHostAddress() ;
	}
	
	@Override
	public long getTimeStamp() {
		return timestamp;
	}

	@Override
	public String getTimeStamperId() {
		return timestamperIP;
	}
	
	@Override
	public long getAverageTime() {
		return averageTime;
	}

}
