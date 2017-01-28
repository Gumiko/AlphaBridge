package fr.upmc.datacenter.admissioncontroller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI;
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
public class AdmissionControllerManagementInboundPort
	extends		AbstractInboundPort implements AdmissionControllerManagementI
	{
		private static final long serialVersionUID = 1L;

		public		AdmissionControllerManagementInboundPort(
				ComponentI owner
				) throws Exception
		{
			super(AdmissionControllerManagementI.class, owner) ;

			assert	owner != null && owner instanceof AdmissionController ;
		}

		public				AdmissionControllerManagementInboundPort(
				String uri,
				ComponentI owner
				) throws Exception
		{
			super(uri, AdmissionControllerManagementI.class, owner);

			assert	owner != null && owner instanceof AdmissionController ;
		}
		
		/**
		 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(String csopURI)
		 */
		@Override
		public void linkComputer(String computerURI,String ComputerServicesInboundPortURI,String ComputerStaticStateDataInboundPortURI,
				String ComputerDynamicStateDataInboundPortURI) throws Exception {
			final AdmissionController c = (AdmissionController) this.owner;
			c.linkComputer(computerURI,ComputerServicesInboundPortURI, ComputerStaticStateDataInboundPortURI,
					 ComputerDynamicStateDataInboundPortURI);
		}
}
