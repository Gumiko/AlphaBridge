package fr.upmc.tests.multijvm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.ComponentI.ComponentTask;
import fr.upmc.components.connectors.DataConnector;
import fr.upmc.components.cvm.AbstractDistributedCVM;
import fr.upmc.components.examples.basic_cs.components.URIConsumer;
import fr.upmc.components.examples.basic_cs.components.URIProvider;
import fr.upmc.components.examples.smoothing.SmoothingDistributedCVM;
import fr.upmc.components.examples.smoothing.filter.Filter;
import fr.upmc.components.examples.smoothing.gauge.Gauge;
import fr.upmc.components.examples.smoothing.sensor.SensorSimulator;
import fr.upmc.components.ports.PortI;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.connectors.ApplicationRequestConnector;
import fr.upmc.datacenter.admissioncontroller.connectors.AdmissionControllerManagementConnector;
import fr.upmc.datacenter.admissioncontroller.ports.ApplicationRequestOutboundPort;
import fr.upmc.datacenter.admissioncontroller.ports.AdmissionControllerManagementOutboundPort;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.hardware.computers.Computer;
import fr.upmc.datacenter.hardware.computers.connectors.ComputerServicesConnector;
import fr.upmc.datacenter.hardware.computers.ports.ComputerDynamicStateDataOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerStaticStateDataOutboundPort;
import fr.upmc.datacenter.hardware.tests.ComputerMonitor;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestNotificationOutboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
import fr.upmc.datacenterclient.requestgenerator.RequestGenerator;
import fr.upmc.datacenterclient.requestgenerator.connectors.RequestGeneratorManagementConnector;
import fr.upmc.datacenterclient.requestgenerator.ports.RequestGeneratorManagementOutboundPort;

