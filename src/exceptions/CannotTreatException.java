package exceptions;

import model.units.Unit;
import simulation.Rescuable;

/*
 * A subclass of UnitException representing an exception that occurs the unit is trying to
rescue a target that is already safe. In order to check whether a target is already safe or not the
unit should check the secondary attributes of the buildings or citizens, respectively. This check
should be done upon the unit's response to the disaster. To make this check easier, you can add
the following method to the Unit class: boolean canTreat(Rescuable r): This method checks
whether the rescuable r is safe or not.
 */
@SuppressWarnings("serial")
public class CannotTreatException extends UnitException {

	public CannotTreatException(Unit unit, Rescuable target)
	{
		super(unit,target);
	}
	public CannotTreatException(Unit unit, Rescuable target,String message)
	{
		super(unit,target,message);
	}
}
