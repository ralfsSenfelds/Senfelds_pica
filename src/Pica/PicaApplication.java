package Pica;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PicaApplication {
	
	public static boolean registerProfile() {
	    HashMap<String, String> credentials = Credentials.getCredentials();
	    if (credentials == null) {
		    return false;
		}
	    List<Map<String, String>> existingCredentials = readUserInfo();
	    boolean isUsernameTaken = existingCredentials.stream()
	            .anyMatch(savedCredentials -> savedCredentials.get("Lietotājs").equals(credentials.get("Lietotājs")));
	    if (isUsernameTaken) {
	        JOptionPane.showMessageDialog(null, "Lietotājvārds jau ir aizņemts. Lūdzu, izvēlieties citu lietotājvārdu.");
	        return false;
	    } else {
	        existingCredentials.add(credentials);
	        try (FileWriter myWriter = new FileWriter("userInfo.txt", true)) {
	            myWriter.write(credentials.get("Lietotājs") + "," + credentials.get("Parole") + "\n");
	            JOptionPane.showMessageDialog(null, "Jūs esat veiksmīgi reģistrējies!");
	            return true;
	        } catch (IOException e) {
	            System.out.println("Ieraksta kļūda.");
	            e.printStackTrace();
	            return false;
	        }
	    }
	}

	public static List<Map<String, String>> readUserInfo() {
        List<Map<String, String>> credentialsList = new ArrayList<>();
        try (Scanner myReader = new Scanner(new File("userInfo.txt"))) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine().trim();
                if (!data.isEmpty()) {
                    String[] arr = data.split(",");
                    Map<String, String> credentials = new HashMap<>();
                    credentials.put("Lietotājs", arr[0]);
                    credentials.put("Parole", arr[1]);
                    credentialsList.add(credentials);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fails nav atrasts.");
            e.printStackTrace();
        }
        return credentialsList;
    }

    public static void login(JFrame frame) {
    	HashMap<String, String> credentials = Credentials.getCredentials();
        List<Map<String, String>> existingCredentials = readUserInfo();
        boolean foundMatch = false;
        for (Map<String, String> savedCredentials : existingCredentials) {
        	if (credentials == null) {
    		    return;
    		}
            if (credentials.get("Lietotājs").equals(savedCredentials.get("Lietotājs")) &&
                credentials.get("Parole").equals(savedCredentials.get("Parole"))) {
                foundMatch = true;
                break;
            }
        }
        if (foundMatch) {
            JOptionPane.showMessageDialog(frame, "Pieslēgšanās veiksmīga!", "Paziņojums", JOptionPane.INFORMATION_MESSAGE);
            showLoggedInMenu(frame, new ArrayList<Item>());
        } else {
            JOptionPane.showMessageDialog(frame, "Lietotājs vai parole nav pareiza!");
        }
    }
    
    public static void showLoggedInMenu(JFrame frame, ArrayList<Item> items) {
        String[] izv2 = {"Izveidot picu", "Pasūtīt picu", "Izdzēst pasūtījumu", "Iziet no profila"};
        ImageIcon icon = new ImageIcon(PicaApplication.class.getResource("picaBanner.png"));
        int izvele = JOptionPane.showOptionDialog(frame, null, "Picas izvēlne", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, izv2, izv2[0]);
            switch (izvele) {
            case 0:
            	 PizzaMenu.pizzaBuilder(frame, items);
            	 showLoggedInMenu(frame, items);
            break;
            case 1:
            	if (items.size() > 0) {
                    Item.generateReceipt(items);
                } else {
                    JOptionPane.showMessageDialog(frame, "Jums nav izveidotas picas!", "Kļūda", JOptionPane.ERROR_MESSAGE);
                }
                showLoggedInMenu(frame, items);
            break;
            case 2:
            	if (items.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Jums nav pasūtījuma ko izdzēst", "Kļūda", JOptionPane.ERROR_MESSAGE);
                } else {
                    items.clear();
                    JOptionPane.showMessageDialog(null, "Pasūtījums izdzēsts", "Paziņojums", JOptionPane.INFORMATION_MESSAGE);
                }
                showLoggedInMenu(frame, items);
            break;
            case 3:
                return;
            }
    }

    public static void main(String[] args) {
        String[] izv1 = {"Reģistrēt profilu", "Pieslēgties profilam", "Aizvērt programmu"};
        int izvele = 0;
        do {
        	JPanel panel = new JPanel(new BorderLayout());
        	ImageIcon icon = new ImageIcon(PicaApplication.class.getResource("picaIntro.png"));
        	JLabel label = new JLabel(icon);
        	panel.add(label, BorderLayout.CENTER);
        	izvele = JOptionPane.showOptionDialog(null, panel, "Opcijas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, izv1, izv1[0]);
        	switch (izvele) {
                case 0:
                    registerProfile();
                break;
                case 1:
                    login(null);
                break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Visu labu!");
                break;
                default:
                System.exit(0);
            }
        } while (izvele != 2);
    }
}