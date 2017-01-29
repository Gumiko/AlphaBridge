package fr.upmc.datacenter.extension.vm.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.extension.vm.VirtualMachineExtended;
import fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
/**
* The class <code>VMExtendedManagementInboundPort</code> implements the
 * inbound port through which the component management methods are called.
* 
* <p>Created on : 2016-2017</p>
* 
* @author	Cédric Ribeiro et Mokrane Kadri
*/
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
/**
 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#removeCore(int)
 */
	@Override
	public AllocatedCore[] removeCore(int number) {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.removeCore(number);
	}
	/**
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#addCore(int)
	 */
	@Override
	public int addCore(int number) throws Exception {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.addCore(number);
		
	}
	/**
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#removeAll()
	 */
	@Override
	public AllocatedCore[] removeAll() {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.removeAll();
	}
/**
 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#getData()
 */
	@Override
	public VMData getData() throws Exception {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		return c.getData();
	}
	/**
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#connectNotificationPort(java.lang.String)
	 */
	@Override
	public void connectNotificationPort(String string) throws Exception {
		final VirtualMachineExtended c = (VirtualMachineExtended) this.owner;
		 c.connectNotificationPort(string);
	}

}
