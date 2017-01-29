package fr.upmc.datacenter.extension.vm.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.extension.vm.VMData;
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
	public int addCore(int number) throws Exception {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.addCore(number);
		
	}

	@Override
	public AllocatedCore[] removeAll() {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.removeAll();
	}

	@Override
	public VMData getData() throws Exception {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.getData();
	}
	
	@Override
	public void connectNotificationPort(String string) throws Exception {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		 c.connectNotificationPort(string);
	}

}
