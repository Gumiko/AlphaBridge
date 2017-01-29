package fr.upmc.datacenter.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.ComponentI;
import fr.upmc.data.StaticData;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
import fr.upmc.datacenter.controller.ports.ControllerManagementInboundPort;
import fr.upmc.datacenter.dispatcher.connectors.RequestDispatcherManagementConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherSensorI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherStaticStateI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataOutboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementOutboundPort;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.interfaces.PushModeControllerI;
import fr.upmc.datacenter.ring.RingDynamicState;
import fr.upmc.datacenter.ring.interfaces.RingDataI;
import fr.upmc.datacenter.ring.interfaces.RingDynamicStateI;
import fr.upmc.datacenter.ring.ports.RingDynamicStateDataInboundPort;
import fr.upmc.datacenter.ring.ports.RingDynamicStateDataOutboundPort;

public class Controller extends AbstractComponent
implements RequestDispatcherSensorI,RingDataI,PushModeControllerI,ControllerManagementI{


	/** ports of the controller receiving the dynamic data from its processor
	 *  components.															*/
	protected final RequestDispatcherDynamicStateDataOutboundPort rddsdop;

	/** URI of the controller*/
	String controllerURI;
	/** ID of the Controller*/
	int controllerID;
	
	/** Uri of the Request Dispatcher linked to the controller*/
	String requestDispatcherURI;
	/** RequestDispatcherManagementOutboundPort of the request dispatcher linked to the controller*/
	RequestDispatcherManagementOutboundPort rdmop;

	/** ControllerManagementInboundPort of the controller */
	ControllerManagementInboundPort cmip;
	/** RingDynamicStateDataOutboundPort of the controller */
	RingDynamicStateDataOutboundPort rdsdop;
	/**RingDynamicStateDataInboundPort of the controller */
	RingDynamicStateDataInboundPort rdsdip;

	/** Uri of the next controller linked in the ring data */
	String nextControllerUri;
	/** Uri of the previous controller linked in the ring data*/
	String previousControllerUri;
	/** List of VMData reserved by the Controller */
	List<VMData> vmReserved;
	/** List of VMData that can circulate in the data ring*/
	List<VMData> vmFree;
	/** Object use to synchronized access to data*/
	private Object o = new Object();
	/** Number of core to be allocated*/
	int waitingAllocation=0;
	/** Number of core to be deallocated*/
	int waitingDeallocation=0;

	/** future of the task scheduled to push dynamic data.					*/
	protected ScheduledFuture<?>			pushingFuture ;

	/**
	 *  Create a Controller
	 * @param controllerURI uri of the controller
	 * @param rddsdipURI Uri of the RequestDispatcherDynamicStateDataInboundPort
	 * @param rdmipURI Uri of the RequestDispatcherManagementInboundPort
	 * @param rdaipURI Uri of the RequestDispatcherActuatorInboundPort
	 * @param controllerID ID of the controller
	 * @param controllerManagementInboundPortUri Uri of the ControllerManagementInboundPort
	 * @throws Exception e
	 */
	public Controller(String controllerURI,String rddsdipURI,String rdmipURI, String rdaipURI,int controllerID,String controllerManagementInboundPortUri) throws Exception{
		super(1, 1) ;
		this.controllerID=controllerID;

		cmip=new ControllerManagementInboundPort(controllerManagementInboundPortUri,this);
		this.addPort(cmip);
		cmip.publishPort();

		vmReserved = new ArrayList<VMData>();
		vmFree = new ArrayList<VMData>();

		this.controllerURI=controllerURI;
		/*Link the controller to the Request Dispatcher */
		rdmop=new RequestDispatcherManagementOutboundPort(this);
		this.addPort(rdmop);
		rdmop.publishPort();
		rdmop.doConnection(rdmipURI, RequestDispatcherManagementConnector.class.getCanonicalName());

		rddsdop=new RequestDispatcherDynamicStateDataOutboundPort(this,controllerURI);
		this.addPort(rddsdop);
		rddsdop.publishPort();
		rddsdop.doConnection(rddsdipURI,ControlledDataConnector.class.getCanonicalName());

		/*Dynamic data sending to the controller */

		rdsdop = new RingDynamicStateDataOutboundPort(this,controllerURI);
		this.addPort(rdsdop);
		this.rdsdop.publishPort();

		rdsdip=new RingDynamicStateDataInboundPort(AdmissionController.C_DSDIP_PREFIX+controllerID, this);
		this.addPort(rdsdip) ;
		this.rdsdip.publishPort();

		this.toggleLogging();
		this.toggleTracing();

	}


	@Override
	public void acceptRequestDispatcherstaticData(String requestDispatcherURI,
			RequestDispatcherStaticStateI staticState) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptRequestDispatcherDynamicData(String requestDispatcherURI,
			RequestDispatcherDynamicStateI currentDynamicState) throws Exception {
		assert	requestDispatcherURI != null ;
		assert	currentDynamicState != null ;

		long time = currentDynamicState.getAverageTime();
		processControl(time,currentDynamicState.getNbreq(),currentDynamicState.getVMDatas());
	}
	/** Start adjusting the request dispatcher power with the data provided */
	private void processControl(long time,int nbreq,ArrayList<VMData> vms) throws Exception {
		this.logMessage("    /!\\ CONTROL : "+this.controllerURI+ " receiving average time :"+time+" with the last "+nbreq+" requests with "+vms.size()+" VM : "+getNumberOfCoreAllocated(vms)+" cores");
		if(nbreq<30 || (nbreq<20 && time>20000))
			return;
		double factor=0;
		int number=0;
		int cores = getNumberOfCoreAllocated(vms);

		switch(getMethod(time)){
		case HIGHER :
			factor = (time/StaticData.AVERAGE_TARGET);
			number =Math.max(1, (int)(cores*factor));
			number =Math.min(StaticData.MAX_ALLOCATION, number);
			allocate(number);
			processAllocation(factor,number,vms,time,nbreq,cores);
			rdmop.resetRequestNumber();
			break;
		case LOWER :
			factor = (StaticData.AVERAGE_TARGET/time);
			number =Math.max(1, (int)(cores-(cores/factor)));
			number =Math.min(StaticData.MAX_DEALLOCATION, number);
			deallocate(number);
			if(vms.size()==1)
				if(vms.get(0).getNbCore()==2)
					break;
			processDeallocate(factor,number,vms,time,nbreq,cores);
			rdmop.resetRequestNumber();
			break;
		case NORMAL :
			allocate(0);
			deallocate(0);
			break;
		default:
			allocate(0);
			deallocate(0);
			break;
		}
	}


	private void processAllocation(double factor, int number, ArrayList<VMData> vms, long time, int nbreq, int cores) throws Exception {
		this.logMessage("\n\n--------"+this.controllerURI+"---[HIGHER]---------------------------\n"
				+ "VM_FREE : "+vmFree.size()+"\n"
				+ "VM_RESERVED : "+vmReserved.size()+"\n"
				+ "VM ALLOCATED : "+vms.size()+"\n"
				+ "AVERAGE TIME : "+time+" : last "+nbreq+" requests\n"
				+ "ALLOCATE : "+waitingAllocation+"\n"
				+ "DEALLOCATE : "+waitingDeallocation+"\n"
				+ "FACTOR : "+factor+"\n"
				+ "NUMBER : "+number+"\n"
				+ "Total Actual Core Allocated : "+cores+"\n"
				+"--------------------------------------------------------------------\n");
		/*Sorting the VMS by the ascending order of the number of cores allocated*/
		ArrayList<VMData> list=new ArrayList<VMData>(vms);
		Collections.sort(list);
		Collections.reverse(list);

		synchronized(o){
			while(waitingAllocation>0 && !list.isEmpty()){
				int allocated=0;
				VMData processing=list.remove(0);
				allocated=rdmop.addCore(waitingAllocation, processing.getVMUri());
				waitingAllocation=waitingAllocation-allocated;
				this.logMessage("[CONTROL] : "+ this.controllerURI + " ALLOCATED : "+allocated+" cores to VM : "+processing.getVMUri());
			}
			if(waitingAllocation==0){

			}else{
				while(waitingAllocation>0){
					if(!vmReserved.isEmpty()){
						VMData v = vmReserved.remove(0);
						rdmop.bindVM(v.getVMUri(), v.getVMRequestSubmission(),v.getVMManagement(),v.getVMEManagement());
						int allocated=rdmop.addCore(waitingAllocation, v.getVMUri());
						waitingAllocation=waitingAllocation-v.getNbCore()-allocated;
						this.logMessage("[CONTROL] : "+ this.controllerURI + " ALLOCATED new VM : "+v.getVMUri()+ " allocated "+allocated+" cores");
					}else if(!vmFree.isEmpty()){
						VMData v = vmFree.remove(0);
						rdmop.bindVM(v.getVMUri(), v.getVMRequestSubmission(),v.getVMManagement(),v.getVMEManagement());
						int allocated=rdmop.addCore(waitingAllocation, v.getVMUri());
						waitingAllocation=waitingAllocation-v.getNbCore()-allocated;
						this.logMessage("[CONTROL] : "+ this.controllerURI + " ALLOCATED new VM : "+v.getVMUri()+ " allocated "+allocated+" cores");
					}else{
						break;
					}
				}
			}
		}
	}



	private void processDeallocate(double factor, int number, ArrayList<VMData> vms, long time, int nbreq,int cores) throws Exception {
		this.logMessage("\n\n--------"+this.controllerURI+"---[LOWER]---------------------------\n"
				+ "VM_FREE : "+vmFree.size()+"\n"
				+ "VM_RESERVED : "+vmReserved.size()+"\n"
				+ "VM ALLOCATED : "+vms.size()+"\n"
				+ "AVERAGE TIME : "+time+" : last "+nbreq+" requests\n"
				+ "ALLOCATE : "+waitingAllocation+"\n"
				+ "DEALLOCATE : "+waitingDeallocation+"\n"
				+ "FACTOR : "+factor+"\n"
				+ "NUMBER : "+number+"\n"
				+ "Total Actual Core Allocated : "+cores+"\n"
				+"--------------------------------------------------------------------\n");
		ArrayList<VMData> list=new ArrayList<VMData>(vms);
		Collections.sort(list);

		synchronized(o){
			while(waitingDeallocation>0 && !list.isEmpty()){
				int deallocated=0;
				VMData processing=list.remove(0);
				int toDeallocate=Math.min(processing.getNbCore()-2,waitingDeallocation);
				deallocated=rdmop.removeCore(toDeallocate, processing.getVMUri()).length;
				waitingDeallocation=waitingDeallocation-deallocated;
				this.logMessage("[CONTROL] : "+ this.controllerURI + " DEALLOCATED : "+deallocated+" of VM : "+processing.getVMUri());
			}
			if(waitingDeallocation==0){
			}else{
				list=new ArrayList<VMData>(vms);
				while(list.size()>2 && waitingDeallocation>0){
					VMData v = list.remove(0);
					rdmop.unbindVM(v.getVMUri());
					vmFree.add(v);
					waitingDeallocation=waitingDeallocation-v.getNbCore();
					this.logMessage("[CONTROL] : "+ this.controllerURI + " DEALLOCATED  VM : "+v.getVMUri());
				}
			}
		}
	}




	private void allocate(int i) {
		waitingAllocation=i;
	}


	private void deallocate(int i) {
		waitingDeallocation=i;
	}


	private int getNumberOfCoreAllocated(ArrayList<VMData> vms) throws Exception {
		int total=0;
		for(VMData vm : vms){
			total+=vm.getNbCore();
		}
		return total;
	}


	public METHOD getMethod(long time){
		if(isHigher(time))
			return METHOD.HIGHER;
		if(isLower(time))
			return METHOD.LOWER;
		return METHOD.LOWER;
	}

	public boolean isHigher(long time){
		return (time > (StaticData.AVERAGE_TARGET*StaticData.HIGHER_PERCENT + StaticData.AVERAGE_TARGET));
	}

	public boolean isLower(long time){
		return (time < (StaticData.AVERAGE_TARGET*StaticData.LOWER_PERCENT - StaticData.AVERAGE_TARGET));
	}

	public enum METHOD{
		LOWER,LOW,NORMAL,HIGH,HIGHER
	}

	@Override
	public void acceptRingDynamicData(String requestDispatcherURI, RingDynamicStateI currentDynamicState)
			throws Exception {
		synchronized(o){
			//this.logMessage("[----DATA----]"+this.controllerURI+ " RECEIVE " +currentDynamicState.getVMDataList().size()+ " FREE VM");

			vmFree.addAll(currentDynamicState.getVMDataList());
			//this.logMessage("[----DATA----]"+this.controllerURI+ " FREE["+vmFree.size()+"] | RESERVED["+vmReserved.size()+"]");
			while(waitingAllocation>0 && !vmFree.isEmpty()){
				vmReserved.add(vmFree.remove(0));
				waitingAllocation--;
			}
		}
	}


	@Override
	public void startUnlimitedPushing(int interval) throws Exception {
		// first, send the static state if the corresponding port is connected
		//this.sendStaticState() ;
		final Controller c = this ;
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
		this.logMessage(this.controllerURI + " startLimitedPushing with interval "
				+ interval + " ms for " + n + " times.") ;

		// first, send the static state if the corresponding port is connected
		//this.sendStaticState() ;

		final Controller c = this ;
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
		if (this.rdsdip.connected()) {
			RingDynamicStateI rds = this.getDynamicState() ;
			this.rdsdip.send(rds) ;
		}
	}

	public void			sendDynamicState(
			final int interval,
			int numberOfRemainingPushes) throws Exception{
		this.sendDynamicState() ;
		final int fNumberOfRemainingPushes = numberOfRemainingPushes - 1 ;
		if (fNumberOfRemainingPushes > 0) {
			final Controller c = this ;
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


	public RingDynamicState getDynamicState() throws UnknownHostException {
		synchronized(o){
			ArrayList<VMData> copy=new ArrayList<>(vmFree);
			RingDynamicState rds=new RingDynamicState(copy);
			vmFree.clear();
			return rds;
		}
	}

	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#stopSending()
	 */
	@Override
	public void stopSending() throws Exception {
		stopPushing();
	}

	/** 
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#startSending()
	 */
	@Override
	public void startSending() throws Exception {
		startUnlimitedPushing(StaticData.RING_PUSH_INTERVAL);

	}

	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getNextControllerUri()
	 */
	@Override
	public String getNextControllerUri() {
		return nextControllerUri;
	}

	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#setPreviousControllerUri(java.lang.String)
	 */
	@Override
	public void setPreviousControllerUri(String controllerManagementUri) {
		previousControllerUri=controllerManagementUri;
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#setNextControllerUri(java.lang.String)
	 */
	@Override
	public void setNextControllerUri(String controllerManagementUri) {
		nextControllerUri=controllerManagementUri;
	}

	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getPreviousControllerUri()
	 */
	@Override
	public String getPreviousControllerUri() {
		return previousControllerUri;
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getControllerRingDataInboundPortUri()
	 */
	@Override
	public String getControllerRingDataInboundPortUri() throws Exception{
		return this.rdsdip.getPortURI();
	}

	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#bindSendingDataUri(java.lang.String)
	 */
	@Override
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception {
		if(rdsdop.connected())
			rdsdop.doDisconnection();
		rdsdop.doConnection(DataInboundPortUri, ControlledDataConnector.class.getCanonicalName());
	}



}
