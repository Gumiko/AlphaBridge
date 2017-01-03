package fr.upmc.datacenter.dispatcher.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The interface <code>RequestDispatcherI</code>defines the methods
 * to manage the Request Dispatcher at initialisation.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public interface RequestDispatcherI extends OfferedI,RequiredI{
	/**
	 * Link the Request generator to the Request Dispatcher
	 * @param c_out			ComputerServicesOutboundPort of the computer.
	 * @throws Exception
	 */
	public void linkRequestGenerator(String rg_rsop,String rg_rnip) throws Exception;
	/**
	 * Link a Vm to the request Dispatcher
	 * @param id			id of the VM.
	 * @param vm			the vm.
	 * @param vm_rsip		vm request submission inbound port uri.
	 * @param vm_rnop		vm request notification outbound port uri.
	 * @throws Exception
	 */
}
