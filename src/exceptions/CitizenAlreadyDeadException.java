package exceptions;

import model.disasters.Disaster;

/*
 * subclass of DisasterException representing an exception that occurs when the dis-
aster is striking a citizen that is already dead.
 */
@SuppressWarnings("serial")
public class CitizenAlreadyDeadException extends DisasterException {
	public CitizenAlreadyDeadException(Disaster disaster) {
		super(disaster);
	}

	public CitizenAlreadyDeadException(Disaster disaster, String message) {
		super(disaster, message);
	}
}
