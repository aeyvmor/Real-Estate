/*
 * AdminFrame.java
 * fiestaSystem.ui
 * SYSADMIN TERMINAL — Admin dashboard.
 * Property table with HouseType column, filter, inject record with HouseType dropdown.
 * All logic delegated to AppState.admin methods.
 */
package fiestaSystem.ui;

import fiestaSystem.AppState;
import fiestaSystem.model.Property;
import fiestaSystem.enums.HouseType;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author eevee
 */
public class AdminFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AdminFrame.class.getName());

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG         = new Color(10, 18, 30);
    private static final Color CARD_BG    = new Color(16, 28, 46);
    private static final Color GREEN      = new Color(0, 230, 118);
    private static final Color GREEN_DIM  = new Color(0, 160, 82);
    private static final Color YELLOW     = new Color(255, 213, 0);
    private static final Color RED        = new Color(255, 59, 59);
    private static final Color TEXT_DIM   = new Color(80, 140, 100);
    private static final Color BORDER_CLR = new Color(0, 180, 90);
    private static final Font  MONO_MD    = new Font("Courier New", Font.BOLD, 14);
    private static final Font  MONO_SM    = new Font("Courier New", Font.PLAIN, 12);
    private static final Font  MONO_XS    = new Font("Courier New", Font.PLAIN, 11);

    // ── State ─────────────────────────────────────────────────────────────────
    private DefaultTableModel tableModel;

    // ── Components ────────────────────────────────────────────────────────────
    private JPanel      topBar, statsRow, contentRow, leftCol, rightCol;
    private JLabel      titleLabel, versionLabel, pulseIcon;
    private JButton     logoutBtn, reportBtn, filterBtn, clearFilterBtn, addPropBtn, removePropBtn;
    private JLabel      statTotalVal, statSoldVal, statRevVal;
    private JScrollPane scrollPane;
    private JTable      propTable;
    private JTextField  filterBlockField, filterSqmField, filterPriceField;
    // Inject form
    private JComboBox<HouseType> houseTypeCombo;
    private JTextField  injectPriceField, injectBlockField, injectLotField,
                        injectSqmField, injectFacingField;
    private JLabel      statusBar;

    public AdminFrame() {
        initComponents();
        refreshTable(AppState.admin.getProperties());
        refreshStats();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("FIESTA HOMES — SYSADMIN TERMINAL");
        setPreferredSize(new Dimension(1160, 900));
        setResizable(false);
        getContentPane().setBackground(BG);
        getContentPane().setLayout(new BorderLayout());

        // ── TOP BAR ──────────────────────────────────────────────────────────
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GREEN));
        topBar.setPreferredSize(new Dimension(1160, 62));

        JPanel topLeft = new JPanel();
        topLeft.setOpaque(false);
        topLeft.setLayout(new BoxLayout(topLeft, BoxLayout.Y_AXIS));
        topLeft.setBorder(BorderFactory.createEmptyBorder(8, 16, 6, 0));

        titleLabel = new JLabel("SYSADMIN TERMINAL");
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 26));
        titleLabel.setForeground(GREEN);

        versionLabel = new JLabel("v2.0.4  //  ROOT ACCESS GRANTED");
        versionLabel.setFont(new Font("Courier New", Font.PLAIN, 11));
        versionLabel.setForeground(GREEN_DIM);

        topLeft.add(titleLabel);
        topLeft.add(versionLabel);
        topBar.add(topLeft, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        topRight.setOpaque(false);

        pulseIcon = new JLabel("⚡");
        pulseIcon.setFont(new Font("Courier New", Font.PLAIN, 22));
        pulseIcon.setForeground(GREEN);
        topRight.add(pulseIcon);

        reportBtn = makeBtn("GENERATE REPORT", GREEN, BG, 165, 32);
        reportBtn.addActionListener(this::reportBtnActionPerformed);
        topRight.add(reportBtn);

        logoutBtn = makeBtn("LOGOUT", RED, BG, 90, 32);
        logoutBtn.addActionListener(this::logoutBtnActionPerformed);
        topRight.add(logoutBtn);

        topBar.add(topRight, BorderLayout.EAST);

        // ── STATS ROW ─────────────────────────────────────────────────────────
        statsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        statsRow.setBackground(BG);
        statsRow.setBorder(BorderFactory.createEmptyBorder(12, 14, 10, 14));

        statTotalVal = new JLabel("0 LOTS");
        statSoldVal  = new JLabel("0 / 0");
        statRevVal   = new JLabel("₱0");

        statsRow.add(makeStatCard("TOTAL INVENTORY",  statTotalVal, GREEN));
        statsRow.add(makeStatCard("SOLD / AVAILABLE", statSoldVal,  GREEN));
        statsRow.add(makeStatCard("TOTAL REVENUE",    statRevVal,   YELLOW));

        JPanel northStack = new JPanel(new BorderLayout());
        northStack.setBackground(BG);
        northStack.add(topBar,   BorderLayout.NORTH);
        northStack.add(statsRow, BorderLayout.CENTER);
        getContentPane().add(northStack, BorderLayout.NORTH);

        // ── CONTENT ROW ───────────────────────────────────────────────────────
        contentRow = new JPanel(new BorderLayout(10, 0));
        contentRow.setBackground(BG);
        contentRow.setBorder(BorderFactory.createEmptyBorder(0, 14, 10, 14));

        // ── LEFT: table + filter ──────────────────────────────────────────────
        leftCol = new JPanel(new BorderLayout(0, 8));
        leftCol.setBackground(BG);

        JLabel dbTitle = new JLabel("PROPERTY DATABASE");
        dbTitle.setFont(new Font("Courier New", Font.BOLD, 16));
        dbTitle.setForeground(GREEN);
        dbTitle.setBorder(BorderFactory.createEmptyBorder(0, 2, 4, 0));

        // Filter strip
        JPanel filterStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterStrip.setBackground(BG);

        filterBlockField = makeField("0", 55);
        filterSqmField   = makeField("0", 60);
        filterPriceField = makeField("0", 90);

        filterBtn      = makeBtn("FILTER", GREEN, BG, 75, 26);
        clearFilterBtn = makeBtn("CLEAR",  TEXT_DIM, BG, 65, 26);
        filterBtn.addActionListener(this::filterBtnActionPerformed);
        clearFilterBtn.addActionListener(this::clearFilterBtnActionPerformed);

        for (Component c : new Component[]{
                dimLabel("BLOCK (0=ALL):"), filterBlockField,
                dimLabel("MIN SQM:"), filterSqmField,
                dimLabel("MAX PRICE:"), filterPriceField,
                filterBtn, clearFilterBtn}) {
            filterStrip.add(c);
        }

        JPanel dbHeader = new JPanel(new BorderLayout());
        dbHeader.setBackground(BG);
        dbHeader.add(dbTitle,     BorderLayout.NORTH);
        dbHeader.add(filterStrip, BorderLayout.CENTER);

        // Table — now includes HOUSE TYPE column
        String[] cols = {"ID", "BLOCK", "LOT", "HOUSE TYPE", "SQM", "PRICE (₱)", "FACING", "STATUS"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        propTable = new JTable(tableModel);
        propTable.setBackground(CARD_BG);
        propTable.setForeground(GREEN);
        propTable.setFont(MONO_XS);
        propTable.setRowHeight(22);
        propTable.setGridColor(new Color(0, 60, 30));
        propTable.setSelectionBackground(new Color(0, 80, 40));
        propTable.setSelectionForeground(Color.WHITE);
        propTable.setShowGrid(true);
        propTable.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = propTable.getTableHeader();
        header.setBackground(new Color(0, 40, 20));
        header.setForeground(GREEN);
        header.setFont(MONO_MD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GREEN));

        int[] cw = {90, 50, 40, 170, 50, 110, 60, 90};
        for (int i = 0; i < cw.length; i++)
            propTable.getColumnModel().getColumn(i).setPreferredWidth(cw[i]);

        // Status column renderer (col 7)
        propTable.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        t, val, sel, foc, row, col);
                String s = val == null ? "" : val.toString();
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(CENTER);
                lbl.setFont(new Font("Courier New", Font.BOLD, 11));
                switch (s) {
                    case "AVAILABLE": lbl.setBackground(new Color(0,100,40));  lbl.setForeground(Color.WHITE); break;
                    case "SOLD":      lbl.setBackground(RED);                  lbl.setForeground(Color.WHITE); break;
                    case "RESERVED":  lbl.setBackground(YELLOW);               lbl.setForeground(Color.BLACK); break;
                    default:          lbl.setBackground(CARD_BG);              lbl.setForeground(GREEN);
                }
                if (sel) lbl.setBackground(new Color(0, 80, 40));
                return lbl;
            }
        });

        scrollPane = new JScrollPane(propTable);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));

        statusBar = new JLabel(" Showing 0 properties.");
        statusBar.setFont(MONO_XS);
        statusBar.setForeground(TEXT_DIM);

        removePropBtn = makeBtn("-- REMOVE SELECTED --", RED, BG, 200, 28);
        removePropBtn.addActionListener(this::removePropBtnActionPerformed);

        JPanel tableBottomBar = new JPanel(new BorderLayout(8, 0));
        tableBottomBar.setBackground(BG);
        tableBottomBar.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        tableBottomBar.add(statusBar,    BorderLayout.CENTER);
        tableBottomBar.add(removePropBtn, BorderLayout.EAST);

        leftCol.add(dbHeader,       BorderLayout.NORTH);
        leftCol.add(scrollPane,     BorderLayout.CENTER);
        leftCol.add(tableBottomBar, BorderLayout.SOUTH);

        // ── RIGHT: inject form ────────────────────────────────────────────────
        rightCol = new JPanel(new BorderLayout());
        rightCol.setBackground(BG);
        rightCol.setPreferredSize(new Dimension(255, 400));

        JLabel injectTitle = new JLabel("INJECT RECORD");
        injectTitle.setFont(new Font("Courier New", Font.BOLD, 16));
        injectTitle.setForeground(GREEN);
        injectTitle.setBorder(BorderFactory.createEmptyBorder(0, 2, 8, 0));

        JPanel formPanel = new JPanel();
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR, 1),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        formPanel.setLayout(new GridLayout(0, 1, 0, 5));

        // HouseType dropdown
        houseTypeCombo = new JComboBox<>(HouseType.values());
        houseTypeCombo.setFont(MONO_XS);
        houseTypeCombo.setBackground(new Color(5, 12, 22));
        houseTypeCombo.setForeground(GREEN);
        houseTypeCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof HouseType) setText(((HouseType) value).displayName);
                setBackground(isSelected ? new Color(0, 80, 40) : new Color(5, 12, 22));
                setForeground(GREEN);
                setFont(MONO_XS);
                return this;
            }
        });
        // Auto-fill price and sqm when house type changes
        houseTypeCombo.addActionListener(e -> autoFillFromHouseType());

        injectPriceField  = makeField("", 0);
        injectBlockField  = makeField("1", 0);
        injectLotField    = makeField("", 0);
        injectSqmField    = makeField("", 0);
        injectFacingField = makeField("NORTH", 0);

        formPanel.add(fieldLabel("HOUSE TYPE"));
        formPanel.add(houseTypeCombo);
        formPanel.add(fieldLabel("PRICE (PHP) — auto-filled, adjustable"));
        formPanel.add(injectPriceField);
        formPanel.add(fieldLabel("BLOCK NUMBER  (1–5)"));
        formPanel.add(injectBlockField);
        formPanel.add(fieldLabel("LOT NUMBER    (1–20)"));
        formPanel.add(injectLotField);
        formPanel.add(fieldLabel("SQUARE METERS — auto-filled, adjustable"));
        formPanel.add(injectSqmField);
        formPanel.add(fieldLabel("FACING  (NORTH / SOUTH / EAST / WEST)"));
        formPanel.add(injectFacingField);

        addPropBtn = new JButton("++ ADD PROPERTY ++");
        addPropBtn.setFont(new Font("Courier New", Font.BOLD, 13));
        addPropBtn.setBackground(YELLOW);
        addPropBtn.setForeground(Color.BLACK);
        addPropBtn.setBorder(BorderFactory.createLineBorder(YELLOW, 2));
        addPropBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addPropBtn.setFocusPainted(false);
        addPropBtn.setPreferredSize(new Dimension(227, 40));
        addPropBtn.addActionListener(this::addPropBtnActionPerformed);
        formPanel.add(new JLabel());   // spacer
        formPanel.add(addPropBtn);

        JPanel rightInner = new JPanel(new BorderLayout(0, 6));
        rightInner.setBackground(BG);
        rightInner.add(injectTitle, BorderLayout.NORTH);
        rightInner.add(formPanel,   BorderLayout.CENTER);

        rightCol.add(rightInner, BorderLayout.NORTH);

        contentRow.add(leftCol,  BorderLayout.CENTER);
        contentRow.add(rightCol, BorderLayout.EAST);
        getContentPane().add(contentRow, BorderLayout.CENTER);

        // Trigger initial auto-fill
        autoFillFromHouseType();

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Auto-fills price and sqm fields when HouseType changes */
    private void autoFillFromHouseType() {
        HouseType ht = (HouseType) houseTypeCombo.getSelectedItem();
        if (ht != null) {
            injectPriceField.setText(String.format("%.0f", ht.basePrice));
            injectSqmField.setText(String.valueOf(ht.lotSqm));
        }
    }

    private void refreshTable(java.util.List<Property> list) {
        tableModel.setRowCount(0);
        for (Property p : list) {
            String typeName = p.getHouseType() != null ? p.getHouseType().displayName : "N/A";
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getBlockNumber(),
                p.getLotNumber(),
                typeName,
                p.getSqm(),
                String.format("%,.2f", p.getPrice()),
                p.getFacing(),
                p.getStatus().name()
            });
        }
        statusBar.setText(" Showing " + list.size()
                + " propert" + (list.size() == 1 ? "y." : "ies."));
    }

    private void refreshStats() {
        int total = AppState.admin.getProperties().size();
        int sold = 0, available = 0;
        double revenue = 0;
        for (Property p : AppState.admin.getProperties()) {
            switch (p.getStatus()) {
                case SOLD:      sold++;     revenue += p.getPrice(); break;
                case AVAILABLE: available++; break;
                default: break;
            }
        }
        statTotalVal.setText(total + " LOTS");
        statSoldVal.setText(sold + " / " + available);
        statRevVal.setText("₱" + formatRevenue(revenue));
    }

    private String formatRevenue(double v) {
        if (v >= 1_000_000) return String.format("%.1fM", v / 1_000_000);
        if (v >= 1_000)     return String.format("%.1fK", v / 1_000);
        return String.format("%.0f", v);
    }

    private JButton makeBtn(String text, Color fg, Color bg, int w, int h) {
        JButton b = new JButton(text);
        b.setFont(new Font("Courier New", Font.BOLD, 12));
        b.setForeground(fg);
        b.setBackground(bg);
        b.setBorder(BorderFactory.createLineBorder(fg, 1));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.setOpaque(true);
        if (w > 0) b.setPreferredSize(new Dimension(w, h));
        return b;
    }

    private JPanel makeStatCard(String label, JLabel valLabel, Color valColor) {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Courier New", Font.PLAIN, 11));
        lbl.setForeground(TEXT_DIM);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 12, 2, 0));
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        valLabel.setFont(new Font("Courier New", Font.BOLD, 32));
        valLabel.setForeground(valColor);
        valLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 10, 0));
        valLabel.setAlignmentX(LEFT_ALIGNMENT);

        card.add(lbl);
        card.add(valLabel);
        return card;
    }

    private JLabel dimLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(MONO_XS);
        l.setForeground(TEXT_DIM);
        return l;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", Font.PLAIN, 10));
        l.setForeground(GREEN_DIM);
        return l;
    }

    private JTextField makeField(String def, int w) {
        JTextField f = new JTextField(def);
        f.setFont(MONO_SM);
        f.setBackground(new Color(5, 12, 22));
        f.setForeground(GREEN);
        f.setCaretColor(GREEN);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR, 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        if (w > 0) f.setPreferredSize(new Dimension(w, 26));
        return f;
    }

    // ── Action handlers ───────────────────────────────────────────────────────

    private void filterBtnActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int    block    = Integer.parseInt(filterBlockField.getText().trim());
            double minSqm   = Double.parseDouble(filterSqmField.getText().trim());
            double maxPrice = Double.parseDouble(filterPriceField.getText().trim());
            ArrayList<Property> result = AppState.admin.filterProperties(block, minSqm, maxPrice);
            refreshTable(result);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid filter values. Use numbers (0 = no limit).",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFilterBtnActionPerformed(java.awt.event.ActionEvent evt) {
        filterBlockField.setText("0");
        filterSqmField.setText("0");
        filterPriceField.setText("0");
        refreshTable(AppState.admin.getProperties());
    }

    private void addPropBtnActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            HouseType ht    = (HouseType) houseTypeCombo.getSelectedItem();
            double price    = Double.parseDouble(injectPriceField.getText().trim());
            int    block    = Integer.parseInt(injectBlockField.getText().trim());
            int    lot      = Integer.parseInt(injectLotField.getText().trim());
            double sqm      = Double.parseDouble(injectSqmField.getText().trim());
            String facing   = injectFacingField.getText().trim().toUpperCase();

            if (block < 1 || block > 5) {
                JOptionPane.showMessageDialog(this, "Block must be 1–5.",
                        "Invalid Block", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (lot < 1 || lot > 20) {
                JOptionPane.showMessageDialog(this, "Lot must be 1–20.",
                        "Invalid Lot", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (facing.isEmpty()) facing = "NORTH";

            Property p = new Property(block, lot, price, sqm, facing, ht);
            AppState.admin.addProperty(p);

            // Clear adjustable fields; keep type/block/facing
            injectLotField.setText("");
            // Re-auto-fill price/sqm from current type
            autoFillFromHouseType();

            refreshTable(AppState.admin.getProperties());
            refreshStats();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields with valid numbers.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removePropBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int row = propTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Select a property from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id     = tableModel.getValueAt(row, 0).toString();
        String status = tableModel.getValueAt(row, 7).toString();
        if (!status.equals("AVAILABLE")) {
            JOptionPane.showMessageDialog(this,
                    "Only AVAILABLE properties can be removed.\n"
                    + id + " is currently " + status + ".",
                    "Cannot Remove", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove property " + id + "? This cannot be undone.",
                "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        AppState.admin.removeProperty(id);
        refreshTable(AppState.admin.getProperties());
        refreshStats();
    }

    private void reportBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String report = AppState.admin.generateReport();
        JTextArea ta = new JTextArea(report);
        ta.setFont(new Font("Courier New", Font.PLAIN, 11));
        ta.setEditable(false);
        ta.setBackground(new Color(10, 18, 30));
        ta.setForeground(new Color(0, 230, 118));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(560, 440));
        JOptionPane.showMessageDialog(this, sp, "MASTER LOT REPORT",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {
        new LoginFrame().setVisible(true);
        dispose();
    }

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(() -> new AdminFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // (all declared as fields above)
    // End of variables declaration//GEN-END:variables
}