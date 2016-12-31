package fr.upmc.datacenter.controller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.controller.interfaces.ControllerActuatorI;

public class ControllerActuatorConnector extends AbstractConnector implements ControllerActuatorI{

	@Override
	public void addCores() throws Exception {
		((ControllerActuatorI)this.offering).addCores();
		
	}

	@Override
	public void removeCores() throws Exception {
		((ControllerActuatorI)this.offering).removeCores();
		
	}

	@Override
	public void addVM() throws Exception {
		((ControllerActuatorI)this.offering).addVM();
		
	}

	@Override
	public void removeVM() throws Exception {
		((ControllerActuatorI)this.offering).removeVM();
		
	}

}
