package agent.behavior.basic.change;

import agent.behavior.BehaviorChange;
import environment.EnergyValues;

public class Charged extends BehaviorChange {
    boolean batteryFull = false;
    boolean otherAgentWaiting = false;
    @Override
    public void updateChange() {
        batteryFull = this.getAgentState().getBatteryState() == EnergyValues.BATTERY_MAX;
        if (this.getAgentState().getMemoryFragment("GoAwayMessageReceived") != null) {
            this.getAgentState().removeMemoryFragment("GoAwayMessageReceived");
            otherAgentWaiting = true;
        }
        else otherAgentWaiting = false;
    }
    @Override
    public boolean isSatisfied() {
        return batteryFull || otherAgentWaiting;
    }
}
