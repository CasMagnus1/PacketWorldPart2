package agent.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.AgentAction;
import agent.AgentCommunication;
import agent.AgentState;
import environment.Coordinate;
//import util.MyColor;

public class BehaviorGoToCharger extends Behavior {

    @Override
    public void communicate(AgentState agentState, AgentCommunication agentCommunication) {
    }

    
    @Override
    public void act(AgentState agentState, AgentAction agentAction) {
        // Potential moves an agent can make (radius of 1 around the agent)
        List<Coordinate> moves = new ArrayList<>(List.of(
            new Coordinate(1, 1), new Coordinate(-1, -1),
            new Coordinate(1, 0), new Coordinate(-1, 0),
            new Coordinate(0, 1), new Coordinate(0, -1),
            new Coordinate(1, -1), new Coordinate(-1, 1)
        ));

        int gradValue = agentState.getPerception().getCellPerceptionOnRelPos(0,0).getGradientRepresentation().get().getValue();

        // Shuffle moves randomly
        Collections.shuffle(moves);
        var perception = agentState.getPerception();


        // Check for viable moves
        for (var move : moves) {
            int x = move.getX();
            int y = move.getY();

            // If the area is null, it is outside the bounds of the environment
            //  (when the agent is at any edge for example some moves are not possible)
            if (perception.getCellPerceptionOnRelPos(x, y) != null && perception.getCellPerceptionOnRelPos(x, y).isWalkable()) {
                if (agentState.hasCarry()) {
                    if (perception.getCellPerceptionOnRelPos(x, y).getGradientRepresentation().get().getValue() > gradValue) {
                        agentAction.putPacket(agentState.getX() + x, agentState.getY() + y);
                        return;  
                    }
                }
                else {
                    if (perception.getCellPerceptionOnRelPos(x, y).getGradientRepresentation().get().getValue() < gradValue) {
                        agentAction.step(agentState.getX() + x, agentState.getY() + y);
                        return;  
                    }
                }
            }
        }

        // No viable moves, skip turn
        agentAction.skip();
    }

}
