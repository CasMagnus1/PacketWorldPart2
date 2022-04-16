package agent.behavior.basic.change;

import agent.behavior.BehaviorChange;

public class goCharging extends BehaviorChange {
    boolean lowBatteryLevel = false;
    @Override
    public void updateChange() {
        int currGradValue = this.getAgentState().getPerception().getCellPerceptionOnRelPos(0,0).getGradientRepresentation().get().getValue();
        int STEP_ENERGY_WITHOUT_PACKET = 10;
        int BUFFER = 150;
        lowBatteryLevel = currGradValue * STEP_ENERGY_WITHOUT_PACKET + BUFFER > this.getAgentState().getBatteryState(); 
    }
    @Override
    public boolean isSatisfied() {
        return lowBatteryLevel;
    }
}
