package fr.upmc.datacenter.controller;

import fr.upmc.components.AbstractComponent;
import fr.upmc.data.StaticData;
import fr.upmc.datacenter.connectors.ControlledDataConnector;
import fr.upmc.datacenter.dispatcher.connectors.RequestDispatcherActuatorConnector;
import fr.upmc.datacenter.dispatcher.connectors.RequestDispatcherManagementConnector;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherDynamicStateI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherSensorI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherStaticStateI;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherActuatorOutboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherDynamicStateDataOutboundPort;
import fr.upmc.datacenter.dispatcher.ports.RequestDispatcherManagementOutboundPort;

public class Controller extends AbstractComponent
implements RequestDispatcherSensorI{

	
	/** ports of the controller receiving the dynamic data from its processor
	 *  components.															*/
	protected final RequestDispatcherDynamicStateDataOutboundPort rddsdop;
	
//	/** ports of the controller receiving the static data from its processor
//	 *  components.															*/
//	protected final RequestDispatcherStaticStateDataOutboundPort rdssdop ;

	
	String controllerURI;
	String requestDispatcherURI;
	RequestDispatcherManagementOutboundPort rdmop;
	RequestDispatcherActuatorOutboundPort rdaop;
	
	/*
	 *  ControllerRingDataInboundPort crdip;
	 *  ControllerRingDataOutboundPort crdop;
	 * 
	 */
	
	int idVM=1;
	
	int waitingAllocation=0;
	int waitingDisallow=0;
	
	public Controller(String controllerURI,String rddsdipURI,String rdmipURI, String rdaipURI) throws Exception{
		this.controllerURI=controllerURI;
		/*Link the controller to the Request Dispatcher */
		rdmop=new RequestDispatcherManagementOutboundPort(this);
		rdmop.doConnection(rdmipURI, RequestDispatcherManagementConnector.class.getCanonicalName());
		
		rdaop=new RequestDispatcherActuatorOutboundPort(this);
		rdaop.doConnection(rdaipURI, RequestDispatcherActuatorConnector.class.getCanonicalName());
		
		rddsdop=new RequestDispatcherDynamicStateDataOutboundPort(this,controllerURI);
		rddsdop.doConnection(rddsdipURI,ControlledDataConnector.class.getCanonicalName());
		
		//rdmop.addVM(int id,String a,String b,String c);
		//rdmop.bindVM(id, str_rsop, str_rnip, str_avmmop);
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
		
		
		long time = currentDynamicState.getAverageTime();
		processControl(time);
	}
	
	private void processControl(long time) {
		double factor=0;
		METHOD method=METHOD.NORMAL;
		if(isHigher(time)){
			 factor = (time/StaticData.AVERAGE_TARGET);
			 method=METHOD.HIGHER;
		}
		if(isLower(time)){
			 factor = (StaticData.AVERAGE_TARGET/time);
			 method=METHOD.LOWER;
		}
		if(method!=METHOD.NORMAL){
			int cores = getNumberOfCoreAllocated();
			if(method==METHOD.LOWER){
				disallow((int)(cores-(cores/factor)));
			}else{
				allow((int)(cores*factor));
			}
		}
	}


	private void allow(int i) {
		waitingAllocation=i;
		
	}


	private void disallow(int i) {
		waitingDisallow=i;
		
	}


	private int getNumberOfCoreAllocated() {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean isHigher(long time){
		return (time > (StaticData.AVERAGE_TARGET*StaticData.PERCENT + StaticData.AVERAGE_TARGET));
	}
	
	public boolean isLower(long time){
		return (time < (StaticData.AVERAGE_TARGET*StaticData.PERCENT - StaticData.AVERAGE_TARGET));
	}
	
	public enum METHOD{
		LOWER,LOW,NORMAL,HIGH,HIGHER
	}



}
