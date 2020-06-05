package exceptions;

import model.disasters.Disaster;

/*
 * A subclass of SimulationException representing an exception that occurs on disasters.
No instances of this exception can be created.
 */
@SuppressWarnings("serial")
public abstract class DisasterException extends SimulationException {

	private Disaster disaster;

	public Disaster getDisaster() {
		return disaster;
	}

	public DisasterException(Disaster disaster)

	{
		this.disaster = disaster;
	}

	public DisasterException(Disaster disaster, String message) {
		super(message);
		this.disaster = disaster;

	}
}
