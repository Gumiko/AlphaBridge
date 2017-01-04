package fr.upmc.datacenter.extension.computer.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

public interface ComputerExtendedManagementI extends RequiredI,OfferedI{
	
	public void releaseCore(AllocatedCore ac) throws Exception;
	public AllocatedCore[] reserveCore(int number) throws Exception;
	public void reserveCore(int processorNo,int coreNo);

}
