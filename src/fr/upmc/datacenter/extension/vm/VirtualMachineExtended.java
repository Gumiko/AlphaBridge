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
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;
import fr.upmc.datacenter.software.connectors.RequestNotificationConnector;
/**
 * The class <code>VirtualMachineExtended</code> implements a component that represents an
 * Virtual Machine in a datacenter.
 *
 * <p><strong>Description</strong></p>
 * 
 *  It Extend the Actual Virtual Machine to offer more Services like adding cores or removing cores directly
 *  by the VM.
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
public class VirtualMachineExtended extends ApplicationVM implements VMExtendedManagementI{
	/** VMExtendedManagementInboundPort of the Extended Virtual Machine */
	protected VMExtendedManagementInboundPort vmemip;
	/** URI of the computer which contain the cores of the VM*/
	protected String computerURI;
	/** URI of the computerServicesInboundPortURI of the computer which contain the cores of the VM*/
	protected String computerServicesInboundPortURI;
	/** URI of the computerServicesOutboundPort of the VM*/
	protected ComputerServicesOutboundPort computerServicesOutboundPort;
	/**
	 *  Create a Virtual Machine Extended
	 * @param computerURI URI of the computer used to create the VM
	 * @param computerServicesInboundPortURI computerServicesInboundPort's URI of the computer
	 * @param vmURI URI of the Virtual Machine
	 * @param applicationVMManagementInboundPortURI applicationVMManagementInboundPort's URI
	 * @param VMExtendedManagementInboundPortURI VMExtendedManagementInboundPort's URI
	 * @param requestSubmissionInboundPortURI requestSubmissionInboundPort's URI
	 * @param requestNotificationOutboundPortURI requestNotificationOutboundPort's URI
	 * @throws Exception e
	 */
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
	
	/**
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#getData()
	 */
	public VMData getData() throws Exception{
		Map<String,Map<ProcessorPortTypes,String>> proc=new HashMap<String,Map<ProcessorPortTypes,String>>();
	
		int nbCore=0;
		for(AllocatedCore ac: this.allocatedCoresIdleStatus.keySet()){
			nbCore++;
			proc.put(ac.processorURI, ac.processorInboundPortURI);
		}
		return new VMData(nbCore,this.vmURI,proc,this.applicationVMManagementInboundPort.getPortURI(),this.vmemip.getPortURI(),this.requestSubmissionInboundPort.getPortURI());
	}
	
	/**
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#removeCore(int)
	 */
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
	/**
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#addCore(int)
	 */
	public int addCore(int number) throws Exception{
		AllocatedCore[] acs = this.computerServicesOutboundPort.allocateCores(number);
		int allocated = acs.length;
		this.allocateCores(acs);
		return allocated;
	}
	/**
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#removeAll()
	 */
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
	/**
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#connectNotificationPort(java.lang.String)
	 */
	@Override
	public void connectNotificationPort(String string) throws Exception {
		this.logMessage("VM Connecting to port : "+string);
		this.requestNotificationOutboundPort.doConnection(string, RequestNotificationConnector.class.getCanonicalName());
	}
	
}
