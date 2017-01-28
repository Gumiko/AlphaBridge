package fr.upmc.datacenter.dispatcher.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;

/**
 * The interface <code>RequestDispatcherManagementI</code>defines the methods
 * to manage the Request Dispatcher.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public interface RequestDispatcherManagementI extends OfferedI,RequiredI{
	/**
	 * deploy a vm to the request dispatcher
	 * @param rd			id of the request dispatcher.
	 * @param RequestDispatcherURIDVM uri of the vm.
	 * @throws Exception
	 */
	public void bindVM(int id, String str_rsop, String str_avmmop,String VMExtendedManagementOutboundPortURI) throws Exception;

	/**
	 * take back a vm of the request dispatcher
	 * @param uriComputerParent the uri of the computer which delivered the allocated cores.
	 * @param vm Uri of the vm.
	 * @throws Exception
	 */
	public void unbindVM(int id) throws Exception;
	
}
