package org.jhove2.module.assess;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.ButtonGroup;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.BoxLayout;

public class RuleSetConfigurator {

    private JFrame frmRulesetConfigurator;
    private JTable tableRules;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JTextField textName;
    private JTextField textDescription;
    private JTextField textObjectFilter;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RuleSetConfigurator window = new RuleSetConfigurator();
                    window.frmRulesetConfigurator.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public RuleSetConfigurator() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmRulesetConfigurator = new JFrame();
        frmRulesetConfigurator.setTitle("RuleSet Configurator");
        frmRulesetConfigurator.setBounds(100, 100, 442, 300);
        frmRulesetConfigurator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panelMetadata = new JPanel();
        frmRulesetConfigurator.getContentPane().add(panelMetadata, BorderLayout.NORTH);
        GridBagLayout gbl_panelMetadata = new GridBagLayout();
        gbl_panelMetadata.columnWidths = new int[]{0, 0, 0};
        gbl_panelMetadata.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_panelMetadata.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_panelMetadata.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        panelMetadata.setLayout(gbl_panelMetadata);
        
        JLabel lblName = new JLabel("Name:");
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.EAST;
        gbc_lblName.insets = new Insets(0, 0, 5, 5);
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 1;
        panelMetadata.add(lblName, gbc_lblName);
        
        textName = new JTextField();
        GridBagConstraints gbc_textName = new GridBagConstraints();
        gbc_textName.insets = new Insets(0, 0, 5, 0);
        gbc_textName.fill = GridBagConstraints.HORIZONTAL;
        gbc_textName.gridx = 1;
        gbc_textName.gridy = 1;
        panelMetadata.add(textName, gbc_textName);
        textName.setColumns(40);
        
        JLabel lblDescription = new JLabel("Description:");
        GridBagConstraints gbc_lblDescription = new GridBagConstraints();
        gbc_lblDescription.anchor = GridBagConstraints.EAST;
        gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
        gbc_lblDescription.gridx = 0;
        gbc_lblDescription.gridy = 2;
        panelMetadata.add(lblDescription, gbc_lblDescription);
        
        textDescription = new JTextField();
        GridBagConstraints gbc_textDescription = new GridBagConstraints();
        gbc_textDescription.insets = new Insets(0, 0, 5, 0);
        gbc_textDescription.fill = GridBagConstraints.HORIZONTAL;
        gbc_textDescription.gridx = 1;
        gbc_textDescription.gridy = 2;
        panelMetadata.add(textDescription, gbc_textDescription);
        textDescription.setColumns(40);
        
        JLabel lblObjectFilter = new JLabel("Object Filter:");
        GridBagConstraints gbc_lblObjectFilter = new GridBagConstraints();
        gbc_lblObjectFilter.anchor = GridBagConstraints.EAST;
        gbc_lblObjectFilter.insets = new Insets(0, 0, 5, 5);
        gbc_lblObjectFilter.gridx = 0;
        gbc_lblObjectFilter.gridy = 3;
        panelMetadata.add(lblObjectFilter, gbc_lblObjectFilter);
        
        textObjectFilter = new JTextField();
        GridBagConstraints gbc_textObjectFilter = new GridBagConstraints();
        gbc_textObjectFilter.insets = new Insets(0, 0, 5, 0);
        gbc_textObjectFilter.fill = GridBagConstraints.HORIZONTAL;
        gbc_textObjectFilter.gridx = 1;
        gbc_textObjectFilter.gridy = 3;
        panelMetadata.add(textObjectFilter, gbc_textObjectFilter);
        textObjectFilter.setColumns(10);
        
        JPanel panelRules = new JPanel();
        frmRulesetConfigurator.getContentPane().add(panelRules, BorderLayout.CENTER);
        panelRules.setLayout(new BorderLayout(0, 0));
        
        JLabel lblRules = new JLabel("Rules");
        lblRules.setHorizontalAlignment(SwingConstants.CENTER);
        panelRules.add(lblRules, BorderLayout.NORTH);
        
        JPanel panelRuleTable = new JPanel();
        panelRules.add(panelRuleTable);
        panelRuleTable.setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPaneRuleTable = new JScrollPane();
        panelRuleTable.add(scrollPaneRuleTable);
        
        tableRules = new JTable();
        tableRules.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null},
            },
            new String[] {
                "Name", "Description"
            }
        ));
        tableRules.getColumnModel().getColumn(0).setPreferredWidth(30);
        scrollPaneRuleTable.setViewportView(tableRules);
        
        JPanel panelButtons = new JPanel();
        frmRulesetConfigurator.getContentPane().add(panelButtons, BorderLayout.SOUTH);
        
        JButton btnAddRule = new JButton("Add Rule");
        buttonGroup.add(btnAddRule);
        panelButtons.add(btnAddRule);
        
        JButton btnEditRule = new JButton("Edit Rule");
        buttonGroup.add(btnEditRule);
        panelButtons.add(btnEditRule);
        
        JButton btnDeleteRule = new JButton("Delete Rule");
        buttonGroup.add(btnDeleteRule);
        panelButtons.add(btnDeleteRule);
    }

}
