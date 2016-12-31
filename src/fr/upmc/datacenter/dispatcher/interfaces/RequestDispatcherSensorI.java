package fr.upmc.datacenter.dispatcher.interfaces;

public interface RequestDispatcherSensorI {
	public void			acceptRequestDispatcherstaticData(
			String					requestDispatcherURI,
			RequestDispatcherStaticStateI	staticState
			) throws Exception ;


		public void			acceptRequestDispatcherDynamicData(
			String					requestDispatcherURI,
			RequestDispatcherDynamicStateI	currentDynamicState
			) throws Exception ;
}
