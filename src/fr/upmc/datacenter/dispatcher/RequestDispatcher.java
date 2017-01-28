package fr.upmc.datacenter.dispatcher;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.ComponentI;
import fr.upmc.components.interfaces.DataOfferedI.DataI;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherActuatorI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataInboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementInboundPort;
import fr.upmc.datacenter.extension.vm.ports.VMExtendedManagementOutboundPort;
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
	protected Map<Integer,VMExtendedManagementOutboundPort> vmemops;

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

	public void linkRequestGenerator(String requestSubmissionOutboundPortURI,String requestNotificationInboundPortURI) throws Exception{
		this.logMessage("Linking RG to Dispatcher["+id+"] ...");
		this.logMessage("CONNECTION : "+requestSubmissionOutboundPortURI+" -> "+rsip.getPortURI());
		this.logMessage("CONNECTION : "+rnop.getPortURI()+" -> "+requestNotificationInboundPortURI);
		rsip.doConnection(requestSubmissionOutboundPortURI, RequestSubmissionConnector.class.getCanonicalName());
		rnop.doConnection(requestNotificationInboundPortURI, RequestNotificationConnector.class.getCanonicalName());
		this.logMessage("RG linked to Dispatcher["+id+"] !"); //"+rg_rnip.getPortURI()+" | "+rg_rsop.getPortURI() );
	}

	@Override
	public void bindVM(int id,String requestSubmissionOutboundPortURI,String applicationVMManagementOutboundPortURI) throws Exception {
		this.logMessage("VM"+id+" : Linking...");
		RequestSubmissionOutboundPort rsopvm = new RequestSubmissionOutboundPort(requestSubmissionOutboundPortURI, this);
		//RequestNotificationInboundPort rnipvm = new RequestNotificationInboundPort(str_rnip, this);
		ApplicationVMManagementOutboundPort avmmop=new ApplicationVMManagementOutboundPort(this);

		this.addPort(rsopvm);
		//this.addPort(rnipvm);

		rsopvm.publishPort();
		//rnipvm.publishPort();

		this.rsop.put(id, rsopvm);
		//this.rnip.put(id, rnipvm);

		avmmop.doConnection(applicationVMManagementOutboundPortURI, ApplicationVMManagementConnector.class.getCanonicalName());
		avmmops.put(id, avmmop);

		this.logMessage("VM"+id+" : Linked !");
	}


	@Override
	public void unbindVM(int id) throws Exception {
		rsop.get(id).doDisconnection();
		rsop.remove(id);
		//rnip.get(id).doDisconnection();
		//rnip.remove(id);
		avmmops.get(id).doDisconnection();
		avmmops.remove(id);
		vmemops.get(id).doDisconnection();
		vmemops.remove(id);
	}

	
	
	@Override
	public void startUnlimitedPushing(int interval) throws Exception {
		// first, send the static state if the corresponding port is connected
		//this.sendStaticState() ;
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
		this.logMessage(this.RDuri + " startLimitedPushing with interval "
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
		if (this.rddsdip.connected()) {
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
		RequestDispatcherDynamicState rdds=new RequestDispatcherDynamicState(this.averageTime);
		return rdds;
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
