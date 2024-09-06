package Screen;

import javax.swing.*;


public class MainMenu extends JMenuBar{

    public MainMenu() {
        initiateMenu();
        this.setVisible(true);
    }

    private void initiateMenu() {

        JMenu fichier = new JMenu("Fichier");
        JMenu option = new JMenu("Options");
        JMenu aide = new JMenu("Aide");

        JMenuItem nouveau = new JMenuItem("Nouveau");
        JMenuItem enregistrer = new JMenuItem("Enregistrer");
        JMenuItem charger = new JMenuItem("Charger");

        fichier.add(nouveau);
        fichier.add(enregistrer);
        fichier.add(charger);

        this.add(fichier);
        this.add(option);
        this.add(aide);
        this.setVisible(true);
    }
}
