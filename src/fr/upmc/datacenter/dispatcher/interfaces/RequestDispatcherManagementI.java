package fr.upmc.datacenter.dispatcher.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

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
	
	/**
	 * Remove a number of Core of a VM
	 * @param number number of cores to deallocate
	 * @param vmURI uri of the vm to deallocate
	 * @throws Exception e
	 * @return return Cores deallocated
	 */
	public AllocatedCore[] removeCore(int number,String vmURI) throws Exception;
	
	/**
	 *  Try to allocate a number of cores to a vm
	 * @param number number of cores to allocate
	 * @param vmURI URI of the vm to allocate
	 * @return real number of cores allocated
	 * @throws Exception e
	 */
	public int addCore(int number,String vmURI) throws Exception;
	
	/**
	 * Remove all the cores allocated to a vm
	 * @param vmURI Uri of the vm
	 * @return all the cores deallocated
	 * @throws Exception e
	 */
	public AllocatedCore[] removeAll(String vmURI) throws Exception;
	
	/**
	 * Reset the number of request send to the controller
	 * Used when changed the cores or vm and want to see the changes
	 * @throws Exception e
	 */
	public void resetRequestNumber() throws Exception;
	
}
