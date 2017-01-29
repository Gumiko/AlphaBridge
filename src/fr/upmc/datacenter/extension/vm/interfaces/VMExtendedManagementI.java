package fr.upmc.datacenter.extension.vm.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
/**
 * The interface <code>VMExtendedManagementI</code>provides extra  the methods
 * to manage VMs
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
public interface VMExtendedManagementI extends OfferedI,RequiredI{
	
	
    /***
     * retrieves the VM data
     * @return the information of the VM
     * @throws Exception e
     */
	public VMData getData() throws Exception;
	
	/***
	 * removes a given core from the Vm
	 * @param number    thr core number 
	 * @return the allocated cores removed
	 */
	public AllocatedCore[] removeCore(int number);
	
	/***
	 * Try to add cores to vm
	 * @param number         number of AllocatedCore to be added
	 * @throws Exception e
	 * @return number of core allocated
	 */
	public int addCore(int number) throws Exception;
	
	/***
	 * remove all vm cores
	 * @return real number of core added
	 */
	public AllocatedCore[] removeAll();
	
	/***
	 * connect the vm to the given port uri
	 * @param port port of Notification
	 * @throws Exception e
	 */
	
	public void connectNotificationPort(String port) throws Exception;
}
