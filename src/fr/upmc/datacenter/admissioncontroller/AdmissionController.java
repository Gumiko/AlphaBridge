package fr.upmc.datacenter.admissioncontroller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.ComponentI;
import fr.upmc.components.connectors.DataConnector;
import fr.upmc.data.StaticData;
import fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI;
import fr.upmc.datacenter.admissioncontroller.ports.ApplicationRequestInboundPort;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.admissioncontroller.ports.AdmissionControllerManagementInboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.connectors.ControllerManagementConnector;
import fr.upmc.datacenter.controller.ports.ControllerManagementOutboundPort;
import fr.upmc.datacenter.dispatcher.RequestDispatcher;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.extension.vm.VirtualMachineExtended;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
import fr.upmc.datacenter.hardware.computers.connectors.ComputerServicesConnector;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerDynamicStateI;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerStateDataConsumerI;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerStaticStateI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerDynamicStateDataOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerStaticStateDataOutboundPort;
import fr.upmc.datacenter.interfaces.PushModeControllerI;
import fr.upmc.datacenter.ring.RingDynamicState;
import fr.upmc.datacenter.ring.interfaces.RingDataI;
import fr.upmc.datacenter.ring.interfaces.RingDynamicStateI;
import fr.upmc.datacenter.ring.ports.RingDynamicStateDataInboundPort;
import fr.upmc.datacenter.ring.ports.RingDynamicStateDataOutboundPort;

/**
 * The class <code>AdmissionController</code> implements a component that represents an
 * Admission Controller in a datacenter, receiving new application.
 *
 * <p><strong>Description</strong></p>
 * 
 * The Admission Controller Purpose is to manage the new applications and computers sent to the datacenter.
 * 
 * When receiving a computer, he split the cores in multiples Virtual Machines then send it to the Ring Data.
 * 
 * The Admission Controller receive new Request Generator through the <code>ApplicationRequestI</code> Interface.
 * When receiving a new request generator, it check if he has enough Virtual Machine at disposition and create
 * a Request Dispatcher linked to the request generator and a Controller linked to the request Dispatcher.
 * He then bind the Virtual Machine to the request Dispatcher.
 * He add the Controller to the Data Ring where Free Virtual Machine that can be used by the controller circulate.
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */

