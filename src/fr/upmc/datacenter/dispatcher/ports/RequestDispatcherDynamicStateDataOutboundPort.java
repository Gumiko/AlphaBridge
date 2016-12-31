package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.interfaces.DataRequiredI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherSensorI;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerStateDataConsumerI;
import fr.upmc.datacenter.ports.AbstractControlledDataOutboundPort;

public class RequestDispatcherDynamicStateDataOutboundPort extends		AbstractControlledDataOutboundPort
{
	private static final long	serialVersionUID = 1L ;
	protected String			requestDispatcherURI ;

	public				RequestDispatcherDynamicStateDataOutboundPort(
		ComponentI owner,
		String requestDispatcherURI
		) throws Exception
	{
		super(owner) ;
		this.requestDispatcherURI = requestDispatcherURI ;

		assert	owner instanceof ComputerStateDataConsumerI ;
	}

	public				RequestDispatcherDynamicStateDataOutboundPort(
		String uri,
		ComponentI owner,
		String requestDispatcherURI
		) throws Exception
	{
		super(uri, owner);
		this.requestDispatcherURI = requestDispatcherURI ;

		assert	owner instanceof RequestDispatcherSensorI ;
	}

	/**
	 * @see fr.upmc.components.interfaces.DataRequiredI.PushI#receive(fr.upmc.components.interfaces.DataRequiredI.DataI)
	 */
	@Override
	public void			receive(DataRequiredI.DataI d)
	throws Exception
	{
		((RequestDispatcherSensorI)this.owner).
						acceptRequestDispatcherDynamicData(this.requestDispatcherURI,
												  (RequestDispatcherDynamicStateI) d) ;
	}
}