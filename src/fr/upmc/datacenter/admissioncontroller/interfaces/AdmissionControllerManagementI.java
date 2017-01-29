package fr.upmc.datacenter.admissioncontroller.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
/**
 * The interface <code>AdmissionControllerManagementI</code> defines the methods
 * to manage the Admission Controller.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
public interface AdmissionControllerManagementI extends OfferedI,RequiredI{
	/**
	 * Add a computer which the Admission Controller will use to create new vm
	 * @param computerURI The Computer URI
	 * @param ComputerStaticStateDataInboundPortURI			ComputerStaticStateDataInboundPortURI URI of the computer.
	 * @param ComputerDynamicStateDataInboundPortURI	ComputerDynamicStateDataInboundPortURI URI of the computer.
	 * @param ComputerServicesInboundPortURI			ComputerServicesInboundPort URI of the computer.
	 * @throws Exception e
	 */
	public void linkComputer(String computerURI,String ComputerServicesInboundPortURI,String ComputerStaticStateDataInboundPortURI,
			String ComputerDynamicStateDataInboundPortURI) throws Exception;
}
