package fr.upmc.datacenter.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.ComponentI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataInboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementInboundPort;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.extension.vm.connectors.VMExtendedManagementConnector;
import fr.upmc.datacenter.extension.vm.ports.VMExtendedManagementOutboundPort;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
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


/**
 * The class <code>RequestDispatcher</code> implements the component representing
 * a Request Dispatcher in the data center.
 *
 * <p><strong>Description</strong></p>
 * 
 * The Request Dispatcher (RD) component receive the requests of the request generator
 * and dispatch them to the Application VM allocated to him
 * 
 * It's Linked to a Controller and the it send him data about
 * the average time that make the requests to finish with others informations. 
 * 
 *
 * 
 * <p>Created on : 2016-2017</p>
 * 
 * @author	C�dric Ribeiro et Mokrane Kadri
 */
public class RequestDispatcher extends AbstractComponent
implements RequestDispatcherI,RequestDispatcherManagementI,RequestSubmissionHandlerI,RequestNotificationHandlerI,PushModeControllerI
{


	//	/** RequestDispatcher data inbound port through which it pushes its static data.	*/
	//	protected RequestDispatcherStaticStateDataInboundPort
	//				requestDispatcherStaticStateDataInboundPort ;
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

	/** URI of the request dispatcher*/
	protected String rdUri;
	/** ID of the request Dispatcher*/
	protected int id;

	/** ID of the RequestNotificationInboundPort*/
	protected int notificationid;

	/** RequestSubmissionInboundPort of the Request Dispatcher*/
	protected RequestSubmissionInboundPort	rsip;
	/** RequestNotificationOutboundPort of the Request Dispatcher*/
	protected RequestNotificationOutboundPort rnop;
	/** RequestDispatcherManagementInboundPort of the Request Dispatcher*/
	protected RequestDispatcherManagementInboundPort rdmip;

	/** Map of RequestSubmissionOutboundPort of VM by their URI */
	protected Map<String,RequestSubmissionOutboundPort> rsop;
	/** Map of RequestNotificationInboundPort of VM by their URI */
	protected Map<String,RequestNotificationInboundPort> rnip;
	/** Map of ApplicationVMManagementOutboundPort of VM by their URI */
	protected Map<String,ApplicationVMManagementOutboundPort> avmmops;
	/** Map of VMExtendedManagementOutboundPort of VM by their URI */
	protected Map<String,VMExtendedManagementOutboundPort> vmemops;
	/**Last VM used to send Request*/
	int lastVM;


	private ArrayList<RequestSubmissionOutboundPort> vms;

	/* Data Management */
	/** Map of the beginning of a request of a VM by their URI */
	Map<String,Long> startTime=new HashMap<String,Long>();
	/** Map of the end of a request of a VM by their URI */
	Map<String,Long> endTime=new HashMap<String,Long>();
	/** Map of the last Time taken by request of a VM by their URI */
	Map<String,Long> lastTime=new HashMap<String,Long>();

	int numberOfRequests;
	int totalTime;
	int averageTime;
	/** Object used to synchronize Data access*/
	private Object o=new Object();
	/**
	 *  Create a Request Dispatcher
	 * @param id ID of the request dispatcher
	 * @param rdURI URI of the request Dispatcher
	 * @param requestDispatcherRequestSubmissionInboundPortURI requestDispatcherRequestSubmissionInboundPortURI's URI of the request dispatcher
	 * @param requestDispatcherActuatorInboundPort requestDispatcherActuatorInboundPort's URI of the request dispatcher
	 * @param requestDispatcherDynamicStateDataInboundPort requestDispatcherDynamicStateDataInboundPort's URI of the request dispatcher
	 * @param requestDispatcherManagementInboundPort requestDispatcherManagementInboundPort's URI of the request dispatcher
	 * @throws Exception e
	 */
	public RequestDispatcher(int id, String rdURI,String requestDispatcherRequestSubmissionInboundPortURI,String requestDispatcherActuatorInboundPort,String requestDispatcherDynamicStateDataInboundPort,String requestDispatcherManagementInboundPort) throws Exception{
		/* Init Request Dispatcher */
		super(1, 1) ;
		this.id=id;
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
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherI#linkRequestGenerator(java.lang.String)
	 */
	public void linkRequestGenerator(String requestNotificationInboundPortURI) throws Exception{
		this.logMessage("Linking Dispatcher["+id+"] to RG to...");
		this.logMessage("CONNECTION : "+rnop.getPortURI()+" -> "+requestNotificationInboundPortURI);
		this.rnop.doConnection(requestNotificationInboundPortURI, RequestNotificationConnector.class.getCanonicalName());
		this.logMessage("RG linked to Dispatcher["+id+"] !"); //"+rg_rnip.getPortURI()+" | "+rg_rsop.getPortURI() );
	}
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#bindVM(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void bindVM(String vmUri,String vmRequestSubmissionInboundPortURI,String applicationVMManagementInboundPortURI,String VMExtendedManagementInboundPortURI) throws Exception {
		this.logMessage("VM["+vmUri+"] : Linking...");
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

	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#unbindVM(java.lang.String)
	 */
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
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#removeCore(int, java.lang.String)
	 */
	@Override
	public AllocatedCore[] removeCore(int number,String vmURI){
		VMExtendedManagementOutboundPort port = vmemops.get(vmURI);
		if(port!=null)
			return port.removeCore(number);
		return null;
	}
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#addCore(int, java.lang.String)
	 */
	@Override
	public int addCore(int number,String vmURI) throws Exception{
		VMExtendedManagementOutboundPort port = vmemops.get(vmURI);
		if(port!=null)
			return port.addCore(number);
		return 0;
	}
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#removeAll(java.lang.String)
	 */
	@Override
	public AllocatedCore[] removeAll(String vmURI){
		VMExtendedManagementOutboundPort port = vmemops.get(vmURI);
		if(port!=null)
			return port.removeAll();
		return null;

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


	public RequestDispatcherDynamicStateI getDynamicState() throws Exception {
		long average = 0;
		if(numberOfRequests>0)
			average=(totalTime/numberOfRequests);
		ArrayList<VMData> vms=new ArrayList<VMData>();
		for(VMExtendedManagementOutboundPort man : vmemops.values()){
			vms.add(man.getData());
		}
		return new RequestDispatcherDynamicState(average,numberOfRequests,vms);
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
		//this.logMessage("Dispatcher&N["+id+"] : "+r.getRequestURI() +" =====> "+vms.get(lastVM).getServerPortURI());
		vms.get(lastVM).submitRequestAndNotify(r);
		startTime.put(r.getRequestURI(), System.currentTimeMillis());
		lastVM=(++lastVM)%vms.size();
	}

	@Override
	public synchronized void acceptRequestTerminationNotification(RequestI r) throws Exception {
		//this.logMessage("Dispatcher&T["+id+"] : "+r.getRequestURI() +" => "+rnop.getPortURI());
		this.rnop.notifyRequestTermination(r);
		endTime.put(r.getRequestURI(), System.currentTimeMillis());
		synchronized(o){
			lastTime.put(r.getRequestURI(), endTime.get(r.getRequestURI())-startTime.get(r.getRequestURI()));
			totalTime+=lastTime.get(r.getRequestURI());
			numberOfRequests++;
		}
	}

	public long getAverageTime(){
		return totalTime/numberOfRequests;
	}

	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#resetRequestNumber()
	 */
	@Override
	public void resetRequestNumber(){
		synchronized(o){
			totalTime=0;
			numberOfRequests=0;
			lastTime.clear();
		}
	}
}
