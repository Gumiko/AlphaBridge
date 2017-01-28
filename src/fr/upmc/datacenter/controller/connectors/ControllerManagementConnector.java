package fr.upmc.datacenter.controller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;

public class ControllerManagementConnector extends		AbstractConnector
implements	ControllerManagementI{

	@Override
	public void stopSending() throws Exception {
		((ControllerManagementI)this.offering).stopSending();

	}

	@Override
	public void startSending() throws Exception {
		((ControllerManagementI)this.offering).startSending();

	}

	@Override
	public String getNextControllerUri() {
		return ((ControllerManagementI)this.offering).getNextControllerUri();
	}

	@Override
	public String getPreviousControllerUri() {
		return ((ControllerManagementI)this.offering).getPreviousControllerUri();
	}

	@Override
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception {
		((ControllerManagementI)this.offering).bindSendingDataUri(DataInboundPortUri);
	}
}
