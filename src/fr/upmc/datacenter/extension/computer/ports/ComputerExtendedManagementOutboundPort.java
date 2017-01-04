package fr.upmc.datacenter.extension.computer.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

public class ComputerExtendedManagementOutboundPort extends		AbstractOutboundPort
implements	ComputerExtendedManagementI{


	public		ComputerExtendedManagementOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ComputerExtendedManagementI.class, owner) ;

		assert	owner != null;
	}

	public				ComputerExtendedManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ComputerExtendedManagementI.class, owner);

		assert	owner != null ;
	}

	@Override
	public void releaseCore(AllocatedCore ac) throws Exception {
		((ComputerExtendedManagementI)this.connector).releaseCore(ac);
		
	}

	@Override
	public AllocatedCore[] reserveCore(int number) throws Exception {
		return ((ComputerExtendedManagementI)this.connector).reserveCore(number);
	}

	@Override
	public void reserveCore(int processorNo, int coreNo) {
		((ComputerExtendedManagementI)this.connector).reserveCore(processorNo, coreNo);
		
	}


}
