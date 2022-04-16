package agent.behavior;

import agent.AgentAction;
import agent.AgentCommunication;
import agent.AgentState;

public class BehaviorCharging extends Behavior {

    @Override
    public void communicate(AgentState agentState, AgentCommunication agentCommunication) {
    }

    
    @Override
    public void act(AgentState agentState, AgentAction agentAction) {
        agentAction.skip();
    }
}
