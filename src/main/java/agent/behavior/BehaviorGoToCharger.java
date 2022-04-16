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
    private boolean waitingForCharger = false;

    @Override
    public void communicate(AgentState agentState, AgentCommunication agentCommunication) {
        if (!waitingForCharger) {
            int gradValue = agentState.getPerception().getCellPerceptionOnRelPos(0,0).getGradientRepresentation().get().getValue();
            if (gradValue == 1) {
                List<Coordinate> moves = new ArrayList<>(List.of(
                    new Coordinate(1, 1), new Coordinate(-1, -1),
                    new Coordinate(1, 0), new Coordinate(-1, 0),
                    new Coordinate(0, 1), new Coordinate(0, -1),
                    new Coordinate(1, -1), new Coordinate(-1, 1)
                ));
                for (var move: moves) {
                    if (agentState.getPerception().getCellPerceptionOnRelPos(move.getX(),move.getY()) != null) {
                        if (agentState.getPerception().getCellPerceptionOnRelPos(move.getX(),move.getY()).getGradientRepresentation().get().getValue() == 0) {
                            if (agentState.getPerception().getCellPerceptionOnRelPos(move.getX(),move.getY()).containsAgent()) {
                                waitingForCharger = true;
                                agentCommunication.sendMessage(agentState.getPerception().getCellPerceptionOnRelPos(move.getX(),move.getY()).getAgentRepresentation().get(), "GO AWAY");
                                return;
                            }
                        }
                    }
                }
            } 
        }

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
                    agentAction.putPacket(agentState.getX() + x, agentState.getY() + y);
                    return;
                }
                else {
                    if (perception.getCellPerceptionOnRelPos(x, y).getGradientRepresentation().get().getValue() < gradValue) {
                        if (!perception.getCellPerceptionOnRelPos(x, y).equals(agentState.getPerceptionLastCell())) {
                            waitingForCharger = false;
                            agentAction.step(agentState.getX() + x, agentState.getY() + y);
                            return;  
                        }
                    }
                }
            }
        }
        if (!waitingForCharger) {
            for (var move : moves) {
                int x = move.getX();
                int y = move.getY();

                // If the area is null, it is outside the bounds of the environment
                //  (when the agent is at any edge for example some moves are not possible)
                if (perception.getCellPerceptionOnRelPos(x, y) != null && perception.getCellPerceptionOnRelPos(x, y).isWalkable()) {
                    if (perception.getCellPerceptionOnRelPos(x, y).getGradientRepresentation().get().getValue() == gradValue) {
                        if (!perception.getCellPerceptionOnRelPos(x, y).equals(agentState.getPerceptionLastCell())) {
                            agentAction.step(agentState.getX() + x, agentState.getY() + y);
                            return;  
                        }
                    }
                }
            }
            for (var move : moves) {
                int x = move.getX();
                int y = move.getY();

                // If the area is null, it is outside the bounds of the environment
                //  (when the agent is at any edge for example some moves are not possible)
                if (perception.getCellPerceptionOnRelPos(x, y) != null && perception.getCellPerceptionOnRelPos(x, y).isWalkable()) {
                    if (perception.getCellPerceptionOnRelPos(x, y).getGradientRepresentation().get().getValue() > gradValue) {
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
