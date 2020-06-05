package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;

@SuppressWarnings("serial")
public class GameView extends JFrame implements ActionListener {
	private JPanel gamepnl = new JPanel(); // Head
	private JPanel Rescuepnl = new JPanel(); /// Grid
	private JPanel Infopnl = new JPanel(); // Info pnl
	private JPanel Unitpnl = new JPanel(); // Unit pnl
	private JPanel Menu = new JPanel();// extra
	private JTextArea txtinfo = new JTextArea(); //
	private JTextArea causal = new JTextArea(); // Number ofCausality
	private JTextArea current = new JTextArea(); // Number of CurrentCycle
	private JTextArea gameLog = new JTextArea(); // game-Log
	private JButton GameGrid[][] = new JButton[10][10]; // world array
	// private JButton CallAFriend = new JButton("CallAFriend"); // extra-bonus
	private JButton StartGame = new JButton("Start Game"); // extra
	private JLabel background = new JLabel(); // ...extra
	private JScrollPane Avascl;
	private JScrollPane Resscl;
	private JScrollPane Trescl;
	private JPanel Avaut = new JPanel();
	private JPanel Resut = new JPanel();
	private JPanel Treut = new JPanel();
	private JScrollPane gameLogScroll;
	private JScrollPane baseScroll;
	private JPanel basepnl = new JPanel();
	private JTextArea basetxt = new JTextArea();
	private JScrollPane infoscroll;
	private JTextArea MasterBasetxt = new JTextArea();
	private JPanel citizenocc = new JPanel();
	private JScrollPane citscroll;
	private JTextArea cittxt = new JTextArea();

	public JPanel getMenu() {
		return Menu;
	}

	public JScrollPane getCitscroll() {
		return citscroll;
	}

	public void Addbasepnl(JButton b) {
		basepnl.add(b);
	}

	public void AddAvaut(JButton b) {
		Avaut.add(b);
	}

	public void AddOccup(JButton b) {
		citizenocc.add(b);
	}

	public JPanel getCitizenocc() {
		return citizenocc;
	}

	public JPanel getBasepnl() {
		return basepnl;
	}

	public void removefromAva(JButton b) {
		Avaut.remove(b);
	}

	public void removeFromTre(JButton b) {
		Treut.remove(b);
	}

	public void removeFromRes(JButton b) {
		Resut.remove(b);
	}

	public void AddResut(JButton b) {
		Resut.add(b);
	}

	public void AddTreat(JButton b) {
		Treut.add(b);
	}

	public GameView() {
		basicSetup();
		basicRescu();
		gameLogtester();
		gridView();
		basicinfo();
		basicUnit();
		basicbase();
		makeCit();
		currentAndCausalites();
		Infopnl.setBackground(Color.DARK_GRAY);
		Unitpnl.setBackground(Color.DARK_GRAY);
		getContentPane().setBackground(Color.DARK_GRAY);
		gamepnl.setLayout(null);
		gamepnl.setBounds(0, 0, 2000, 1100);
		gamepnl.setBackground(Color.DARK_GRAY);
		gamepnl.setForeground(Color.DARK_GRAY);
		menu();
		setContentPane(Menu);

	}

	public void gameLogtester() {
		gameLog.setLayout(null);
		gameLog.setWrapStyleWord(true);
		Font f = new Font("Verdana", Font.BOLD, 22);
		gameLog.setFont(f);
		gameLog.setText("                                               Log");
		gameLog.setBounds(0, 400, 420, 600);
		gameLog.setBackground(Color.black);
		gameLog.setForeground(Color.red);
		gameLog.setEditable(false);
		Font s = new Font("Verdana", Font.BOLD, 12);
		gameLog.setFont(s);
		gamepnl.add(gameLog);
		gameLogScroll = new JScrollPane(gameLog);
		gameLogScroll.setBounds(0, 600, 420, 400);
		gameLogScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		gamepnl.add(gameLogScroll);

	}

	public void menu() {
		Menu.setLayout(null);
		Menu.setBounds(0, 0, 2000, 1100);
		background.setLayout(null);
		background.setBounds(0, 0, 2000, 1100);
		ImageIcon imgAmbl = new ImageIcon(getClass().getResource("cr7.jpg"));
		Image newAmbl = imgAmbl.getImage().getScaledInstance(background.getWidth(), background.getHeight(),
				Image.SCALE_DEFAULT);
		ImageIcon image = new ImageIcon(newAmbl);
		background.setIcon(image);
		Menu.add(background);
		Font f = new Font("Verdana", Font.BOLD, 45);
		StartGame.setFont(f);
		StartGame.setBackground(Color.BLACK);
		StartGame.setForeground(Color.red);

		StartGame.setBounds(200, 500, 350, 300);

		StartGame.addActionListener(this);
		Menu.add(StartGame);
	}

