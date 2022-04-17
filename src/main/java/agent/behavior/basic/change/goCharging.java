package agent.behavior.basic.change;

import agent.behavior.BehaviorChange;

public class goCharging extends BehaviorChange {
    boolean lowBatteryLevel = false;
    @Override
    public void updateChange() {
        int currGradValue = this.getAgentState().getPerception().getCellPerceptionOnRelPos(0,0).getGradientRepresentation().get().getValue();
        int STEP_ENERGY_WITHOUT_PACKET = 10;
        int BUFFER = 150;
        //low battery level is calculated based on energy needed to go to charging station (== currGradValue * STEP_ENERGY_WITHOUT_PACKET) on shortest path
        //and buffer for if agent has to wait at charging station or needs to take detour because shortest path to charging station is blocked
        lowBatteryLevel = currGradValue * STEP_ENERGY_WITHOUT_PACKET + BUFFER > this.getAgentState().getBatteryState(); 
    }
    @Override
    public boolean isSatisfied() {
        return lowBatteryLevel;
    }
}
