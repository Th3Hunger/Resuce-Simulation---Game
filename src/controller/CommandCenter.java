package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulator;
import view.GameView;

public class CommandCenter implements SOSListener, ActionListener {

	private Simulator engine;
	private GameView gameView;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;
	ArrayList<JButton> BuildingHelper = new ArrayList<>();
	ArrayList<JButton> CitizenHelper = new ArrayList<>();
	ArrayList<JButton> UnitsHelper = new ArrayList<>();
	JButton nextCycle = new JButton("nextCycle");
	JButton Avaunit[] = new JButton[5];
	JPanel tester = new JPanel();
	Unit unitlocal = null;
	JButton Amb = new JButton("Ambulance");
	JButton Fire = new JButton("FireTruck");
	JButton Disease = new JButton("DiseaseControlUnit");
	JButton Gas = new JButton("GasControlUnit");
	JButton Evac = new JButton("Evacuator");
	boolean lazycheck = false;
	boolean check2 = false;
	boolean messageB = false;
	boolean messageC = false;
	boolean finalMessage = false;
	private ArrayList<JButton> general = new ArrayList<>();
	private ArrayList<Citizen> deadC = new ArrayList<Citizen>();
	private ArrayList<Citizen> deadB = new ArrayList<Citizen>();
	private ArrayList<Disaster> AliveDisaster = new ArrayList<>();
	private ArrayList<Disaster> ExcDisaster = new ArrayList<>();
	private ArrayList<Unit> Unitbase = new ArrayList<>();
	private ArrayList<JButton> occupantsbutton = new ArrayList<>();
	private ArrayList<Citizen> occupants = new ArrayList<>();

	public Simulator getEngine() {
		return engine;
	}

	public ArrayList<Unit> getEmergencyUnits() {
		return emergencyUnits;
	}

	public CommandCenter() throws Exception {
		gameView = new GameView();
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		nextCycle();
		show();
		gameView.setVisible(true);

	}

	public void show() {
		avablibleUnit();
		setUnits();
		setBuilding();
		setCitizen();
		RespondUnit();
		TreatUnit();
		gameView.getGameGrid(0, 0).setForeground(Color.red);
		gameView.getGameGrid(0, 0).setText("Base");
		gameView.getGameGrid(0, 0).addActionListener(this);
		currentAndCausalites();
	}

	public void currentAndCausalites() {
		int Cau = getEngine().calculateCasualties();
		int curr = getEngine().getCurrentCycle();
		String causality = Integer.toString(Cau);
		String CurrentCycle = Integer.toString(curr);
		gameView.addcausalCurrent(causality, CurrentCycle);

	}

