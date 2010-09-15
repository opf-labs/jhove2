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

public class AssessmentConfigurator {

    private JFrame frmAssessmentConfigurator;
    private JTable table;
    private final ButtonGroup buttonGroup = new ButtonGroup();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AssessmentConfigurator window = new AssessmentConfigurator();
                    window.frmAssessmentConfigurator.setVisible(true);
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
    public AssessmentConfigurator() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmAssessmentConfigurator = new JFrame();
        frmAssessmentConfigurator.setTitle("Assessment Configurator");
        frmAssessmentConfigurator.setBounds(100, 100, 442, 300);
        frmAssessmentConfigurator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        frmAssessmentConfigurator.setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);
        
        JScrollPane scrollPane = new JScrollPane();
        frmAssessmentConfigurator.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        table = new JTable();
        table.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null, null},
            },
            new String[] {
                "Name", "Description", "Object Filter"
            }
        ) {
            Class[] columnTypes = new Class[] {
                String.class, String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        scrollPane.setViewportView(table);
        
        JPanel panel = new JPanel();
        frmAssessmentConfigurator.getContentPane().add(panel, BorderLayout.SOUTH);
        
        JButton btnAddRuleset = new JButton("Add RuleSet");
        buttonGroup.add(btnAddRuleset);
        panel.add(btnAddRuleset);
        
        JButton btnEditRuleset = new JButton("Edit RuleSet");
        buttonGroup.add(btnEditRuleset);
        panel.add(btnEditRuleset);
        
        JButton btnDeleteRuleset = new JButton("Delete RuleSet");
        buttonGroup.add(btnDeleteRuleset);
        panel.add(btnDeleteRuleset);
        
        JLabel lblRulesets = new JLabel("RuleSets");
        lblRulesets.setHorizontalAlignment(SwingConstants.CENTER);
        frmAssessmentConfigurator.getContentPane().add(lblRulesets, BorderLayout.NORTH);
    }

}
