package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.dispatcher.RequestDispatcher;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherActuatorI;

public class RequestDispatcherActuatorInboundPort extends	AbstractInboundPort implements RequestDispatcherActuatorI
{
	
	private static final long serialVersionUID = 1L;

	public		RequestDispatcherActuatorInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(RequestDispatcherActuatorI.class, owner) ;

		assert	owner != null && owner instanceof RequestDispatcher ;
	}

	public				RequestDispatcherActuatorInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, RequestDispatcherActuatorI.class, owner);

		assert	owner != null && owner instanceof RequestDispatcher ;
	}

//	@Override
//	public void addCores() throws Exception {
//		final RequestDispatcher c = (RequestDispatcher) this.owner;
//		 c.addCores();
//	}
//
//	@Override
//	public void removeCores() throws Exception {
//		final RequestDispatcher c = (RequestDispatcher) this.owner;
//		 c.removeCores();
//	}
//
//	@Override
//	public void addVM() throws Exception {
//		final RequestDispatcher c = (RequestDispatcher) this.owner;
//		 c.addVM();
//	}
//	
//	@Override
//	public void removeVM() throws Exception {
//		final RequestDispatcher c = (RequestDispatcher) this.owner;
//		 c.removeVM();
//	}

}
