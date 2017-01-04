package fr.upmc.datacenter.extension.vm.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.extension.vm.VirtualMachineExtended;
import fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

public class VMExtendedManagementInboundPort extends AbstractOutboundPort implements VMExtendedManagementI{
	private static final long serialVersionUID = 1L;

	public		VMExtendedManagementInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(VMExtendedManagementI.class, owner) ;

		assert	owner != null && owner instanceof VirtualMachineExtended ;
	}

	public				VMExtendedManagementInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, VMExtendedManagementI.class, owner);

		assert	owner != null && owner instanceof VirtualMachineExtended ;
	}

	@Override
	public AllocatedCore[] removeCore(int number) {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.removeCore(number);
	}

	@Override
	public void addCore(AllocatedCore[] ac) throws Exception {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		c.addCore(ac);
		
	}

	@Override
	public AllocatedCore[] removeAll() {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.removeAll();
	}

}
