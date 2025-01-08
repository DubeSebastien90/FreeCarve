package UI;

import Common.DTO.VertexDTO;
import Common.Exceptions.InvalidFileExtensionException;
import UI.Listeners.ExportGcodeActionListener;
import UI.Listeners.LoadProjectActionListener;
import UI.Listeners.SaveProjectActionListener;
import UI.Listeners.SaveProjectAsActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.IOException;

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

        JMenuItem exporter = new JMenuItem("Exporter GCODE");

        JMenuItem recharger = new JMenuItem("Recharger le projet");

        JMenuItem exit = new JMenuItem("Fermer l'application");
        JMenuItem settings = new JMenuItem("Paramètres");
        JMenuItem contact = new JMenuItem("Contactez nous");
        JMenuItem attributionLink = new JMenuItem("À propos");
        JMenuItem guide = new JMenuItem("Guide");
        JMenuItem close_project = new JMenuItem("Fermer le projet");


        contact.addActionListener(e -> openEmailClient());
        attributionLink.addActionListener(e -> mainWindow.showAttributionWindow());
        guide.addActionListener(e->mainWindow.showHelp());
        settings.addActionListener(e -> mainWindow.showOptionWindow());
        exit.addActionListener(e -> mainWindow.getFrame().dispatchEvent(new WindowEvent(mainWindow.getFrame(), WindowEvent.WINDOW_CLOSING)));
        nouveau.addActionListener(e -> {
            mainWindow.getController().resetPanelCNC();
            mainWindow.getMiddleContent().getCutWindow().notifyObservers();
            VertexDTO v = mainWindow.getController().getPanelDTO().getPanelDimension();
            mainWindow.getMiddleContent().getConfigChoiceWindow().getRendering2DWindow().resizePanneau(v.getX(), v.getY());
            mainWindow.getMiddleContent().getConfigChoiceWindow().getRendering2DWindow().clearPoints();
        });
        enregistrer.addActionListener(new SaveProjectActionListener(mainWindow));
        enregistrerSous.addActionListener(new SaveProjectAsActionListener(mainWindow));
        charger.addActionListener(new LoadProjectActionListener(mainWindow));
        close_project.addActionListener(e -> mainWindow.showFileSelectionWindow());
        exporter.addActionListener(new ExportGcodeActionListener(mainWindow));

        fichier.add(nouveau);
        fichier.add(enregistrer);
        fichier.add(enregistrerSous);
        fichier.add(charger);
        fichier.add(exporter);
        fichier.add(recharger);
        fichier.add(close_project);

        option.add(settings);
        option.add(exit);

        aide.add(contact);
        aide.add(attributionLink);
        aide.add(guide);

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
