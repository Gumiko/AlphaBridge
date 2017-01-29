package fr.upmc.datacenter.extension.vm.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
/**
 * The class <code>VMExtendedManagementConnector</code>implements a
 * connector for ports exchanging through the interface
 * <code>VMExtendedManagementI</code>.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
/**
 * @author Mokrane
 *
 */
public class VMExtendedManagementConnector
	extends		AbstractConnector
	implements	VMExtendedManagementI{

	
	/* 
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#removeCore(int)
	 */
	@Override
	public AllocatedCore[] removeCore(int number) {
		return ((VMExtendedManagementI)this.offering).removeCore(number);
	}

	
	/* 
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#addCore(fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore[])
	 */
	@Override
	public int  addCore(int number) throws Exception {
		return ((VMExtendedManagementI)this.offering).addCore(number);
		
	}

	/* 
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#removeAll()
	 */
	@Override
	public AllocatedCore[] removeAll() {
		return ((VMExtendedManagementI)this.offering).removeAll();
	}

	
	/* 
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#getData()
	 */
	@Override
	public VMData getData() throws Exception {
		return ((VMExtendedManagementI)this.offering).getData();
	}
	
	
	/*
	 * @see fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI#connectNotificationPort(java.lang.String)
	 */
	@Override
	public void connectNotificationPort(String string) throws Exception{
		((VMExtendedManagementI)this.offering).connectNotificationPort(string);
	}
	
	


}
