/**
 * Coitainer containing information about a sensor.
 */
public class SensorStatus
{
    private boolean drawSensor;
    private final Direction position;

    public SensorStatus(Direction position) {
	this.position = position;
	drawSensor = false;
    }

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
