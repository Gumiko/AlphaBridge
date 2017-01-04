package fr.upmc.datacenter.extension.computer.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

public class ComputerExtendedManagementConnector 	extends		AbstractConnector implements ComputerExtendedManagementI{

	@Override
	public void releaseCore(AllocatedCore ac) throws Exception {
		((ComputerExtendedManagementI)this.offering).releaseCore(ac);
		
	}

	@Override
	public AllocatedCore[] reserveCore(int number) throws Exception {
		return ((ComputerExtendedManagementI)this.offering).reserveCore(number);
	}

	@Override
	public void reserveCore(int processorNo, int coreNo) {
		((ComputerExtendedManagementI)this.offering).reserveCore(processorNo, coreNo);
		
	}

}
