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
public class ControllerManagementOutboundPort extends AbstractOutboundPort implements AdmissionControllerManagementI{
	public	ControllerManagementOutboundPort(ComponentI owner) throws Exception
	{
		super(AdmissionControllerManagementI.class, owner) ;
		assert	owner != null && owner instanceof AdmissionController ;
	}

	public	ControllerManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, AdmissionControllerManagementI.class, owner);

		assert	owner != null && owner instanceof AdmissionController ;
	}
	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(ComputerServicesOutboundPort c_out) 
	 */
	@Override
	public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception {
		((AdmissionControllerManagementI)this.connector).linkComputer(c_out);
	}
	
	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(String csopUri)
	 */
	@Override
	public void linkComputer(String csopUri,String csipUri) throws Exception {
		((AdmissionControllerManagementI)this.connector).linkComputer(csopUri,csipUri);
	}

}
