package fr.upmc.datacenter.dispatcher.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
/**
 * The interface <code>RequestDispatcherI</code>defines the methods
 * to manage the Request Dispatcher at initialisation.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	C�dric Ribeiro et Mokrane Kadri
 */
public interface RequestDispatcherI extends OfferedI,RequiredI{
	/**
	 * Link The request Generator to the request Dispatcher
	 * @param requestGeneratorRequestNotificationInboundPort RequestNotificationInboundPortURI of the Request Generator
	 * @throws Exception e
	 */
	public void linkRequestGenerator(String requestGeneratorRequestNotificationInboundPort) throws Exception;

}
