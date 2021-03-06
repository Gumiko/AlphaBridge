package fr.upmc.datacenter.admissioncontroller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI;
/**
 * The class <code>ControllerManagementConnector</code>implements a
 * connector for ports exchanging through the interface
 * <code>AdmissionControllerManagementI</code>.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	C�dric Ribeiro et Mokrane Kadri
 */
public class AdmissionControllerManagementConnector
	extends		AbstractConnector
	implements	AdmissionControllerManagementI
	{
		/**
		 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(String,String,String,String)
		 */
		@Override
		public void linkComputer(String computerURI,String ComputerServicesInboundPortURI,String ComputerStaticStateDataInboundPortURI,
				String ComputerDynamicStateDataInboundPortURI) throws Exception {
			((AdmissionControllerManagementI)this.offering).linkComputer(computerURI,ComputerServicesInboundPortURI, ComputerStaticStateDataInboundPortURI,
					 ComputerDynamicStateDataInboundPortURI);
		}
	}

