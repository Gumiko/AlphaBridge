package fr.upmc.datacenter.dispatcher.interfaces;
/**
 * The interface <code>RequestDispatcherManagementI</code>defines the methods
 * to manage the Request Dispatcher.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public interface RequestDispatcherManagementI {
	/**
	 * deploy a vm to the request dispatcher
	 * @param rd			id of the request dispatcher.
	 * @param RequestDispatcherURIDVM uri of the vm.
	 * @throws Exception
	 */
	public void deployVM(int rd, String RequestDispatcherURIDVM);
	/**
	 * take back a vm of the request dispatcher
	 * @param uriComputerParent the uri of the computer which delivered the allocated cores.
	 * @param vm Uri of the vm.
	 * @throws Exception
	 */
	public void destroyVM(String uriComputerParent, String vm) ;

	/**
	 * Initialise a vm and send it to the request dispatcher
	 * @param application id of the request generator
	 * @param uriComputerParent the uri of the computer which delivered the allocated cores.
	 * @param vm Uri of the vm.
	 * @throws Exception
	 */
	public void initVM(int application, String uriComputerParent, String vm);

	/**
	 * take back a vm of the request dispatcher
	 * @param uriComputerParent the uri of the computer which delivered the allocated cores.
	 * @param vm Uri of the vm.
	 * @throws Exception
	 */
	public void unbindVM(String uriComputerParent, String vm) throws Exception;
}
