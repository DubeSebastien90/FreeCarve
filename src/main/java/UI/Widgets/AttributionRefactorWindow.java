package UI.Widgets;

import UI.MainWindow;
import UI.OptionWrapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AttributionRefactorWindow extends JScrollPane {

    JPanel panel;

    public AttributionRefactorWindow(MainWindow mainWindow) {
        init(mainWindow);
    }

    private void init(MainWindow mainWindow) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(null);

        JLabel titleLabel = new JLabel("À propos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel equipe = createGroupPanel("Notre équipe");
        panel.add(equipe);
        JLabel equipeMarkdown = createMarkdownContent("""
                L'équipe de développeurs ayant créé ce projet est composée de :\s
                 Adam Côté
                Antoine Morin
                Kamran Charles Nayebi
                Louis-Étienne Messier
                Sébastien Dubé
                Tous étaient étudiants à l'Université Laval au moment de la création de l'application.""");
        equipe.add(equipeMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));


        JPanel attribution = createGroupPanel("Attributions");
        panel.add(attribution);
        JLabel attributionMarkdown = createMarkdownContent("""
                                Les icônes utilisées dans ce projet proviennent des collections suivantes :\s
                                
                  - https://www.svgrepo.com/collection/solar-bold-icons/
                  - https://www.svgrepo.com/collection/tetrisly-interface-icons/
                  
                 Ces icônes sont sous licence CC Attribution License, qui permet leur utilisation à condition de fournir une attribution et un lien vers la source, comme indiqué ci-dessus.""");
        attribution.add(attributionMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));


        JPanel pourquoi = createGroupPanel("Pourquoi ce projet?");
        panel.add(pourquoi);
        JLabel pourquoiMarkdown = createMarkdownContent("""
                Ce projet a été réalisé dans le cadre du cours GLO-2004, enseigné, au moment de sa réalisation, par M. Marc Philippe Parent et M. Anthony Deschênes.""");
        pourquoi.add(pourquoiMarkdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));



        JButton returnButton2 = new JButton("Revenir à l'application principale");
        returnButton2.setFont(new Font("Arial", Font.BOLD, 14));
        returnButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton2.addActionListener(e -> {
            mainWindow.showTrueMode();
        });
        panel.add(returnButton2);

        for (Component i : panel.getComponents()) {
            i.setPreferredSize(this.getPreferredSize());
        }

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





