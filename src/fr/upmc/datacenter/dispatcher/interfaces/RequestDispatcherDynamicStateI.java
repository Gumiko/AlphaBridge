package fr.upmc.datacenter.dispatcher.interfaces;

import java.util.ArrayList;

import fr.upmc.components.interfaces.DataOfferedI;
import fr.upmc.components.interfaces.DataRequiredI;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.interfaces.TimeStampingI;

public interface RequestDispatcherDynamicStateI extends 	DataOfferedI.DataI,
DataRequiredI.DataI,
TimeStampingI{

	/***
	 * 
	 * @return
	 */
	long getAverageTime();
	
	/***
	 * 
	 * @return
	 */
	ArrayList<VMData> getVMDatas();
	
	/***
	 * 
	 * @return the number of requests
	 */
	int getNbreq();

}
