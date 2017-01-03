package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.dispatcher.RequestDispatcher;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherActuatorI;

public class RequestDispatcherActuatorOutboundPort extends AbstractOutboundPort implements RequestDispatcherActuatorI{
	public		RequestDispatcherActuatorOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(RequestDispatcherActuatorI.class, owner) ;

		assert	owner != null && owner instanceof RequestDispatcher ;
	}

	public				RequestDispatcherActuatorOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, RequestDispatcherActuatorI.class, owner);

		assert	owner != null && owner instanceof RequestDispatcher ;
	}

//	@Override
//	public void addCores() throws Exception {
//		((RequestDispatcherActuatorI)this.connector).addCores();
//	}
//
//	@Override
//	public void removeCores() throws Exception {
//		((RequestDispatcherActuatorI)this.connector).removeCores();
//	}
//
//	@Override
//	public void addVM() throws Exception {
//		((RequestDispatcherActuatorI)this.connector).addVM();
//	}
//	
//	@Override
//	public void removeVM() throws Exception {
//		((RequestDispatcherActuatorI)this.connector).removeVM();
//	}
	
}