package UI;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * The {@code TopBar} class creates a menu at the top of the application which contains various functionality and support to the user
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-09-21
 */
public class TopBar extends JMenuBar {

    private final UIConfig uiConfig = UIConfig.INSTANCE;
    private final MainWindow mainWindow;

    /**
     * Creates a new menu for the top of the application. The items of the menu can be modified in the initiateMenu function
     */
    public TopBar(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initiateMenu();
        this.setVisible(true);
    }

    /**
     * Initiate the menu with all the base item it should have
     */
    private void initiateMenu() {

        JMenu fichier = new JMenu("Fichier");
        JMenu option = new JMenu("Options");
        JMenu aide = new JMenu("Aide");

        JMenuItem nouveau = new JMenuItem("Nouveau");
        JMenuItem enregistrer = new JMenuItem("Enregistrer");
        JMenuItem enregistrerSous = new JMenuItem("Enregistrer Sous");
        JMenuItem charger = new JMenuItem("Charger");
        JMenuItem chargerRecent = new JMenuItem("Charger récent");
        JMenuItem exporter = new JMenuItem("Exporter GCODE");
        JMenuItem recharger = new JMenuItem("Recharger le projet");
        JMenuItem exit = new JMenuItem("Fermer l'application");
        JMenuItem settings = new JMenuItem("Paramètres");
        JMenuItem contact = new JMenuItem("Contactez nous");
        /***/JMenuItem attributionLink = new JMenuItem("À propos");
        /***/JMenuItem reset_panel = new JMenuItem("Recréer le panneau");
        /***/JMenuItem close_project = new JMenuItem("Fermer le projet");

        attributionLink.addActionListener(e -> mainWindow.showAttributionWindow());
        settings.addActionListener(e -> mainWindow.showOptionWindow());
        exit.addActionListener(e -> mainWindow.getFrame().dispatchEvent(new WindowEvent(mainWindow.getFrame(), WindowEvent.WINDOW_CLOSING)));
        reset_panel.addActionListener(e -> {
            mainWindow.getController().resetPanelCNC();
            mainWindow.getMiddleContent().getCutWindow().notifyObservers();
        });
        close_project.addActionListener(e -> mainWindow.showFileSelectionWindow());

        fichier.add(nouveau);
        fichier.add(enregistrer);
        fichier.add(enregistrerSous);
        fichier.add(charger);
        fichier.add(exporter);
        fichier.add(chargerRecent);
        fichier.add(recharger);
        fichier.add(close_project);

        option.add(settings);
        option.add(reset_panel);
        option.add(exit);

        aide.add(contact);
        aide.add(attributionLink);

        this.add(fichier);
        this.add(option);
        this.add(aide);
    }

}
