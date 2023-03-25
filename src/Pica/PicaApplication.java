package Pica;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PicaApplication {
    static boolean isRegistered = false;

    public static HashMap<String, String> getCredentials(JFrame frame) {
        HashMap<String, String> credentials = new HashMap<String, String>();

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
        HashMap<String, String> credentials = getCredentials(null);
        try {
            FileWriter myWriter = new FileWriter("userInfo.txt");
            myWriter.write(credentials.get("Lietotājs") + "," + credentials.get("Parole"));
            myWriter.close();
            System.out.println("Dati veiksmīgi saglabāti.");
            JOptionPane.showMessageDialog(null, "Jūs esat veiksmīgi reģistrējies!");
            return true;
        } catch (IOException e) {
            System.out.println("Ieraksta kļūda.");
            e.printStackTrace();
            return false;
        }
    }


    public static List<Map<String, String>> readUserInfo() {
        List<Map<String, String>> credentialsList = new ArrayList<>();
        try {
            File myObj = new File("userInfo.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arr = data.split(",");
                Map<String, String> credentials = new HashMap<>();
                credentials.put("Lietotājs", arr[0]);
                credentials.put("Parole", arr[1]);
                credentialsList.add(credentials);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fails nav atrasts.");
            e.printStackTrace();
        }
        return credentialsList;
    }

    public static void login(JFrame frame) {
        HashMap<String, String> credentials = getCredentials(frame);
        HashMap<String, String> savedCredentials = readUserInfo();
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