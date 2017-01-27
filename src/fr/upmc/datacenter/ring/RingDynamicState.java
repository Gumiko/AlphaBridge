package fr.upmc.datacenter.ring;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.ring.interfaces.RingDynamicStateI;

public class RingDynamicState implements RingDynamicStateI{

	private static final long serialVersionUID = 1L ;
	/** timestamp in Unix time format, local time of the timestamper.		*/
	protected final long timestamp;
	/** IP of the node that did the timestamping.							*/
	protected final String timestamperIP ;
	protected final List<VMData> vmDataList;
	
	
	public RingDynamicState(List<VMData> vmDataList) throws UnknownHostException{
		super() ;
		this.vmDataList=vmDataList;
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
	public List<VMData> getVMDataList() {
		return vmDataList;
	}

	
}
