package org.jhove2.module.assess;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: rnanders
 * Date: Jun 24, 2010
 * Time: 9:48:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class RuleConfigurator {
    private JFrame frame;
    private JPanel framePanel;
    private JTextField ruleNameText;
    private JTextField ruleDescriptionText;
    private JTextField consequentText;
    private JTextField alternativeText;
    private JRadioButton quantifierAllOfRadio;
    private JRadioButton quantifierAnyOfRadio;
    private JButton button1;
    private JList predicateList;
    protected JPanel predicatePanel;
    protected JPanel gridBagPanel;
    protected JPanel quantifierPanel;
    protected JPanel predicateButtonPanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RuleConfigurator window = new RuleConfigurator();
                    window.frame.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    

    public RuleConfigurator() {
        initialize();
    }


    private void initialize() {
        JFrame frame = new JFrame("RuleConfigurator");
        frame.setContentPane(new RuleConfigurator().framePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        framePanel = new JPanel();
        framePanel.setLayout(new BorderLayout(0, 0));
        framePanel.setBorder(BorderFactory.createTitledBorder(null, "Rule Configurator", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(framePanel.getFont().getName(), framePanel.getFont().getStyle(), framePanel.getFont().getSize())));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setText("Enter the data to specify your rule");
        framePanel.add(label1, BorderLayout.NORTH);
        gridBagPanel = new JPanel();
        gridBagPanel.setLayout(new GridBagLayout());
        framePanel.add(gridBagPanel, BorderLayout.CENTER);
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(10);
        label2.setText("Rule Name:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gridBagPanel.add(label2, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gridBagPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gridBagPanel.add(spacer2, gbc);
        ruleNameText = new JTextField();
        ruleNameText.setColumns(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gridBagPanel.add(ruleNameText, gbc);
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(10);
        label3.setText("Rule Description:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gridBagPanel.add(label3, gbc);
        ruleDescriptionText = new JTextField();
        ruleDescriptionText.setColumns(40);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gridBagPanel.add(ruleDescriptionText, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gridBagPanel.add(spacer3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Consequent:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gridBagPanel.add(label4, gbc);
        consequentText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gridBagPanel.add(consequentText, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gridBagPanel.add(spacer4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Alternative:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gridBagPanel.add(label5, gbc);
        alternativeText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gridBagPanel.add(alternativeText, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.VERTICAL;
        gridBagPanel.add(spacer5, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Quantifier:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gridBagPanel.add(label6, gbc);
        quantifierPanel = new JPanel();
        quantifierPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gridBagPanel.add(quantifierPanel, gbc);
        quantifierAllOfRadio = new JRadioButton();
        quantifierAllOfRadio.setText("All of");
        quantifierPanel.add(quantifierAllOfRadio);
        quantifierAnyOfRadio = new JRadioButton();
        quantifierAnyOfRadio.setText("Any of");
        quantifierPanel.add(quantifierAnyOfRadio);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.VERTICAL;
        gridBagPanel.add(spacer6, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Predicates:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        gridBagPanel.add(label7, gbc);
        predicatePanel = new JPanel();
        predicatePanel.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.BOTH;
        gridBagPanel.add(predicatePanel, gbc);
        predicateButtonPanel = new JPanel();
        predicateButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        predicatePanel.add(predicateButtonPanel, BorderLayout.SOUTH);
        button1 = new JButton();
        button1.setText("Button");
        predicateButtonPanel.add(button1);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(30);
        scrollPane1.setVerticalScrollBarPolicy(22);
        predicatePanel.add(scrollPane1, BorderLayout.CENTER);
        predicateList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("DefaultListModel");
        predicateList.setModel(defaultListModel1);
        predicateList.setSelectionMode(0);
        scrollPane1.setViewportView(predicateList);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(quantifierAllOfRadio);
        buttonGroup.add(quantifierAnyOfRadio);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return framePanel;
    }
}
