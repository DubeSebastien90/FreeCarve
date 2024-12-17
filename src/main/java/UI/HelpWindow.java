package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HelpWindow extends JScrollPane {

    OptionWrapper optionWrapper;
    JPanel panel;

    public HelpWindow(MainWindow mainWindow) {
        init(mainWindow);
    }

    private void init(MainWindow mainWindow) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(null);

        JLabel titleLabel = new JLabel("Guide et Questions/Réponses");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton returnButton1 = new JButton("Revenir à l'application principale");
        returnButton1.setFont(new Font("Arial", Font.BOLD, 14));
        returnButton1.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton1.addActionListener(e -> {
            mainWindow.showTrueMode();
        });
        panel.add(returnButton1);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel debuter = createGroupPanel("Comment débuter avec l'application?");
        panel.add(debuter);
        JLabel debuterMarkdown = createMarkdownContent("""
                L'application se compose de 3 fenêtres principales : 
                 1. <b>Configuration</b> : Une fenêtre dédiée à la modification des outils utilisés, des dimensions du panneau et des paramètres de la grille/aimant
                 2. <b>Coupes</b> : Une fenêtre dédiée à l'ajout, la modification et la supression de coupes
                 3. <b>Exportation</b> : Une fenêtre dédiée à la visualisation de la plance résultante et de la modification des paramètres d'exports
                 
                 Il est possible de se déplacer au travers les différentes fenêtres grâce à la Timeline dans le <b>bas - au centre</b> de l'application.
                
                """);
        debuter.add(debuterMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel modifierOutil = createGroupPanel("Comment modifier les outils de ma CNC ?");
        panel.add(modifierOutil);
        JLabel modifierOutilMarkdown = createMarkdownContent("""
                   <ul>
                        <li>Accédez à la <strong>fenêtre de configuration</strong>.</li>
                        <li>Dans la partie de droite, une série de <strong>12 items</strong> sont présents, représentant les outils de votre machine.</li>
                        <li>Un <strong>outil par défaut</strong> est déjà présent.</li>
                        <li>Pour <strong>ajouter un nouvel outil</strong> :
                            <ul>
                                <li>Choisissez un <strong>emplacement</strong> pour le nouvel outil.</li>
                                <li>Définissez un <strong>nom</strong> pour l'outil.</li>
                                <li>Indiquez un <strong>diamètre supérieur à 0</strong> (sinon l'outil ne sera pas enregistré).</li>
                            </ul>
                        </li>
                        <li>Pour <strong>supprimer un outil</strong> :
                            <ul>
                                <li>Sélectionnez l'outil à supprimer.</li>
                                <li>Pressez le bouton en forme de <strong>poubelle</strong>.</li>
                                <li><em>Remarque :</em> Il est impossible de supprimer un outil si c'est le dernier.</li>
                            </ul>
                        </li>
                    </ul>
                """);
        modifierOutil.add(modifierOutilMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel creerCoupe = createGroupPanel("Comment créer des coupes?");
        panel.add(creerCoupe);
        JLabel creerCoupeMarkdown = createMarkdownContent("""
                <h2> Coupe Horizontale: </h2>
                1. Sélectionnez le point de réference de la coupe. Doit être sur une bordure ou une autre coupe.
                2. Sélectionnez le premier point de la coupe et faites un clic gauche avec votre souris.
                3. Sélectionnez le deuxième point pour la coupe et faites un clic gauche pour confirmer la création.
                
                <h2> Coupe Verticale: </h2>
                Comme la coupe horizontale, mais le deuxième point à sélectionner devra être sur la même axe Y.
                
                <h2> Coupe Libre: </h2>
                1. Sélectionnez le point de réference, qui est aussi le premier point.
                2. Sélectionnez le deuxième point.
                
                <h2> Coupe en L: </h2>
                1. Sélectionnez l'intersection entre 2 coupes.
                2. Sélectionnez la dimension de la coupe et confirmez la création.
                
                <h2> Coupe rectangulaire: </h2>
                1. Sélectionnez le point de réference, il doit être une intersection.
                2. Sélectionnez le premier coin du rectangle.
                3. Sélectionnez le deuxième point du rectangle.
                
                <h2> Coupe de bordure: </h2>
                1. Sélectionnez une bordure et la coupe sera créée.
                
                <h2> Zone Interdite: </h2>
                1. Sélectionnez le premier point du rectangle.
                2. Sélectionnez le deuxième point du rectangle.
                
                <h1> Information importante: </h1>
                Si un point est rouge, cela signifie qu'il est invalide.
                Si un point est vert, le point est valide et peut être utilisé.
                Si un point est jaune, cela signifie qu'il s'est attaché à la ligne la plus près.
                Si vous voulez annuler une coupe, vous pouvez faire un clic droit et la création va s'arrêter.
               
                """);
        creerCoupe.add(creerCoupeMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel modifierTaille = createGroupPanel("Comment modifier la taille de mon panneau?");
        panel.add(modifierTaille);
        JLabel modifierTailleMarkdown = createMarkdownContent("""
                 <ul>
                        <li>Rendez-vous dans la <strong>fenêtre de configuration</strong>.</li>
                        <li>Cliquez sur votre panneau <em>ou</em> sélectionnez le bouton pour changer la taille du panneau dans la barre de gauche pour effectuer le redimensionnement.</li>
                        <li>Utilisez le <strong>panneau attribut à droite</strong> pour entrer les dimensions.</li>
                        <li>Il est également possible de modifier les paramètres de la grille à partir de cet endroit.</li>
                        <li>Des <strong>points apparaîtront aux coins de votre panneau</strong> lorsque vous le modifiez :</li>
                        <li>Sélectionnez le point en haut à droite pour une modification de taille à main levée.</li>
                    </ul>
                """);
        modifierTaille.add(modifierTailleMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));


        JPanel modifierGrille = createGroupPanel("Comment modifier les dimensions de la grille");
        panel.add(modifierGrille);
        JLabel modifierGrilleMarkdown = createMarkdownContent("""
                    <ul>
                        <li>Allez consulter la section <strong>"Comment modifier la taille de mon panneau"</strong></li>
                    </ul>
                """);
        modifierGrille.add(modifierGrilleMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));


        JPanel contact = createGroupPanel("Comment avoir du support?");
        panel.add(contact);
        JLabel contactMarkdown = createMarkdownContent("""
                 <ul>
                        <li>Des problèmes ?</li>
                        <li>Contactez-nous !</li>
                        <li>Notre équipe dévouée se fera un plaisir de vous aider.</li>
                        <li>Envoyez-nous un courriel à : <a href="mailto:expressogloo@gmail.com">expressogloo@gmail.com</a></li>
                    </ul>
                """);
        contact.add(contactMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));


        JButton returnButton2 = new JButton("Revenir à l'application principale");
        returnButton2.setFont(new Font("Arial", Font.BOLD, 14));
        returnButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton2.addActionListener(e -> {
            mainWindow.showTrueMode();
        });
        panel.add(returnButton2);

        this.setViewportView(panel);
    }

    private JPanel createGroupPanel(String title) {
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setBorder(BorderFactory.createTitledBorder(title));
        groupPanel.setBackground(null);
        groupPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return groupPanel;
    }

    private JLabel createMarkdownContent(String contentText) {
        contentText = contentText.replaceAll("\n", "<br>");
        JLabel content = new JLabel("<html>" + contentText + "</html>"); // Enable HTML for auto-wrapping
        content.setFont(new Font("Arial", Font.PLAIN, 14)); // Regular content font
        content.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Padding
        return content;
    }


}




