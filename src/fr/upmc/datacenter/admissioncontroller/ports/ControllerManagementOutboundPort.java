package fr.upmc.datacenter.admissioncontroller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.admissioncontroller.interfaces.ControllerManagementI;
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
public class ControllerManagementOutboundPort extends AbstractOutboundPort implements ControllerManagementI{
	public	ControllerManagementOutboundPort(ComponentI owner) throws Exception
	{
		super(ControllerManagementI.class, owner) ;
		assert	owner != null && owner instanceof AdmissionController ;
	}

	public	ControllerManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerManagementI.class, owner);

		assert	owner != null && owner instanceof AdmissionController ;
	}
	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ControllerManagementI#linkComputer(ComputerServicesOutboundPort c_out) 
	 */
	@Override
	public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception {
		((ControllerManagementI)this.connector).linkComputer(c_out);
	}
	
	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ControllerManagementI#linkComputer(String csopUri)
	 */
	@Override
	public void linkComputer(String csopUri,String csipUri) throws Exception {
		((ControllerManagementI)this.connector).linkComputer(csopUri,csipUri);
	}

}
