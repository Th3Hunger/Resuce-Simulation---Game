package exceptions;

import model.units.Unit;
import simulation.Rescuable;

/*
 * A subclass of UnitException representing an exception that occurs when the target is
not compatible with the current unit and thus it cannot handle it.
 */
@SuppressWarnings("serial")
public class IncompatibleTargetException extends UnitException {

	public IncompatibleTargetException(Unit unit, Rescuable target) {
		super(unit, target);
	}

	public IncompatibleTargetException(Unit unit, Rescuable target, String message)

	{
		super(unit, target, message);

	}

}
