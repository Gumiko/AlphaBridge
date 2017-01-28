package fr.upmc.datacenter.admissioncontroller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
/**
 * The class <code>ControllerManagementOutboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
@SuppressWarnings("unused")
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
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(String csopUri)
	 */
	@Override
	public void linkComputer(String computerURI,String ComputerServicesInboundPortURI,String ComputerStaticStateDataInboundPortURI,
			String ComputerDynamicStateDataInboundPortURI) throws Exception {
		((AdmissionControllerManagementI)this.connector).linkComputer(computerURI,ComputerServicesInboundPortURI, ComputerStaticStateDataInboundPortURI,
				ComputerDynamicStateDataInboundPortURI);
	}

}
