package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public String getUnitType(Unit unit) {
		String result = "";
		if (unit instanceof Ambulance) {
			result = "Ambulance";
		}
		if (unit instanceof DiseaseControlUnit) {
			result = "DiseaseControlUnit";
		}
		if (unit instanceof Evacuator) {
			result = "Evacuator";
		}
		if (unit instanceof FireTruck) {
			result = "FireTruck";
		} else if (unit instanceof GasControlUnit) {
			result = "GasControlUnit";
		}
		return result;

	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof Citizen) {
			throw new IncompatibleTargetException(this, r, "Incompatitable Target Exception");
		}

		if (!canTreat(r)) {
			throw new CannotTreatException(this, r, "Cant Treat Exception");
		}

		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX()) + Math.abs(t.getY() - location.getY());

	}

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;
	}

	public boolean canTreat(Rescuable r) {
		if (r instanceof ResidentialBuilding) {
			ResidentialBuilding building = (ResidentialBuilding) r;
			if (this instanceof GasControlUnit) {
				if (building.getGasLevel() == 0) {
					return false;
				}
			}
			if (this instanceof FireTruck) {
				if (building.getFireDamage() == 0) {
					return false;
				}
			}
			if (this instanceof Evacuator) {
				if (building.getFoundationDamage() == 0) {
					return false;
				}
			}

		}

		return true;

	}

	public String getInfo(Unit unit) {
//		String ID = unit.getUnitID() + "\n";
		String type = unit.getState().toString();
//		if (unit instanceof Ambulance) {
//			type = "Ambulance" + "\n";
//		}
//		if (unit instanceof DiseaseControlUnit) {
//			type = "DiseaseControlUnit" + "\n";
//		}
//		if (unit instanceof Evacuator) {
//			type = "Evacuator" + "\n";
//		}
//		if (unit instanceof FireTruck) {
//			type = "FireTruck" + "\n";
//		} else if (unit instanceof GasControlUnit) {
//			type = "GasControlUnit" + "\n";
//		}

//		String Location = unit.getLocation().toString() + "\n";
//		String steps = unit.getStepsPerCycle() + "\n";
//		String sate = unit.getState() + "\n";
//		String target = "";
//		if (unit.getTarget() instanceof Citizen) {
//			target = "C";
//		} else {
//			target = "B";
//		}
//		String UnitInfo = "-----UnitInfo----" + "\n" + "Location:" + " " + Location + "ID:" + " " + ID + "Type:" + " "
//				+ type + "StepsPerCycle:" + " " + steps + "UnitState:" + " " + sate + "Target:" + " " + target + "\n";
		return type;
	}

}
