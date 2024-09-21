package UI;

import javax.swing.*;


public class TopBar extends JMenuBar {

    UIConfig uiConfig = UIConfig.INSTANCE;

    /**
     * Creates a new menu for the top of the application. The items of the menu can be modified in the initiateMenu function
     */
    public TopBar() {
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
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("redo");
        JMenuItem attributionLink = new JMenuItem("Icon Source");

        fichier.add(nouveau);
        fichier.add(enregistrer);
        fichier.add(enregistrerSous);
        fichier.add(charger);
        fichier.add(exporter);
        fichier.add(chargerRecent);
        fichier.add(recharger);

        option.add(undo);
        option.add(redo);
        option.add(settings);
        option.add(exit);

        aide.add(contact);

        // Je vais marquer les liens ici, on est obligé de les mettre a quelque part dans l'application finale,
        // je pensais peut-être faire un pdf avec tous les liens dedans que quand tu cliques sur cet item dans le menu ca t'ouvre le pdf
        // https://www.svgrepo.com/svg/525834/diskette
        // https://www.svgrepo.com/svg/525562/undo-left-round
        // https://www.svgrepo.com/svg/525566/undo-right-round
        // https://www.svgrepo.com/svg/526331/structure
        // https://www.svgrepo.com/svg/525420/magnet
        // https://www.svgrepo.com/svg/525558/trash-bin-trash
        // https://www.svgrepo.com/svg/525496/scale
        // https://www.svgrepo.com/svg/526006/magnifer-zoom-in
        // https://www.svgrepo.com/svg/526007/magnifer-zoom-out
        // https://www.svgrepo.com/svg/526081/pen
        // https://www.svgrepo.com/svg/526221/settings
        // https://www.svgrepo.com/svg/525346/forbidden-circle
        // https://www.svgrepo.com/svg/526012/map-arrow-right
        // https://www.svgrepo.com/svg/526011/map-arrow-left
        //
        // ALL can be found in this portfolio
        // https://www.svgrepo.com/collection/solar-bold-icons/


        // https://www.svgrepo.com/svg/379356/border-all
        // https://www.svgrepo.com/svg/379093/border-horizontal
        // https://www.svgrepo.com/svg/379254/select-all
        // https://www.svgrepo.com/svg/379153/flip-to-front
        // https://www.svgrepo.com/svg/379154/flip-to-back
        //
        // ceux juste en haut font partie du portfolio suivant :
        // https://www.svgrepo.com/collection/tetrisly-interface-icons/
        aide.add(attributionLink);

        this.add(fichier);
        this.add(option);
        this.add(aide);
    }
}
