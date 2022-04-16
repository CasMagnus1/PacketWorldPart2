package environment.world.gradient;

import java.util.Collection;

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
        int w = this.getEnvironment().getWidth();
        int h = this.getEnvironment().getHeight();
        for(int i = 0; i < w; i++) {
            for(int j=0; j < h; j++) {
                int circleValue = Distances.numberOfSteps(startX, startY-1, i, j);
                Gradient currentGrad = this.getItem(i,j);
                int newValue;
                if(currentGrad==null) {
                    newValue = circleValue;
                } else {
                    newValue = Math.min(circleValue, currentGrad.getValue());
                }
                Gradient newGrad = new Gradient(i,j,newValue);
                this.placeItem(newGrad);
            }
        }
    }
}
