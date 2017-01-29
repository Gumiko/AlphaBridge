package fr.upmc.datacenter.extension.vm.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
/**
 * The interface <code>VMExtendedManagementI</code>provides exatra  the methods
 * to manage VMs
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public interface VMExtendedManagementI extends OfferedI,RequiredI{
	
	
    /***
     * retrieves the VM data
     * @return
     * @throws Exception
     */
	public VMData getData() throws Exception;
	
	/***
	 * removes a given core from the Vm
	 * @param number    thr core number 
	 * @return
	 */
	public AllocatedCore[] removeCore(int number);
	
	/***
	 * add the cores to vm
	 * @param ac         AllocatedCore to be added
	 * @throws Exception
	 */
	public void addCore(AllocatedCore[] ac) throws Exception;
	
	/***
	 * remove all vm cores
	 * @return
	 */
	public AllocatedCore[] removeAll();
	
	/***
	 * connect the vm to the given port uri
	 * @param port
	 * @throws Exception
	 */
	
	public void connectNotificationPort(String port) throws Exception;
}
