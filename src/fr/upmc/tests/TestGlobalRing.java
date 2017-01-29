package fr.upmc.tests;

import java.util.ArrayList;
import java.util.List;
import fr.upmc.components.AbstractComponent;
import fr.upmc.components.cvm.AbstractCVM;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.connectors.ApplicationRequestConnector;
import fr.upmc.datacenter.admissioncontroller.connectors.AdmissionControllerManagementConnector;
import fr.upmc.datacenter.admissioncontroller.ports.ApplicationRequestOutboundPort;
import fr.upmc.datacenter.admissioncontroller.ports.AdmissionControllerManagementOutboundPort;
import fr.upmc.datacenter.hardware.computers.Computer;
import fr.upmc.datacenter.hardware.computers.ports.ComputerDynamicStateDataOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
import fr.upmc.datacenter.hardware.computers.ports.ComputerStaticStateDataOutboundPort;
import fr.upmc.datacenter.software.connectors.RequestSubmissionConnector;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestNotificationOutboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
import fr.upmc.datacenterclient.requestgenerator.RequestGenerator;
import fr.upmc.datacenterclient.requestgenerator.connectors.RequestGeneratorManagementConnector;
import fr.upmc.datacenterclient.requestgenerator.ports.RequestGeneratorManagementOutboundPort;

public class TestGlobalRing extends AbstractCVM{
	public static final String	RequestSubmissionInboundPortURI = "rsibp" ;
	public static final String	RequestSubmissionOutboundPortURI = "rsobp" ;

	public static final String	RequestNotificationInboundPortURI = "rnibp" ;
	public static final String	RequestNotificationOutboundPortURI = "rnobp" ;

	public static final String	RequestGeneratorManagementInboundPortURI = "rgmip" ;
	public static final String	RequestGeneratorManagementOutboundPortURI = "rgmop" ;

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

	int lastComputer=1;
	int lastRG=0;

	int nbGenerator=8;
	int nbComputer=15;

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
	protected List<RequestGeneratorManagementOutboundPort>	rgmop =new ArrayList<RequestGeneratorManagementOutboundPort>();;
	protected List<RequestSubmissionOutboundPort> rg_rsop=new ArrayList<RequestSubmissionOutboundPort>();
	protected List<RequestNotificationInboundPort> rg_rnip=new ArrayList<RequestNotificationInboundPort>();


	protected ApplicationRequestOutboundPort arop;
	protected AdmissionControllerManagementOutboundPort cmop;


	public TestGlobalRing() throws Exception {
		super();
	}

	@Override
	public void	deploy() throws Exception
	{
		// --------------------------------------------------------------------
		// Create and deploy a computer component with its 2 processors and
		// each with 8 cores.
		// --------------------------------------------------------------------

		/* Create The Controller */
		AdmissionController admissionController= new AdmissionController("admissioncontroller1", ApplicationRequestInboundPortURI,ControllerManagementInboundPortURI);

		/*Management Port to submit computer*/
		this.cmop = new AdmissionControllerManagementOutboundPort(
				ControllerManagementOutboundPortURI,
				new AbstractComponent() {}) ;
		this.cmop.publishPort() ;
		this.cmop.doConnection(
				ControllerManagementInboundPortURI,
				AdmissionControllerManagementConnector.class.getCanonicalName()) ;

		for(int i=0;i<nbComputer;i++){
			Computer c = FactoryComputer.createComputer("computerURI"+lastComputer, 12, 8, ComputerServicesInboundPortURI+lastComputer, ComputerStaticStateDataInboundPortURI+lastComputer, ComputerDynamicStateDataInboundPortURI+lastComputer);
			this.addDeployedComponent(c) ;
			/* Link The controller and Computer */
			cmop.linkComputer("computerURI"+lastComputer,ComputerServicesInboundPortURI+lastComputer,ComputerStaticStateDataInboundPortURI+lastComputer,ComputerDynamicStateDataInboundPortURI+lastComputer);
			lastComputer++;
		}
		/*Application submit port */
		this.arop = new ApplicationRequestOutboundPort(
				ApplicationRequestOutboundPortURI,
				new AbstractComponent() {}) ;
		this.arop.publishPort() ;
		this.arop.doConnection(
				ApplicationRequestInboundPortURI,
				ApplicationRequestConnector.class.getCanonicalName()) ;

		/* Create Request Generators */
		/* RG 1 */

		for(int i=0;i<nbGenerator;i++){
			RequestGenerator rg1 =
					new RequestGenerator(
							"rg"+i,			// generator component URI
							900.0,			// mean time between two requests
							2500000000L,	// mean number of instructions in requests
							RequestGeneratorManagementInboundPortURI+i,
							RequestSubmissionOutboundPortURI+i,
							RequestNotificationInboundPortURI+i) ;
			this.addDeployedComponent(rg1) ;

			
			this.rgmop.add(new RequestGeneratorManagementOutboundPort(RequestGeneratorManagementOutboundPortURI+i,new AbstractComponent(){}));
			this.rgmop.get(i).publishPort() ;
			this.rgmop.get(i).doConnection(
					RequestGeneratorManagementInboundPortURI+i,
					RequestGeneratorManagementConnector.class.getCanonicalName()) ;

			rg_rsop.add((RequestSubmissionOutboundPort) rg1.findPortFromURI(RequestSubmissionOutboundPortURI+i));
		}

		super.deploy();
	}

	public void	start() throws Exception
	{
		super.start() ;
	}

	public void	shutdown() throws Exception
	{
		// disconnect all ports explicitly connected in the deploy phase.
		//		this.csPort.doDisconnection() ;
		//		this.avmPort.doDisconnection() ;
		//		this.rsobp.doDisconnection() ;
		//		this.nobp.doDisconnection() ;
		//		this.rgmop.doDisconnection() ;

		super.shutdown() ;
	}



	public void			testScenario() throws Exception
	{
		/* TODO */
		/* Send New Application to the Controller*/

		Thread.sleep(2000L) ;
		for(int i =0;i<nbGenerator;i++){
			if(arop.acceptApplication(i, "rg"+i,"a"+i,RequestNotificationInboundPortURI+i)){
				rg_rsop.get(i).doConnection("a"+i, RequestSubmissionConnector.class.getCanonicalName());
				rgmop.get(i).startGeneration();
				Thread.sleep(2000L);
			}
		}
		Thread.sleep(900000L);
		for(int i=0;i<nbGenerator;i++){
			rgmop.get(i).stopGeneration();
		}

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
			final TestGlobalRing tc = new TestGlobalRing() ;
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
			Thread.sleep(1000000L) ;
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