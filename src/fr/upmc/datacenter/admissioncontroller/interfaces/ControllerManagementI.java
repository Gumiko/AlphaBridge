package fr.upmc.datacenter.admissioncontroller.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
/**
 * The interface <code>ControllerManagementI</code>defines the methods
 * to manage the Controller.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public interface ControllerManagementI extends OfferedI,RequiredI{
	
	/**
	 * add a computer which the controller will use to create new vm
	 * @param c_out			ComputerServicesOutboundPort of the computer.
	 * @throws Exception
	 */
	public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception;
	
	/**
	 * add a computer which the controller will use to create new vm
	 * @param csopURI			ComputerServicesOutboundPort URI of the computer.
	 * @param csipURI			ComputerServicesInboundPort URI of the computer
	 * @throws Exception
	 */
	public void linkComputer(String csopURI,String csipURI) throws Exception;
}
