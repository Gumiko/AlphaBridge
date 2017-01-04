package fr.upmc.datacenter.extension.computer.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.extension.computer.ComputerExtended;
import fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

public class ComputerExtendedManagementInboundPort extends	AbstractInboundPort
implements	ComputerExtendedManagementI{
	
	private static final long serialVersionUID = 1L;

	public		ComputerExtendedManagementInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ComputerExtendedManagementI.class, owner) ;

		assert	owner != null && owner instanceof ComputerExtended ;
	}

	public				ComputerExtendedManagementInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ComputerExtendedManagementI.class, owner);

		assert	owner != null && owner instanceof ComputerExtended ;
	}

	@Override
	public void releaseCore(AllocatedCore ac) throws Exception {
		final ComputerExtended c = (ComputerExtended) this.owner;
		 c.releaseCore(ac);
		
	}

	@Override
	public AllocatedCore[] reserveCore(int number) throws Exception {
		final ComputerExtended c = (ComputerExtended) this.owner;
		 return c.reserveCore(number);
	}

	@Override
	public void reserveCore(int processorNo, int coreNo) {
		final ComputerExtended c = (ComputerExtended) this.owner;
		 c.reserveCore(processorNo,coreNo);
		
	}


}
