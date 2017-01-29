package fr.upmc.datacenter.dispatcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.extension.vm.VMData;
/**
 * The interface <code>RequestDispatcherDynamicState</code> gives access to the
 * dynamic state information of RequestDispatcher transmitted by data interfaces of
 * RequestDispatcher.
 *
 * <p><strong>Description</strong></p>
 * 
 * The interface is used to type objects pulled from or pushed by a RequestDispatcher
 * using a data interface in pull or push mode.  It gives access to dynamic
 * information, that is information subject to changes during the existence of
 * the RequestDispatcher.
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * @author	Cédric Ribeiro et Mokrane Kadri

 */
public class RequestDispatcherDynamicState implements RequestDispatcherDynamicStateI{
	
	private static final long serialVersionUID = 1L ;
	/** timestamp in Unix time format, local time of the timestamper.		*/
	protected final long timestamp;
	/** IP of the node that did the timestamping.							*/
	protected final String timestamperIP ;
	protected final long averageTime;
	protected final ArrayList<VMData> VMDatas;
	protected int nbreq;

	
	/***
	 * 
	 * @param averageTime
	 * @param nbreq
	 * @param VMDatas
	 * @throws UnknownHostException
	 */
	
	public RequestDispatcherDynamicState(long averageTime,int nbreq,ArrayList<VMData> VMDatas) throws UnknownHostException{
		super() ;
		this.nbreq=nbreq;
		this.averageTime=averageTime;
		this.timestamp = System.currentTimeMillis() ;
		this.timestamperIP = InetAddress.getLocalHost().getHostAddress() ;
		this.VMDatas=VMDatas;
	}
	
	
	/* 
	 * @see fr.upmc.datacenter.interfaces.TimeStampingI#getTimeStamp()
	 */
	@Override
	public long getTimeStamp() {
		return timestamp;
	}

	
	/*
	 * @see fr.upmc.datacenter.interfaces.TimeStampingI#getTimeStamperId()
	 */
	@Override
	public String getTimeStamperId() {
		return timestamperIP;
	}
	
	
	/*
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI#getAverageTime()
	 */
	@Override
	public long getAverageTime() {
		return averageTime;
	}
	
	/* 
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI#getVMDatas()
	 */
	@Override
	public ArrayList<VMData> getVMDatas(){
		return VMDatas;
	}
	
	/* 
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI#getNbreq()
	 */
	@Override
	public int getNbreq() {
		return nbreq;
	}

}
