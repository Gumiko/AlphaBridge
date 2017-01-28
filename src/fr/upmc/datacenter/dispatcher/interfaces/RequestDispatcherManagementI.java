package fr.upmc.datacenter.dispatcher.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;

/**
 * The interface <code>RequestDispatcherManagementI</code> defines the methods
 * to manage the Request Dispatcher.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
public interface RequestDispatcherManagementI extends OfferedI,RequiredI{
	/**
	 * Add a Virtual Machine to the Request Dispatcher
	 * @param vmUri			The Virtual Machine URI.
	 * @param requestSubmissionInboundPortUri The Uri of the requestSubmissionInboundPort of the vm
	 * @param avmManagementInboundPortUri The Management Port URI of The Virtual Machine 
	 * @param VMExtendedManagementInboundPortURI The Extended Management Port URI of The Virtual Machine 
	 * @throws Exception e
	 */
	public void bindVM(String vmUri, String requestSubmissionInboundPortUri, String avmManagementInboundPortUri,String VMExtendedManagementInboundPortURI) throws Exception;

	/**
	 * Remove a Virtual Machine of the Request Dispatcher
	 * @param vmUri The Virtual Machine URI
	 * @throws Exception e
	 */
	public void unbindVM(String vmUri) throws Exception;
	
}
