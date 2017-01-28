package fr.upmc.datacenter.admissioncontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.connectors.DataConnector;
import fr.upmc.components.interfaces.DataRequiredI;
import fr.upmc.datacenter.admissioncontroller.factory.VMFactory;
import fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerI;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI;
import fr.upmc.datacenter.admissioncontroller.ports.ApplicationRequestInboundPort;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.admissioncontroller.ports.AdmissionControllerManagementInboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.dispatcher.RequestDispatcher;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataOutboundPort;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.extension.vm.VirtualMachineExtended;
import fr.upmc.datacenter.hardware.computers.Computer;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
import fr.upmc.datacenter.hardware.computers.connectors.ComputerServicesConnector;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerDynamicStateI;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerServicesI;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerStateDataConsumerI;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerStaticStateI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerDynamicStateDataInboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerDynamicStateDataOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerStaticStateDataOutboundPort;
import fr.upmc.datacenter.hardware.processors.Processor.ProcessorPortTypes;
import fr.upmc.datacenter.hardware.tests.ComputerMonitor;
import fr.upmc.datacenter.interfaces.ControlledDataRequiredI;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM.ApplicationVMPortTypes;
import fr.upmc.datacenter.software.applicationvm.connectors.ApplicationVMIntrospectionConnector;
import fr.upmc.datacenter.software.applicationvm.connectors.ApplicationVMManagementConnector;
import fr.upmc.datacenter.software.applicationvm.interfaces.ApplicationVMManagementI;
import fr.upmc.datacenter.software.applicationvm.ports.ApplicationVMIntrospectionOutboundPort;
import fr.upmc.datacenter.software.applicationvm.ports.ApplicationVMManagementOutboundPort;
import fr.upmc.datacenter.software.connectors.RequestNotificationConnector;
import fr.upmc.datacenter.software.connectors.RequestSubmissionConnector;
import fr.upmc.datacenter.software.interfaces.RequestSubmissionI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestNotificationOutboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
import fr.upmc.datacenterclient.requestgenerator.RequestGenerator;
import fr.upmc.datacenterclient.requestgenerator.interfaces.RequestGeneratorManagementI;
import fr.upmc.datacenterclient.requestgenerator.ports.RequestGeneratorManagementInboundPort;

public class AdmissionController extends AbstractComponent
implements ApplicationRequestI,AdmissionControllerManagementI,ComputerStateDataConsumerI
{
	public static final String COMPUTER_STATIC_DATA_PREFIX = "COMPUTER_STATIC_DATA";
	public static final String COMPUTER_DYNAMIC_DATA_PREFIX = "COMPUTER_DYNAMIC_DATA";

	public static final String CONTROLLER_PREFIX = "CO_";
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
	private final int MAX_VM_RESERVED=4;

	private int VM_ID=1;
	private int APP_ID=1;
	private int RD_ID=1;
	private int CO_ID=1;
	private int COMP_ID=1;

	/*Admission Controller*/
	String admissionControllerURI;
	AdmissionControllerManagementInboundPort admissionControllerManagementInboundPort;
	ApplicationRequestInboundPort applicationRequestInboundPort;

	/*Controllers*/
	String nextController=null;
	String previousController=null;

	/*Request Dispatcher*/
	Map<Integer,String> applicationURI;
	Map<Integer,String> requestDispatcherURI;
	Map<Integer,String> requestDispatcherManagementURIs;

	/*Computers*/
	Map<Integer,String> computerURIs;
	Map<Integer,ComputerServicesOutboundPort> computerServicesPorts;
	Map<Integer, ComputerDynamicStateDataOutboundPort> computerDynamicData;
	Map<Integer, ComputerStaticStateDataOutboundPort> computerStaticData;

	/*VM*/
	List<VMData> Reserved;
	List<VMData> Free;

	/* VM ??*/
	Map<Integer, ApplicationVMManagementOutboundPort> vmManagementOutBountPorts;
	Map<ApplicationVMManagementOutboundPort,String> vmManagementOBPwithVMUris=new HashMap<ApplicationVMManagementOutboundPort,String>();

	public AdmissionController(String admissionControllerURI,String applicationRequestInboundURI) throws Exception{
		/* TODO */

		this.toggleLogging();
		this.toggleTracing();
		this.addOfferedInterface(DataRequiredI.PushI.class) ;
		this.addRequiredInterface(DataRequiredI.PullI.class) ;
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
		Reserved = new ArrayList<>();
		Free= new ArrayList<>();
		vmManagementOutBountPorts=new HashMap<Integer, ApplicationVMManagementOutboundPort>();

		/* Init all ports */

		this.addOfferedInterface(ApplicationRequestI.class) ;
		this.applicationRequestInboundPort = new ApplicationRequestInboundPort(
				applicationRequestInboundURI, this) ;
		this.addPort(this.applicationRequestInboundPort) ;
		this.applicationRequestInboundPort.publishPort() ;

	}

	public AdmissionController(String admissionControllerURI,String applicationRequestInboundURI,String admissionControllerManagementInboundURI) throws Exception{
		/* TODO */
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
		Reserved = new ArrayList<>();
		Free= new ArrayList<>();
		vmManagementOutBountPorts=new HashMap<Integer, ApplicationVMManagementOutboundPort>();

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


	}



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

			VMData temp=Reserved.remove(0);

			rd.bindVM(temp.getVMUri(), temp.getVMRequestSubmission(),temp.getVMManagement(),temp.getVMEManagement());

			Controller co= new Controller(CONTROLLER_PREFIX+CO_ID,RD_DSDIP_PREFIX+RD_ID,RD_MIP_PREFIX+RD_ID,RD_AIP_PREFIX+RD_ID,CO_ID);
			rd.startUnlimitedPushing(5000);
			RD_ID++;
			APP_ID++;
			CO_ID++;

			if(!Free.isEmpty()){
				VMData add = Free.remove(0);
				Reserved.add(add);
			}
			this.logMessage("-------------------------------------------");
			this.logMessage("Reserved VM : "+Reserved.size());
			this.logMessage("Free VM : "+Free.size());
			this.logMessage("-------------------------------------------");
			return true;
		}
		this.logMessage("Refusing Application...");
		return false;
	}

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
		for(int i=0;i<(nbCores*nbProc)/2;i++){
			AllocatedCore[] acs=csPort.allocateCores(PARAMETER_INITIAL_NB_CORE);
			if(acs.length!=0){
				VirtualMachineExtended vme=new VirtualMachineExtended(VM_URI_PREFIX+VM_ID,VM_AVMMIP_PREFIX+VM_ID,VM_VMEMIP_PREFIX+VM_ID,VM_RSIP_PREFIX+VM_ID,VM_RNOP_PREFIX+VM_ID);
				vme.allocateCores(acs);
				if(Reserved.size()<4)
					Reserved.add(vme.getData());
				else
					Free.add(vme.getData());
			}
		}

		this.logMessage("Reserved VM : "+Reserved.size());
		this.logMessage("Free VM : "+Free.size());

		this.logMessage("Computer linked !");
		this.logMessage("------------------------------");
		VM_ID++;
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





}