	public void avablibleUnit() {
		// hena ana mogrd b intialize el buttons wl image bta3thom
		JTextArea AvaUnits = new JTextArea();
		Font f = new Font("Verdana", Font.BOLD, 12);
		AvaUnits.setForeground(Color.red);
		AvaUnits.setFont(f);
		AvaUnits.setText("AvaliableUnits");
		AvaUnits.setBackground(Color.black);
		AvaUnits.setEditable(false);
		AvaUnits.setBounds(10, 10, 1000, 15);

		Amb.setBounds(10, 60, 70, 40);
		Amb.setBackground(Color.DARK_GRAY);
		Amb.addActionListener(this);
		ImageIcon imgAmbl = new ImageIcon(getClass().getResource("Ambulance.png"));
		Image newAmbl = imgAmbl.getImage().getScaledInstance(Amb.getWidth(), Amb.getHeight(), Image.SCALE_DEFAULT);
		ImageIcon image = new ImageIcon(newAmbl);
		Amb.setIcon(image);

		Disease.setBounds(90, 60, 70, 40);
		Disease.setBackground(Color.DARK_GRAY);
		Disease.addActionListener(this);
		ImageIcon imgEDis = new ImageIcon(getClass().getResource("disease.png"));
		Image newDis = imgEDis.getImage().getScaledInstance(Disease.getWidth(), Disease.getHeight(),
				Image.SCALE_SMOOTH);
		ImageIcon imageDis = new ImageIcon(newDis);
		Disease.setIcon(imageDis);

		Evac.setBackground(Color.DARK_GRAY);
		Evac.addActionListener(this);
		Evac.setBounds(190, 60, 70, 40);
		ImageIcon imgEvac = new ImageIcon(getClass().getResource("police.png"));
		Image newEvac = imgEvac.getImage().getScaledInstance(Evac.getWidth(), Evac.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon imageEvac = new ImageIcon(newEvac);

		Evac.setIcon(imageEvac);
		Fire.setBackground(Color.DARK_GRAY);
		Fire.addActionListener(this);
		Fire.setBounds(290, 60, 70, 40);
		ImageIcon imgFire = new ImageIcon(getClass().getResource("fire.png"));
		Image newFire = imgFire.getImage().getScaledInstance(Fire.getWidth(), Fire.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon imageFire = new ImageIcon(newFire);
		Fire.setIcon(imageFire);

		Gas.setBounds(10, 120, 70, 40);
		Gas.addActionListener(this);
		Gas.setBackground(Color.DARK_GRAY);

		ImageIcon imgGas = new ImageIcon(getClass().getResource("Gas.png"));
		Image newGas = imgGas.getImage().getScaledInstance(Gas.getWidth(), Gas.getHeight(), Image.SCALE_DEFAULT);
		ImageIcon imagGas = new ImageIcon(newGas);
		Gas.setIcon(imagGas);
		gameView.addUnitTxtArea(AvaUnits);
//		gameView.addUnit(Amb);
//		gameView.addUnit(Disease);
//		gameView.addUnit(Evac);
//		gameView.addUnit(Fire);
//		gameView.addUnit(Gas);

	}

	public void TreatUnit() {

		JTextArea ResTitle = new JTextArea();
		Font f = new Font("Verdana", Font.BOLD, 12);
		ResTitle.setFont(f);
		ResTitle.setText("Treating Unit");
		ResTitle.setForeground(Color.red);

		ResTitle.setBackground(Color.BLACK);
		ResTitle.setEditable(false);
		ResTitle.setBounds(10, 500, 1000, 15);
		gameView.addUnitTxtArea(ResTitle);

	}

	public void RespondUnit() {
		JTextArea ResTitle = new JTextArea();
		Font f = new Font("Verdana", Font.BOLD, 12);
		ResTitle.setFont(f);
		ResTitle.setText("Responding Unit");
		ResTitle.setForeground(Color.red);
		ResTitle.setBackground(Color.black);
		ResTitle.setEditable(false);
		ResTitle.setBounds(10, 250, 1000, 15);
		gameView.addUnitTxtArea(ResTitle);

	}

	public void nextCycle() {
		nextCycle.setBounds(100, 940, 150, 50);
		nextCycle.setText("nextCycle");
		nextCycle.addActionListener(this);
		Font f = new Font("Verdana", Font.BOLD, 20);
		nextCycle.setFont(f);
		nextCycle.setBackground(Color.gray);
		nextCycle.setForeground(Color.white);
		gameView.addUnit(nextCycle);
	}

	public void setBuilding() {
		for (ResidentialBuilding building : getVisibleBuildings()) {
			int i = building.getLocation().getX();
			int j = building.getLocation().getY();
			gameView.setTextGrid(i, j, "B");
			gameView.getGameGrid(i, j).addActionListener(this);
			if (!BuildingHelper.contains(gameView.getGameGrid(i, j))) {
				// gameView.putLog("\n" + "--" + "A " + building.DisasterType(building) + "
				// Struck the building");
				BuildingHelper.add(gameView.getGameGrid(i, j));
			}
		}
	}

	public void setCitizen() {
		for (Citizen citizen : getVisibleCitizens()) {
			int i = citizen.getLocation().getX();
			int j = citizen.getLocation().getY();
			gameView.setTextGrid(i, j, "C");
			gameView.getGameGrid(i, j).addActionListener(this);

			if (!CitizenHelper.contains(gameView.getGameGrid(i, j))) {
				// gameView.putLog("\n" + "--" + "A " + citizen.DisasterType(citizen) + " Struck
				// the Citizen");
				CitizenHelper.add(gameView.getGameGrid(i, j));
			}

		}
	}

	public void setUnits() {
		for (Unit unit : getEmergencyUnits()) {
			if (unit instanceof Ambulance) {
				JButton Ambs = new JButton("Ambulance");
				Ambs.setSize(50, 40);
				Ambs.setBackground(Color.DARK_GRAY);
				Ambs.addActionListener(this);
				ImageIcon imgAmbl = new ImageIcon(getClass().getResource("Ambulance.png"));
				Image newAmbl = imgAmbl.getImage().getScaledInstance(Ambs.getWidth(), Ambs.getHeight(),
						Image.SCALE_DEFAULT);
				ImageIcon image = new ImageIcon(newAmbl);
				Ambs.setIcon(image);
				gameView.AddAvaut(Ambs);
				UnitsHelper.add(Ambs);
			} else if (unit instanceof DiseaseControlUnit) {
				JButton Diseases = new JButton("DiseaseControlUnit");
				Diseases.setSize(50, 40);
				Diseases.setBackground(Color.DARK_GRAY);
				Diseases.addActionListener(this);
				ImageIcon imgEDis = new ImageIcon(getClass().getResource("disease.png"));
				Image newDis = imgEDis.getImage().getScaledInstance(Diseases.getWidth(), Diseases.getHeight(),
						Image.SCALE_SMOOTH);
				ImageIcon imageDis = new ImageIcon(newDis);
				Diseases.setIcon(imageDis);
				gameView.AddAvaut(Diseases);
				UnitsHelper.add(Diseases);

			} else if (unit instanceof Evacuator) {
				JButton Evacs = new JButton("Evacuator");

				Evacs.setBackground(Color.DARK_GRAY);
				Evacs.addActionListener(this);
				Evacs.setSize(50, 40);
				ImageIcon imgEvac = new ImageIcon(getClass().getResource("police.png"));
				Image newEvac = imgEvac.getImage().getScaledInstance(Evacs.getWidth(), Evacs.getHeight(),
						Image.SCALE_SMOOTH);
				ImageIcon imageEvac = new ImageIcon(newEvac);
				Evacs.setIcon(imageEvac);
				gameView.AddAvaut(Evacs);
				UnitsHelper.add(Evacs);

			} else if (unit instanceof FireTruck) {
				JButton Fires = new JButton("FireTruck");

				Fires.setBackground(Color.DARK_GRAY);
				Fires.addActionListener(this);
				Fires.setSize(50, 40);
				ImageIcon imgFire = new ImageIcon(getClass().getResource("fire.png"));
				Image newFire = imgFire.getImage().getScaledInstance(Fires.getWidth(), Fires.getHeight(),
						Image.SCALE_SMOOTH);
				ImageIcon imageFire = new ImageIcon(newFire);
				Fires.setIcon(imageFire);
				gameView.AddAvaut(Fires);
				UnitsHelper.add(Fires);

			} else if (unit instanceof GasControlUnit) {
				JButton Gass = new JButton("GasControlUnit");
				Gass.setBackground(Color.DARK_GRAY);
				Gass.addActionListener(this);
				Gass.setSize(30, 40);
				ImageIcon imgFire = new ImageIcon(getClass().getResource("gas.png"));
				Image newFire = imgFire.getImage().getScaledInstance(Gass.getWidth(), Gass.getHeight(),
						Image.SCALE_SMOOTH);
				ImageIcon imageFire = new ImageIcon(newFire);
				Gass.setIcon(imageFire);
				gameView.AddAvaut(Gass);
				UnitsHelper.add(Gass);

			}
		}
	}

	@Override
	public void receiveSOSCall(Rescuable r) {

		if (r instanceof ResidentialBuilding) {

			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);

		} else {

			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}

	}

	public void helper2() {

		for (Unit unit : getEmergencyUnits()) {
			if (unit instanceof Ambulance) {

				if (unit.getState() == UnitState.IDLE) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromTre(b);
					gameView.AddAvaut(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();

				}

			}
			if (unit instanceof FireTruck) {
				if (unit.getState() == UnitState.IDLE) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromTre(b);
					gameView.AddAvaut(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();
//					gameView.UpdateUnit(unit);

				}
			}
			if (unit instanceof GasControlUnit) {
				if (unit.getState() == UnitState.IDLE) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromTre(b);
					gameView.AddAvaut(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();
				}

			}
			if (unit instanceof DiseaseControlUnit) {
				if (unit.getState() == UnitState.IDLE) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromTre(b);
					gameView.AddAvaut(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();
				}
			}
			if (unit instanceof Evacuator) {
				if (unit.getState() == UnitState.IDLE) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromTre(b);
					gameView.AddAvaut(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();
				}

			}
		}

	}

	public void helper() {
		for (Unit unit : getEmergencyUnits()) {
			if (unit instanceof Ambulance) {
				if (unit.getState() == UnitState.TREATING) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					int i = unit.getLocation().getX();
					int j = unit.getLocation().getY();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromRes(b);
					gameView.AddTreat(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
//					this.Amb.setBounds(10, 420, 70, 40);
//					this.Amb.setVisible(true);
					gameView.getGameGrid(i, j).setText(unit.getUnitType(unit));
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();
				}

			}
			if (unit instanceof FireTruck) {
				if (unit.getState() == UnitState.TREATING) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					int i = unit.getLocation().getX();
					int j = unit.getLocation().getY();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromRes(b);
					gameView.AddTreat(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGameGrid(i, j).setText(unit.getUnitType(unit));
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();
				}
			}
			if (unit instanceof GasControlUnit) {
				if (unit.getState() == UnitState.TREATING) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					int i = unit.getLocation().getX();
					int j = unit.getLocation().getY();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromRes(b);
					gameView.AddTreat(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
					gameView.getGameGrid(i, j).setText(unit.getUnitType(unit));
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();
				}

			}
			if (unit instanceof DiseaseControlUnit) {
				if (unit.getState() == UnitState.TREATING) {
					gameView.getGamepnl().remove((gameView.getRescuepnl()));
					gameView.getGamepnl().repaint();
					int i = unit.getLocation().getX();
					int j = unit.getLocation().getY();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromRes(b);
					gameView.AddTreat(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update

					gameView.getGameGrid(i, j).setText(unit.getUnitType(unit));
					gameView.getGamepnl().add(gameView.getRescuepnl());
					gameView.getGamepnl().repaint();
				}
			}
			if (unit instanceof Evacuator) {
				if (unit.getState() == UnitState.TREATING) {
					gameView.getContentPane().remove(gameView.getRescuepnl());
					gameView.repaint();
					int i = unit.getLocation().getX();
					int j = unit.getLocation().getY();
					/// new update
					gameView.getGamepnl().remove((gameView.getUnitpnl()));
					gameView.getGamepnl().repaint();
					int index = getEmergencyUnits().indexOf(unit);
					JButton b = UnitsHelper.get(index);
					gameView.removeFromRes(b);
					gameView.AddTreat(b);
					gameView.getGamepnl().add(gameView.getUnitpnl());
					gameView.getGamepnl().revalidate();
					gameView.getGamepnl().repaint();
					/// new update
					System.out.println(i + " " + j);
					if (i == 0) {
						String r = gameView.getGameGrid(0, 0).getToolTipText();
						gameView.getGameGrid(0, 0).setToolTipText(r + " " + "Evacuator");

					} else {
						gameView.getGameGrid(i, j).setText(unit.getUnitType(unit));
					}
					gameView.add(gameView.getRescuepnl());
					gameView.repaint();
				}

			}
		}
	}

	public void baseeee() {
		for (Unit unit : getEmergencyUnits()) {
			if (general.size() == 0) {
				int index = getEmergencyUnits().indexOf(unit);
				JButton b = UnitsHelper.get(index);
				gameView.getBasepnl().add(b);
				general.add(b);
			}
			if (unit.getLocation().getX() == 0 && unit.getLocation().getY() == 0) {

				if (unit instanceof DiseaseControlUnit) {
					JButton b = new JButton("DiseaseControlUnit");
					for (JButton x : general) {
						System.out.println("ASdasdsadasds");
						if (x.getText() == b.getText()) {
							break;
						} else {
							gameView.getBasepnl().add(b);
							general.add(b);
						}
					}
				}

			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();

		if (button.getText().equals("nextCycle")) {
			if (getEngine().checkGameOver() == true) {
				JOptionPane.showMessageDialog(null, "Game Over");
				gameView.getContentPane().remove(gameView.getGamepnl());
				gameView.setContentPane(gameView.getMenu());

			}
			if (getEngine().getCurrentCycle() == 0) {
				gameView.putLog("\n" + "--Game has started you are in first Cycle..");
			}
			if (getEngine().checkGameOver() == true) {
				gameView.putLog("\n" + "--The Game has ended");
			}
			try {

				Unitbase.clear();
				ExcDisaster.clear();
				AliveDisaster.clear();
				deadB.clear();
				deadC.clear();
				occupants.clear();
				occupantsbutton.clear();
				this.engine.nextCycle();
				citoccup();
				gameView.getCitizenocc().removeAll();
				for (JButton b : occupantsbutton) {
					gameView.AddOccup(b);
				}
				Log();
				baselogg();
				gameView.UpdateBase(Unitbase);
				gameView.updateGameLog(deadB, deadC, ExcDisaster, AliveDisaster);
				setCitizen(); // need to be fixed
				setBuilding(); // need to be fixed
				helper();
				helper2();
				currentAndCausalites();

			} catch (CitizenAlreadyDeadException | BuildingAlreadyCollapsedException e1) {
				JOptionPane.showMessageDialog(null, "You Cant Treat your Target");
			}
		}
		if (button.getText().equals("Ambulance")) {
			messageC = false;
			messageB = false;
			finalMessage = false;
			int indexUnit = UnitsHelper.indexOf(button);
			Unit u = getEmergencyUnits().get(indexUnit);
			Unit unit = u;
			unitlocal = unit;
			if (unit.getState() == UnitState.IDLE) {
				// button.setVisible(false);
			}
			gameView.UpdateUnit(unit);
			// unitlocal=null;

		}
		if (button.getText().equals("Citizen")) {
			int indexUnit = occupantsbutton.indexOf(button);
			Citizen c = occupants.get(indexUnit);
			if (unitlocal != null) {
				try {
					unitlocal.respond(c);
					citizenRespond();
				} catch (IncompatibleTargetException e1) {
					if (finalMessage == false) {
						String type = unitlocal.getUnitType(unitlocal);
						JOptionPane.showMessageDialog(null, type + " " + "IncompatibleTargetException WRONG Respond");
						finalMessage = true;
					}

				} catch (CannotTreatException e2) {
					if (finalMessage == false) {
						String type = unitlocal.getUnitType(unitlocal);
						JOptionPane.showMessageDialog(null, type + " " + "CannotTreatException WRONG");
						finalMessage = true;
					}
				}
				unitlocal = null;
			}

			gameView.updateCitizen(c);

		}
		if (button.getText().equals("GasControlUnit")) {
			messageB = false;
			messageC = false;
			finalMessage = false;

			int indexUnit = UnitsHelper.indexOf(button);
			Unit u = getEmergencyUnits().get(indexUnit);
			Unit unit = u;
			unitlocal = unit;
			if (unit.getState() == UnitState.IDLE) {
				// button.setVisible(false);
			}
			gameView.UpdateUnit(unit);
		}

		if (button.getText().equals("DiseaseControlUnit")) {
			messageC = false;
			messageB = false;
			finalMessage = false;

			int indexUnit = UnitsHelper.indexOf(button);
			Unit u = getEmergencyUnits().get(indexUnit);
			Unit unit = u;
			unitlocal = unit;
			if (unit.getState() == UnitState.IDLE) {
				// button.setVisible(false);
			}
			gameView.UpdateUnit(unit);

		}

		if (button.getText().equals("Evacuator")) {
			messageB = false;
			messageC = false;
			finalMessage = false;

			int indexUnit = UnitsHelper.indexOf(button);
			Unit u = getEmergencyUnits().get(indexUnit);
			Unit unit = u;
			unitlocal = unit;
			gameView.UpdateUnit(unit);
			if (unit.getState() == UnitState.IDLE) {
				// button.setVisible(false);
			}
		}

		if (button.getText().equals("FireTruck")) {
			messageB = false;
			messageC = false;
			finalMessage = false;

			int indexUnit = UnitsHelper.indexOf(button);
			Unit u = getEmergencyUnits().get(indexUnit);
			Unit unit = u;
			unitlocal = unit;
			if (unit.getState() == UnitState.IDLE) {
				// button.setVisible(false);
			}
			gameView.UpdateUnit(unit);

		}
		if (button.getText().equals("B")) {
			int indexBuilding = BuildingHelper.indexOf(button);
			ResidentialBuilding buildz = getVisibleBuildings().get(indexBuilding);
			if (unitlocal != null) {
				try {
					unitlocal.respond(buildz);
					buildingRespond();
				} catch (IncompatibleTargetException e1) {
					if (messageB == false) {
						String type = unitlocal.getUnitType(unitlocal);
						JOptionPane.showMessageDialog(null, type + " " + "IncompatibleTargetException WRONG Respond");
						messageB = true;
					}

				} catch (CannotTreatException e2) {
					if (messageB == false) {
						String type = unitlocal.getUnitType(unitlocal);
						JOptionPane.showMessageDialog(null, type + " " + "CannotTreatException Wrong");
						messageB = true;
					}

				}
				unitlocal = null;
			}

			gameView.updateBuilding(buildz);

		}
		if (button.getText().equals("C")) {
			int indexCitizen = CitizenHelper.indexOf(button);
			Citizen citizen = getVisibleCitizens().get(indexCitizen);

			if (unitlocal != null) {
				try {
					unitlocal.respond(citizen);
					citizenRespond();
				} catch (IncompatibleTargetException e1) {
					if (messageC == false) {
						String type = unitlocal.getUnitType(unitlocal);
						JOptionPane.showMessageDialog(null, type + " " + "IncompatibleTargetException WRONG Respond");
						messageC = true;
					}

				} catch (CannotTreatException e2) {
					if (messageC == false) {
						String type = unitlocal.getUnitType(unitlocal);
						JOptionPane.showMessageDialog(null, type + " " + "CannotTreatException Target WRONG");
						messageC = true;
					}
				}
				unitlocal = null;
			}

			gameView.updateCitizen(citizen);

		}

	}

	public void citizenRespond() {
		String Unittype = unitlocal.getUnitType(unitlocal);
		if (Unittype.equals("Ambulance")) {
			gameView.getGamepnl().remove((gameView.getUnitpnl()));
			gameView.getGamepnl().repaint();
			int index = getEmergencyUnits().indexOf(unitlocal);
			JButton b = UnitsHelper.get(index);
			gameView.removefromAva(b);
			gameView.AddResut(b);
			gameView.getGamepnl().add(gameView.getUnitpnl());
			gameView.getGamepnl().revalidate();
			gameView.getGamepnl().repaint();
		}
		if (Unittype.equals("DiseaseControlUnit")) {
			gameView.getGamepnl().remove((gameView.getUnitpnl()));
			gameView.getGamepnl().repaint();
			int index = getEmergencyUnits().indexOf(unitlocal);
			JButton b = UnitsHelper.get(index);
			gameView.removefromAva(b);
			gameView.AddResut(b);
			gameView.getGamepnl().add(gameView.getUnitpnl());
			gameView.getGamepnl().revalidate();
			gameView.getGamepnl().repaint();
		}
	}

	public void buildingRespond() {
		String Unittype = unitlocal.getUnitType(unitlocal);

		if (Unittype.equals("FireTruck")) {
			gameView.getGamepnl().remove((gameView.getUnitpnl()));
			gameView.getGamepnl().repaint();
			int index = getEmergencyUnits().indexOf(unitlocal);
			JButton b = UnitsHelper.get(index);
			gameView.removefromAva(b);
			gameView.AddResut(b);
			gameView.getGamepnl().add(gameView.getUnitpnl());
			gameView.getGamepnl().revalidate();
			gameView.getGamepnl().repaint();

			this.Fire.setVisible(true);
		} else if (Unittype.equals("GasControlUnit")) {
			gameView.getGamepnl().remove((gameView.getUnitpnl()));
			gameView.getGamepnl().repaint();
			int index = getEmergencyUnits().indexOf(unitlocal);
			JButton b = UnitsHelper.get(index);
			gameView.removefromAva(b);
			gameView.AddResut(b);
			gameView.getGamepnl().add(gameView.getUnitpnl());
			gameView.getGamepnl().revalidate();
			gameView.getGamepnl().repaint();
		} else {
			gameView.getGamepnl().remove((gameView.getUnitpnl()));
			gameView.getGamepnl().repaint();
			int index = getEmergencyUnits().indexOf(unitlocal);
			JButton b = UnitsHelper.get(index);
			gameView.removefromAva(b);
			gameView.AddResut(b);
			gameView.getGamepnl().add(gameView.getUnitpnl());
			gameView.getGamepnl().revalidate();
			gameView.getGamepnl().repaint();

		}
	}

	public static void main(String[] args) throws Exception {
		new CommandCenter();

	}

	public ArrayList<ResidentialBuilding> getVisibleBuildings() {
		return visibleBuildings;
	}

	public ArrayList<Citizen> getVisibleCitizens() {
		return visibleCitizens;
	}

	public void Log() {
		for (Citizen citizen : getVisibleCitizens()) {
			if (citizen.getState() == CitizenState.DECEASED) {
				deadC.add(citizen);
			}
			if (citizen.getDisaster() != null) {

				ExcDisaster.add(citizen.getDisaster());

				if (citizen.getDisaster().isActive()) {

					AliveDisaster.add(citizen.getDisaster());
				}
			}
		}
		for (ResidentialBuilding building : getVisibleBuildings()) {
			for (Citizen citizen : building.getOccupants()) {
				if (citizen.getState() == CitizenState.DECEASED) {
					deadB.add(citizen);
				}
				if (citizen.getDisaster() != null) {
					if (citizen.getDisaster().isActive()) {

						AliveDisaster.add(citizen.getDisaster());
					}
					ExcDisaster.add(citizen.getDisaster());

				}
			}
			if (building.getDisaster() != null) {
				if (building.getDisaster().isActive() == true) {
					AliveDisaster.add(building.getDisaster());
				}
				ExcDisaster.add(building.getDisaster());
			}
		}
	}

	public void baselogg() {
		for (Unit unit : getEmergencyUnits()) {
			if (unit.getLocation().getX() == 0 && unit.getLocation().getY() == 0) {
				Unitbase.add(unit);
			}
		}
	}

	public void citoccup() {

		for (ResidentialBuilding building : getVisibleBuildings()) {
			for (Citizen citizen : building.getOccupants()) {
				JButton b = new JButton("Citizen");
				b.addActionListener(this);
				b.setSize(30, 40);
				occupantsbutton.add(b);
				occupants.add(citizen);
			}
		}
	}
}
