package fr.upmc.datacenter.ring.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.interfaces.DataRequiredI;
import fr.upmc.datacenter.hardware.computers.interfaces.ComputerStateDataConsumerI;
import fr.upmc.datacenter.ports.AbstractControlledDataOutboundPort;
import fr.upmc.datacenter.ring.interfaces.RingDataI;
import fr.upmc.datacenter.ring.interfaces.RingDynamicStateI;

public class RingDynamicStateDataOutboundPort extends AbstractControlledDataOutboundPort{
	private static final long serialVersionUID = 1L;
	protected String			ringURI ;

	public				RingDynamicStateDataOutboundPort(
			ComponentI owner,
			String ringURI
			) throws Exception
	{
		super(owner) ;
		this.ringURI = ringURI ;

		assert	owner instanceof ComputerStateDataConsumerI ;
	}

	public				RingDynamicStateDataOutboundPort(
			String uri,
			ComponentI owner,
			String ringURI
			) throws Exception
	{
		super(uri, owner);
		this.ringURI = ringURI ;

		assert	owner instanceof ComputerStateDataConsumerI ;
	}

	/**
	 * @see fr.upmc.components.interfaces.DataRequiredI.PushI#receive(fr.upmc.components.interfaces.DataRequiredI.DataI)
	 */
	@Override
	public void			receive(DataRequiredI.DataI d)
			throws Exception
	{
		((RingDataI)this.owner).acceptRingDynamicData(this.ringURI,(RingDynamicStateI) d) ;
	}
}
