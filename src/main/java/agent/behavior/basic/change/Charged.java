package agent.behavior.basic.change;

import agent.behavior.BehaviorChange;
import environment.EnergyValues;

public class Charged extends BehaviorChange {
    boolean batteryFull = false;
    @Override
    public void updateChange() {
        batteryFull = this.getAgentState().getBatteryState() == EnergyValues.BATTERY_MAX;
    }
    @Override
    public boolean isSatisfied() {
        return batteryFull;
    }

}
