package fr.upmc.datacenter.controller;

import fr.upmc.components.AbstractComponent;
import fr.upmc.datacenter.controller.interfaces.ControllerActuatorI;
import fr.upmc.datacenter.controller.interfaces.ControllerSensorI;
import fr.upmc.datacenter.dispatcher.connectors.RequestDispatcherManagementConnector;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementOutboundPort;

public class Controller extends AbstractComponent
implements ControllerActuatorI,ControllerSensorI{

	String controllerURI;
	String requestDispatcherURI;
	RequestDispatcherManagementOutboundPort rdmop;
	
	public Controller(String controllerURI,String requestDispatcherURI) throws Exception{
		this.controllerURI=controllerURI;
		/*Link the controller to the Request Dispatcher */
		rdmop=new RequestDispatcherManagementOutboundPort(controllerURI,this);
		rdmop.doConnection(requestDispatcherURI, RequestDispatcherManagementConnector.class.getCanonicalName());
	}
	
	
	@Override
	public void sendData() throws Exception {
		// TODO Auto-generated method stub
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

}
