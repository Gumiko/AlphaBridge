package fr.upmc.datacenter.extension.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;

public class VirtualMachineExtended extends ApplicationVM{

	public VirtualMachineExtended(String vmURI, String applicationVMManagementInboundPortURI,
			String requestSubmissionInboundPortURI, String requestNotificationOutboundPortURI) throws Exception {
		super(vmURI, applicationVMManagementInboundPortURI, requestSubmissionInboundPortURI,
				requestNotificationOutboundPortURI);
	}

	public AllocatedCore[] removeCore(int number){
		int retrieve=Math.max(0, number);
		List<AllocatedCore> removelist=new ArrayList<AllocatedCore>();
		AllocatedCore temp=null;
		for(Entry<AllocatedCore, Boolean> set :this.allocatedCoresIdleStatus.entrySet()){
			if(retrieve>0)
				retrieve--;
			else break;
			if(!set.getValue()){
				temp=set.getKey();
				removelist.add(temp);
			}
		}
		AllocatedCore[] rm=new AllocatedCore[removelist.size()];
		for(int i=0;i<removelist.size();i++){
			this.allocatedCoresIdleStatus.remove(removelist.get(i));
			rm[i]=removelist.get(i);
		}
		
		return rm;
		
	}
	
	public void addCore(AllocatedCore[] ac) throws Exception{
		this.allocateCores(ac);
	}
	
	public AllocatedCore[] removeAll(){
		List<AllocatedCore> removelist=new ArrayList<AllocatedCore>();
		AllocatedCore temp=null;
		for(Entry<AllocatedCore, Boolean> set :this.allocatedCoresIdleStatus.entrySet()){
				temp=set.getKey();
				removelist.add(temp);
		}
		AllocatedCore[] rm=new AllocatedCore[removelist.size()];
		for(int i=0;i<removelist.size();i++){
			this.allocatedCoresIdleStatus.remove(removelist.get(i));
			rm[i]=removelist.get(i);
		}
		
		return rm;
	}

}
