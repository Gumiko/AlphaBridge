package fr.upmc.datacenter.admissioncontroller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
/**
 * The class <code>ControllerManagementConnector</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class AdmissionControllerManagementConnector
	extends		AbstractConnector
	implements	AdmissionControllerManagementI
	{
	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(ComputerServicesOutboundPort c_out) 
	 */
		@Override
		public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception {
			((AdmissionControllerManagementI)this.offering).linkComputer(c_out);
		}
		/**
		 * @see fr.upmc.datacenter.admissioncontroller.interfaces.AdmissionControllerManagementI#linkComputer(String csopURI)
		 */
		@Override
		public void linkComputer(String csopURI,String csipURI) throws Exception {
			((AdmissionControllerManagementI)this.offering).linkComputer(csopURI,csipURI);
		}
	}

