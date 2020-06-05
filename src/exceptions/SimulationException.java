package exceptions;

@SuppressWarnings("serial")
public abstract class SimulationException extends Exception {

	public SimulationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SimulationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

/*
 * parent class
 * Class representing a generic exception that can occur during the simulation. These
exceptions arise from any invalid action that is performed. No instances of this exception can be
created.
 */
	
}
