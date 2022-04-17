package agent.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.AgentAction;
import agent.AgentCommunication;
import agent.AgentState;
import environment.Coordinate;
public class BehaviorGoToCharger extends Behavior {
    //agent stands next to charger but another agent is already charging at this station
    private boolean waitingForCharger = false;

    //if the agent stands next to a charger and another agent is already charging at the station send a message to this agent to go away
    @Override
    public void communicate(AgentState agentState, AgentCommunication agentCommunication) {
        if (!waitingForCharger) { //message not already sent
            int gradValue = agentState.getPerception().getCellPerceptionOnRelPos(0,0).getGradientRepresentation().get().getValue();
            if (gradValue == 1) { //agent stands next to a charger
                List<Coordinate> moves = new ArrayList<>(List.of(
                    new Coordinate(1, 1), new Coordinate(-1, -1),
                    new Coordinate(1, 0), new Coordinate(-1, 0),
                    new Coordinate(0, 1), new Coordinate(0, -1),
                    new Coordinate(1, -1), new Coordinate(-1, 1)
                ));
                for (var move: moves) {
                    if (agentState.getPerception().getCellPerceptionOnRelPos(move.getX(),move.getY()) != null) {
                        if (agentState.getPerception().getCellPerceptionOnRelPos(move.getX(),move.getY()).getGradientRepresentation().get().getValue() == 0) {
                            if (agentState.getPerception().getCellPerceptionOnRelPos(move.getX(),move.getY()).containsAgent()) { //another agent is charging
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

    //move in the direction of a charging station == a cell with lower gradient
    //if this is not possible move in direction of equal gradient
    //if this is also not possible move in direction of lower gradient (== away from charging station)
    @Override
    public void act(AgentState agentState, AgentAction agentAction) {
        List<Coordinate> moves = new ArrayList<>(List.of(
            new Coordinate(1, 1), new Coordinate(-1, -1),
            new Coordinate(1, 0), new Coordinate(-1, 0),
            new Coordinate(0, 1), new Coordinate(0, -1),
            new Coordinate(1, -1), new Coordinate(-1, 1)
        ));

        int gradValue = agentState.getPerception().getCellPerceptionOnRelPos(0,0).getGradientRepresentation().get().getValue();

        Collections.shuffle(moves);
        var perception = agentState.getPerception();

        for (var move : moves) {
            int x = move.getX();
            int y = move.getY();

            if (perception.getCellPerceptionOnRelPos(x, y) != null && perception.getCellPerceptionOnRelPos(x, y).isWalkable()) {
                if (agentState.hasCarry()) { //drop packet because stepping with packet costs double the energy than stepping without a packet
                    agentAction.putPacket(agentState.getX() + x, agentState.getY() + y);
                    return;
                }
                else { //try moving towards charger == try moving to cell with lower gradient
                    if (perception.getCellPerceptionOnRelPos(x, y).getGradientRepresentation().get().getValue() < gradValue) {
                        if (!perception.getCellPerceptionOnRelPos(x, y).equals(agentState.getPerceptionLastCell())) { //to avoid agents moving back and forth between two cells
                            waitingForCharger = false;
                            agentAction.step(agentState.getX() + x, agentState.getY() + y);
                            return;  
                        }
                    }
                }
            }
        }
        if (!waitingForCharger) { //if waiting for charger dont move randomly around charger
            //try moving to a cell with equal gradient (to avoid agents getting stuck when no cell with lower gradient is available)
            for (var move : moves) {
                int x = move.getX();
                int y = move.getY();

                if (perception.getCellPerceptionOnRelPos(x, y) != null && perception.getCellPerceptionOnRelPos(x, y).isWalkable()) {
                    if (perception.getCellPerceptionOnRelPos(x, y).getGradientRepresentation().get().getValue() == gradValue) {
                        if (!perception.getCellPerceptionOnRelPos(x, y).equals(agentState.getPerceptionLastCell())) {
                            agentAction.step(agentState.getX() + x, agentState.getY() + y);
                            return;  
                        }
                    }
                }
            }
            //try moving to a cell with lower gradient (to avoid agents getting stuck when no cell with lower or equal gradient is available)
            for (var move : moves) {
                int x = move.getX();
                int y = move.getY();

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
