package fr.upmc.datacenter.controller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
/**
 * The class <code>ControllerManagementConnector</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class ControllerManagementConnector
	extends		AbstractConnector
	implements	ControllerManagementI
	{
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#linkComputer(ComputerServicesOutboundPort c_out) 
	 */
		@Override
		public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception {
			((ControllerManagementI)this.offering).linkComputer(c_out);
		}
	}

