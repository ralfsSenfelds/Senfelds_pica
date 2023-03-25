package Pica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PicaApplication {

	HashMap<String, String> credentials = getCredential.getCredentials();
	String username = credentials.get("Lietotājs");
	String password = credentials.get("Parole");

    public static boolean registerProfile() {
    	HashMap<String, String> credentials = getCredential.getCredentials();
        List<Map<String, String>> savedCredentialsList = readUserInfo();
        boolean isUsernameTaken = savedCredentialsList.stream()
                .anyMatch(savedCredentials -> savedCredentials.get("Lietotājs").equals(credentials.get("Lietotājs")));
        if (isUsernameTaken) {
            JOptionPane.showMessageDialog(null, "Lietotājvārds jau ir aizņemts. Lūdzu, izvēlieties citu lietotājvārdu.");
            return false;
        } else {
        	savedCredentialsList.add(credentials);
        	try (FileWriter myWriter = new FileWriter("userInfo.txt", true)) {
                myWriter.write(credentials.get("Lietotājs") + "," + credentials.get("Parole") + "\n");
                System.out.println("Dati veiksmīgi saglabāti.");
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
    	HashMap<String, String> credentials = getCredential.getCredentials();
        List<Map<String, String>> savedCredentialsList = readUserInfo();
        boolean foundMatch = false;
        for (Map<String, String> savedCredentials : savedCredentialsList) {
            if (credentials.get("Lietotājs").equals(savedCredentials.get("Lietotājs")) &&
                credentials.get("Parole").equals(savedCredentials.get("Parole"))) {
                foundMatch = true;
                break;
            }
        }
        if (foundMatch) {
            JOptionPane.showMessageDialog(frame, "Pieslēgšanās veiksmīga!");
            showLoggedInMenu(frame);
        } else {
            JOptionPane.showMessageDialog(frame, "Lietotājs vai parole nav pareiza!");
        }
    }

    static void logout() {
            JOptionPane.showMessageDialog(null, "Jūs esat veiksmīgi izgājuši no profila.");
    }
    
    public static void showLoggedInMenu(JFrame frame) {
        String[] izv2 = {"Izveidot picu", "Pasūtīt picu", "Apskatīt picu vēsturi", "Apskatīt čeku vēsturi", "Iziet no profila"};
        int izvele = JOptionPane.showOptionDialog(frame, null, "Picas izvēlne", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, izv2, izv2[0]);
        if (izvele == -1) {
            logout();
        } else {
            switch (izvele) {
                case 0:
                    
                    break;
                case 1:
                    
                    break;
                case 2:
                    
                    break;
                case 3:
                    
                    break;
                case 4:
                	logout();
                	break;
                default:
                    JOptionPane.showMessageDialog(frame, "Nepareiza izvēle!");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        String[] izv1 = {"Reģistrēt profilu", "Pieslēgties profilam", "Aizvert programmu"};
        int izvele;
        do {
            izvele = JOptionPane.showOptionDialog(null,
                       "Laipni lūgti picas aplikācijā! ", "Opcijas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, izv1, izv1[0]);
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
                    JOptionPane.showMessageDialog(null, "Nepareiza izvēle!");
                    break;
            }
        } while (izvele != 2);
    }
}