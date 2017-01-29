package fr.upmc.datacenter.extension.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI;
import fr.upmc.datacenter.extension.vm.ports.VMExtendedManagementInboundPort;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
import fr.upmc.datacenter.hardware.computers.connectors.ComputerServicesConnector;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
import fr.upmc.datacenter.hardware.processors.Processor.ProcessorPortTypes;
import fr.upmc.datacenter.hardware.processors.ports.ProcessorServicesOutboundPort;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;
import fr.upmc.datacenter.software.connectors.RequestNotificationConnector;

public class VirtualMachineExtended extends ApplicationVM implements VMExtendedManagementI{
	protected VMExtendedManagementInboundPort vmemip;
	protected String computerURI;
	protected String computerServicesInboundPortURI;
	
	protected ComputerServicesOutboundPort computerServicesOutboundPort;
	
	public VirtualMachineExtended(String computerURI,String computerServicesInboundPortURI,String vmURI, String applicationVMManagementInboundPortURI,
			String VMExtendedManagementInboundPortURI,String requestSubmissionInboundPortURI, String requestNotificationOutboundPortURI) throws Exception {
		super(vmURI, applicationVMManagementInboundPortURI, requestSubmissionInboundPortURI,
				requestNotificationOutboundPortURI);
		this.computerURI=computerURI;
		this.computerServicesInboundPortURI=computerServicesInboundPortURI;
		computerServicesOutboundPort=new ComputerServicesOutboundPort(this);
		this.addPort(computerServicesOutboundPort);
		computerServicesOutboundPort.publishPort();
		
		computerServicesOutboundPort.doConnection(computerServicesInboundPortURI, ComputerServicesConnector.class.getCanonicalName());
		
		vmemip=new VMExtendedManagementInboundPort(VMExtendedManagementInboundPortURI, this);
		this.addPort(vmemip);
		vmemip.publishPort();
		
		//this.toggleTracing();
		//this.toggleLogging();
	}

	public VMData getData() throws Exception{
		Map<String,Map<ProcessorPortTypes,String>> proc=new HashMap<String,Map<ProcessorPortTypes,String>>();
	
		int nbCore=0;
		for(AllocatedCore ac: this.allocatedCoresIdleStatus.keySet()){
			nbCore++;
			proc.put(ac.processorURI, ac.processorInboundPortURI);
		}
		return new VMData(nbCore,this.vmURI,proc,this.applicationVMManagementInboundPort.getPortURI(),this.vmemip.getPortURI(),this.requestSubmissionInboundPort.getPortURI());
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

	public int addCore(int number) throws Exception{
		AllocatedCore[] acs = this.computerServicesOutboundPort.allocateCores(number);
		int allocated = acs.length;
		this.allocateCores(acs);
		return allocated;
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

	@Override
	public void connectNotificationPort(String string) throws Exception {
		this.logMessage("VM Connecting to port : "+string);
		this.requestNotificationOutboundPort.doConnection(string, RequestNotificationConnector.class.getCanonicalName());
	}
	
}
