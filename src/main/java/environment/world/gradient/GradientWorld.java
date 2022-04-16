package environment.world.gradient;

import java.util.Collection;
import java.util.List;

import com.google.common.eventbus.EventBus;

import environment.World;
import util.Distances;

public class GradientWorld extends World<Gradient> {

    /**
     * Initialize the GradientWorld
     */
    public GradientWorld(EventBus eventBus) {
        super(eventBus);
    }


    /**
     * Place a collection of gradients inside the Gradient World.
     *
     * @param gradients The collection of gradients.
     */
    @Override
    public void placeItems(Collection<Gradient> gradients) {
        gradients.forEach(this::placeItem);
    }

    /**
     * Place a single gradient in the Gradient World.
     *
     * @param item The gradient.
     */
    @Override
    public void placeItem(Gradient item) {
        putItem(item);
    }

    public void addGradientCircle(int startX, int startY) {
        for (List<Gradient> gradients : this.getItems()) {
            for (Gradient grad : gradients) {
                int currentValue = grad.getValue();
                int circleValue = Distances.numberOfSteps(startX, startY, grad.getX(), grad.getY());
                Gradient newGrad = new Gradient(grad.getX(), grad.getY(), Math.min(currentValue, circleValue));
                this.placeItem(newGrad);
            }
        }
    }
}
