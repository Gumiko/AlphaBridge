package fr.upmc.datacenter.extension.computer.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
/**
 * The interface <code>ComputerExtendedManagementI</code>defines extra the methods
 * for Cores management 
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public interface ComputerExtendedManagementI extends RequiredI,OfferedI{
	
	/***
	 * Releases allocated cores
	 * @param ac         Allocated cores to release
	 * @throws Exception e
	 */
	public void releaseCore(AllocatedCore ac) throws Exception;
	
	/***
	 * Reserves the requested number of cores
	 * @param number  number of cores
	 * @return The Allocated core reserved
	 * @throws Exception e
	 */
	public AllocatedCore[] reserveCore(int number) throws Exception;
	
	
	/***
	 * Reserves coreNo Cores on the processor processorNo
	 * @param processorNo   the processor number
	 * @param coreNo        number of cores to reserve
	 */
	public void reserveCore(int processorNo,int coreNo);

}
