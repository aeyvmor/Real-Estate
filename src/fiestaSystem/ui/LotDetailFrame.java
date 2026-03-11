/*
 * LotDetailFrame.java
 * fiestaSystem.ui
 * Lot detail + Financial Terminal.
 * Shows lot info, blueprint placeholder, payment calculator,
 * RESERVE and INITIATE PURCHASE buttons.
 * All logic delegated to AppState.currentCustomer methods.
 */
package fiestaSystem.ui;

import fiestaSystem.AppState;
import fiestaSystem.model.Property;
import fiestaSystem.payment.*;
import fiestaSystem.enums.PropertyStatus;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;

/**
 * @author eevee
 */
public class LotDetailFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(LotDetailFrame.class.getName());

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_PINK    = new Color(255, 105, 180);
    private static final Color BG_WHITE   = new Color(255, 255, 255);
    private static final Color DARK       = new Color(10, 10, 10);
    private static final Color CYAN       = new Color(0, 220, 230);
    private static final Color CYAN_DARK  = new Color(0, 160, 200);
    private static final Color YELLOW     = new Color(255, 213, 0);
    private static final Color GREEN      = new Color(34, 197, 94);
    private static final Color GREEN_BG   = new Color(34, 197, 94);
    private static final Color TEXT_GRAY  = new Color(100, 100, 100);
    private static final Color BORDER_LT  = new Color(220, 220, 220);
    private static final Color BLUEPRINT  = new Color(0, 30, 80);
    private static final Color BP_LINE    = new Color(0, 180, 220);

    // ── Data ─────────────────────────────────────────────────────────────────
    private final Property property;
    private final CustomerMapFrame parent;
    private double finalPrice;

    // ── Components ────────────────────────────────────────────────────────────
    private JLabel  propIdLabel, basePriceLabel, sqmLabel, facingLabel, zoningLabel;
    private JButton cashBtn, financingBtn;
    private JRadioButton bdoRadio, pagibigRadio, inhouseRadio;
    private ButtonGroup payGroup;
    private JSlider downpaySlider, termSlider;
    private JLabel  downpayLabel, termLabel;
    private JLabel  finalPriceVal, principalVal, rateVal, monthlyPIVal, monthlyTotalVal;
    private JPanel  financingOptionsPanel, breakdownPanel;
    private JButton reserveBtn, purchaseBtn;
    private boolean isCash = true;

    public LotDetailFrame(Property property, CustomerMapFrame parent) {
        this.property = property;
        this.parent   = parent;
        this.finalPrice = property.getPrice();
        initComponents();
        recalculate();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("LOT DETAIL — " + property.getId());
        setPreferredSize(new Dimension(820, 680));
        setResizable(false);
        getContentPane().setBackground(BG_PINK);
        getContentPane().setLayout(new BorderLayout(10, 0));

        JPanel outerPad = new JPanel(new BorderLayout(10, 0));
        outerPad.setBackground(BG_PINK);
        outerPad.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ══════════════════════════════════════════════════════════════════════
        // LEFT PANEL
        // ══════════════════════════════════════════════════════════════════════
        JPanel leftPanel = new JPanel(new BorderLayout(0, 8));
        leftPanel.setBackground(BG_PINK);
        leftPanel.setPreferredSize(new Dimension(330, 660));

        // ── Header card ───────────────────────────────────────────────────────
        JPanel headerCard = new JPanel();
        headerCard.setBackground(BG_WHITE);
        headerCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK, 3),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        headerCard.setLayout(new BorderLayout(8, 4));

        propIdLabel = new JLabel(property.getId());
        propIdLabel.setFont(new Font("Courier New", Font.BOLD, 28));
        propIdLabel.setForeground(DARK);

        basePriceLabel = new JLabel("BASE: ₱" + String.format("%,.0f", property.getPrice()));
        basePriceLabel.setFont(new Font("Courier New", Font.BOLD, 13));
        basePriceLabel.setBackground(YELLOW);
        basePriceLabel.setForeground(DARK);
        basePriceLabel.setOpaque(true);
        basePriceLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        JPanel specTags = new JPanel(new GridLayout(3, 1, 0, 2));
        specTags.setOpaque(false);
        sqmLabel    = tagLabel("SQM: " + property.getSqm());
        facingLabel = tagLabel("FACING: " + property.getFacing());
        zoningLabel = tagLabel("ZONING: RES-" + property.getBlockNumber());
        specTags.add(sqmLabel);
        specTags.add(facingLabel);
        specTags.add(zoningLabel);

        JPanel headerTop = new JPanel(new BorderLayout());
        headerTop.setOpaque(false);
        headerTop.add(propIdLabel, BorderLayout.WEST);
        headerTop.add(specTags, BorderLayout.EAST);

        headerCard.add(headerTop, BorderLayout.NORTH);
        headerCard.add(basePriceLabel, BorderLayout.SOUTH);

        // ── Blueprint panel ───────────────────────────────────────────────────
        JPanel blueprintCard = new JPanel(new BorderLayout(0, 6));
        blueprintCard.setBackground(BG_WHITE);
        blueprintCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK, 3),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JPanel bpHeader = new JPanel(new BorderLayout());
        bpHeader.setOpaque(false);
        JLabel bpTitle = new JLabel("🏠 BLUEPRINT EXPLORER");
        bpTitle.setFont(new Font("Courier New", Font.BOLD, 13));
        bpTitle.setForeground(DARK);
        bpHeader.add(bpTitle, BorderLayout.WEST);

        JPanel bpCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBlueprint(g);
            }
        };
        bpCanvas.setBackground(BLUEPRINT);
        bpCanvas.setPreferredSize(new Dimension(280, 200));
        bpCanvas.setBorder(BorderFactory.createLineBorder(CYAN_DARK, 1));

        blueprintCard.add(bpHeader, BorderLayout.NORTH);
        blueprintCard.add(bpCanvas, BorderLayout.CENTER);

        leftPanel.add(headerCard, BorderLayout.NORTH);
        leftPanel.add(blueprintCard, BorderLayout.CENTER);

        // ══════════════════════════════════════════════════════════════════════
        // RIGHT PANEL — FINANCIAL TERMINAL
        // ══════════════════════════════════════════════════════════════════════
        JPanel rightPanel = new JPanel(new BorderLayout(0, 8));
        rightPanel.setBackground(BG_WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK, 3),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        // ── FT Header ─────────────────────────────────────────────────────────
        JLabel ftTitle = new JLabel("🖩  FINANCIAL TERMINAL");
        ftTitle.setFont(new Font("Courier New", Font.BOLD, 16));
        ftTitle.setForeground(DARK);
        ftTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LT));

        // ── Payment method toggle ─────────────────────────────────────────────
        JPanel payToggle = new JPanel(new GridLayout(1, 2, 0, 0));
        payToggle.setBorder(BorderFactory.createLineBorder(DARK, 2));
        payToggle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        cashBtn      = new JButton("CASH");
        financingBtn = new JButton("FINANCING");
        styleCashBtn(cashBtn, true);
        styleCashBtn(financingBtn, false);

        cashBtn.addActionListener(e -> {
            isCash = true;
            styleCashBtn(cashBtn, true);
            styleCashBtn(financingBtn, false);
            financingOptionsPanel.setVisible(false);
            recalculate();
        });
        financingBtn.addActionListener(e -> {
            isCash = false;
            styleCashBtn(cashBtn, false);
            styleCashBtn(financingBtn, true);
            financingOptionsPanel.setVisible(true);
            recalculate();
        });
        payToggle.add(cashBtn);
        payToggle.add(financingBtn);

        // ── Financing sub-options ─────────────────────────────────────────────
        financingOptionsPanel = new JPanel(new GridLayout(0, 1, 0, 4));
        financingOptionsPanel.setBackground(BG_WHITE);
        financingOptionsPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        payGroup     = new ButtonGroup();
        bdoRadio     = new JRadioButton("BDO Home Loan");
        pagibigRadio = new JRadioButton("PAG-IBIG");
        inhouseRadio = new JRadioButton("In-House");
        for (JRadioButton rb : new JRadioButton[]{bdoRadio, pagibigRadio, inhouseRadio}) {
            rb.setFont(new Font("Courier New", Font.PLAIN, 12));
            rb.setBackground(BG_WHITE);
            payGroup.add(rb);
            rb.addActionListener(e -> recalculate());
        }
        bdoRadio.setSelected(true);

        JPanel radioRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        radioRow.setBackground(BG_WHITE);
        radioRow.add(bdoRadio);
        radioRow.add(pagibigRadio);
        radioRow.add(inhouseRadio);
        financingOptionsPanel.add(radioRow);

        // ── Sliders ───────────────────────────────────────────────────────────
        downpaySlider = new JSlider(10, 50, 20);
        downpaySlider.setBackground(BG_WHITE);
        downpaySlider.addChangeListener((ChangeEvent e) -> recalculate());

        termSlider = new JSlider(5, 30, 20);
        termSlider.setBackground(BG_WHITE);
        termSlider.addChangeListener((ChangeEvent e) -> recalculate());

        downpayLabel = new JLabel("DOWNPAYMENT (20%)      ₱0");
        downpayLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
        downpayLabel.setForeground(DARK);

        termLabel = new JLabel("LOAN TERM                          20 YEARS");
        termLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
        termLabel.setForeground(DARK);

        JPanel sliderPanel = new JPanel(new GridLayout(0, 1, 0, 2));
        sliderPanel.setBackground(BG_WHITE);
        sliderPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER_LT));
        sliderPanel.add(downpayLabel);
        sliderPanel.add(downpaySlider);
        sliderPanel.add(termLabel);
        sliderPanel.add(termSlider);
        financingOptionsPanel.add(sliderPanel);
        financingOptionsPanel.setVisible(false);

        // ── Cost breakdown ────────────────────────────────────────────────────
        breakdownPanel = new JPanel(new GridLayout(0, 1, 0, 3));
        breakdownPanel.setBackground(BG_WHITE);
        breakdownPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, DARK),
                BorderFactory.createEmptyBorder(8, 0, 8, 0)));

        JLabel breakdownTitle = new JLabel("COST BREAKDOWN");
        breakdownTitle.setFont(new Font("Courier New", Font.BOLD, 13));
        breakdownTitle.setBackground(DARK);
        breakdownTitle.setForeground(Color.WHITE);
        breakdownTitle.setOpaque(true);
        breakdownTitle.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        finalPriceVal  = breakdownRow("Final Price:");
        principalVal   = breakdownRow("Principal:");
        rateVal        = breakdownRow("Rate:");
        monthlyPIVal   = breakdownRow("Mortgage P&I:");

        // Monthly total highlight
        JPanel monthlyTotal = new JPanel(new BorderLayout());
        monthlyTotal.setBackground(GREEN_BG);
        monthlyTotal.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JLabel estLbl = new JLabel("EST. MONTHLY");
        estLbl.setFont(new Font("Courier New", Font.BOLD, 13));
        estLbl.setForeground(DARK);
        monthlyTotalVal = new JLabel("₱0");
        monthlyTotalVal.setFont(new Font("Courier New", Font.BOLD, 16));
        monthlyTotalVal.setForeground(DARK);
        monthlyTotal.add(estLbl, BorderLayout.WEST);
        monthlyTotal.add(monthlyTotalVal, BorderLayout.EAST);

        breakdownPanel.add(breakdownTitle);
        breakdownPanel.add(finalPriceVal);
        breakdownPanel.add(principalVal);
        breakdownPanel.add(rateVal);
        breakdownPanel.add(monthlyPIVal);
        breakdownPanel.add(new JSeparator());
        breakdownPanel.add(monthlyTotal);

        // ── Action buttons ────────────────────────────────────────────────────
        JPanel actionRow = new JPanel(new GridLayout(1, 2, 8, 0));
        actionRow.setBackground(BG_WHITE);
        actionRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        reserveBtn = new JButton("RESERVE (₱20K)");
        reserveBtn.setFont(new Font("Courier New", Font.BOLD, 13));
        reserveBtn.setBackground(YELLOW);
        reserveBtn.setForeground(DARK);
        reserveBtn.setBorder(BorderFactory.createLineBorder(DARK, 2));
        reserveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reserveBtn.setFocusPainted(false);
        reserveBtn.addActionListener(this::reserveBtnActionPerformed);

        purchaseBtn = new JButton("$ INITIATE PURCHASE");
        purchaseBtn.setFont(new Font("Courier New", Font.BOLD, 13));
        purchaseBtn.setBackground(DARK);
        purchaseBtn.setForeground(Color.WHITE);
        purchaseBtn.setBorder(BorderFactory.createLineBorder(DARK, 2));
        purchaseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        purchaseBtn.setFocusPainted(false);
        purchaseBtn.addActionListener(this::purchaseBtnActionPerformed);

        actionRow.add(reserveBtn);
        actionRow.add(purchaseBtn);

        // ── Assemble right panel ──────────────────────────────────────────────
        JPanel rightInner = new JPanel();
        rightInner.setBackground(BG_WHITE);
        rightInner.setLayout(new BoxLayout(rightInner, BoxLayout.Y_AXIS));
        rightInner.add(ftTitle);
        rightInner.add(Box.createVerticalStrut(8));
        rightInner.add(payToggle);
        rightInner.add(Box.createVerticalStrut(6));
        rightInner.add(financingOptionsPanel);
        rightInner.add(Box.createVerticalStrut(6));
        rightInner.add(breakdownPanel);
        rightInner.add(actionRow);

        rightPanel.add(rightInner, BorderLayout.CENTER);

        // ── Assemble outer ────────────────────────────────────────────────────
        outerPad.add(leftPanel,  BorderLayout.WEST);
        outerPad.add(rightPanel, BorderLayout.CENTER);
        getContentPane().add(outerPad, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // ── Blueprint drawing ──────────────────────────────────────────────────────

    private void drawBlueprint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = 280, h = 200;
        int pad = 12;

        // Grid lines
        g2.setColor(new Color(0, 60, 120));
        g2.setStroke(new BasicStroke(0.5f));
        for (int x = pad; x < w - pad; x += 16) g2.drawLine(x, pad, x, h - pad);
        for (int y = pad; y < h - pad; y += 16) g2.drawLine(pad, y, w - pad, y);

        // Outer walls
        g2.setColor(BP_LINE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(pad + 8, pad + 8, w - pad * 2 - 16, h - pad * 2 - 16);

        // Rooms
        int ox = pad + 8, oy = pad + 8;
        int fw = w - pad * 2 - 16, fh = h - pad * 2 - 16;

        g2.setStroke(new BasicStroke(1.5f));
        // Living (top-left)
        g2.drawRect(ox, oy, fw / 2, (int)(fh * 0.55));
        drawRoomLabel(g2, "LIVING", ox + fw / 4, oy + (int)(fh * 0.27));

        // Kitchen (top-right)
        g2.drawRect(ox + fw / 2, oy, fw / 2, (int)(fh * 0.55));
        drawRoomLabel(g2, "KITCHEN", ox + (int)(fw * 0.75), oy + (int)(fh * 0.27));

        // Garage/Utilities (bottom)
        g2.drawRect(ox, oy + (int)(fh * 0.55), fw, (int)(fh * 0.45));
        drawRoomLabel(g2, "GARAGE / UTILITIES", ox + fw / 2, oy + (int)(fh * 0.775));
    }

    private void drawRoomLabel(Graphics2D g2, String text, int cx, int cy) {
        g2.setColor(BP_LINE);
        g2.setFont(new Font("Courier New", Font.BOLD, 9));
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text);
        g2.drawString(text, cx - tw / 2, cy + fm.getAscent() / 2);
    }

    // ── Calculation ───────────────────────────────────────────────────────────

    private void recalculate() {
        double price = property.getPrice();

        if (isCash) {
            // Cash: no monthly payment
            updateBreakdownRow(finalPriceVal,  "Final Price:",   String.format("₱%,.2f", price));
            updateBreakdownRow(principalVal,   "Principal:",     "N/A (full cash)");
            updateBreakdownRow(rateVal,        "Rate:",          "0%");
            updateBreakdownRow(monthlyPIVal,   "Mortgage P&I:",  "₱0.00 /mo");
            monthlyTotalVal.setText("CASH — ₱" + String.format("%,.0f", price));
        } else {
            // Financing
            int downPct  = downpaySlider.getValue();
            int years    = termSlider.getValue();
            double down  = price * downPct / 100.0;
            double principal = price - down;
            PaymentStrategy strategy = getSelectedStrategy();
            double monthly = strategy.calculateMonthlyPayment(principal, years);
            double rate    = strategy.getInterestRate();

            downpayLabel.setText(String.format("DOWNPAYMENT (%d%%)      ₱%,.0f", downPct, down));
            termLabel.setText(String.format("LOAN TERM   %28s", years + " YEARS"));

            updateBreakdownRow(finalPriceVal, "Final Price:",  String.format("₱%,.2f", price));
            updateBreakdownRow(principalVal,  "Principal:",    String.format("₱%,.2f", principal));
            updateBreakdownRow(rateVal,       "Rate (" + strategyName() + "):", String.format("%.2f%%", rate * 100));
            updateBreakdownRow(monthlyPIVal,  "Mortgage P&I:", String.format("₱%,.2f /mo", monthly));
            monthlyTotalVal.setText(String.format("₱%,.0f /mo", monthly));
        }
    }

    private PaymentStrategy getSelectedStrategy() {
        if (pagibigRadio.isSelected()) return new PagIbigLoan();
        if (inhouseRadio.isSelected()) return new InHouseFinancing();
        return new BDOLoan();
    }

    private String strategyName() {
        if (pagibigRadio.isSelected()) return "PAG-IBIG";
        if (inhouseRadio.isSelected()) return "In-House";
        return "BDO";
    }

    // ── Action handlers ────────────────────────────────────────────────────────

    private void reserveBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (AppState.currentCustomer == null) return;
        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            JOptionPane.showMessageDialog(this, "This property is no longer available.",
                    "Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reserve " + property.getId() + " for ₱20,000?",
                "Confirm Reservation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            AppState.currentCustomer.submitReservation(property, AppState.agent);
            JOptionPane.showMessageDialog(this,
                    "Reservation submitted!\nAwaiting agent approval.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            parent.refreshGrid();
            dispose();
        }
    }

    private void purchaseBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (AppState.currentCustomer == null) return;
        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            JOptionPane.showMessageDialog(this, "This property is no longer available.",
                    "Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }
        PaymentStrategy strategy = isCash ? new CashPayment() : getSelectedStrategy();
        String payLabel = isCash ? "Cash" : strategyName();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Purchase " + property.getId() +
                " for ₱" + String.format("%,.2f", property.getPrice()) +
                "\nPayment method: " + payLabel + "?",
                "Confirm Purchase", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            AppState.currentCustomer.submitPurchase(property, AppState.agent, strategy);
            JOptionPane.showMessageDialog(this,
                    "Purchase submitted!\nAwaiting agent approval.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            parent.refreshGrid();
            dispose();
        }
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────

    private void styleCashBtn(JButton b, boolean active) {
        b.setFont(new Font("Courier New", Font.BOLD, 13));
        b.setBackground(active ? DARK : BG_WHITE);
        b.setForeground(active ? Color.WHITE : DARK);
        b.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JLabel tagLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", Font.PLAIN, 11));
        l.setBackground(new Color(240, 240, 240));
        l.setForeground(DARK);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)));
        return l;
    }

    private JLabel breakdownRow(String label) {
        JLabel l = new JLabel(label + "   —");
        l.setFont(new Font("Courier New", Font.PLAIN, 12));
        l.setForeground(TEXT_GRAY);
        return l;
    }

    private void updateBreakdownRow(JLabel lbl, String label, String value) {
        lbl.setText(String.format("%-22s %s", label, value));
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
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // (all declared as fields above)
    // End of variables declaration//GEN-END:variables
}