	public void putLog(String l) {
		gameLog.setText(gameLog.getText() + "\n" + l);
	}

	public void currentAndCausalites() {
		Font f = new Font("Verdana", Font.BOLD, 12);

		causal.setFont(f);
		causal.setEditable(false);
		causal.setBounds(10, 875, 500, 15);
		causal.setBackground(Color.black);
		causal.setForeground(Color.red);
		Unitpnl.add(causal);
//		CallAFriend.setLayout(null);
//		CallAFriend.setBounds(10, 800, 150, 80);
//		CallAFriend.setToolTipText("This tool is currently unavailable"); // ToolTipText bt3ml el shakl ele 3la el
		// button
		// Unitpnl.add(CallAFriend);
		current.setFont(f);
		current.setEditable(false);
		current.setBounds(10, 905, 500, 15);
		current.setBackground(Color.black);
		current.setForeground(Color.red);
		Unitpnl.add(current);

	}

	public void gridView() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JButton b = GameGrid[i][j] = new JButton();
				b.setBackground(Color.DARK_GRAY);
				addGridPnl(b);

			}
		}

	}

	public void addcausalCurrent(String causality, String Current) {
		causal.setText("Number of Causalties :" + " " + causality);
		current.setText("CurrentCycle :" + " " + Current);
		// tb3 command center

	}

	public void basicUnit() {

		Unitpnl.setLayout(null);
		Unitpnl.setBounds(1480, 0, 450, 2000);
		Avaut.setBounds(10, 30, 410, 210);
		Resut.setBounds(10, 70, 410, 210);
		Treut.setBounds(10, 120, 410, 130);

		Avaut.setBackground(Color.DARK_GRAY);
		Resut.setBackground(Color.DARK_GRAY);
		Treut.setBackground(Color.DARK_GRAY);

		Avascl = new JScrollPane(Avaut);
		Avascl.setBounds(10, 30, 410, 210);
		Avascl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		Resscl = new JScrollPane(Resut);
		Resscl.setBounds(10, 270, 410, 210);
		Resscl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		Trescl = new JScrollPane(Treut);
		Trescl.setBounds(10, 520, 410, 130);
		Trescl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		Unitpnl.add(Avascl);
		Unitpnl.add(Resscl);
		Unitpnl.add(Trescl);

		gamepnl.add(Unitpnl);

	}

	public void makeCit() {
		Font f = new Font("Verdana", Font.BOLD, 12);
		cittxt.setFont(f);
		cittxt.setText("Citizen In Building");
		cittxt.setForeground(Color.red);
		cittxt.setBackground(Color.BLACK);
		cittxt.setEditable(false);
		cittxt.setBounds(10, 680, 1000, 15);

		Unitpnl.add(cittxt);
		citizenocc.setBounds(10, 700, 410, 110);
		citizenocc.setBackground(Color.DARK_GRAY);
		citscroll = new JScrollPane(citizenocc);
		citscroll.setBounds(10, 700, 410, 150);
		citscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		Unitpnl.add(citscroll);
	}

	public void basicinfo() {
		// Infopnl.setBounds(0, 0, 420, 200);
		// Infopnl.add(txtinfo);
		Font f = new Font("Verdana", Font.BOLD, 12);
		txtinfo.setBackground(Color.black);
		txtinfo.setForeground(Color.red);
		txtinfo.setEditable(false); // mabt5lksh t3del 3leh
		txtinfo.setFont(f); // font
		txtinfo.setBounds(0, 0, 420, 200); // ma2sat
		gamepnl.add(txtinfo);
		infoscroll = new JScrollPane(txtinfo);
		infoscroll.setBounds(0, 0, 420, 200);
		infoscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		gamepnl.add(infoscroll);

	}

	public void basicbase() {

//		basepnl.setBounds(5, 300, 300, 300);
//		basepnl.setBackground(Color.DARK_GRAY);
//------------------------------------------------------------
		// bzbt shakl el head
		basetxt.setBounds(0, 210, 420, 15);
		Font f = new Font("Verdana", Font.BOLD, 12);
		basetxt.setFont(f);
		basetxt.setText("UnitsinBase");
		basetxt.setForeground(Color.red);
		basetxt.setBackground(Color.BLACK);
		basetxt.setEditable(false);
		gamepnl.add(basetxt);
//--		---------------------------------------------------------
		Font z = new Font("Verdana", Font.BOLD, 12);
		MasterBasetxt.setBackground(Color.black);
		MasterBasetxt.setForeground(Color.red);
		MasterBasetxt.setEditable(false); // mabt5lksh t3del 3leh
		MasterBasetxt.setFont(z); // font
		MasterBasetxt.setBounds(0, 300, 410, 300); // ma2sat
		gamepnl.add(MasterBasetxt);
		baseScroll = new JScrollPane(MasterBasetxt);
		baseScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		baseScroll.setBounds(0, 240, 410, 330);

		gamepnl.add(baseScroll);

	}

	public void basicRescu() {
		Rescuepnl.setLayout(new GridLayout(0, 10));
		Rescuepnl.setBounds(430, 0, 1050, 1000);

		gamepnl.add(Rescuepnl);
	}

	public void basicSetup() {
		setTitle("Rescure Simulation");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(0, 0, 2000, 1100);
	}

	public void updateCitizen(Citizen citizen) {
		String name = citizen.getName() + "\n";
		String Location = citizen.getLocation().toString() + "\n";
		String id = citizen.getNationalID() + "\n";
		String age = Integer.toString(citizen.getAge()) + "\n";
		String blood = Integer.toString(citizen.getBloodLoss()) + "\n";
		String hp = Integer.toString(citizen.getHp()) + "\n";
		String Disaster = " " + "\n";
		String state = citizen.getState().toString();
		if (citizen.getDisaster() != null) {
			if (citizen.getDisaster() instanceof Infection) {
				Disaster = "Infection";
			} else {
				Disaster = "Injury";
			}
		} else {
			Disaster = "No Disaster";
		}
		String toxcity = Integer.toString(citizen.getToxicity()) + "\n";
		String citinfo = "-----Citizen----  " + "\n" + "name:" + " " + name + "ID:" + " " + id + "Age:" + " " + age
				+ "bloodLoss:" + " " + blood + "hp:" + " " + hp + "Toxcity:" + " " + toxcity + "location:" + " "
				+ Location + "Disaster: " + Disaster + "\n" + " State : " + state;
		txtinfo.setText(citinfo);
	}

	public void updateBuilding(ResidentialBuilding building) {
		String Location = building.getLocation().toString() + "\n";
		String StrtIntg = building.getStructuralIntegrity() + "\n";
		String Firedmg = building.getFireDamage() + "\n";
		String gas = building.getGasLevel() + "\n";
		String founddmg = building.getFoundationDamage() + "\n";
		String Occup = building.getOccupants().size() + "\n";
		String Disaster = "" + "\n";
		String occinfo = building.OccuInfo(building) + "\n";
		if (building.getDisaster() != null) {
			if (building.getDisaster() instanceof Collapse) {
				Disaster = "Collapse";
			} else if (building.getDisaster() instanceof Fire) {
				Disaster = "Fire";
			} else {
				Disaster = "GasLeak";
			}
		} else {
			Disaster = "no Disaster ";
		}

		String buildinfo = "-----building----" + "\n" + "Location:" + " " + Location + "StructureIntegrity:" + " "
				+ StrtIntg + "FireDamage:" + " " + Firedmg + "GasLevel:" + " " + gas + "Foundation damage:" + " "
				+ founddmg + "Occupants Citizen:" + " " + Occup + "\n" + "Disaster:" + Disaster + "\n" + occinfo + "\n";
		txtinfo.setText(buildinfo);
	}

	public void UpdateUnit(Unit unit) {
		String ID = unit.getUnitID() + "\n";
		String type = "" + "\n";
		String evc = "" + "\n";
		String evcPassinfo = "" + "\n";
		String citizenLocation = "";
		String buildinglocation = "";
		if (unit instanceof Ambulance) {
			type = "Ambulance" + "\n";
		}
		if (unit instanceof DiseaseControlUnit) {
			type = "DiseaseControlUnit" + "\n";
		}
		if (unit instanceof Evacuator) {
			type = "Evacuator" + "\n";
			evc = "Passenger :" + Integer.toString(((Evacuator) unit).getPassengers().size()) + "\n"
					+ "Distance To Base :" + Integer.toString(((Evacuator) unit).getDistanceToBase()) + "\n"
					+ "Max Capacity:" + Integer.toString(((Evacuator) unit).getMaxCapacity());
			evcPassinfo = ((Evacuator) unit).Passinfo();
		}
		if (unit instanceof FireTruck) {
			type = "FireTruck" + "\n";
		} else if (unit instanceof GasControlUnit) {
			type = "GasControlUnit" + "\n";
		}

		String Location = unit.getLocation().toString() + "\n";
		String steps = unit.getStepsPerCycle() + "\n";
		String sate = unit.getState() + "\n";
		String target = "";
		if (unit.getTarget() != null && unit.getTarget() instanceof Citizen) {
			citizenLocation = unit.getTarget().getLocation().toString();
			target = "C" + "withLocation" + citizenLocation;
		} else if (unit.getTarget() != null) {
			buildinglocation = unit.getTarget().getLocation().toString();

			target = "B" + "withLocation" + buildinglocation;
		}
		String UnitInfo = "-----UnitInfo----" + "\n" + "Location:" + " " + Location + "ID:" + " " + ID + "Type:" + " "
				+ type + "StepsPerCycle:" + " " + steps + "UnitState:" + " " + sate + "Target:" + " " + target + "\n"
				+ evc + "\n" + evcPassinfo + "\n";
		txtinfo.setText(UnitInfo);

	}

	public JPanel getGamepnl() {
		return gamepnl;
	}

	public void setGamepnl(JPanel gamepnl) {
		this.gamepnl = gamepnl;
	}

	public void addGridPnl(JButton b) {
		Rescuepnl.add(b);
	}

	public void addInfo(JTextArea a) {
		Infopnl.add(a);
	}

	public void addUnit(JButton b) {
		Unitpnl.add(b);
	}

	public void addUnitTxtArea(JTextArea b) {
		Unitpnl.add(b);
	}

	public JPanel getUnitpnl() {
		return Unitpnl;
	}

	public JButton getGameGrid(int i, int j) {
		return GameGrid[i][j];
	}

	public void setTextGrid(int i, int j, String txt) {
		// comand center
		GameGrid[i][j].setText(txt);
		if (txt.equals("B")) {
			GameGrid[i][j].setBackground(Color.green);
		} else {
			GameGrid[i][j].setBackground(Color.cyan);

		}
	}

	public JPanel getRescuepnl() {
		return Rescuepnl;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();
		if (button.getText() == "Start Game") {
			setContentPane(gamepnl);
		}

	}

	public void updateGameLog(ArrayList<Citizen> DB, ArrayList<Citizen> DC, ArrayList<Disaster> exc,
			ArrayList<Disaster> Alive) {
		String DeadCitizen = "";
		String DeadBuilding = "";
		String excdis = "";
		String alivedis = "";
		for (Citizen citizen : DC) {
			String name = citizen.getName();
			String Location = citizen.getLocation().toString();
			DeadCitizen = DeadCitizen + "Citizen" + name + " " + "Dead with Location :" + Location + "\n";
		}
		for (Citizen citizen : DB) {
			String name5 = citizen.getName();
			String Location5 = citizen.getLocation().toString();
			DeadBuilding = DeadBuilding + "Citizen" + name5 + " " + "Dead with Location :" + Location5 + "\n";
		}
		for (Disaster d : exc) {
			String lol = "";
			String loc = "";
			if (d instanceof Fire) {

				loc = d.getTarget().getLocation().toString();
				lol = "A Fire Disaster Struck Building with Location : " + loc;

			} else if (d instanceof Collapse) {
				loc = d.getTarget().getLocation().toString();
				lol = "A Collapse Disaster Struck Building with Location : " + loc;
			} else if (d instanceof GasLeak) {
				loc = d.getTarget().getLocation().toString();
				lol = "A GasLeak Disaster Struck Building with Location : " + loc;
			} else if (d instanceof Infection) {
				loc = d.getTarget().getLocation().toString();
				lol = "A Infection Disaster Struck Citizen with Location : " + loc;
			} else {
				loc = d.getTarget().getLocation().toString();
				lol = "A Injury Disaster Struck Citizen with Location : " + loc;
			}
			excdis = excdis + "\n" + lol;

		}
		for (Disaster d : Alive) {
			String lol1 = "";
			String loc1 = "";
			if (d instanceof Fire) {

				loc1 = d.getTarget().getLocation().toString();
				lol1 = "still Fire Disaster Struck Building with Location : " + loc1;

			} else if (d instanceof Collapse) {
				loc1 = d.getTarget().getLocation().toString();
				lol1 = "still Collapse Disaster Struck Building with Location : " + loc1;
			} else if (d instanceof GasLeak) {
				loc1 = d.getTarget().getLocation().toString();
				lol1 = "still GasLeak Disaster Struck Building with Location : " + loc1;
			} else if (d instanceof Infection) {
				loc1 = d.getTarget().getLocation().toString();
				lol1 = "still Infection Disaster Struck Citizen with Location : " + loc1;
			} else {
				loc1 = d.getTarget().getLocation().toString();
				lol1 = "still Injury Disaster Struck Citizen with Location : " + loc1;
			}
			alivedis = alivedis + "\n" + lol1;

		}
		gameLog.setText("                                    GameLog" + "\n" + DeadBuilding + "\n" + DeadCitizen + "\n"
				+ "ExectuedDisater:" + "\n" + excdis + "\n" + "Alive Disaster:" + "\n" + alivedis + "\n");

	}

	public void UpdateBase(ArrayList<Unit> a) {
		String type = "";
		String finalu = "";
		for (Unit unit : a) {
			type = unit.getUnitType(unit) + "     ID:" + unit.getUnitID() + "\n";
			finalu = finalu + type;
		}
		MasterBasetxt.setText(finalu);
	}

}
