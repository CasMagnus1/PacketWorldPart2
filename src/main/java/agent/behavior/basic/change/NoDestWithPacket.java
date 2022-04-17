package agent.behavior.basic.change;

import agent.behavior.BehaviorChange;

public class NoDestWithPacket extends BehaviorChange {
    private boolean hasPacket = false;
    private boolean seesDest = true;
    @Override
    public void updateChange() {
        this.hasPacket = this.getAgentState().hasCarry();
        if (this.hasPacket) {
            this.seesDest = this.getAgentState().seesDestination(this.getAgentState().getCarry().get().getColor());
        } else {
            this.seesDest = false;
        }
    }
    @Override
    public boolean isSatisfied() {
        // Decides when the Behavior change is triggered, i.e.,
        // if the agent has a packet, it will change to BehaviorTwo
        return this.hasPacket && !this.seesDest;
    }

}
