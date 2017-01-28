package fr.upmc.datacenter.dispatcher;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.ComponentI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherActuatorInboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataInboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementInboundPort;
import fr.upmc.datacenter.extension.vm.connectors.VMExtendedManagementConnector;
import fr.upmc.datacenter.extension.vm.ports.VMExtendedManagementOutboundPort;
import fr.upmc.datacenter.interfaces.PushModeControllerI;
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
	//				requestDispatcherStaticStateDataInboundPort ;
	/** RequestDispatcher data inbound port through which it pushes its dynamic data.	*/
	protected RequestDispatcherDynamicStateDataInboundPort rddsdip ;
	protected RequestDispatcherActuatorInboundPort rdaip;
	/** future of the task scheduled to push dynamic data.					*/
	protected ScheduledFuture<?>			pushingFuture ;

	public static final String VM_MANAGEMENT="DispatcherVMManagementOut";
	public static final String REQ_SUB_IN="DispatcherRequestSubInURI";
	public static final String REQ_SUB_OUT="DispatcherRequestSubOutURI";

	public static final String REQ_NOT_OUT="DispatcherRequestNotOutURI";
	public static final String REQ_NOT_IN="DispatcherRequestNotInURI";

	public static final String DYNAMIC_DATA_URI="RequestDispatcherDD";

	protected String rdUri;
	protected int idVM;
	protected int id;
	
	
	protected int notificationid;

	protected RequestSubmissionInboundPort	rsip;
	protected RequestNotificationOutboundPort rnop;
	protected RequestDispatcherManagementInboundPort rdmip;

	protected Map<String,RequestSubmissionOutboundPort> rsop;
	protected Map<String,RequestNotificationInboundPort> rnip;
	protected Map<String,ApplicationVMManagementOutboundPort> avmmops;
	protected Map<String,VMExtendedManagementOutboundPort> vmemops;

	int lastVM;
	private ArrayList<RequestSubmissionOutboundPort> vms;
	
	/* Data Management */
	Map<String,Long> startTime=new HashMap<String,Long>();
	Map<String,Long> endTime=new HashMap<String,Long>();
	Map<String,Long> lastTime=new HashMap<String,Long>();

	int numberOfRequests;
	int totalTime;
	int averageTime;
	
	public RequestDispatcher(int id, String rdURI,String requestDispatcherRequestSubmissionInboundPortURI,String requestDispatcherActuatorInboundPort,String requestDispatcherDynamicStateDataInboundPort,String requestDispatcherManagementInboundPort) throws Exception{
		/* Init Request Dispatcher */
		super(1, 1) ;
		this.id=id;
		this.idVM=0;
		this.rdUri=rdURI;
		this.lastVM=0;
		this.notificationid=0;
		this.rsop=new HashMap<String,RequestSubmissionOutboundPort>();
		this.rnip=new HashMap<String,RequestNotificationInboundPort>();
		this.avmmops = new HashMap<String,ApplicationVMManagementOutboundPort> ();
		this.vmemops = new HashMap<String,VMExtendedManagementOutboundPort> ();
		
		/*RD Ports connection with RG*/
		this.addOfferedInterface(RequestSubmissionI.class);
		this.rsip = new RequestSubmissionInboundPort(requestDispatcherRequestSubmissionInboundPortURI, this);
		this.addPort(rsip);
		this.rsip.publishPort();

		this.addRequiredInterface(RequestNotificationI.class);
		this.rnop=new RequestNotificationOutboundPort(this);
		this.addPort(rnop);
		this.rnop.publishPort();

		/*Dynamic data sending to the controller */
		this.rddsdip=new RequestDispatcherDynamicStateDataInboundPort(requestDispatcherDynamicStateDataInboundPort, this);
		this.addPort(rddsdip) ;
		this.rddsdip.publishPort();
		
		/*Actuator*/
		this.rdaip=new RequestDispatcherActuatorInboundPort(requestDispatcherActuatorInboundPort,this);
		this.addPort(rdaip);
		this.rdaip.publishPort();
		/*
		 *Management port used by the controller 
		 */

		this.rdmip=new RequestDispatcherManagementInboundPort(requestDispatcherManagementInboundPort,this);
		this.addPort(rdmip);
		this.rdmip.publishPort();
	
		/*Test*/
		//this.addRequiredInterface(RequestSubmissionI.class);
		//this.addOfferedInterface(RequestNotificationI.class);
	}

	public void linkRequestGenerator(String requestNotificationInboundPortURI) throws Exception{
		this.logMessage("Linking Dispatcher["+id+"] to RG to...");
		this.logMessage("CONNECTION : "+rnop.getPortURI()+" -> "+requestNotificationInboundPortURI);
		this.rnop.doConnection(requestNotificationInboundPortURI, RequestNotificationConnector.class.getCanonicalName());
		this.logMessage("RG linked to Dispatcher["+id+"] !"); //"+rg_rnip.getPortURI()+" | "+rg_rsop.getPortURI() );
	}

	@Override
	public void bindVM(String vmUri,String vmRequestSubmissionInboundPortURI,String applicationVMManagementInboundPortURI,String VMExtendedManagementInboundPortURI) throws Exception {
		this.logMessage("VM"+id+" : Linking...");
		String notificationURI=rdUri+"NOTIF"+(notificationid++);
		
		VMExtendedManagementOutboundPort vmemop=new VMExtendedManagementOutboundPort(this);
		RequestSubmissionOutboundPort rsopvm = new RequestSubmissionOutboundPort(this);
		ApplicationVMManagementOutboundPort avmmop=new ApplicationVMManagementOutboundPort(this);
		RequestNotificationInboundPort rnipvm = new RequestNotificationInboundPort(notificationURI,this);
		
		this.addPort(vmemop);
		this.addPort(rsopvm);
		this.addPort(avmmop);
		this.addPort(rnipvm);
		
		vmemop.publishPort();
		rsopvm.publishPort();
		avmmop.publishPort();
		rnipvm.publishPort();
		
		vmemop.doConnection(VMExtendedManagementInboundPortURI, VMExtendedManagementConnector.class.getCanonicalName());
		rsopvm.doConnection(vmRequestSubmissionInboundPortURI, RequestSubmissionConnector.class.getCanonicalName());
		avmmop.doConnection(applicationVMManagementInboundPortURI, ApplicationVMManagementConnector.class.getCanonicalName());
		
		vmemop.connectNotificationPort(notificationURI);
		
		rsop.put(vmUri, rsopvm);
		avmmops.put(vmUri, avmmop);
		vmemops.put(vmUri, vmemop);
		rnip.put(vmUri, rnipvm);
		
		vms=new ArrayList<>(rsop.values());
		
		this.logMessage("VM"+id+" : Linked !");
	}


	@Override
	public void unbindVM(String vmUri) throws Exception {
		rsop.get(vmUri).doDisconnection();
		rsop.remove(vmUri);
		rnip.get(vmUri).doDisconnection();
		rnip.remove(vmUri);
		avmmops.get(vmUri).doDisconnection();
		avmmops.remove(vmUri);
		vmemops.get(vmUri).doDisconnection();
		vmemops.remove(vmUri);
		
		vms=new ArrayList<>(rsop.values());
	}

	
	
	@Override
	public void startUnlimitedPushing(int interval) throws Exception {
		// first, send the static state if the corresponding port is connected
		//this.sendStaticState() ;
		this.logMessage("Starting pushing Data");
		final RequestDispatcher c = this ;
		this.pushingFuture =
				this.scheduleTaskAtFixedRate(
						new ComponentI.ComponentTask() {
							@Override
							public void run() {
								try {
									c.sendDynamicState() ;
								} catch (Exception e) {
									throw new RuntimeException(e) ;
								}
							}
						}, interval, interval, TimeUnit.MILLISECONDS) ;

	}

	@Override
	public void startLimitedPushing(final int interval, final int n) throws Exception {
		assert	n > 0 ;
		this.logMessage(this.rdUri + " startLimitedPushing with interval "
				+ interval + " ms for " + n + " times.") ;

		// first, send the static state if the corresponding port is connected
		//this.sendStaticState() ;

		final RequestDispatcher c = this ;
		this.pushingFuture =
				this.scheduleTask(
						new ComponentI.ComponentTask() {
							@Override
							public void run() {
								try {
									c.sendDynamicState(interval, n) ;
								} catch (Exception e) {
									throw new RuntimeException(e) ;
								}
							}
						}, interval, TimeUnit.MILLISECONDS) ;
	}


	public void			sendDynamicState() throws Exception
	{
		if (this.rddsdip.connected() && numberOfRequests>0) {
			RequestDispatcherDynamicStateI rdds = this.getDynamicState() ;
			this.rddsdip.send(rdds) ;
		}
	}
	
	public void			sendDynamicState(
			final int interval,
			int numberOfRemainingPushes) throws Exception{
		this.sendDynamicState() ;
		final int fNumberOfRemainingPushes = numberOfRemainingPushes - 1 ;
		if (fNumberOfRemainingPushes > 0) {
			final RequestDispatcher c = this ;
			this.pushingFuture =
					this.scheduleTask(
							new ComponentI.ComponentTask() {
								@Override
								public void run() {
									try {
										c.sendDynamicState(
												interval,
												fNumberOfRemainingPushes) ;
									} catch (Exception e) {
										throw new RuntimeException(e) ;
									}
								}
							}, interval, TimeUnit.MILLISECONDS) ;
		}
	}

	@Override
	public void stopPushing() throws Exception {
		if (this.pushingFuture != null &&
				!(this.pushingFuture.isCancelled() ||
						this.pushingFuture.isDone())) {
			this.pushingFuture.cancel(false) ;
		}
	}


	public RequestDispatcherDynamicStateI getDynamicState() throws UnknownHostException {
		long average = (totalTime/numberOfRequests);
		return new RequestDispatcherDynamicState(average);
	}


	public void	acceptRequestSubmission(final RequestI r)
			throws Exception
	{
		
		//this.logMessage("Dispatcher["+id+"] : "+r.getRequestURI() +" => "+vms.get(lastVM).getPortURI());
		vms.get(lastVM).submitRequest(r);
		lastVM=(++lastVM)%vms.size();
	}

	public void	acceptRequestSubmissionAndNotify(final RequestI r) throws Exception
	{
		//this.logMessage("Dispatcher&N["+id+"] : "+r.getRequestURI() +" =====> "+vms.get(lastVM).getPortURI());
		vms.get(lastVM).submitRequestAndNotify(r);
		startTime.put(r.getRequestURI(), System.currentTimeMillis());
		lastVM=(++lastVM)%vms.size();
	}

	@Override
	public void acceptRequestTerminationNotification(RequestI r) throws Exception {
		//this.logMessage("Dispatcher&T["+id+"] : "+r.getRequestURI() +" => "+rnop.getPortURI());
		this.rnop.notifyRequestTermination(r);
		endTime.put(r.getRequestURI(), System.currentTimeMillis());
		lastTime.put(r.getRequestURI(), endTime.get(r.getRequestURI())-startTime.get(r.getRequestURI()));
		totalTime+=lastTime.get(r.getRequestURI());
		numberOfRequests++;
	}

	public long getAverageTime(){
		return totalTime/numberOfRequests;
	}

}
