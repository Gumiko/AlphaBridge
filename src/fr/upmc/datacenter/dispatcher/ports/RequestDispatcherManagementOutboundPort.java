package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The class <code>RequestDispatcherManagementOutboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
@SuppressWarnings("unused")
public class RequestDispatcherManagementOutboundPort
extends		AbstractOutboundPort
implements	RequestDispatcherManagementI{


	public		RequestDispatcherManagementOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(RequestDispatcherManagementI.class, owner) ;

		assert	owner != null && owner instanceof Controller ;
	}

	public				RequestDispatcherManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, RequestDispatcherManagementI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}
	/**
	 * @throws Exception 
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#deployVM(int rd, String RequestDispatcherURIDVM)
	 */
	@Override
	public void bindVM(String vmUri, String str_rsop, String str_avmmop,String VMExtendedManagementOutboundPortURI) throws Exception {
		((RequestDispatcherManagementI)this.connector).bindVM(vmUri,str_rsop,str_avmmop, VMExtendedManagementOutboundPortURI);

	}

	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#unbindVM(String uriComputerParent, String vm)
	 */
	@Override
	public void unbindVM(String vmUri) throws Exception {
		((RequestDispatcherManagementI)this.connector).unbindVM(vmUri);
	}
}
