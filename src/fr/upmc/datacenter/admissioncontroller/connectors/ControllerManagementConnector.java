package fr.upmc.datacenter.admissioncontroller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.admissioncontroller.interfaces.ControllerManagementI;
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
public class ControllerManagementConnector
	extends		AbstractConnector
	implements	ControllerManagementI
	{
	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ControllerManagementI#linkComputer(ComputerServicesOutboundPort c_out) 
	 */
		@Override
		public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception {
			((ControllerManagementI)this.offering).linkComputer(c_out);
		}
		/**
		 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ControllerManagementI#linkComputer(String csopURI)
		 */
		@Override
		public void linkComputer(String csopURI,String csipURI) throws Exception {
			((ControllerManagementI)this.offering).linkComputer(csopURI,csipURI);
		}
	}

