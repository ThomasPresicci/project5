import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HammingPanel extends JPanel implements ActionListener, ChangeListener {

	private static final long serialVersionUID = -4909498016783544854L;
	
	private JSlider hammingDistSlide;
	private JLabel hammingDistLabel;
	private JTextField hammingDistField;
	private JButton showStation;
	private JTextArea stationArea;
	private JScrollPane scrollPane;
	private JLabel stationListLabel;
	private JComboBox<String> stationList;
	private JButton calculateButton;
	private JLabel distanceZeroLabel;
	private JLabel distanceOneLabel;
	private JLabel distanceTwoLabel;
	private JLabel distanceThreeLabel;
	private JLabel distanceFourLabel;
	private JTextField distanceZeroText;
	private JTextField distanceOneText;
	private JTextField distanceTwoText;
	private JTextField distanceThreeText;
	private JTextField distanceFourText;
	private JButton addStationButton;
	private JTextField addStationInput;
	private InputStream imagePath;
	private BufferedImage image;
	private JLabel imageLabel;
	
	private int[] cityDistanceCount = new int[5];
	private String[] mesonetData;
	private String[] distance0 = new String[250];
	private String[] distance1 = new String[250];
	private String[] distance2 = new String[250];
	private String[] distance3 = new String[250];
	private String[] distance4 = new String[250];
	
	private final int HAMMING_DIST_INIT = 2;
	private final int HAMMING_DIST_MAX = 4;
	private final int HAMMING_DIST_MIN = 1;
	
	public HammingPanel() {
		super(new GridBagLayout());		
		GridBagConstraints layoutConst = null;
		
		hammingDistLabel = new JLabel("Enter Hamming Dist: ");
		hammingDistField = new JTextField(11);
		hammingDistField.setText("" + HAMMING_DIST_INIT);
		hammingDistField.setEditable(false);
		
		hammingDistSlide = new JSlider(HAMMING_DIST_MIN, HAMMING_DIST_MAX);
		hammingDistSlide.setPaintTicks(true);
		hammingDistSlide.setPaintLabels(true);
		hammingDistSlide.setMinorTickSpacing(1);
		hammingDistSlide.addChangeListener(this);
		Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
		position.put(1, new JLabel("1"));
		position.put(2, new JLabel("2"));
		position.put(3, new JLabel("3"));
		position.put(4, new JLabel("4"));
		hammingDistSlide.setLabelTable(position);
		
		showStation = new JButton("Show Station");
		showStation.addActionListener(this);
		
		stationArea = new JTextArea(15, 20);
		scrollPane = new JScrollPane(stationArea);
		stationArea.setEditable(false);
		
		try {
			mesonetData = readMesonet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		stationListLabel = new JLabel("Compare with: ");
		stationList = new JComboBox<String>(mesonetData);
		
		calculateButton = new JButton("Calculate HD");
		calculateButton.addActionListener(this);
		
		distanceZeroLabel = new JLabel("Distance 0");
		distanceOneLabel = new JLabel("Distance 1");
		distanceTwoLabel = new JLabel("Distance 2");
		distanceThreeLabel = new JLabel("Distance 3");
		distanceFourLabel = new JLabel("Distance 4");
		
		distanceZeroText = new JTextField(9);
		distanceOneText = new JTextField(9);
		distanceTwoText = new JTextField(9);
		distanceThreeText = new JTextField(9);
		distanceFourText = new JTextField(9);
		distanceZeroText.setEditable(false);
		distanceOneText.setEditable(false);
		distanceTwoText.setEditable(false);
		distanceThreeText.setEditable(false);
		distanceFourText.setEditable(false);
		
		addStationButton = new JButton("Add Station");
		addStationButton.addActionListener(this);
		
		addStationInput = new JTextField(9);
		addStationInput.setEditable(true);
		addStationInput.setText("ZERO");
		
		try {
			image = ImageIO.read(new File("image.jpeg"));
			imageLabel = new JLabel(new ImageIcon(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(0, 0, 0, 0);
		layoutConst.weightx = 100;
		layoutConst.gridx = 0;
		layoutConst.gridy = 1;
		add(hammingDistLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(0, 240, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 1;
		add(hammingDistField, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(5, 120, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 2;
		add(hammingDistSlide, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 0, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 3;
		add(showStation, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(15, 140, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 4;
		add(scrollPane, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(35, 0, 0, 15);
		layoutConst.gridx = 0;
		layoutConst.gridy = 5;
		add(stationListLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(35, 265, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 5;
		add(stationList, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 6, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 6;
		add(calculateButton, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 0, 0, 0);
		layoutConst.gridwidth = 50;
		layoutConst.gridx = 0;
		layoutConst.gridy = 7;
		add(distanceZeroLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 0, 0, 0);
		layoutConst.gridwidth = 50;
		layoutConst.gridx = 0;
		layoutConst.gridy = 8;
		add(distanceOneLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 0, 0, 0);
		layoutConst.gridwidth = 50;
		layoutConst.gridx = 0;
		layoutConst.gridy = 9;
		add(distanceTwoLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 0, 0, 0);
		layoutConst.weightx = 50;
		layoutConst.gridx = 0;
		layoutConst.gridy = 10;
		add(distanceThreeLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 0, 0, 0);
		layoutConst.weightx = 50;
		layoutConst.gridx = 0;
		layoutConst.gridy = 11;
		add(distanceFourLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 250, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 7;
		add(distanceZeroText, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 250, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 8;
		add(distanceOneText, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 250, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 9;
		add(distanceTwoText, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 250, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 10;
		add(distanceThreeText, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 250, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 11;
		add(distanceFourText, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 0, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 12;
		add(addStationButton, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(20, 250, 0, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 12;
		add(addStationInput, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(0, 120, 25, 0);
		layoutConst.gridx = 0;
		layoutConst.gridy = 0;
		add(imageLabel, layoutConst);
		
		setPreferredSize(new Dimension(400, 1000));
	}
	
	private static String[] readMesonet() throws IOException {		    
		String[] stations = new String[250];
		int index = 0;
		try {
			File filename = new File("Mesonet.txt");
			BufferedReader br = new BufferedReader(new FileReader(filename));
		      
			String line;
			  
			while ((line = br.readLine()) != null) {
				stations[index] = line;
				index++;
			}
			br.close();

		}
		catch (IOException e) {
			System.out.print("Mesonet.txt file not found.");
		}
		    
		String[] returnStations = new String[index];
		for (int i = 0; i < index; i++) {
			returnStations[i] = stations[i];
		}
		return returnStations;
	}
	
	private void calculateHammingTotals(String city) throws IOException {
		String[] mesonetData = readMesonet();
		
		cityDistanceCount = new int[5];
		distance0 = new String[250];
		distance1 = new String[250];
		distance2 = new String[250];
		distance3 = new String[250];
		distance4 = new String[250];
		
		for (String otherCity : mesonetData) {
			int count = 0;
			for (int index = 0; index < otherCity.length(); index++) {
				if (otherCity.charAt(index) != city.charAt(index))
					count++;
			}
			cityDistanceCount[count] += 1;
			if (count == 0)
				distance0[cityDistanceCount[count] - 1] = otherCity;
			if (count == 1)
				distance1[cityDistanceCount[count] - 1] = otherCity;
			if (count == 2)
				distance2[cityDistanceCount[count] - 1] = otherCity;
			if (count == 3)
				distance3[cityDistanceCount[count] - 1] = otherCity;
			if (count == 4)
				distance4[cityDistanceCount[count] - 1] = otherCity;
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String userInput = "";
		userInput = (String)stationList.getSelectedItem();
		
		try {
			calculateHammingTotals(userInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JButton source = (JButton)event.getSource();
		if (source == showStation) {
			stationArea.setText("");
			switch (hammingDistSlide.getValue()) {
			case 1:
				for (int index = 0; index < cityDistanceCount[1]; index++)
					stationArea.append(distance1[index] + "\n");
				break;
			case 2:
				for (int index = 0; index < cityDistanceCount[2]; index++)
					stationArea.append(distance2[index] + "\n");
				break;
			case 3:
				for (int index = 0; index < cityDistanceCount[3]; index++)
					stationArea.append(distance3[index] + "\n");
				break;
			case 4:
				for (int index = 0; index < cityDistanceCount[4]; index++)
					stationArea.append(distance4[index] + "\n");
				break;	
			}
		}
		if (source == calculateButton) {
			distanceZeroText.setText(String.valueOf(cityDistanceCount[0]));
			distanceOneText.setText(String.valueOf(cityDistanceCount[1]));
			distanceTwoText.setText(String.valueOf(cityDistanceCount[2]));
			distanceThreeText.setText(String.valueOf(cityDistanceCount[3]));
			distanceFourText.setText(String.valueOf(cityDistanceCount[4]));
		}
		if (source == addStationButton) {
			String inputText = addStationInput.getText();
			inputText = inputText.substring(0, 4);
			List<String> mesonetList = Arrays.asList(mesonetData);
			if (!mesonetList.contains(inputText)) {	//Prevents duplication
				System.out.println(inputText);
				int newCount = mesonetData.length + 1;
				String[] newMesonetData = new String[newCount];
				for (int index = 0; index < mesonetData.length; index++)
					newMesonetData[index] = mesonetData[index];
				newMesonetData[mesonetData.length] = inputText;
				mesonetData = newMesonetData;
				System.out.println(mesonetData);
				Arrays.sort(mesonetData);	//Sorts the new names
				DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(mesonetData);
				stationList.setModel(model);
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		JSlider source = (JSlider)event.getSource();
		if (source == hammingDistSlide) {
			int value = hammingDistSlide.getValue();
            hammingDistField.setText("" + value);
		}
	}
}
