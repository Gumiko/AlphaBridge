package fr.upmc.datacenter.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.ComponentI;
import fr.upmc.data.StaticData;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
import fr.upmc.datacenter.dispatcher.RequestDispatcher;
import fr.upmc.datacenter.dispatcher.RequestDispatcherDynamicState;
import fr.upmc.datacenter.dispatcher.connectors.RequestDispatcherActuatorConnector;
import fr.upmc.datacenter.dispatcher.connectors.RequestDispatcherManagementConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherSensorI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherStaticStateI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherActuatorOutboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataInboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataOutboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementOutboundPort;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.extension.vm.ports.VMExtendedManagementOutboundPort;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
import fr.upmc.datacenter.hardware.processors.Processor.ProcessorPortTypes;
import fr.upmc.datacenter.hardware.processors.interfaces.ProcessorManagementI;
import fr.upmc.datacenter.hardware.processors.ports.ProcessorIntrospectionOutboundPort;
import fr.upmc.datacenter.hardware.processors.ports.ProcessorManagementOutboundPort;
import fr.upmc.datacenter.interfaces.PushModeControllerI;
import fr.upmc.datacenter.ring.RingDynamicState;
import fr.upmc.datacenter.ring.interfaces.RingDataI;
import fr.upmc.datacenter.ring.interfaces.RingDynamicStateI;
import fr.upmc.datacenter.ring.ports.RingDynamicStateDataInboundPort;
import fr.upmc.datacenter.ring.ports.RingDynamicStateDataOutboundPort;
import fr.upmc.datacenter.software.applicationvm.ports.ApplicationVMManagementOutboundPort;