public class AdmissionController extends AbstractComponent
implements ApplicationRequestI,AdmissionControllerManagementI,ComputerStateDataConsumerI,RingDataI,PushModeControllerI
{
	public static final String COMPUTER_STATIC_DATA_PREFIX = "COMPUTER_STATIC_DATA";
	public static final String COMPUTER_DYNAMIC_DATA_PREFIX = "COMPUTER_DYNAMIC_DATA";
	public static final String CONTROLLER_PREFIX = "CONTROLLER_";
	public static final String CONTROLLER_MANAGEMENT_PREFIX = "CO_MANAGEMENT";
	public static final String C_DSDIP_PREFIX = "C_DSDIP";

	public static final String RD_PREFIX = "RD_";
	public static final String RD_MIP_PREFIX = "RD_MIP";
	public static final String RD_AIP_PREFIX = "RD_AIP";
	public static final String RD_DSDIP_PREFIX = "RD_DSDIP";

	public static final String RD_MOP_PREFIX = "RD_MOP";
	public static final String RD_AOP_PREFIX = "RD_AOP";
	public static final String RD_DSDOP_PREFIX = "RD_DSDOP";

	public static final String VM_URI_PREFIX = "VM_";
	public static final String VM_AVMMIP_PREFIX = "VM_AVMMIP";
	public static final String VM_RSIP_PREFIX = "VM_RSIP";
	public static final String VM_RNOP_PREFIX = "VM_RNOP";
	public static final String VM_VMEMIP_PREFIX = "VM_VMEMIP";



	private final int PARAMETER_INITIAL_NB_CORE=2;
	private final int PARAMETER_INITIAL_NB_VM=2;
	private final int MAX_VM_RESERVED=8;

	private int VM_ID=1;
	private int APP_ID=1;
	private int RD_ID=1;
	private int CO_ID=1;
	private int COMP_ID=1;

	Object o=new Object();

	/*Admission Controller*/
	/** Admission Controller URI*/
	String admissionControllerURI;
	/** Admission Controller Management InboundPort */
	AdmissionControllerManagementInboundPort admissionControllerManagementInboundPort;
	/** The ApplicationRequestInboundPort of the Admission Controller */
	ApplicationRequestInboundPort applicationRequestInboundPort;

	/*Controllers*/
	String nextControllerManagementUri=null;
	String previousControllerManagementUri=null;

	/*Request Dispatcher*/
	/** Map of applicationURI received by the Admissions Controller*/
	Map<Integer,String> applicationURI;
	/** Map of requestDispatcherURI created by the Admissions Controller*/
	Map<Integer,String> requestDispatcherURI;
	/** Map of requestDispatcherManagementURIs created by the Admissions Controller*/
	Map<Integer,String> requestDispatcherManagementURIs;

	/*Computers*/
	/** Map of computerUris sent to the Admissions Controller*/
	Map<Integer,String> computerURIs;
	/** Map of computerServicesPorts sent to the Admissions Controller*/
	Map<Integer,ComputerServicesOutboundPort> computerServicesPorts;
	/** Map of computerDynamicData sent to the Admissions Controller*/
	Map<Integer, ComputerDynamicStateDataOutboundPort> computerDynamicData;
	/** Map of computerStaticData sent to the Admissions Controller*/
	Map<Integer, ComputerStaticStateDataOutboundPort> computerStaticData;

	/*VM*/
	/** Virtual Machine Reserved by the Admission Controller */
	List<VMData> Reserved;
	/** Virtual Machine that can circulate to the Ring Data */
	List<VMData> Free;

	/** future of the task scheduled to push dynamic data.					*/
	protected ScheduledFuture<?>			pushingFuture ;
	/** RingDynamicStateDataOutboundPort of the admission controller */
	RingDynamicStateDataOutboundPort rdsdop;
	/**RingDynamicStateDataInboundPort of the admission controller */
	RingDynamicStateDataInboundPort rdsdip;
	/**
	 * Create an Admission Controller
	 * 
	 * @param admissionControllerURI URI of the admission controller
	 * @param applicationRequestInboundURI applicationRequestInboundURI of the admission controller
	 * @param admissionControllerManagementInboundURI admissionControllerManagementInboundURI of the admission controller
	 * @throws Exception e
	 */
	public AdmissionController(String admissionControllerURI,String applicationRequestInboundURI,String admissionControllerManagementInboundURI) throws Exception{

		super(1,1);
		this.toggleLogging();
		this.toggleTracing();
		this.admissionControllerURI=admissionControllerURI;
		/*Initialize Request Dispatchers Mapping*/
		applicationURI=new HashMap<Integer,String>();
		requestDispatcherURI=new HashMap<Integer,String>();
		requestDispatcherManagementURIs=new HashMap<Integer,String>();
		/*Initialize Computers Mapping*/
		computerURIs = new HashMap<Integer,String>();
		computerServicesPorts = new HashMap<Integer,ComputerServicesOutboundPort>();
		computerDynamicData = new HashMap<Integer,ComputerDynamicStateDataOutboundPort>();
		computerStaticData = new HashMap<Integer,ComputerStaticStateDataOutboundPort>();

		/*Initialized VMs Data List */
		Reserved = new ArrayList<VMData>();
		Free= new ArrayList<VMData>();

		/* Init all ports */

		this.addOfferedInterface(ApplicationRequestI.class) ;
		this.applicationRequestInboundPort = new ApplicationRequestInboundPort(
				applicationRequestInboundURI, this) ;
		this.addPort(this.applicationRequestInboundPort) ;
		this.applicationRequestInboundPort.publishPort() ;

		this.addOfferedInterface(AdmissionControllerManagementI.class) ;
		this.admissionControllerManagementInboundPort = new AdmissionControllerManagementInboundPort(
				admissionControllerManagementInboundURI, this) ;
		this.addPort(this.admissionControllerManagementInboundPort) ;
		this.admissionControllerManagementInboundPort.publishPort() ;

		rdsdop = new RingDynamicStateDataOutboundPort(this,this.admissionControllerURI);
		this.addPort(rdsdop);
		this.rdsdop.publishPort();

		rdsdip=new RingDynamicStateDataInboundPort(this);
		this.addPort(rdsdip) ;
		this.rdsdip.publishPort();

	}


	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI#acceptApplication(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI, String requestDispatcherRequestSubmissionInboundPortURI,String requestGeneratorRequestNotificationInboundPort) throws Exception {
		if(Reserved.size()>=3){
			this.logMessage("New Application : "+application+" from ["+requestGeneratorURI+"]");
			/*Creation of the RequestDispatcher*/
			RequestDispatcher rd=new RequestDispatcher(RD_ID,RD_PREFIX+RD_ID,requestDispatcherRequestSubmissionInboundPortURI, RD_AIP_PREFIX+RD_ID, RD_DSDIP_PREFIX+RD_ID, RD_MIP_PREFIX+RD_ID);
			this.logMessage("Controller : RD["+RD_ID+"] created");
			rd.toggleLogging();
			rd.toggleTracing();

			rd.linkRequestGenerator(requestGeneratorRequestNotificationInboundPort);
			VMData temp;
			synchronized(o){
				temp=Reserved.remove(0);
			}
			rd.bindVM(temp.getVMUri(), temp.getVMRequestSubmission(),temp.getVMManagement(),temp.getVMEManagement());

			Controller co= new Controller(CONTROLLER_PREFIX+CO_ID,RD_DSDIP_PREFIX+RD_ID,RD_MIP_PREFIX+RD_ID,RD_AIP_PREFIX+RD_ID,CO_ID,CONTROLLER_MANAGEMENT_PREFIX+CO_ID);
			rd.startUnlimitedPushing(StaticData.DISPATCHER_PUSH_INTERVAL);

			synchronized(o){
				if(!Free.isEmpty()){
					VMData add = Free.remove(0);
					Reserved.add(add);
				}
			}
			this.logMessage("-------------------------------------------");
			this.logMessage("Reserved VM : "+Reserved.size());
			this.logMessage("Free VM : "+Free.size());
			this.logMessage("-------------------------------------------");


			/*Linking Ring*/
			if(nextControllerManagementUri==null && previousControllerManagementUri==null){
				nextControllerManagementUri=CONTROLLER_MANAGEMENT_PREFIX+CO_ID;
				previousControllerManagementUri=CONTROLLER_MANAGEMENT_PREFIX+CO_ID;
				co.setNextControllerUri(this.admissionControllerManagementInboundPort.getPortURI());
				co.setPreviousControllerUri(this.admissionControllerManagementInboundPort.getPortURI());
				co.bindSendingDataUri(this.rdsdip.getPortURI());
				rdsdop.doConnection(co.getControllerRingDataInboundPortUri(),ControlledDataConnector.class.getCanonicalName());
			}else{
				stopPushing();
				/*Add Controller to the ring */

				/*Connecting the Admission Controller to the new Controller*/
				rdsdop.doConnection(co.getControllerRingDataInboundPortUri(),ControlledDataConnector.class.getCanonicalName());
				/*Get the old next controller data inbound port uri*/
				ControllerManagementOutboundPort cmop=new ControllerManagementOutboundPort(this);
				this.addPort(cmop);
				cmop.publishPort();
				cmop.doConnection(this.nextControllerManagementUri, ControllerManagementConnector.class.getCanonicalName());
				/*Connect the new next controller to the old next controller */
				co.bindSendingDataUri(cmop.getControllerRingDataInboundPortUri());

				co.setNextControllerUri(this.nextControllerManagementUri);
				co.setPreviousControllerUri(this.admissionControllerManagementInboundPort.getPortURI());

				this.nextControllerManagementUri=CONTROLLER_MANAGEMENT_PREFIX+CO_ID;
				
				cmop.doDisconnection();
				cmop.unpublishPort();
				cmop.destroyPort();

			}

			co.startUnlimitedPushing(StaticData.RING_PUSH_INTERVAL);
			this.startUnlimitedPushing(StaticData.RING_PUSH_INTERVAL);
			RD_ID++;
			APP_ID++;
			CO_ID++;
			this.logMessage("Application Accepted...");
			return true;
		}
		this.logMessage("Refusing Application...");
		return false;
	}

	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void linkComputer(String computerURI,String computerServicesInboundPortURI,String computerStaticStateDataInboundPortURI,
			String computerDynamicStateDataInboundPortURI) throws Exception {
		this.logMessage(" --/!\\-- Linking Computer "+computerURI+" to :"+this.admissionControllerURI);
		this.logMessage("Ports : "+computerServicesInboundPortURI+" | "+ computerStaticStateDataInboundPortURI+" | "+computerDynamicStateDataInboundPortURI);
		/*Services*/
		ComputerServicesOutboundPort csPort = new ComputerServicesOutboundPort(this) ;
		csPort.publishPort() ;
		csPort.doConnection(
				computerServicesInboundPortURI,
				ComputerServicesConnector.class.getCanonicalName()) ;
		computerServicesPorts.put(COMP_ID,csPort);
		/*DynamicData & Static Data*/
		ComputerStaticStateDataOutboundPort cssPort = new ComputerStaticStateDataOutboundPort(
				COMPUTER_STATIC_DATA_PREFIX+COMP_ID,
				this,
				computerURI) ;
		this.addPort(cssPort) ;
		cssPort.publishPort() ;
		cssPort.doConnection(computerStaticStateDataInboundPortURI, DataConnector.class.getCanonicalName());

		/**/
		ComputerDynamicStateDataOutboundPort cdsPort = new ComputerDynamicStateDataOutboundPort(
				COMPUTER_DYNAMIC_DATA_PREFIX+COMP_ID,
				this,
				computerURI) ;
		this.addPort(cdsPort) ;
		cdsPort.publishPort() ;
		cdsPort.doConnection(computerDynamicStateDataInboundPortURI, ControlledDataConnector.class.getCanonicalName());


		computerDynamicData.put(COMP_ID,cdsPort);
		computerStaticData.put(COMP_ID,cssPort);
		/*Creating Inital VM*/

		ComputerStaticStateI staticState= (ComputerStaticStateI) cssPort.request();
		int nbCores =staticState.getNumberOfCoresPerProcessor();
		int nbProc = staticState.getNumberOfProcessors();
		this.logMessage("Computer : "+nbCores+" Cores & "+nbProc+" Processor");
		this.logMessage("Creating "+(((nbCores*nbProc)/2)/PARAMETER_INITIAL_NB_CORE)+" VM with "+PARAMETER_INITIAL_NB_CORE+" cores");
		for(int i=0;i<(((nbCores*nbProc)/2)/PARAMETER_INITIAL_NB_CORE);i++){
			AllocatedCore[] acs=csPort.allocateCores(PARAMETER_INITIAL_NB_CORE);
			if(acs.length!=0){
				
				VirtualMachineExtended vme=new VirtualMachineExtended(computerURI,computerServicesInboundPortURI,VM_URI_PREFIX+(VM_ID),VM_AVMMIP_PREFIX+VM_ID,VM_VMEMIP_PREFIX+VM_ID,VM_RSIP_PREFIX+VM_ID,VM_RNOP_PREFIX+VM_ID);
				VM_ID++;
				vme.allocateCores(acs);
				synchronized(o){
					if(Reserved.size()<4)
						Reserved.add(vme.getData());
					else
						Free.add(vme.getData());
				}
			}
		}

		this.logMessage("Reserved VM : "+Reserved.size());
		this.logMessage("Free VM : "+Free.size());

		this.logMessage("Computer linked !");
		this.logMessage("------------------------------");
		//VM_ID++;
		COMP_ID++;
	}

	@Override
	public void acceptComputerStaticData(String computerURI, ComputerStaticStateI staticState) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptComputerDynamicData(String computerURI, ComputerDynamicStateI currentDynamicState)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void startUnlimitedPushing(int interval) throws Exception {
		// first, send the static state if the corresponding port is connected
		//this.sendStaticState() ;
		final AdmissionController c = this ;
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
		this.logMessage(this.admissionControllerURI + " startLimitedPushing with interval "
				+ interval + " ms for " + n + " times.") ;

		// first, send the static state if the corresponding port is connected
		//this.sendStaticState() ;

		final AdmissionController c = this ;
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

	@Override
	public void stopPushing() throws Exception {
		if (this.pushingFuture != null &&
				!(this.pushingFuture.isCancelled() ||
						this.pushingFuture.isDone())) {
			this.pushingFuture.cancel(false) ;
		}
	}

	@Override
	public void acceptRingDynamicData(String requestDispatcherURI, RingDynamicStateI currentDynamicState)
			throws Exception {
		synchronized(o){
			//this.logMessage("[----DATA----]"+this.admissionControllerURI+ " RECEIVE " +currentDynamicState.getVMDataList().size()+ " FREE VM");
			
			if(!currentDynamicState.getVMDataList().isEmpty())
				Free.addAll(currentDynamicState.getVMDataList());
			while(Reserved.size() < MAX_VM_RESERVED && !Free.isEmpty()){
				Reserved.add(Free.remove(0));
			}
			//this.logMessage("[----DATA----]"+this.admissionControllerURI+ " FREE["+Free.size()+"] | RESERVED["+Reserved.size()+"]");
		}

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
			final AdmissionController c = this ;
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

	public RingDynamicState getDynamicState() throws UnknownHostException {
		synchronized(o){
			ArrayList<VMData> copy=new ArrayList<>(Free);
			RingDynamicState rds=new RingDynamicState(copy);
			Free.clear();
			return rds;
		}
	}



}
