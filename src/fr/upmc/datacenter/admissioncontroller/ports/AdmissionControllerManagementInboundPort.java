package fr.upmc.datacenter.admissioncontroller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI;
/**
* The class <code>AdmissionControllerManagementInboundPort</code>implements the
 * inbound port requiring the interface <code>AdmissionControllerManagementI</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	C�dric Ribeiro et Mokrane Kadri
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
		 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(String,String,String,String)
		 */
		@Override
		public void linkComputer(String computerURI,String ComputerServicesInboundPortURI,String ComputerStaticStateDataInboundPortURI,
				String ComputerDynamicStateDataInboundPortURI) throws Exception {
			final AdmissionController c = (AdmissionController) this.owner;
			c.linkComputer(computerURI,ComputerServicesInboundPortURI, ComputerStaticStateDataInboundPortURI,
					 ComputerDynamicStateDataInboundPortURI);
		}
}
