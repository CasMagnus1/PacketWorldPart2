package agent.behavior.basic.change;

import agent.behavior.BehaviorChange;

public class Charge extends BehaviorChange {
    boolean connectedToCharger = false;
    @Override
    public void updateChange() {
        if (this.getAgentState().getPerception().getCellPerceptionOnRelPos(0, 1) != null) {
            connectedToCharger = this.getAgentState().getPerception().getCellPerceptionOnRelPos(0, 1).containsEnergyStation();
        }
        else {
            connectedToCharger = false;
        }
    }
    @Override
    public boolean isSatisfied() {
        return connectedToCharger;
    }

}
