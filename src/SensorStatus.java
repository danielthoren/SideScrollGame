/**
 * Coitainer containing information about a sensor.
 */
public class SensorStatus
{
    private boolean drawSensor;
    private final Direction position;

    /**
     * Initializes a container for values held by sensors.
     * @param position The relative position of the sensor (relative to the body its attached to)
     */
    public SensorStatus(Direction position) {
	this.position = position;
	drawSensor = false;
    }

    /**
     * If 'drawSensor' is true, the sensors will be drawn
     * @param sensor the boolean flag that will set the 'drawSensor' variable
     */
    public void setDrawSensor(boolean sensor){
	this.drawSensor = sensor;
    }

    public boolean isDrawSensor() {
	return drawSensor;
    }

    public Direction getPosition() {
	return position;
    }
}
