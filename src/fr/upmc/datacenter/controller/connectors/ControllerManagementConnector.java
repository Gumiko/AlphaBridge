package fr.upmc.datacenter.controller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;

/**
 * The class <code>ControllerManagementConnector</code>implements a
 * connector for ports exchanging through the interface
 * <code>ControllerManagementI</code>.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
/**
 * @author Mokrane
 *
 */
public class ControllerManagementConnector extends		AbstractConnector
implements	ControllerManagementI{

	
	
	/*
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#stopSending()
	 */
	@Override
	public void stopSending() throws Exception {
		((ControllerManagementI)this.offering).stopSending();

	}

	/* 
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#startSending()
	 */
	@Override
	public void startSending() throws Exception {
		((ControllerManagementI)this.offering).startSending();

	}

	/* 
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getNextControllerUri()
	 */
	@Override
	public String getNextControllerUri() throws Exception {
		return ((ControllerManagementI)this.offering).getNextControllerUri();
	}

	/* 
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getPreviousControllerUri()
	 */
	@Override
	public String getPreviousControllerUri() throws Exception {
		return ((ControllerManagementI)this.offering).getPreviousControllerUri();
	}
	
	
	/* 
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#setNextControllerUri(String)
	 */
	@Override
	public void setNextControllerUri(String controllerManagementUri) throws Exception {
		 ((ControllerManagementI)this.offering).setNextControllerUri(controllerManagementUri);
	}

	
	
	/* 
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#setPreviousControllerUri(String)
	 */
	@Override
	public void setPreviousControllerUri(String controllerManagementUri) throws Exception {
		 ((ControllerManagementI)this.offering).setPreviousControllerUri(controllerManagementUri);
	}

	/* 
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#bindSendingDataUri(String)
	 */
	@Override
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception {
		((ControllerManagementI)this.offering).bindSendingDataUri(DataInboundPortUri);
	}
	
	/* 
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getControllerRingDataInboundPortUri()
	 */
	@Override
	public String getControllerRingDataInboundPortUri() throws Exception{
		return ((ControllerManagementI)this.offering).getControllerRingDataInboundPortUri();
	}
}
