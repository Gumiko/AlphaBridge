package fr.upmc.datacenter.ring.interfaces;

import java.util.List;

import fr.upmc.components.interfaces.DataOfferedI;
import fr.upmc.components.interfaces.DataRequiredI;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.interfaces.TimeStampingI;

public interface RingDynamicStateI extends 	DataOfferedI.DataI,
DataRequiredI.DataI,
TimeStampingI{

	List<VMData> getVMDataList();

}
