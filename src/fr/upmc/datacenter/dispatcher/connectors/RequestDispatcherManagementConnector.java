package fr.upmc.datacenter.dispatcher.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
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
	/*
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#removeCore(int, java.lang.String)
	 */
	@Override
	public AllocatedCore[] removeCore(int number,String vmURI) throws Exception{
		return ((RequestDispatcherManagementI)this.offering).removeCore(number,vmURI);
	}
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#addCore(int, java.lang.String)
	 */
	@Override
	public int addCore(int number,String vmURI) throws Exception{
		return ((RequestDispatcherManagementI)this.offering).addCore(number,vmURI);
	}
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#removeAll(java.lang.String)
	 */
	@Override
	public AllocatedCore[] removeAll(String vmURI) throws Exception{
		return ((RequestDispatcherManagementI)this.offering).removeAll(vmURI);
	}
	
	/**
	 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#resetRequestNumber()
	 */
	@Override
	public void resetRequestNumber() throws Exception{
		((RequestDispatcherManagementI)this.offering).resetRequestNumber();
	}
	
}
