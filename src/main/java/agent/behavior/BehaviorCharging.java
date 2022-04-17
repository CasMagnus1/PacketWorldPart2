package agent.behavior;

import agent.AgentAction;
import agent.AgentCommunication;
import agent.AgentState;
import environment.Mail;

public class BehaviorCharging extends Behavior {

    @Override
    public void communicate(AgentState agentState, AgentCommunication agentCommunication) {
        for (Mail message: agentCommunication.getMessages()) {
            if (message.getMessage().equals("GO AWAY")) {
                System.out.println("RECEIVED A GO AWAY MESSAGE; STORED IN MEMORY");
                agentState.addMemoryFragment("go", "received");
            }
        }
        agentCommunication.clearMessages();
        System.out.println("ALL MESSAGES CLEARED FROM QUEUE");
    }

    
    @Override
    public void act(AgentState agentState, AgentAction agentAction) {
        agentAction.skip();
    }
}
