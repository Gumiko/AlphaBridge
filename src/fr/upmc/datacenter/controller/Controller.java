package fr.upmc.datacenter.controller;

import fr.upmc.components.AbstractComponent;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.controller.interfaces.ControllerActuatorI;
import fr.upmc.datacenter.dispatcher.connectors.RequestDispatcherManagementConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherSensorI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherStaticStateI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataOutboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementOutboundPort;

public class Controller extends AbstractComponent
implements ControllerActuatorI,RequestDispatcherSensorI{

	
	/** ports of the controller receiving the dynamic data from its processor
	 *  components.															*/
	protected final RequestDispatcherDynamicStateDataOutboundPort rddsdop;
	
//	/** ports of the controller receiving the static data from its processor
//	 *  components.															*/
//	protected final RequestDispatcherStaticStateDataOutboundPort rdssdop ;

	
	String controllerURI;
	String requestDispatcherURI;
	RequestDispatcherManagementOutboundPort rdmop;
	
	public Controller(String controllerURI,String requestDispatcherURI,String SensorURI) throws Exception{
		this.controllerURI=controllerURI;
		/*Link the controller to the Request Dispatcher */
		rdmop=new RequestDispatcherManagementOutboundPort(controllerURI,this);
		rdmop.doConnection(requestDispatcherURI, RequestDispatcherManagementConnector.class.getCanonicalName());
		
		rddsdop=new RequestDispatcherDynamicStateDataOutboundPort(this,controllerURI);
		rddsdop.doConnection(requestDispatcherURI,ControlledDataConnector.class.getCanonicalName());
	}
	

	@Override
	public void addCores() throws Exception {
		
	}

	@Override
	public void removeCores() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addVM() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeVM() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptRequestDispatcherstaticData(String requestDispatcherURI,
			RequestDispatcherStaticStateI staticState) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptRequestDispatcherDynamicData(String requestDispatcherURI,
			RequestDispatcherDynamicStateI currentDynamicState) throws Exception {
		assert	requestDispatcherURI != null ;
		assert	currentDynamicState != null ;

		System.out.println("Controller " + this.controllerURI +
						   " accepting dynamic data from " + requestDispatcherURI) ;
		System.out.println("  timestamp                : " +
				currentDynamicState.getTimeStamp()) ;
		System.out.println("  timestamper id           : " +
				currentDynamicState.getTimeStamperId()) ;
		System.out.print(  "  current idle status      : [") ;
		
		/*TODO 
		 * Action with the data received (addCore ? RemoveCore ? AddVM ? removeVM ?)
		 * */
		
	}


}
