package fr.upmc.datacenter.controller.interfaces;

import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
/**
 * The interface <code>ControllerManagementI</code>defines the methods
 * to manage the Controller.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public interface ControllerManagementI {
	
	/**
	 * add a computer which the controller will use to create new vm
	 * @param c_out			ComputerServicesOutboundPort of the computer.
	 * @throws Exception
	 */
	public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception;
}
