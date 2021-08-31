package com.educationalbotters.customscripts.WoodCutter.script;

import com.educationalbotters.EducationalBottersApi.Entities.Tree;
import com.educationalbotters.EducationalBottersApi.Items.Axe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WoodcutterGui {

    private final JDialog dialogBox;
    private final JComboBox<Tree> treeSelector;
    private final JComboBox<Axe> axeSelector;

    private boolean started;

    public WoodcutterGui() {
        // Main Dialog
        dialogBox = new JDialog();
        dialogBox.setTitle("Floyd's Woodcutter");
        dialogBox.setModal(true);
        dialogBox.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialogBox.setPreferredSize(new Dimension(200, 200));

        // Add something that has a layout and can hold components to main dialog
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        dialogBox.getContentPane().add(panel);

        // Another panel, for tree selection
        JPanel treeSelectionPanel = new JPanel();
        treeSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Add a tree label
        JLabel treeSelectionLabel = new JLabel("Select tree: ");
        treeSelectionPanel.add(treeSelectionLabel);

        // Add a tree selector
        treeSelector = new JComboBox<>(Tree.values());
        treeSelectionPanel.add(treeSelector);

        // Add tree selection panel to main panel
        panel.add(treeSelectionPanel);

        // Another panel, for axe selection
        JPanel axeSelectionPanel = new JPanel();
        axeSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Add an axe label
        JLabel axeSelectionLabel = new JLabel("Select axe: ");
        axeSelectionPanel.add(axeSelectionLabel);

        // Add an axe selector
        axeSelector = new JComboBox<>(Axe.values());
        axeSelectionPanel.add(axeSelector);

        // Add axe selection panel to main panel
        panel.add(axeSelectionPanel);

        // Add a start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            started = true;
            close();
        });
        panel.add(startButton);

        dialogBox.pack();
    }

    public boolean isStarted() {
        return started;
    }

    public Tree getSelectedTree() {
        return (Tree) treeSelector.getSelectedItem();
    }

    public Axe getSelectedAxe() { return (Axe) axeSelector.getSelectedItem(); }

    public void open() {
        dialogBox.setVisible(true);
    }

    public void close() {
        dialogBox.setVisible(false);
        dialogBox.dispose();
    }

}
