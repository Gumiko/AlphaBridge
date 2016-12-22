package fr.upmc.datacenter.admissioncontroller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.ControllerManagementI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
/**
 * The class <code>ControllerManagementInboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class ControllerManagementInboundPort
	extends		AbstractInboundPort implements ControllerManagementI
	{
		private static final long serialVersionUID = 1L;

		public		ControllerManagementInboundPort(
				ComponentI owner
				) throws Exception
		{
			super(ControllerManagementI.class, owner) ;

			assert	owner != null && owner instanceof AdmissionController ;
		}

		public				ControllerManagementInboundPort(
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
			final AdmissionController c = (AdmissionController) this.owner;
			c.linkComputer(c_out);
		}
		
		/**
		 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ControllerManagementI#linkComputer(String csopURI)
		 */
		@Override
		public void linkComputer(String csopURI,String csipURI) throws Exception {
			final AdmissionController c = (AdmissionController) this.owner;
			c.linkComputer(csopURI,csipURI);
		}
}
