package fr.upmc.datacenter.dispatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.interfaces.DataOfferedI.DataI;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherActuatorI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataInboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementInboundPort;
import fr.upmc.datacenter.interfaces.PushModeControllerI;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;
import fr.upmc.datacenter.software.applicationvm.connectors.ApplicationVMManagementConnector;
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

public class RequestDispatcher extends AbstractComponent
implements RequestDispatcherI,RequestDispatcherManagementI,RequestSubmissionHandlerI,RequestNotificationHandlerI,PushModeControllerI
{
	
//	/** RequestDispatcher data inbound port through which it pushes its static data.	*/
//	protected RequestDispatcherStaticStateDataInboundPort
//											requestDispatcherStaticStateDataInboundPort ;
	/** RequestDispatcher data inbound port through which it pushes its dynamic data.	*/
	protected RequestDispatcherDynamicStateDataInboundPort rddsdip ;
	/** future of the task scheduled to push dynamic data.					*/
	protected ScheduledFuture<?>			pushingFuture ;
	
	public static final String VM_MANAGEMENT="DispatcherVMManagementOut";
	public static final String REQ_SUB_IN="DispatcherRequestSubInURI";
	public static final String REQ_SUB_OUT="DispatcherRequestSubOutURI";
	
	public static final String REQ_NOT_OUT="DispatcherRequestNotOutURI";
	public static final String REQ_NOT_IN="DispatcherRequestNotInURI";
	
	public static final String DYNAMIC_DATA_URI="RequestDispatcherDD";
	
	protected String RDuri;
	protected int idVM;
	protected int id;
	int lastVM;
	
	protected RequestSubmissionInboundPort	rsip;
	protected RequestNotificationOutboundPort rnop;
	
	protected RequestDispatcherManagementInboundPort rdmip;
	
	protected Map<Integer,RequestSubmissionOutboundPort> rsop;
	protected Map<Integer,RequestNotificationInboundPort> rnip;
	protected Map<Integer,ApplicationVMManagementOutboundPort> avmmops;
	
	/* Data Management */
	Map<String,Long> startTime=new HashMap<String,Long>();
	Map<String,Long> endTime=new HashMap<String,Long>();
	Map<String,Long> lastTime=new HashMap<String,Long>();
	
	int numberOfRequests;
	int totalTime;
	int averageTime;
	

	public RequestDispatcher(int id, String controllerURi) throws Exception{
		/* Init Request Dispatcher */
		this.id=id;
		idVM=0;
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
		
		/*Dynamic data sending to the controller */
		rddsdip=new RequestDispatcherDynamicStateDataInboundPort(AdmissionController.RD_DSDIP_PREFIX+id, this);
		this.addPort(rddsdip) ;
		this.rddsdip.publishPort();
		
		/*
		 *Management port used by the controller 
		 */
		
		rdmip=new RequestDispatcherManagementInboundPort(AdmissionController.RD_MIP_PREFIX+id,this);
		this.addPort(rdmip);
		this.rdmip.publishPort();
		
		/*Test*/
		//this.addRequiredInterface(RequestSubmissionI.class);
		//this.addOfferedInterface(RequestNotificationI.class);
	}
	
	public void linkRequestGenerator(String rg_rsopURI,String rg_rnipURI) throws Exception{
		this.logMessage("Linking RG to Dispatcher["+id+"] ...");
		this.logMessage("CONNECTION : "+rg_rsopURI+" -> "+rsip.getPortURI());
		this.logMessage("CONNECTION : "+rnop.getPortURI()+" -> "+rg_rnipURI);
		rsip.doConnection(rg_rsopURI, RequestSubmissionConnector.class.getCanonicalName());
		rnop.doConnection(rg_rnipURI, RequestNotificationConnector.class.getCanonicalName());
		this.logMessage("RG linked to Dispatcher["+id+"] !"); //"+rg_rnip.getPortURI()+" | "+rg_rsop.getPortURI() );
	}

	@Override
	public void bindVM(int id,String str_rsop,String str_rnip,String str_avmmop) throws Exception {
		this.logMessage("VM"+id+" : Linking...");
		RequestSubmissionOutboundPort rsopvm = new RequestSubmissionOutboundPort(str_rsop, this);
		RequestNotificationInboundPort rnipvm = new RequestNotificationInboundPort(str_rnip, this);
		ApplicationVMManagementOutboundPort avmmop=new ApplicationVMManagementOutboundPort(this);

		this.addPort(rsopvm);
		this.addPort(rnipvm);

		rsopvm.publishPort();
		rnipvm.publishPort();
		
		this.rsop.put(id, rsopvm);
		this.rnip.put(id, rnipvm);
		
		avmmop.doConnection(str_avmmop, ApplicationVMManagementConnector.class.getCanonicalName());
		avmmops.put(id, avmmop);
		
		this.logMessage("VM"+id+" : Linked !");
	}


	@Override
	public void unbindVM(int id) throws Exception {
		 rsop.get(id).doDisconnection();
		 rsop.remove(id);
		 rnip.get(id).doDisconnection();
		 rnip.remove(id);
		 avmmops.get(id).doDisconnection();
		 avmmops.remove(id);
	}
	
	@Override
	public void startUnlimitedPushing(int interval) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLimitedPushing(int interval, int n) throws Exception {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void stopPushing() throws Exception {
		// TODO Auto-generated method stub
		
	}


	public DataI getDynamicState() {
		// TODO Auto-generated method stub
		return null;
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


}
