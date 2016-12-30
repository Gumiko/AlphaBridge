package fr.upmc.datacenter.dispatcher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import fr.upmc.components.AbstractComponent;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;
import fr.upmc.datacenter.software.applicationvm.ports.ApplicationVMManagementOutboundPort;
import fr.upmc.datacenter.software.connectors.RequestNotificationConnector;
import fr.upmc.datacenter.software.connectors.RequestSubmissionConnector;
import fr.upmc.datacenter.software.interfaces.RequestI;
import fr.upmc.datacenter.software.interfaces.RequestNotificationHandlerI;
import fr.upmc.datacenter.software.interfaces.RequestNotificationI;
import fr.upmc.datacenter.software.interfaces.RequestSubmissionHandlerI;
import fr.upmc.datacenter.software.interfaces.RequestSubmissionI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestNotificationOutboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
import fr.upmc.datacenterclient.requestgenerator.RequestGenerator;

public class RequestDispatcher extends AbstractComponent
implements RequestDispatcherI,RequestDispatcherManagementI,RequestSubmissionHandlerI,RequestNotificationHandlerI
{
	
	public static final String VM_MANAGEMENT="DispatcherVMManagementOut";
	
	public static final String REQ_SUB_IN="DispatcherRequestSubInURI";
	public static final String REQ_SUB_OUT="DispatcherRequestSubOutURI";
	
	public static final String REQ_NOT_OUT="DispatcherRequestNotOutURI";
	public static final String REQ_NOT_IN="DispatcherRequestNotInURI";
	
	protected String RDuri;
	protected int id;
	
	int lastVM;
	
	protected RequestSubmissionInboundPort	rsip;
	protected RequestNotificationOutboundPort rnop;
	
	protected Map<Integer,RequestSubmissionOutboundPort> rsop;
	protected Map<Integer,RequestNotificationInboundPort> rnip;
	protected Map<Integer,ApplicationVMManagementOutboundPort> avmmop;
	
	/* Data Management */
	Map<String,Long> startTime=new HashMap<String,Long>();
	Map<String,Long> endTime=new HashMap<String,Long>();
	Map<String,Long> lastTime=new HashMap<String,Long>();
	
	int numberOfRequests;
	int totalTime;
	int averageTime;
	
	public RequestDispatcher(int id) throws Exception{
		/* Init Request Dispatcher */
		this.id=id;
		this.RDuri="Dispatcher"+id;
		lastVM=0;
		
		rsop=new HashMap<Integer,RequestSubmissionOutboundPort>();
		rnip=new HashMap<Integer,RequestNotificationInboundPort>();
		
		/*RD Ports connection with RG*/
		this.addOfferedInterface(RequestSubmissionI.class);
		rsip = new RequestSubmissionInboundPort(REQ_SUB_IN+id, this);
		this.addPort(rsip);
		this.rsip.publishPort();
		
		this.addRequiredInterface(RequestNotificationI.class);
		rnop=new RequestNotificationOutboundPort(REQ_NOT_OUT+id, this);
		this.addPort(rnop);
		this.rnop.publishPort();
		
		/*Test*/
		//this.addRequiredInterface(RequestSubmissionI.class);
		//this.addOfferedInterface(RequestNotificationI.class);
	}
	
	
	
	public void linkVM(int id, ApplicationVM virtualMachine) throws Exception {
		this.logMessage("VM"+id+" : Linking...");
		RequestSubmissionOutboundPort rsopvm = new RequestSubmissionOutboundPort(REQ_SUB_OUT + id, this);
		RequestNotificationInboundPort rnipvm = new RequestNotificationInboundPort(REQ_NOT_IN + id, this);

		this.addPort(rsopvm);
		this.addPort(rnipvm);

		rsopvm.publishPort();
		rnipvm.publishPort();
		
		this.rsop.put(id, rsopvm);
		this.rnip.put(id, rnipvm);
		this.logMessage("VM"+id+" : Linked !");
}
	
	/*TOREMOVE - Managed by the controller*/
	public void linkVM(int id,ApplicationVM vm,String vm_rsip,String vm_rnop)throws Exception{
		this.logMessage("VM"+id+" : Linking...");
		
		RequestSubmissionOutboundPort rsopvm=new RequestSubmissionOutboundPort(this);
		this.addPort(rsopvm);
		rsopvm.publishPort();
		
		RequestNotificationInboundPort rnipvm=new RequestNotificationInboundPort(this);
		this.addPort(rnipvm);
		rnipvm.publishPort();
		
		this.rsop.put(id,rsopvm);
		this.rnip.put(id,rnipvm);
		
		RequestSubmissionInboundPort rsip=(RequestSubmissionInboundPort) vm.findPortFromURI(vm_rsip);//new RequestSubmissionOutboundPort(REQ_SUB_OUT+id,this);
		RequestNotificationOutboundPort rnop=(RequestNotificationOutboundPort) vm.findPortFromURI(vm_rnop);//new RequestNotificationInboundPort(REQ_NOT_IN+id,this);
		
		
		rsopvm.doConnection(rsip.getPortURI(), RequestSubmissionConnector.class.getCanonicalName());
		rnop.doConnection(rnipvm.getPortURI(), RequestNotificationConnector.class.getCanonicalName());
		
		this.logMessage("VM"+id+" : Linked !");
	}
	
	public void linkRequestGenerator(RequestSubmissionOutboundPort rg_rsop,RequestNotificationInboundPort rg_rnip) throws Exception{
		this.logMessage("Linking RG to Dispatcher["+id+"] ...");
		this.logMessage("CONNECTION : "+rg_rsop.getPortURI()+" -> "+rsip.getPortURI());
		this.logMessage("CONNECTION : "+rnop.getPortURI()+" -> "+rg_rnip.getPortURI());
		rg_rsop.doConnection(rsip.getPortURI(), RequestSubmissionConnector.class.getCanonicalName());
		rnop.doConnection(rg_rnip.getPortURI(), RequestNotificationConnector.class.getCanonicalName());
		
		this.logMessage("RG linked to Dispatcher["+id+"] !"); //"+rg_rnip.getPortURI()+" | "+rg_rsop.getPortURI() );
	}
	
	public void linkRequestGenerator(String rg_rsopURI,String rg_rnipURI) throws Exception{
		this.logMessage("Linking RG to Dispatcher["+id+"] ...");
		this.logMessage("CONNECTION : "+rg_rsopURI+" -> "+rsip.getPortURI());
		this.logMessage("CONNECTION : "+rnop.getPortURI()+" -> "+rg_rnipURI);
		RequestSubmissionOutboundPort rg_rsop=new RequestSubmissionOutboundPort(rg_rsopURI, this);
		RequestNotificationInboundPort rg_rnip = new RequestNotificationInboundPort(rg_rnipURI, this);
		rg_rsop.doConnection(rsip.getPortURI(), RequestSubmissionConnector.class.getCanonicalName());
		rnop.doConnection(rg_rnip.getPortURI(), RequestNotificationConnector.class.getCanonicalName());
		this.logMessage("RG linked to Dispatcher["+id+"] !"); //"+rg_rnip.getPortURI()+" | "+rg_rsop.getPortURI() );
	}
	
	public void	acceptRequestSubmission(final RequestI r)
	throws Exception
	{
		this.logMessage("Dispatcher["+id+"] : "+r.getRequestURI() +" => "+rsop.get(lastVM).getPortURI());
		rsop.get(lastVM).submitRequest(r);
		lastVM=(++lastVM)%rsop.keySet().size();
	}

	public void	acceptRequestSubmissionAndNotify(final RequestI r) throws Exception
	{
		this.logMessage("Dispatcher&N["+id+"] : "+r.getRequestURI() +" ==> "+"VM-"+lastVM);
		rsop.get(lastVM).submitRequestAndNotify(r);
		startTime.put(r.getRequestURI(), System.currentTimeMillis());
		lastVM=(++lastVM)%rsop.keySet().size();
	}

	@Override
	public void acceptRequestTerminationNotification(RequestI r) throws Exception {
		this.logMessage("Dispatcher&T["+id+"] : "+r.getRequestURI() +" => "+rnop.getPortURI());
		this.rnop.notifyRequestTermination(r);
		endTime.put(r.getRequestURI(), System.currentTimeMillis());
		lastTime.put(r.getRequestURI(), endTime.get(r.getRequestURI())-startTime.get(r.getRequestURI()));
		totalTime+=lastTime.get(r.getRequestURI());
	}



	@Override
	public void deployVM(int rd, String RequestDispatcherURIDVM) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void destroyVM(String uriComputerParent, String vm) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void initVM(int application, String uriComputerParent, String vm) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void unbindVM(String uriComputerParent, String vm) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
