package fr.upmc.datacenter.admissioncontroller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI;
/**
 * The class <code>ControllerManagementOutboundPort</code>implements the
 * outbound port through which the component management methods are called.
 * <p><strong>Description</strong></p>
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */

public class AdmissionControllerManagementOutboundPort extends AbstractOutboundPort implements AdmissionControllerManagementI{
	

	public	AdmissionControllerManagementOutboundPort(ComponentI owner) throws Exception
	{
		super(AdmissionControllerManagementI.class, owner) ;
		assert	owner != null && owner instanceof AdmissionController ;
	}


	public	AdmissionControllerManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, AdmissionControllerManagementI.class, owner);

		assert	owner != null && owner instanceof AdmissionController ;
	}
	
	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(String,String,String,String)
	 */
	@Override
	public void linkComputer(String computerURI,String ComputerServicesInboundPortURI,String ComputerStaticStateDataInboundPortURI,
			String ComputerDynamicStateDataInboundPortURI) throws Exception {
		((AdmissionControllerManagementI)this.connector).linkComputer(computerURI,ComputerServicesInboundPortURI, ComputerStaticStateDataInboundPortURI,
				ComputerDynamicStateDataInboundPortURI);
	}

}
