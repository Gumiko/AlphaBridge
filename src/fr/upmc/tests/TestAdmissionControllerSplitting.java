package fr.upmc.tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.upmc.components.AbstractComponent;
import fr.upmc.components.connectors.DataConnector;
import fr.upmc.components.cvm.AbstractCVM;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.connectors.ApplicationRequestConnector;
import fr.upmc.datacenter.admissioncontroller.connectors.AdmissionControllerManagementConnector;
import fr.upmc.datacenter.admissioncontroller.ports.ApplicationRequestInboundPort;
import fr.upmc.datacenter.admissioncontroller.ports.ApplicationRequestOutboundPort;
import fr.upmc.datacenter.admissioncontroller.ports.AdmissionControllerManagementOutboundPort;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.hardware.computers.Computer;
import fr.upmc.datacenter.hardware.computers.connectors.ComputerServicesConnector;
import fr.upmc.datacenter.hardware.computers.ports.ComputerDynamicStateDataOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerStaticStateDataOutboundPort;
import fr.upmc.datacenter.hardware.tests.ComputerMonitor;
import fr.upmc.datacenter.software.applicationvm.ports.ApplicationVMManagementOutboundPort;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestNotificationOutboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
import fr.upmc.datacenterclient.requestgenerator.RequestGenerator;
import fr.upmc.datacenterclient.requestgenerator.connectors.RequestGeneratorManagementConnector;
import fr.upmc.datacenterclient.requestgenerator.ports.RequestGeneratorManagementOutboundPort;

public class TestAdmissionControllerSplitting extends AbstractCVM{
	public static final String	RequestSubmissionInboundPortURI = "rsibp" ;
	public static final String	RequestSubmissionOutboundPortURI = "rsobp" ;

	public static final String	RequestSubmissionInboundPortURI2 = "rsibp2" ;
	public static final String	RequestSubmissionOutboundPortURI2 = "rsobp2" ;

	public static final String	RequestNotificationInboundPortURI = "rnibp" ;
	public static final String	RequestNotificationOutboundPortURI = "rnobp" ;

	public static final String	RequestNotificationInboundPortURI2 = "rnibp2" ;
	public static final String	RequestNotificationOutboundPortURI2 = "rnobp2" ;

	public static final String	RequestGeneratorManagementInboundPortURI = "rgmip" ;
	public static final String	RequestGeneratorManagementOutboundPortURI = "rgmop" ;

	public static final String	RequestGeneratorManagementInboundPortURI2 = "rgmip2" ;
	public static final String	RequestGeneratorManagementOutboundPortURI2 = "rgmop2" ;

	public static final String	ComputerServicesInboundPortURI = "cs-ibp" ;
	public static final String	ComputerServicesOutboundPortURI = "cs-obp" ;
	public static final String	ComputerStaticStateDataInboundPortURI = "css-dip" ;
	public static final String	ComputerStaticStateDataOutboundPortURI = "css-dop" ;
	public static final String	ComputerDynamicStateDataInboundPortURI = "cds-dip" ;
	public static final String	ComputerDynamicStateDataOutboundPortURI = "cds-dop" ;
	private static final String ApplicationRequestOutboundPortURI = "ar-op";
	private static final String ApplicationRequestInboundPortURI = "ar-ip";

	private static final String ControllerManagementOutboundPortURI = "cm-op";
	private static final String ControllerManagementInboundPortURI = "cm-ip";

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


	public TestAdmissionControllerSplitting() throws Exception {
		super();
	}

