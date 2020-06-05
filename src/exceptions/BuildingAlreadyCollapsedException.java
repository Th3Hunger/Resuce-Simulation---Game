package exceptions;

import model.disasters.Disaster;

/*
 * A subclass of DisasterException representing an exception that occurs when the dis-
aster is striking a building that is already collapsed.
 */
@SuppressWarnings("serial")
public class BuildingAlreadyCollapsedException extends DisasterException {
	
public BuildingAlreadyCollapsedException(Disaster disaster)
{
	super(disaster);
}
public BuildingAlreadyCollapsedException(Disaster disaster,String message)
{
	super(disaster,message);
}
}
