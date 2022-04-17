package agent.behavior;

import agent.AgentAction;
import agent.AgentCommunication;
import agent.AgentState;
import environment.Mail;

public class BehaviorCharging extends Behavior {

    //while charging listen for messages from agents waiting for the charger to go away
    @Override
    public void communicate(AgentState agentState, AgentCommunication agentCommunication) {
        for (Mail message: agentCommunication.getMessages()) {
            message.getMessage().equals("GO AWAY");
            agentState.addMemoryFragment("GoAwayMessageReceived", "dummy"); //fact that "go away" message is received is added to internal memory of agent to be accessible in Charged.java
        }
        agentCommunication.clearMessages();
    }

    //charging == skipping    
    @Override
    public void act(AgentState agentState, AgentAction agentAction) {
        agentAction.skip();
    }
}
