package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
/**
 * The class <code>RequestDispatcherManagementOutboundPort</code> implements the
 * outbound port through which the component management methods are called.
 * 
 * <p>Created on : 2016-2017</p>
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
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#removeCore(int, java.lang.String)
	 */
	@Override
	public AllocatedCore[] removeCore(int number,String vmURI) throws Exception{
		return ((RequestDispatcherManagementI)this.connector).removeCore(number, vmURI);
	}
	
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#addCore(int, String)
	 */
	@Override
	public int addCore(int number,String vmURI) throws Exception{
		return ((RequestDispatcherManagementI)this.connector).addCore(number, vmURI);
	}
	
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#removeAll(java.lang.String)
	 */
	@Override
	public AllocatedCore[] removeAll(String vmURI) throws Exception{
		return ((RequestDispatcherManagementI)this.connector).removeAll(vmURI);
	}
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#resetRequestNumber()
	 */
	@Override
	public void resetRequestNumber() throws Exception {
		((RequestDispatcherManagementI)this.connector).resetRequestNumber();
	}
}