	@Override
	public void	deploy() throws Exception
	{
		/*TODO*/
		/* Create Processor / Computer */

		// --------------------------------------------------------------------
		// Create and deploy a computer component with its 2 processors and
		// each with 8 cores.
		// --------------------------------------------------------------------
		String computerURI = "computer1" ;
		int numberOfProcessors = 2 ;
		int numberOfCores = 8 ;
		Set<Integer> admissibleFrequencies = new HashSet<Integer>() ;
		admissibleFrequencies.add(1500) ;	// Cores can run at 1,5 GHz
		admissibleFrequencies.add(3000) ;	// and at 3 GHz
		Map<Integer,Integer> processingPower = new HashMap<Integer,Integer>() ;
		processingPower.put(1500, 1500000) ;	// 1,5 GHz executes 1,5 Mips
		processingPower.put(3000, 3000000) ;	// 3 GHz executes 3 Mips
		Computer c = new Computer(
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
		this.addDeployedComponent(c) ;

		// Create a mock-up computer services port to later allocate its cores
		// to the application virtual machine.
		//		this.csPort = new ComputerServicesOutboundPort(
		//				ComputerServicesOutboundPortURI,
		//				new AbstractComponent() {}) ;
		//		this.csPort.publishPort() ;
		//		this.csPort.doConnection(
		//				ComputerServicesInboundPortURI,
		//				ComputerServicesConnector.class.getCanonicalName()) ;
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		// Create the computer monitor component and connect its to ports
		// with the computer component.
		// --------------------------------------------------------------------
//				ComputerMonitor cm =
//						new ComputerMonitor(computerURI,
//								true,
//								ComputerStaticStateDataOutboundPortURI,
//								ComputerDynamicStateDataOutboundPortURI) ;
//				this.addDeployedComponent(cm) ;
//				this.cssPort =
//						(ComputerStaticStateDataOutboundPort)
//						cm.findPortFromURI(ComputerStaticStateDataOutboundPortURI) ;
//				this.cssPort.doConnection(
//						ComputerStaticStateDataInboundPortURI,
//						DataConnector.class.getCanonicalName()) ;
//		
//				this.cdsPort =
//						(ComputerDynamicStateDataOutboundPort)
//						cm.findPortFromURI(ComputerDynamicStateDataOutboundPortURI) ;
//				this.cdsPort.
//				doConnection(
//						ComputerDynamicStateDataInboundPortURI,
//						ControlledDataConnector.class.getCanonicalName()) ;

		/* Create The Controller */
		AdmissionController admissionController= new AdmissionController("controller1", ApplicationRequestInboundPortURI,ControllerManagementInboundPortURI);

		/*Application submit port */
//		this.arop = new ApplicationRequestOutboundPort(
//				ApplicationRequestOutboundPortURI,
//				new AbstractComponent() {}) ;
//		this.arop.publishPort() ;
//		this.arop.doConnection(
//				ApplicationRequestInboundPortURI,
//				ApplicationRequestConnector.class.getCanonicalName()) ;

		/*Management Port to submit computer*/
		this.cmop = new AdmissionControllerManagementOutboundPort(
				ControllerManagementOutboundPortURI,
				new AbstractComponent() {}) ;
		this.cmop.publishPort() ;
		this.cmop.doConnection(
				ControllerManagementInboundPortURI,
				AdmissionControllerManagementConnector.class.getCanonicalName()) ;

		/* Link The controller and Computer */
		cmop.linkComputer(computerURI,ComputerServicesInboundPortURI,ComputerStaticStateDataInboundPortURI,ComputerDynamicStateDataInboundPortURI);
		super.deploy();
	}

	public void	start() throws Exception
	{
		super.start() ;
	}

	public void	shutdown() throws Exception
	{
		super.shutdown() ;
	}



	public void			testScenario() throws Exception
	{

	}

	/**
	 * execute the test application.
	 * 
	 * @param args	command line arguments, disregarded here.
	 */
	public static void	main(String[] args)
	{
		// Uncomment next line to execute components in debug mode.
		// AbstractCVM.toggleDebugMode() ;
		try {
			final TestAdmissionControllerSplitting tc = new TestAdmissionControllerSplitting() ;
			// Deploy the components
			tc.deploy() ;
			System.out.println("starting.......") ;
			// Start them.
			tc.start() ;
			// Execute the chosen request generation test scenario in a
			// separate thread.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						tc.testScenario() ;
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			}).start() ;
			// Sleep to let the test scenario execute to completion.
			Thread.sleep(90000L) ;
			// Shut down the application.
			System.out.println("shutting down...") ;
			tc.shutdown() ;
			System.out.println("ending...") ;
			// Exit from Java.
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}