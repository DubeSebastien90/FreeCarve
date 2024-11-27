package UI;

import javax.swing.*;
import java.awt.*;

public class AttributionWindow extends JPanel {
    public AttributionWindow(MainWindow mainWindow) {
        init(mainWindow);
    }

    private void init(MainWindow mainWindow) {
        JScrollPane scroll = new JScrollPane();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel vbox = new JPanel();
        vbox.setLayout(new BoxLayout(vbox, BoxLayout.Y_AXIS));
        vbox.setBackground(null);
        vbox.add(createMarkdownHeader("# Notre Équipe"));
        vbox.add(createMarkdownContent("""
                L'équipe de développeurs ayant créé ce projet est composée de :\s
                 Adam Côté
                Antoine Morin
                Kamran Charles Nayebi
                Louis-Étienne Messier
                Sébastien Dubé
                Tous étaient étudiants à l'Université Laval au moment de la création de l'application."""));
        vbox.add(createMarkdownSeparator());

        vbox.add(createMarkdownHeader("### Attribution"));
        vbox.add(createMarkdownContent("""
                Les icônes utilisées dans ce projet proviennent des collections suivantes :\s
                                
                  - https://www.svgrepo.com/collection/solar-bold-icons/
                  - https://www.svgrepo.com/collection/tetrisly-interface-icons/
                  
                 Ces icônes sont sous licence CC Attribution License, qui permet leur utilisation à condition de fournir une attribution et un lien vers la source, comme indiqué ci-dessus."""));

        vbox.add(createMarkdownSeparator());

        vbox.add(createMarkdownHeader("### À propos"));
        vbox.add(createMarkdownContent("Ce projet a été réalisé dans le cadre du cours GLO-2004, enseigné, au moment de sa réalisation, par M. Marc Philippe Parent et M. Anthony Deschênes."));
        vbox.add(createMarkdownSeparator());

        for (Component i : vbox.getComponents()) {
            i.setPreferredSize(scroll.getPreferredSize());
        }
        JButton returnButton = new JButton("Revenir à l'application principale");
        returnButton.addActionListener(e -> mainWindow.showTrueMode());
        scroll.setViewportView(vbox);
        add(returnButton);
        add(scroll);
    }

    private static JLabel createMarkdownHeader(String headerText) {
        JLabel header = new JLabel(headerText);
        Font font;
        if (headerText.startsWith("# ")) {
            font = new Font("Arial", Font.BOLD, 24);
        } else if (headerText.startsWith("## ")) {
            font = new Font("Arial", Font.BOLD, 18);
        } else if (headerText.startsWith("### ")) {
            font = new Font("Arial", Font.BOLD, 14);
        } else {
            font = new Font("Arial", Font.PLAIN, 12);
        }
        header.setText(header.getText().replaceAll("#", ""));
        header.setFont(font);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); // Padding
        return header;
    }

    private static JLabel createMarkdownContent(String contentText) {
        contentText = contentText.replaceAll("\n", "<br>");
        JLabel content = new JLabel("<html>" + contentText + "</html>"); // Enable HTML for auto-wrapping
        content.setFont(new Font("Arial", Font.PLAIN, 14)); // Regular content font
        content.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Padding
        return content;
    }

    // Method to create a Markdown-like separator
    private static JSeparator createMarkdownSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // Full width, thin line
        separator.setForeground(Color.LIGHT_GRAY); // Light gray color
        return separator;
    }

}
