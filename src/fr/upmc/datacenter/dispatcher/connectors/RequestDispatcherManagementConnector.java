package fr.upmc.datacenter.dispatcher.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
/**
 * The class <code>RequestDispatcherManagementConnector</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class RequestDispatcherManagementConnector extends		AbstractConnector
implements	RequestDispatcherManagementI{
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#deployVM(int rd, String RequestDispatcherURIDVM)
	 */
	@Override
	public void deployVM(int rd, String RequestDispatcherURIDVM) {
		((RequestDispatcherManagementI)this.offering).deployVM(rd, RequestDispatcherURIDVM);
		
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#destroyVM(String uriComputerParent, String vm)
	 */
	@Override
	public void destroyVM(String uriComputerParent, String vm) {
		((RequestDispatcherManagementI)this.offering).destroyVM(uriComputerParent, vm);
		
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#initVM(int application, String uriComputerParent, String vm)
	 */
	@Override
	public void initVM(int application, String uriComputerParent, String vm) {
		((RequestDispatcherManagementI)this.offering).initVM(application, uriComputerParent, vm);
		
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#unbindVM(String uriComputerParent, String vm)
	 */
	@Override
	public void unbindVM(String uriComputerParent, String vm) throws Exception {
		((RequestDispatcherManagementI)this.offering).unbindVM(uriComputerParent, vm);
		
	}
}