public class TestDistributedCVM 
extends		AbstractDistributedCVM
{
	/* JVM URI */
	public final static String CONTROLLER_JVMURI = "controller";
	public final static String COMPUTER_JVMURI = "computer";

	/* RG 1 */
	public static final String	RequestSubmissionInboundPortURI = "rsibp" ;
	public static final String	RequestSubmissionOutboundPortURI = "rsobp" ;

	public static final String	RequestNotificationInboundPortURI = "rnibp" ;
	public static final String	RequestNotificationOutboundPortURI = "rnobp" ;

	public static final String	RequestGeneratorManagementInboundPortURI = "rgmip" ;
	public static final String	RequestGeneratorManagementOutboundPortURI = "rgmop" ;
	
	/*RG 2*/
	
	public static final String	RequestSubmissionInboundPortURI2 = "rsibp2" ;
	public static final String	RequestSubmissionOutboundPortURI2 = "rsobp2" ;
	
	public static final String	RequestNotificationInboundPortURI2 = "rnibp2" ;
	public static final String	RequestNotificationOutboundPortURI2 = "rnobp2" ;

	public static final String	RequestGeneratorManagementInboundPortURI2 = "rgmip2" ;
	public static final String	RequestGeneratorManagementOutboundPortURI2 = "rgmop2" ;

	/*Computer*/
	public static final String	ComputerServicesInboundPortURI = "cs-ibp" ;
	public static final String	ComputerServicesOutboundPortURI = "cs-obp" ;
	public static final String	ComputerStaticStateDataInboundPortURI = "css-dip" ;
	public static final String	ComputerStaticStateDataOutboundPortURI = "css-dop" ;
	public static final String	ComputerDynamicStateDataInboundPortURI = "cds-dip" ;
	public static final String	ComputerDynamicStateDataOutboundPortURI = "cds-dop" ;
	
	/*Controller*/
	private static final String ApplicationRequestOutboundPortURI = "ar-op";
	private static final String ApplicationRequestInboundPortURI = "ar-ip";
	private static final String ControllerManagementOutboundPortURI = "cm-op";
	private static final String ControllerManagementInboundPortURI = "cm-ip";

	protected AdmissionController controller;
	protected Computer computer;
	protected ComputerMonitor computermonitor;
	protected RequestGenerator rg1;
	protected RequestGenerator rg2;

	/** Port connected to the computer component to access its services.	*/
	protected ComputerServicesOutboundPort				csPort ;
	/** Port connected to the computer component to receive the static
	 *  state data.															*/
	protected ComputerStaticStateDataOutboundPort		cssPort ;
	/** Port connected to the computer component to receive the dynamic
	 *  state data.															*/
	protected ComputerDynamicStateDataOutboundPort		cdsPort ;

	/** Port of the request generator component sending requests to the
	 *  AVM component.														*/
	protected RequestSubmissionOutboundPort				rsobp ;
	/** Port of the request generator component used to receive end of
	 *  execution notifications from the AVM component.						*/
	protected RequestNotificationOutboundPort			nobp ;
	/** Port connected to the request generator component to manage its
	 *  execution (starting and stopping the request generation).			*/
	protected RequestGeneratorManagementOutboundPort	rgmop ;
	protected RequestGeneratorManagementOutboundPort	rgmop2 ;

	protected RequestSubmissionOutboundPort rg_rsop;
	protected RequestNotificationInboundPort rg_rnip;

	protected RequestSubmissionOutboundPort rg_rsop2;
	protected RequestNotificationInboundPort rg_rnip2;

	protected ApplicationRequestOutboundPort arop;
	protected AdmissionControllerManagementOutboundPort cmop;

	public	TestDistributedCVM(String[] args)
			throws Exception
	{
		super(args);
	}

	/**
	 * @see fr.upmc.components.cvm.AbstractDistributedCVM#instantiateAndPublish()
	 */
	@Override
	public void			instantiateAndPublish()
			throws Exception
	{
		/*
		 * 
		 * 
		 */
		if(thisJVMURI.equals(CONTROLLER_JVMURI)){
			/*Controller*/
			controller= new AdmissionController("controller1", ApplicationRequestInboundPortURI,ControllerManagementInboundPortURI);
			this.deployedComponents.add(controller);

			this.rgmop = new RequestGeneratorManagementOutboundPort(
					RequestGeneratorManagementOutboundPortURI,
					new AbstractComponent() {}) ;
			this.rgmop.publishPort() ;
			
			this.rgmop2 = new RequestGeneratorManagementOutboundPort(
					RequestGeneratorManagementOutboundPortURI2,
					new AbstractComponent() {}) ;
			this.rgmop2.publishPort() ;
			
			this.arop = new ApplicationRequestOutboundPort(
					ApplicationRequestOutboundPortURI,
					controller) ;
			this.arop.publishPort() ;
			
			this.cmop = new AdmissionControllerManagementOutboundPort(
					ControllerManagementOutboundPortURI,
					controller) ;
			this.cmop.publishPort() ;
			/* RG 1 */
			
			rg1 =
					new RequestGenerator(
							"rg1",			// generator component URI
							500.0,			// mean time between two requests
							6000000000L,	// mean number of instructions in requests
							RequestGeneratorManagementInboundPortURI,
							RequestSubmissionOutboundPortURI,
							RequestNotificationInboundPortURI) ;
			this.deployedComponents.add(rg1) ;
			
			
			rg_rsop=(RequestSubmissionOutboundPort) rg1.findPortFromURI(RequestSubmissionOutboundPortURI);
			rg_rnip=(RequestNotificationInboundPort) rg1.findPortFromURI(RequestNotificationInboundPortURI);

//			this.rg_rsop=new RequestSubmissionOutboundPort(rg1);
//			this.rg_rnip=new RequestNotificationInboundPort(rg1);
//			
//			rg1.addPort(rg_rsop);
//			rg1.addPort(rg_rnip);
//			
//			this.rg_rsop.publishPort();
//			this.rg_rnip.publishPort();
			
		
			/* RG 2 */
			rg2 =
					new RequestGenerator(
							"rg2",			// generator component URI
							500.0,			// mean time between two requests
							6000000000L,	// mean number of instructions in requests
							RequestGeneratorManagementInboundPortURI2,
							RequestSubmissionOutboundPortURI2,
							RequestNotificationInboundPortURI2) ;
			this.deployedComponents.add(rg2) ;
			
			
			rg_rsop2=(RequestSubmissionOutboundPort) rg2.findPortFromURI(RequestSubmissionOutboundPortURI2);
			rg_rnip2=(RequestNotificationInboundPort) rg2.findPortFromURI(RequestNotificationInboundPortURI2);

//			this.rg_rsop2=new RequestSubmissionOutboundPort(rg2);
//			this.rg_rnip2=new RequestNotificationInboundPort(rg2);
//			
//			rg2.addPort(rg_rsop2);
//			rg2.addPort(rg_rnip2);
//						
//			this.rg_rsop2.publishPort();
//			this.rg_rnip2.publishPort();

			rg1.toggleLogging();
			rg1.toggleTracing();
			rg2.toggleLogging();
			rg2.toggleTracing();
			controller.toggleLogging();
			controller.toggleTracing();
			
		}else if(thisJVMURI.equals(COMPUTER_JVMURI)){
			String computerURI = "computer0" ;
			int numberOfProcessors = 2 ;
			int numberOfCores = 8 ;
			Set<Integer> admissibleFrequencies = new HashSet<Integer>() ;
			admissibleFrequencies.add(1500) ;	// Cores can run at 1,5 GHz
			admissibleFrequencies.add(3000) ;	// and at 3 GHz
			Map<Integer,Integer> processingPower = new HashMap<Integer,Integer>() ;
			processingPower.put(1500, 1500000) ;	// 1,5 GHz executes 1,5 Mips
			processingPower.put(3000, 3000000) ;	// 3 GHz executes 3 Mips
			computer = new Computer(
					computerURI,
					admissibleFrequencies,
					processingPower,  
					1500,		// Test scenario 1, frequency = 1,5 GHz
					// 3000,	// Test scenario 2, frequency = 3 GHz
					1500,		// max frequency gap within a processor
					numberOfProcessors,
					numberOfCores,
					ComputerServicesInboundPortURI,
					ComputerStaticStateDataInboundPortURI,
					ComputerDynamicStateDataInboundPortURI) ;
			this.deployedComponents.add(computer);

			computermonitor =
					new ComputerMonitor(computerURI,
							true,
							ComputerStaticStateDataOutboundPortURI,
							ComputerDynamicStateDataOutboundPortURI) ;
			this.deployedComponents.add(computermonitor);
			
			this.cssPort =
					(ComputerStaticStateDataOutboundPort)
					computermonitor.findPortFromURI(ComputerStaticStateDataOutboundPortURI) ;
			
	

			this.cdsPort =
					(ComputerDynamicStateDataOutboundPort)
					computermonitor.findPortFromURI(ComputerDynamicStateDataOutboundPortURI) ;
			
			
		}else {
			System.out.println("Error, wrong JVM URI: " + thisJVMURI) ;
			System.exit(1) ;
		}
		super.instantiateAndPublish();
	}

	/**
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.upmc.components.cvm.AbstractDistributedCVM#interconnect()
	 */
	@Override
	public void			interconnect() throws Exception
	{
		assert	this.instantiationAndPublicationDone ;

		if (thisJVMURI.equals(CONTROLLER_JVMURI)) {
			
			/*Controller Application Submit Port */
			/*Application submit port */
			
			this.arop.doConnection(
					ApplicationRequestInboundPortURI,
					ApplicationRequestConnector.class.getCanonicalName()) ;

			/*Management Port to submit computer*/
		
			this.cmop.doConnection(
					ControllerManagementInboundPortURI,
					AdmissionControllerManagementConnector.class.getCanonicalName()) ;
			
			/*Request Generator 1*/
			
			this.rgmop.doConnection(
					RequestGeneratorManagementInboundPortURI,
					RequestGeneratorManagementConnector.class.getCanonicalName()) ;

			/*Request Generator 2*/
			
			this.rgmop2.doConnection(
					RequestGeneratorManagementInboundPortURI2,
					RequestGeneratorManagementConnector.class.getCanonicalName()) ;

		} else if (thisJVMURI.equals(COMPUTER_JVMURI)) {

			this.cssPort.doConnection(
					ComputerStaticStateDataInboundPortURI,
					DataConnector.class.getCanonicalName()) ;
			
			this.cdsPort.
			doConnection(
					ComputerDynamicStateDataInboundPortURI,
					ControlledDataConnector.class.getCanonicalName()) ;
			
			/* Link The controller and Computer */
			
		} else {
			System.out.println("Error, wrong JVM URI: " + thisJVMURI) ;
			System.exit(1) ;
		}

		super.interconnect();
	}

	/**
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.upmc.components.cvm.AbstractDistributedCVM#start()
	 */
	@Override
	public void			start() throws Exception
	{
		super.start() ;

		if (thisJVMURI.equals(CONTROLLER_JVMURI)) {
			cmop.linkComputer(ComputerServicesOutboundPortURI,ComputerServicesInboundPortURI);
			
		} else if (thisJVMURI.equals(COMPUTER_JVMURI)) {
			
		} else {
			System.out.println("Error, wrong JVM URI: " + thisJVMURI) ;
			System.exit(1) ;
		}
	}

	/**
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.upmc.components.cvm.AbstractDistributedCVM#shutdown()
	 */
	@Override
	public void			shutdown() throws Exception
	{
		if (thisJVMURI.equals(CONTROLLER_JVMURI)) {
			
		} else if (thisJVMURI.equals(COMPUTER_JVMURI)) {
			
		} else {
			System.out.println("Error, wrong JVM URI: " + thisJVMURI) ;
			System.exit(1) ;
		}

		super.shutdown();
	}

	/**
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.upmc.components.cvm.AbstractDistributedCVM#shutdownNow()
	 */
	@Override
	public void			shutdownNow() throws Exception
	{
		if (thisJVMURI.equals(CONTROLLER_JVMURI)) {

		} else if (thisJVMURI.equals(COMPUTER_JVMURI)) {

		} else {
			System.out.println("Error, wrong JVM URI: " + thisJVMURI) ;
			System.exit(1) ;
		}

		super.shutdownNow();
	}

	public void	testScenario() throws Exception
	{

		if (thisJVMURI.equals(CONTROLLER_JVMURI)) {
			Thread.sleep(2000L) ;
			if(arop.acceptApplication(1, "rg1",rg_rsop,rg_rnip)){
				Thread.sleep(3000L) ;
				rgmop.startGeneration();
				Thread.sleep(5000L);
			}
			if(arop.acceptApplication(2, "rg2",rg_rsop2,rg_rnip2)){
				Thread.sleep(3000L) ;
				rgmop2.startGeneration();
			}
			Thread.sleep(20000L);
			rgmop.stopGeneration();
			rgmop2.stopGeneration();
		} else if (thisJVMURI.equals(COMPUTER_JVMURI)) {

		} else {
			System.out.println("Error, wrong JVM URI: " + thisJVMURI) ;
			System.exit(1) ;
		}

	}

	public static void	main(String[] args)
	{
		System.out.println("Beginning") ;
		try {
			final TestDistributedCVM da = new TestDistributedCVM(args) ;
			da.deploy() ;
			System.out.println("starting...") ;
			da.start() ;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						da.testScenario() ;
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			}).start() ;
			Thread.sleep(90000L) ;
			System.out.println("shutting down...") ;
			da.shutdown() ;
			System.out.println("ending...") ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
		System.out.println("Main thread ending") ;
		System.exit(0);
	}
}