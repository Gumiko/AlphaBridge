package fr.upmc.datacenter.dispatcher.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
/**
 * The class <code>RequestDispatcherManagementConnector</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class RequestDispatcherManagementConnector extends		AbstractConnector
implements	RequestDispatcherManagementI{
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#bindVM(String,String,String,String)
	 */
	@Override
	public void bindVM(String vmUri, String requestSubmissionOutboundPortUri, String avmManagementOutboundPortUri,String VMExtendedManagementOutboundPortURI) throws Exception {
		((RequestDispatcherManagementI)this.offering).bindVM(vmUri,requestSubmissionOutboundPortUri,avmManagementOutboundPortUri, VMExtendedManagementOutboundPortURI);
		
	}
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#unbindVM(String)
	 */
	@Override
	public void unbindVM(String vmUri) throws Exception {
		((RequestDispatcherManagementI)this.offering).unbindVM(vmUri);
	}
	
}
