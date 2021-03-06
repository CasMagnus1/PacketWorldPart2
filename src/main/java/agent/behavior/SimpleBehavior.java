package agent.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.AgentAction;
import agent.AgentCommunication;
import agent.AgentState;
//import environment.CellPerception;
import environment.Coordinate;
//import util.MyColor;
//import environment.Perception;

public class SimpleBehavior extends Behavior {

    @Override
    public void communicate(AgentState agentState, AgentCommunication agentCommunication) {
        // No communication
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

        // Shuffle moves randomly
        Collections.shuffle(moves);

        // Check for viable moves
        for (var move : moves) {
            var perception = agentState.getPerception();
            int x = move.getX();
            int y = move.getY();

            // If the area is null, it is outside the bounds of the environment
            //  (when the agent is at any edge for example some moves are not possible)
            if (perception.getCellPerceptionOnRelPos(x, y) != null && perception.getCellPerceptionOnRelPos(x, y).isWalkable()) {
                //System.out.println("Cell is walkable");
                agentAction.step(agentState.getX() + x, agentState.getY() + y);
                //System.out.println(agentState.getName() + " " + agentState.getX() + " " + agentState.getY() + " " + agentState.getPerception().getCellPerceptionOnAbsPos(0, 0));
                return;
            } else if (perception.getCellPerceptionOnRelPos(x, y) != null && !perception.getCellPerceptionOnRelPos(x, y).isWalkable()) {
                //System.out.println("Cell is not walkable");
                if (perception.getCellPerceptionOnRelPos(x, y).containsPacket() && !agentState.hasCarry()) {
                    //System.out.println("Cell contains packet");
                    agentAction.pickPacket(agentState.getX() + x, agentState.getY() + y);
                    return;
                } else if (agentState.hasCarry() && perception.getCellPerceptionOnRelPos(x, y).containsDestination(agentState.getCarry().get().getColor())) {
                    //System.out.println("Put packet");
                    //System.out.println(agentState.getCarry().get().getColor());        
                    agentAction.putPacket(agentState.getX() + x, agentState.getY() + y);
                    return;
                } 
            }
        }

        // No viable moves, skip turn
        agentAction.skip();
    }
}
