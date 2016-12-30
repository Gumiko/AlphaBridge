package fr.upmc.datacenter.extension.vm;

import java.util.Map.Entry;

import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;

public class VirtualMachineExtended extends ApplicationVM{

	public VirtualMachineExtended(String vmURI, String applicationVMManagementInboundPortURI,
			String requestSubmissionInboundPortURI, String requestNotificationOutboundPortURI) throws Exception {
		super(vmURI, applicationVMManagementInboundPortURI, requestSubmissionInboundPortURI,
				requestNotificationOutboundPortURI);
	}

	public void removeCore(int number){
		int retrieve=number;
		
		for(Entry<AllocatedCore, Boolean> set :this.allocatedCoresIdleStatus.entrySet()){
			if(set.getValue()){
				
			}
		}
		
	}

}
