package Pica;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PicaApplication {
    static boolean isRegistered = false;
    
    public static Hashtable<String, String> getCredentials(JFrame frame) {
        Hashtable<String, String> credentials = new Hashtable<String, String>();

        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
        label.add(new JLabel("Lietotājs", SwingConstants.RIGHT));
        label.add(new JLabel("Parole", SwingConstants.RIGHT));
        panel.add(label, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField username = new JTextField();
        controls.add(username);
        JPasswordField password = new JPasswordField();
        controls.add(password);
        panel.add(controls, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(frame, panel, "Pieslēgties", JOptionPane.QUESTION_MESSAGE);

        credentials.put("Lietotājs", username.getText());
        credentials.put("Parole", new String(password.getPassword()));
        return credentials;
    }

    public static boolean registerProfile(boolean isNewRegistration) {
        Hashtable<String, String> credentials = getCredentials(null);
        JOptionPane.showMessageDialog(null, "Jūs esat veiksmīgi reģistrējies!");
        try {
            FileWriter myWriter = new FileWriter("userInfo.txt");
            myWriter.write(credentials.get("Lietotājs") + "," + credentials.get("Parole"));
            myWriter.close();
            System.out.println("Dati veiksmīgi saglabāti.");
        } catch (IOException e) {
            System.out.println("Ieraksta kļūda.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Hashtable<String, String> readUserInfo() {
        Hashtable<String, String> credentials = new Hashtable<String, String>();
        try {
            File myObj = new File("userInfo.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arr = data.split(",");
                credentials.put("Lietotājs", arr[0]);
                credentials.put("Parole", arr[1]);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fails nav atrasts.");
            e.printStackTrace();
        }
        return credentials;
    }

    public static void login(JFrame frame) {
        Hashtable<String, String> credentials = getCredentials(frame);
        Hashtable<String, String> savedCredentials = readUserInfo();
        if (credentials.get("Lietotājs").equals(savedCredentials.get("Lietotājs")) &&
                credentials.get("Parole").equals(savedCredentials.get("Parole"))) {
            JOptionPane.showMessageDialog(frame,
            		"Pieslēgšanās veiksmīga!");
            isRegistered = true;
        } else {
            JOptionPane.showMessageDialog(frame,
            		"Lietotājs vai parole nav pareiza!");
            isRegistered = false;
        }
    }
    
    static void logout() {
        if (isRegistered == true) {
            isRegistered = false;
            JOptionPane.showMessageDialog(null,
            		"Jūs esat veiksmīgi izgājuši no profila.");
        } else {
    		JOptionPane.showMessageDialog(null,
    				"Kluda");
    	}
    }

    public static void main(String[] args) {
        String[] izv1 = {"Reģistrēt profilu", "Pieslēgties profilam", "Iziet no profila", "Aizvert programmu"};
        String[] izv2 = {"Izveidot picu", "Pasūtīt picu", "Apskatīt picu vēsturi", "Apskatīt čeku vēsturi"};
        
        int izvele;
        do {
            izvele = JOptionPane.showOptionDialog(null,
            		"Laipni lūgti picas aplikācijā! ", "Opcijas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, izv1, izv1[0]);
            switch (izvele) {
                case 0:
                    registerProfile(true);
                    break;
                case 1:
                    login(null);
                    break;
                case 2:
                    logout();
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Visu labu!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Nepareiza izvēle!");
                    break;
            }
        } while (izvele != 3);
    }
}