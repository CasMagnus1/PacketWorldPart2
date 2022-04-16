package agent.behavior;

import agent.AgentAction;
import agent.AgentCommunication;
import agent.AgentState;
import environment.Mail;

public class BehaviorCharging extends Behavior {

    @Override
    public void communicate(AgentState agentState, AgentCommunication agentCommunication) {
        for (Mail message: agentCommunication.getMessages()) {
            message.getMessage().equals("GO AWAY");
            agentState.addMemoryFragment("message", "received");
        }
        agentCommunication.clearMessages();
    }

    
    @Override
    public void act(AgentState agentState, AgentAction agentAction) {
        agentAction.skip();
    }
}
