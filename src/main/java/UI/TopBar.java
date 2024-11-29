package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

        /***/JMenuItem exporter = new JMenuItem("Exporter GCODE");

        JMenuItem recharger = new JMenuItem("Recharger le projet");

        /***/JMenuItem exit = new JMenuItem("Fermer l'application");
        /***/JMenuItem settings = new JMenuItem("Paramètres");
        /***/JMenuItem contact = new JMenuItem("Contactez nous");
        /***/JMenuItem attributionLink = new JMenuItem("À propos");
        /***/JMenuItem reset_panel = new JMenuItem("Recréer le panneau");
        /***/JMenuItem close_project = new JMenuItem("Fermer le projet");


        exporter.addActionListener(e -> {
            String path = Utils.chooseFile("Enregistrer", "ProjectGcode.gcode", TopBar.this, "Gcode files", "gcode");
            if (path != null) {
                mainWindow.getController().saveGcode(path);
            }
        });
        contact.addActionListener(e -> openEmailClient());
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


    private static void openEmailClient() {
        try {
            String recipient = "expressogloo@gmail.com";
            String subject = "Demande d'assistance";
            String body = "Bonjour,\n\nJe souhaite obtenir de l'aide concernant votre application.";
            String mailto = String.format("mailto:%s?subject=%s&body=%s",
                    recipient,
                    URLEncoder.encode(subject, StandardCharsets.UTF_8),
                    URLEncoder.encode(body, StandardCharsets.UTF_8));
            Desktop desktop = Desktop.getDesktop();
            desktop.mail(new URI(mailto));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Impossible d'ouvrir le client de messagerie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

}
