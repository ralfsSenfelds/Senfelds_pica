package Pica;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class getCredential {
	public static HashMap<String, String> getCredentials() {
	    JTextField usernameField = new JTextField(20);
	    JPasswordField passwordField = new JPasswordField(20);
	    JPanel panel = new JPanel(new GridLayout(0, 1));
	    panel.add(new JLabel("Lietotājvārds:"));
	    panel.add(usernameField);
	    panel.add(new JLabel("Parole:"));
	    panel.add(passwordField);
	    boolean validInput = false;
	    HashMap<String, String> credentials = null;
	    while (!validInput) {
	        int result = JOptionPane.showConfirmDialog(null, panel, "Ievadiet savus datus:",
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        if (result == JOptionPane.OK_OPTION && !usernameField.getText().isEmpty() && !String.valueOf(passwordField.getPassword()).isEmpty()) {
	            credentials = new HashMap<>();
	            credentials.put("Lietotājs", usernameField.getText());
	            credentials.put("Parole", String.valueOf(passwordField.getPassword()));
	            validInput = true;
	        } else {
	            JOptionPane.showMessageDialog(null, "Lūdzu, ievadiet lietotājvārdu un paroli.");
	        }
	    }
	    return credentials;
	}
}