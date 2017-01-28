package fr.upmc.datacenter.extension.vm.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.extension.vm.interfaces.VMExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

public class VMExtendedManagementConnector
	extends		AbstractConnector
	implements	VMExtendedManagementI{

	@Override
	public AllocatedCore[] removeCore(int number) {
		return ((VMExtendedManagementI)this.offering).removeCore(number);
	}

	@Override
	public void addCore(AllocatedCore[] ac) throws Exception {
		((VMExtendedManagementI)this.offering).addCore(ac);
		
	}

	@Override
	public AllocatedCore[] removeAll() {
		return ((VMExtendedManagementI)this.offering).removeAll();
	}

	@Override
	public VMData getData() throws Exception {
		return ((VMExtendedManagementI)this.offering).getData();
	}
	
	@Override
	public void connectNotificationPort(String string) throws Exception{
		((VMExtendedManagementI)this.offering).connectNotificationPort(string);
	}
	
	


}
