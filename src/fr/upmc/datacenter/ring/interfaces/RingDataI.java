package fr.upmc.datacenter.ring.interfaces;

public interface RingDataI {
	
	
		public void			acceptRingDynamicData(
			String					requestDispatcherURI,
			RingDynamicStateI	currentDynamicState
			) throws Exception ;
}
