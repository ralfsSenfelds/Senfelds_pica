package pica;

import javax.swing.JOptionPane;

public class pica {
    static boolean reg = false;
	
    public static boolean regProfilu(boolean newReg) {
        JOptionPane.showMessageDialog(null, "Esat reģistrējies");
        return true;
    }
	
    static void pieProfilam() {
        if (reg == true)
            JOptionPane.showMessageDialog(null, "Esat pieslēdzies");
        else
            JOptionPane.showMessageDialog(null, "Neesat izveidojis profilu");
    }

    public static void main(String[] args) {
        String[] izv1 = {"Reģistrēt profilu", "Pieslēgties profilam", "Aizvert programmu"};
        String[] izv2 = {"Izveidot picu", "Pasūtīt picu", "Apskatīt picu vēsturi", "Apskatīt čeku vēsturi"};
        
        int izvele;
        do {
            izvele = JOptionPane.showOptionDialog(null,
            		"Laipni lūgti picas aplikācijā! ", "Opcijas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, izv1, izv1[0]);
            switch (izvele) {
                case 0:
                    reg = regProfilu(reg);
                    break;
                case 1:
                    pieProfilam();
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Visu labu!");
                    break;
            }
        } while (izvele != 2);
    }
}