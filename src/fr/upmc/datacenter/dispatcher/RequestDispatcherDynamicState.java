package fr.upmc.datacenter.dispatcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.extension.vm.VMData;

public class RequestDispatcherDynamicState implements RequestDispatcherDynamicStateI{
	
	private static final long serialVersionUID = 1L ;
	/** timestamp in Unix time format, local time of the timestamper.		*/
	protected final long timestamp;
	/** IP of the node that did the timestamping.							*/
	protected final String timestamperIP ;
	protected final long averageTime;
	protected final ArrayList<VMData> VMDatas;
	protected int nbreq;

	public RequestDispatcherDynamicState(long averageTime,int nbreq,ArrayList<VMData> VMDatas) throws UnknownHostException{
		super() ;
		this.nbreq=nbreq;
		this.averageTime=averageTime;
		this.timestamp = System.currentTimeMillis() ;
		this.timestamperIP = InetAddress.getLocalHost().getHostAddress() ;
		this.VMDatas=VMDatas;
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
	
	@Override
	public ArrayList<VMData> getVMDatas(){
		return VMDatas;
	}
	
	@Override
	public int getNbreq() {
		return nbreq;
	}

}
