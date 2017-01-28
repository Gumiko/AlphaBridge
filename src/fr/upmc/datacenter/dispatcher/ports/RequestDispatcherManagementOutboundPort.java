package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.dispatcher.RequestDispatcher;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The class <code>RequestDispatcherManagementOutboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */

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
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#bindVM(String,String,String,String)
	 */
	@Override
	public void bindVM(String vmUri, String requestSubmissionInboundPortURI, String applicationVMManagementInboundPortURI,String VMExtendedManagementInboundPortURI) throws Exception {
		((RequestDispatcherManagementI)this.connector).bindVM(vmUri,requestSubmissionInboundPortURI,applicationVMManagementInboundPortURI, VMExtendedManagementInboundPortURI);

	}

	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#unbindVM(String)
	 */
	@Override
	public void unbindVM(String vmUri) throws Exception {
		((RequestDispatcherManagementI)this.connector).unbindVM(vmUri);
	}
}
