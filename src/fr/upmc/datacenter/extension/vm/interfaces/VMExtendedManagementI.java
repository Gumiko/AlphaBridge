package fr.upmc.datacenter.extension.vm.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

public interface VMExtendedManagementI extends OfferedI,RequiredI{

	public VMData getData() throws Exception;
	
	public AllocatedCore[] removeCore(int number);
	
	public void addCore(AllocatedCore[] ac) throws Exception;
	
	public AllocatedCore[] removeAll();
	
	public void connectNotificationPort(String string) throws Exception;
}
