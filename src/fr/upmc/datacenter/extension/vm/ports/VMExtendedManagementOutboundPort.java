package fr.upmc.datacenter.extension.vm.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

public class VMExtendedManagementOutboundPort extends		AbstractOutboundPort
implements	VMExtendedManagementI{


	public		VMExtendedManagementOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(VMExtendedManagementI.class, owner) ;

		assert	owner != null && owner instanceof Controller ;
	}

	public				VMExtendedManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, VMExtendedManagementI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}

	@Override
	public AllocatedCore[] removeCore(int number) {
		return ((VMExtendedManagementI)this.connector).removeCore(number);
	}

	@Override
	public void addCore(AllocatedCore[] ac) throws Exception {
		 ((VMExtendedManagementI)this.connector).addCore(ac);
		
	}

	@Override
	public AllocatedCore[] removeAll() {
		 return ((VMExtendedManagementI)this.connector).removeAll();
	}

	@Override
	public VMData getData() throws Exception {
		return ((VMExtendedManagementI)this.connector).getData();
	}

}