public class Controller extends AbstractComponent
implements RequestDispatcherSensorI,RingDataI,PushModeControllerI,ControllerManagementI{


	/** ports of the controller receiving the dynamic data from its processor
	 *  components.															*/
	protected final RequestDispatcherDynamicStateDataOutboundPort rddsdop;

	//	/** ports of the controller receiving the static data from its processor
	//	 *  components.															*/
	//	protected final RequestDispatcherStaticStateDataOutboundPort rdssdop ;


	String controllerURI;
	int controllerID;
	String requestDispatcherURI;
	RequestDispatcherManagementOutboundPort rdmop;
	RequestDispatcherActuatorOutboundPort rdaop;

	Map<Integer,ApplicationVMManagementOutboundPort> mapVMManagement;
	Map<Integer,VMExtendedManagementOutboundPort> mapVMEManagement;

	RingDynamicStateDataOutboundPort rdsdop;
	RingDynamicStateDataInboundPort rdsdip;

	List<AllocatedCore> acReserved;
	List<AllocatedCore> acFree;

	List<VMData> vmReserved;
	List<VMData> vmFree;

	int idVM=1;

	int waitingAllocation=0;
	int waitingDisallocation=0;

	/** future of the task scheduled to push dynamic data.					*/
	protected ScheduledFuture<?>			pushingFuture ;

	public Controller(String controllerURI,String rddsdipURI,String rdmipURI, String rdaipURI,int controllerID) throws Exception{
		this.controllerID=controllerID;
		mapVMManagement=new HashMap<Integer,ApplicationVMManagementOutboundPort>();
		mapVMEManagement=new HashMap<Integer,VMExtendedManagementOutboundPort>();

		vmReserved = new ArrayList<VMData>();
		vmFree = new ArrayList<VMData>();
		
		acReserved = new ArrayList<AllocatedCore>();
		acFree=new ArrayList<AllocatedCore>();

		this.controllerURI=controllerURI;
		/*Link the controller to the Request Dispatcher */
		rdmop=new RequestDispatcherManagementOutboundPort(this);
		rdmop.doConnection(rdmipURI, RequestDispatcherManagementConnector.class.getCanonicalName());

		rdaop=new RequestDispatcherActuatorOutboundPort(this);
		this.addPort(rdaop);
		rdaop.publishPort();
		rdaop.doConnection(rdaipURI, RequestDispatcherActuatorConnector.class.getCanonicalName());

		
		rddsdop=new RequestDispatcherDynamicStateDataOutboundPort(this,controllerURI);
		this.addPort(rddsdop);
		rddsdop.publishPort();
		rddsdop.doConnection(rddsdipURI,ControlledDataConnector.class.getCanonicalName());

		//rdmop.addVM(int id,String a,String b,String c);
		//rdmop.bindVM(id, str_rsop, str_rnip, str_avmmop);

		/*Dynamic data sending to the controller */

		rdsdop = new RingDynamicStateDataOutboundPort(this,controllerURI);

		rdsdip=new RingDynamicStateDataInboundPort(AdmissionController.C_DSDIP_PREFIX+controllerID, this);
		this.addPort(rdsdip) ;
		this.rdsdip.publishPort();
		
		
		
		//rddsdop.startUnlimitedPushing(5000);
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

		System.out.println("Controller " + this.controllerURI +
				" accepting dynamic data from " + requestDispatcherURI) ;
		System.out.println("  timestamp                : " +
				currentDynamicState.getTimeStamp()) ;
		System.out.println("  timestamper id           : " +
				currentDynamicState.getTimeStamperId()) ;
		System.out.print(  "  current idle status      : [") ;


		long time = currentDynamicState.getAverageTime();
		processControl(time);
	}

	private void processControl(long time) {
		double factor=0;
		METHOD method=METHOD.NORMAL;
		if(isHigher(time)){
			factor = (time/StaticData.AVERAGE_TARGET);
			method=METHOD.HIGHER;
		}
		if(isLower(time)){
			factor = (StaticData.AVERAGE_TARGET/time);
			method=METHOD.LOWER;
		}
		if(method!=METHOD.NORMAL){
			int cores = getNumberOfCoreAllocated();
			if(method==METHOD.LOWER){
				disallocate(Math.max(1, (int)(cores-(cores/factor))));
			}else{
				allocate(Math.max(1, (int)(cores*factor)));
			}
		}
	}

	public void raiseFrequency(int nbCore,int id) throws Exception{

		ApplicationVMManagementOutboundPort freqVMM=this.mapVMManagement.get(id);
		VMExtendedManagementOutboundPort freqVMEM=this.mapVMEManagement.get(id);

		VMData d=freqVMEM.getData();

		ProcessorManagementOutboundPort pmop=new ProcessorManagementOutboundPort(this);
		ProcessorIntrospectionOutboundPort piop=new ProcessorIntrospectionOutboundPort(this);

		for(int i=0;i<nbCore;i++){
			for(Entry<String, Map<ProcessorPortTypes, String>> e : d.getProc().entrySet()){
				pmop.doConnection(e.getValue().get(ProcessorPortTypes.MANAGEMENT), ProcessorManagementI.class.getCanonicalName());
				piop.doConnection(e.getValue().get(ProcessorPortTypes.INTROSPECTION), ProcessorManagementI.class.getCanonicalName());


				//pmop.setCoreFrequency(coreNo, frequency);
			}
		}
	}

	public void lowerFrequency(int nbCore){

	}


	private void allocate(int i) {
		waitingAllocation=i;

	}


	private void disallocate(int i) {
		waitingDisallocation=i;

	}


	private int getNumberOfCoreAllocated() {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean isHigher(long time){
		return (time > (StaticData.AVERAGE_TARGET*StaticData.PERCENT + StaticData.AVERAGE_TARGET));
	}

	public boolean isLower(long time){
		return (time < (StaticData.AVERAGE_TARGET*StaticData.PERCENT - StaticData.AVERAGE_TARGET));
	}

	public enum METHOD{
		LOWER,LOW,NORMAL,HIGH,HIGHER
	}

	@Override
	public void acceptRingDynamicData(String requestDispatcherURI, RingDynamicStateI currentDynamicState)
			throws Exception {

		if(waitingAllocation>0){

		}
		/*TODO*/
		/*
		 * 
		 * 
		 * currentDynamicState.getVMDatas();
		 * 
		 */

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

	/*TODO MODIFY*/
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
		ArrayList<VMData> copy=new ArrayList<>();
		RingDynamicState rds=new RingDynamicState(copy);
		vmFree.clear();
		return rds;
	}


	@Override
	public void stopSending() throws Exception {
		stopPushing();
	}


	@Override
	public void startSending() throws Exception {
		startUnlimitedPushing(StaticData.RING_PUSH_INTERVAL);

	}


	@Override
	public String getNextControllerUri() {
		return null;
	}


	@Override
	public String getPreviousControllerUri() {
		return null;
	}


	@Override
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception {
		if(rdsdop.connected())
			rdsdop.doDisconnection();
		rdsdop.doConnection(DataInboundPortUri, ControlledDataConnector.class.getCanonicalName());
	}



}
