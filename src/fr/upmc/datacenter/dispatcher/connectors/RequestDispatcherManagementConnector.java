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
	 * @throws Exception 
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#deployVM(int rd, String RequestDispatcherURIDVM)
	 */
	@Override
	public void bindVM(String vmUri, String str_rsop, String str_avmmop,String VMExtendedManagementOutboundPortURI) throws Exception {
		((RequestDispatcherManagementI)this.offering).bindVM(vmUri,str_rsop,str_avmmop, VMExtendedManagementOutboundPortURI);
		
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#unbindVM(String uriComputerParent, String vm)
	 */
	@Override
	public void unbindVM(String vmUri) throws Exception {
		((RequestDispatcherManagementI)this.offering).unbindVM(vmUri);
	}
	
}
