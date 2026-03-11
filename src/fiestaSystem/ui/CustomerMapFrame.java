/*
 * CustomerMapFrame.java
 * fiestaSystem.ui
 * BUYER KIOSK — Customer property browser.
 * Visual grid of 5 blocks x 20 lots, filter sidebar.
 * Clicking an available lot opens LotDetailFrame.
 * All logic delegated to AppState.currentCustomer / AppState.admin.
 */
package fiestaSystem.ui;

import fiestaSystem.AppState;
import fiestaSystem.model.Property;
import fiestaSystem.enums.PropertyStatus;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eevee
 */
public class CustomerMapFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(CustomerMapFrame.class.getName());

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_OUTER   = new Color(0, 210, 220);   // cyan teal bg
    private static final Color BG_SIDE    = new Color(255, 255, 255);
    private static final Color BG_GRID    = new Color(230, 232, 235);
    private static final Color DARK       = new Color(10, 10, 10);
    private static final Color AVAIL_BG   = new Color(255, 255, 255);
    private static final Color AVAIL_BDR  = new Color(10, 10, 10);
    private static final Color SOLD_STRIPE= new Color(220, 50, 50);
    private static final Color RSVD_COLOR = new Color(255, 213, 0);
    private static final Color YELLOW     = new Color(255, 213, 0);
    private static final Color PINK       = new Color(255, 100, 180);
    private static final Color GREEN      = new Color(34, 197, 94);
    private static final Color TEXT_DIM   = new Color(120, 120, 120);

    // ── Components ────────────────────────────────────────────────────────────
    private JPanel     sidebar, gridContainer, gridInner;
    private JComboBox<String> blockCombo;
    private JTextField minSqmField, maxPriceField;
    private JButton    filterBtn, logoutBtn;
    private JLabel     selectHint;

    // Currently displayed properties (after filter)
    private List<Property> displayedProps;

    public CustomerMapFrame() {
        initComponents();
        applyFilter(); // draw grid with current (empty) filter
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("FIESTA HOMES — BUYER KIOSK");
        setPreferredSize(new Dimension(1120, 680));
        setResizable(false);
        getContentPane().setBackground(BG_OUTER);
        getContentPane().setLayout(new BorderLayout(10, 0));

        // ── SIDEBAR ──────────────────────────────────────────────────────────
        sidebar = new JPanel();
        sidebar.setBackground(BG_SIDE);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK, 3),
                BorderFactory.createEmptyBorder(16, 14, 16, 14)));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, 680));

        // Title block
        JLabel kiosk = boldLabel("BUYER KIOSK", 22);
        kiosk.setAlignmentX(LEFT_ALIGNMENT);
        selectHint = new JLabel("Select a Lot");
        selectHint.setFont(new Font("Courier New", Font.BOLD, 12));
        selectHint.setBackground(YELLOW);
        selectHint.setForeground(DARK);
        selectHint.setOpaque(true);
        selectHint.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        selectHint.setAlignmentX(LEFT_ALIGNMENT);

        sidebar.add(kiosk);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(selectHint);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(makeDivider());
        sidebar.add(Box.createVerticalStrut(12));

        // Lot Specifications section
        JLabel specTitle = boldLabel("LOT SPECIFICATIONS", 13);
        specTitle.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(specTitle);
        sidebar.add(Box.createVerticalStrut(10));

        // Block combo
        JLabel blockLbl = dimLabel("BLOCK LOCATION");
        blockLbl.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(blockLbl);
        sidebar.add(Box.createVerticalStrut(3));
        String[] blockOptions = {"All Blocks (1-5)", "Block 1", "Block 2", "Block 3", "Block 4", "Block 5"};
        blockCombo = new JComboBox<>(blockOptions);
        blockCombo.setFont(new Font("Courier New", Font.PLAIN, 12));
        blockCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        blockCombo.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(blockCombo);
        sidebar.add(Box.createVerticalStrut(10));

        // Min SQM
        JLabel sqmLbl = dimLabel("MIN SIZE (SQM)");
        sqmLbl.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(sqmLbl);
        sidebar.add(Box.createVerticalStrut(3));
        minSqmField = sideField("e.g. 150");
        sidebar.add(minSqmField);
        sidebar.add(Box.createVerticalStrut(10));

        // Max Price
        JLabel priceLbl = dimLabel("MAX PRICE (PHP)");
        priceLbl.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(priceLbl);
        sidebar.add(Box.createVerticalStrut(3));
        maxPriceField = sideField("e.g. 3000000");
        sidebar.add(maxPriceField);
        sidebar.add(Box.createVerticalStrut(14));

        // Filter button
        filterBtn = new JButton("SEARCH LOTS");
        filterBtn.setFont(new Font("Courier New", Font.BOLD, 13));
        filterBtn.setBackground(DARK);
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setBorder(BorderFactory.createLineBorder(DARK, 2));
        filterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterBtn.setFocusPainted(false);
        filterBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        filterBtn.setAlignmentX(LEFT_ALIGNMENT);
        filterBtn.addActionListener(e -> applyFilter());
        sidebar.add(filterBtn);
        sidebar.add(Box.createVerticalStrut(6));

        JButton clearBtn = new JButton("CLEAR");
        clearBtn.setFont(new Font("Courier New", Font.BOLD, 12));
        clearBtn.setBackground(BG_SIDE);
        clearBtn.setForeground(DARK);
        clearBtn.setBorder(BorderFactory.createLineBorder(DARK, 1));
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.setFocusPainted(false);
        clearBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        clearBtn.setAlignmentX(LEFT_ALIGNMENT);
        clearBtn.addActionListener(e -> {
            blockCombo.setSelectedIndex(0);
            minSqmField.setText("");
            maxPriceField.setText("");
            applyFilter();
        });
        sidebar.add(clearBtn);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeDivider());
        sidebar.add(Box.createVerticalStrut(8));

        // Legend
        sidebar.add(boldLabel("LEGEND", 11));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(makeLegendRow(AVAIL_BG, AVAIL_BDR, "AVAILABLE"));
        sidebar.add(Box.createVerticalStrut(3));
        sidebar.add(makeLegendRow(RSVD_COLOR, DARK, "RESERVED"));
        sidebar.add(Box.createVerticalStrut(3));
        sidebar.add(makeLegendRow(SOLD_STRIPE, DARK, "SOLD"));
        sidebar.add(Box.createVerticalStrut(12));

        // Logout
        logoutBtn = new JButton("LOGOUT");
        logoutBtn.setFont(new Font("Courier New", Font.BOLD, 12));
        logoutBtn.setBackground(BG_SIDE);
        logoutBtn.setForeground(new Color(180, 0, 0));
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(180, 0, 0), 1));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        logoutBtn.setAlignmentX(LEFT_ALIGNMENT);
        logoutBtn.addActionListener(this::logoutBtnActionPerformed);
        sidebar.add(logoutBtn);

        getContentPane().add(sidebar, BorderLayout.WEST);

        // ── GRID AREA ─────────────────────────────────────────────────────────
        gridContainer = new JPanel(new BorderLayout());
        gridContainer.setBackground(BG_OUTER);
        gridContainer.setBorder(BorderFactory.createEmptyBorder(12, 4, 12, 12));

        gridInner = new JPanel();
        gridInner.setBackground(BG_GRID);
        gridInner.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        // Layout set dynamically in buildGrid()

        JScrollPane gridScroll = new JScrollPane(gridInner);
        gridScroll.setBackground(BG_GRID);
        gridScroll.getViewport().setBackground(BG_GRID);
        gridScroll.setBorder(BorderFactory.createEmptyBorder());

        gridContainer.add(gridScroll, BorderLayout.CENTER);
        getContentPane().add(gridContainer, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // ── Grid builder ──────────────────────────────────────────────────────────

    /**
     * Applies current filter and rebuilds the grid.
     * Reads filter inputs, calls customer.filterProperties(), redraws.
     */
    public void applyFilter() {
        int blockSel = blockCombo.getSelectedIndex(); // 0=all, 1-5=specific block
        double minSqm = 0, maxPrice = 0;
        try { minSqm  = Double.parseDouble(minSqmField.getText().trim());  } catch (Exception ignored) {}
        try { maxPrice = Double.parseDouble(maxPriceField.getText().trim()); } catch (Exception ignored) {}

        if (AppState.currentCustomer != null) {
            displayedProps = AppState.currentCustomer.filterProperties(
                    AppState.admin.getProperties(), blockSel, minSqm, maxPrice);
        } else {
            // fallback: show all (shouldn't reach here normally)
            displayedProps = new ArrayList<>(AppState.admin.getProperties());
        }

        buildGrid();
    }

    /** Refreshes the grid (called after returning from LotDetailFrame) */
    public void refreshGrid() {
        applyFilter();
    }

    private void buildGrid() {
        gridInner.removeAll();

        // Collect all properties (not just filtered) for the full grid
        List<Property> allProps = AppState.admin.getProperties();

        if (allProps.isEmpty()) {
            JLabel empty = new JLabel("No lots available. Admin must add properties first.");
            empty.setFont(new Font("Courier New", Font.BOLD, 14));
            empty.setForeground(TEXT_DIM);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            gridInner.setLayout(new BorderLayout());
            gridInner.add(empty, BorderLayout.CENTER);
            gridInner.revalidate();
            gridInner.repaint();
            return;
        }

        // Group by block
        int maxBlock = 0, maxLot = 0;
        for (Property p : allProps) {
            if (p.getBlockNumber() > maxBlock) maxBlock = p.getBlockNumber();
            if (p.getLotNumber()   > maxLot)   maxLot   = p.getLotNumber();
        }

        // Grid: rows = blocks, cols = lots
        gridInner.setLayout(new GridLayout(maxBlock, maxLot, 4, 4));
        gridInner.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Build lookup map
        java.util.Map<String, Property> propMap = new java.util.HashMap<>();
        for (Property p : allProps) propMap.put(p.getBlockNumber() + "_" + p.getLotNumber(), p);

        // Build highlighted IDs from filter
        java.util.Set<String> highlightIds = new java.util.HashSet<>();
        if (displayedProps != null) {
            for (Property p : displayedProps) highlightIds.add(p.getId());
        }

        for (int b = 1; b <= maxBlock; b++) {
            for (int l = 1; l <= maxLot; l++) {
                Property p = propMap.get(b + "_" + l);
                JPanel cell = makeLotCell(p, highlightIds);
                gridInner.add(cell);
            }
        }

        gridInner.revalidate();
        gridInner.repaint();
    }

    private JPanel makeLotCell(Property p, java.util.Set<String> highlightIds) {
        // Dim cell if filtered out
        boolean highlighted = (p != null) && highlightIds.contains(p.getId());
        boolean filterActive = (displayedProps != null) &&
                               (displayedProps.size() < AppState.admin.getProperties().size());
        boolean dimmed = filterActive && !highlighted;

        JPanel cell = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (p != null && p.getStatus() == PropertyStatus.SOLD) {
                    // Draw diagonal stripes for SOLD
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(220, 50, 50, 180));
                    g2.setStroke(new BasicStroke(3));
                    int w = getWidth(), h = getHeight();
                    for (int i = -h; i < w + h; i += 10) {
                        g2.drawLine(i, 0, i + h, h);
                    }
                } else if (p != null && p.getStatus() == PropertyStatus.RESERVED) {
                    // Draw diagonal stripes for RESERVED in gold
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(255, 180, 0, 160));
                    g2.setStroke(new BasicStroke(3));
                    int w = getWidth(), h = getHeight();
                    for (int i = -h; i < w + h; i += 10) {
                        g2.drawLine(i, 0, i + h, h);
                    }
                }
            }
        };

        cell.setLayout(new BorderLayout());
        cell.setPreferredSize(new Dimension(72, 72));

        if (p == null) {
            cell.setBackground(new Color(200, 200, 200));
            cell.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
            return cell;
        }

        // Background based on status
        Color cellBg;
        switch (p.getStatus()) {
            case SOLD:     cellBg = new Color(240, 200, 200); break;
            case RESERVED: cellBg = new Color(255, 245, 180); break;
            default:       cellBg = AVAIL_BG;
        }
        if (dimmed) cellBg = new Color(200, 200, 200);
        cell.setBackground(cellBg);

        Color borderColor = dimmed ? new Color(180, 180, 180) : AVAIL_BDR;
        int bw = highlighted ? 2 : 1;
        cell.setBorder(BorderFactory.createLineBorder(borderColor, bw));

        // Lot label (top-left)
        JLabel lotLbl = new JLabel("L" + String.format("%02d", p.getLotNumber()));
        lotLbl.setFont(new Font("Courier New", Font.BOLD, 9));
        lotLbl.setForeground(dimmed ? new Color(160, 160, 160) : DARK);
        lotLbl.setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 0));
        cell.add(lotLbl, BorderLayout.NORTH);

        // Status badge (center)
        if (p.getStatus() != PropertyStatus.AVAILABLE) {
            String tag = p.getStatus() == PropertyStatus.SOLD ? "SOLD" : "RSVD";
            JLabel badge = new JLabel(tag) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    // Rotate 45 degrees
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.rotate(Math.toRadians(-30), getWidth() / 2.0, getHeight() / 2.0);
                    super.paintComponent(g);
                }
            };
            badge.setFont(new Font("Courier New", Font.BOLD, 11));
            badge.setForeground(p.getStatus() == PropertyStatus.SOLD ? SOLD_STRIPE : new Color(180, 120, 0));
            badge.setHorizontalAlignment(SwingConstants.CENTER);
            cell.add(badge, BorderLayout.CENTER);
        }

        // Click only on AVAILABLE and not dimmed
        if (p.getStatus() == PropertyStatus.AVAILABLE && !dimmed) {
            cell.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cell.setBackground(new Color(200, 240, 255));
                    cell.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 220), 2));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    cell.setBackground(AVAIL_BG);
                    cell.setBorder(BorderFactory.createLineBorder(borderColor, bw));
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    openLotDetail(p);
                }
            });
        }

        return cell;
    }

    private void openLotDetail(Property p) {
        LotDetailFrame detail = new LotDetailFrame(p, this);
        detail.setVisible(true);
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────

    private JLabel boldLabel(String text, int size) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", Font.BOLD, size));
        l.setForeground(DARK);
        return l;
    }

    private JLabel dimLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", Font.PLAIN, 11));
        l.setForeground(TEXT_DIM);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JTextField sideField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Courier New", Font.PLAIN, 12));
        f.setForeground(DARK);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        f.setAlignmentX(LEFT_ALIGNMENT);
        // Placeholder effect
        f.setText(placeholder);
        f.setForeground(TEXT_DIM);
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(DARK); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(TEXT_DIM); }
            }
        });
        return f;
    }

    private JSeparator makeDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 220, 220));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JPanel makeLegendRow(Color color, Color border, String label) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        JPanel swatch = new JPanel();
        swatch.setBackground(color);
        swatch.setBorder(BorderFactory.createLineBorder(border, 1));
        swatch.setPreferredSize(new Dimension(18, 18));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Courier New", Font.PLAIN, 11));
        lbl.setForeground(DARK);
        row.add(swatch);
        row.add(lbl);
        return row;
    }

    // ── Action handlers ────────────────────────────────────────────────────────

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {
        AppState.currentCustomer = null;
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
        java.awt.EventQueue.invokeLater(() -> new CustomerMapFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // (all declared as fields above)
    // End of variables declaration//GEN-END:variables
}