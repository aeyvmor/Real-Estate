package fiestaSystem.ui;

import fiestaSystem.AppState;
import fiestaSystem.model.Property;
import fiestaSystem.enums.HouseType;
import fiestaSystem.enums.PropertyStatus;
import fiestaSystem.payment.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class LotDetailFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(LotDetailFrame.class.getName());

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_PINK   = new Color(255, 105, 180);
    private static final Color BG_WHITE  = new Color(255, 255, 255);
    private static final Color DARK      = new Color(10, 10, 10);
    private static final Color YELLOW    = new Color(255, 213, 0);
    private static final Color GREEN     = new Color(34, 197, 94);
    private static final Color RED_LABEL = new Color(200, 30, 30);
    private static final Color TEXT_GRAY = new Color(100, 100, 100);
    private static final Color BORDER_LT = new Color(220, 220, 220);
    private static final Color IMG_PH    = new Color(200, 220, 240); // placeholder bg

    // ── Data ──────────────────────────────────────────────────────────────────
    private final Property         property;
    private final CustomerMapFrame parent;

    // ── Components ────────────────────────────────────────────────────────────
    private JLabel  propIdLabel, basePriceLabel, sqmLabel, facingLabel;
    private JLabel  floorSqmLabel, bedroomsLabel, bathroomsLabel, processingFeeLabel;
    private JButton cashBtn, financingBtn;
    private JRadioButton bdoRadio, pagibigRadio, inhouseRadio;
    private ButtonGroup  payGroup;
    private JSlider downpaySlider, termSlider;
    private JLabel  downpayLabel, termLabel;
    // Breakdown labels
    private JLabel  lbFinalPrice, lbProcessingFee, lbReservFee,
                    lbBalanceFee, lbPrincipal, lbRate,
                    lbMonthlyPI, lbMonthlyTotal;
    private JPanel  financingOptionsPanel;
    private JButton reserveBtn, purchaseBtn;
    private JLabel  ownedBanner;
    private boolean isCash = true;

    public LotDetailFrame(Property property, CustomerMapFrame parent) {
        this.property = property;
        this.parent   = parent;
        initComponents();
        recalculate();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("LOT DETAIL — " + property.getId());
        setPreferredSize(new Dimension(860, 700));
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
        leftPanel.setPreferredSize(new Dimension(340, 680));

        // ── Header card ───────────────────────────────────────────────────────
        JPanel headerCard = new JPanel();
        headerCard.setBackground(BG_WHITE);
        headerCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK, 3),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        headerCard.setLayout(new BorderLayout(6, 4));

        propIdLabel = new JLabel(property.getId());
        propIdLabel.setFont(new Font("Courier New", Font.BOLD, 26));
        propIdLabel.setForeground(DARK);

        String typeStr = property.getHouseType() != null
                ? property.getHouseType().displayName : "Unknown Type";
        basePriceLabel = new JLabel("BASE: ₱" + String.format("%,.0f", property.getPrice())
                + "   •   " + typeStr);
        basePriceLabel.setFont(new Font("Courier New", Font.BOLD, 11));
        basePriceLabel.setBackground(YELLOW);
        basePriceLabel.setForeground(DARK);
        basePriceLabel.setOpaque(true);
        basePriceLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        JPanel specRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        specRow.setOpaque(false);
        sqmLabel     = tagLabel("LOT: " + property.getSqm() + " sqm");
        facingLabel  = tagLabel("FACING: " + property.getFacing());
        specRow.add(sqmLabel);
        specRow.add(facingLabel);

        headerCard.add(propIdLabel,    BorderLayout.NORTH);
        headerCard.add(specRow,        BorderLayout.CENTER);
        headerCard.add(basePriceLabel, BorderLayout.SOUTH);

        // ── House image panel ─────────────────────────────────────────────────
        JPanel imageCard = new JPanel(new BorderLayout(0, 6));
        imageCard.setBackground(BG_WHITE);
        imageCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel imgArea = buildImagePanel();
        imageCard.add(imgArea, BorderLayout.CENTER);

        JPanel specsCard = buildSpecsCard();
        imageCard.add(specsCard, BorderLayout.SOUTH);

        leftPanel.add(headerCard, BorderLayout.NORTH);
        leftPanel.add(imageCard,  BorderLayout.CENTER);

        // ══════════════════════════════════════════════════════════════════════
        // RIGHT PANEL — FINANCIAL TERMINAL
        // ══════════════════════════════════════════════════════════════════════
        JPanel rightPanel = new JPanel(new BorderLayout(0, 0));
        rightPanel.setBackground(BG_WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK, 3),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        JPanel rightInner = new JPanel();
        rightInner.setBackground(BG_WHITE);
        rightInner.setLayout(new BoxLayout(rightInner, BoxLayout.Y_AXIS));

        // ── Owned reservation banner (hidden unless applicable) ───────────────
        ownedBanner = new JLabel("  ★ YOUR RESERVED UNIT — Proceed to Purchase Below");
        ownedBanner.setFont(new Font("Courier New", Font.BOLD, 11));
        ownedBanner.setBackground(new Color(34, 197, 94));
        ownedBanner.setForeground(Color.WHITE);
        ownedBanner.setOpaque(true);
        ownedBanner.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        ownedBanner.setAlignmentX(LEFT_ALIGNMENT);
        ownedBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        ownedBanner.setVisible(isOwnedReservation());

        // ── FT Header ─────────────────────────────────────────────────────────
        JLabel ftTitle = new JLabel("🖩  FINANCIAL TERMINAL");
        ftTitle.setFont(new Font("Courier New", Font.BOLD, 15));
        ftTitle.setForeground(DARK);
        ftTitle.setAlignmentX(LEFT_ALIGNMENT);
        ftTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LT));

        // ── Cash / Financing toggle ────────────────────────────────────────────
        JPanel payToggle = new JPanel(new GridLayout(1, 2, 0, 0));
        payToggle.setBorder(BorderFactory.createLineBorder(DARK, 2));
        payToggle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        payToggle.setAlignmentX(LEFT_ALIGNMENT);

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

        // ── Financing sub-options ──────────────────────────────────────────────
        financingOptionsPanel = new JPanel(new GridLayout(0, 1, 0, 3));
        financingOptionsPanel.setBackground(BG_WHITE);
        financingOptionsPanel.setAlignmentX(LEFT_ALIGNMENT);
        financingOptionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        payGroup     = new ButtonGroup();
        bdoRadio     = styledRadio("BDO (7.25%)");
        pagibigRadio = styledRadio("PAG-IBIG");
        inhouseRadio = styledRadio("In-House (12%)");
        payGroup.add(bdoRadio);
        payGroup.add(pagibigRadio);
        payGroup.add(inhouseRadio);
        bdoRadio.setSelected(true);
        bdoRadio.addActionListener(e -> recalculate());
        pagibigRadio.addActionListener(e -> recalculate());
        inhouseRadio.addActionListener(e -> recalculate());

        // GridLayout(1,3) ensures all 3 buttons always fit on one row
        JPanel radioRow = new JPanel(new GridLayout(1, 3, 4, 0));
        radioRow.setBackground(BG_WHITE);
        radioRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        radioRow.add(bdoRadio);
        radioRow.add(pagibigRadio);
        radioRow.add(inhouseRadio);

        downpayLabel = new JLabel("DOWNPAYMENT (20%)      ₱0");
        downpayLabel.setFont(new Font("Courier New", Font.PLAIN, 11));
        downpayLabel.setForeground(DARK);

        downpaySlider = new JSlider(10, 50, 20);
        downpaySlider.setBackground(BG_WHITE);
        downpaySlider.addChangeListener((ChangeEvent e) -> recalculate());

        termLabel = new JLabel("LOAN TERM   20 YEARS");
        termLabel.setFont(new Font("Courier New", Font.PLAIN, 11));
        termLabel.setForeground(DARK);

        termSlider = new JSlider(5, 30, 20);
        termSlider.setBackground(BG_WHITE);
        termSlider.addChangeListener((ChangeEvent e) -> recalculate());

        financingOptionsPanel.add(radioRow);
        financingOptionsPanel.add(downpayLabel);
        financingOptionsPanel.add(downpaySlider);
        financingOptionsPanel.add(termLabel);
        financingOptionsPanel.add(termSlider);
        financingOptionsPanel.setVisible(false);

        // ── Cost Breakdown ────────────────────────────────────────────────────
        JPanel breakdownOuter = new JPanel(new BorderLayout());
        breakdownOuter.setBackground(BG_WHITE);
        breakdownOuter.setAlignmentX(LEFT_ALIGNMENT);

        JLabel breakTitle = new JLabel(" COST BREAKDOWN");
        breakTitle.setFont(new Font("Courier New", Font.BOLD, 12));
        breakTitle.setBackground(DARK);
        breakTitle.setForeground(Color.WHITE);
        breakTitle.setOpaque(true);
        breakTitle.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JPanel breakRows = new JPanel(new GridLayout(0, 1, 0, 3));
        breakRows.setBackground(BG_WHITE);
        breakRows.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));

        lbFinalPrice    = breakdownRow();
        lbProcessingFee = breakdownRow();
        lbReservFee     = breakdownRow();
        lbBalanceFee    = breakdownRow();
        lbPrincipal     = breakdownRow();
        lbRate          = breakdownRow();
        lbMonthlyPI     = breakdownRow();

        breakRows.add(lbFinalPrice);
        breakRows.add(lbProcessingFee);
        breakRows.add(lbReservFee);
        breakRows.add(lbBalanceFee);
        breakRows.add(new JSeparator());
        breakRows.add(lbPrincipal);
        breakRows.add(lbRate);
        breakRows.add(lbMonthlyPI);

        // Monthly total highlight bar
        JPanel monthlyBar = new JPanel(new BorderLayout());
        monthlyBar.setBackground(GREEN);
        monthlyBar.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        monthlyBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        monthlyBar.setAlignmentX(LEFT_ALIGNMENT);
        JLabel estLbl = new JLabel("EST. MONTHLY");
        estLbl.setFont(new Font("Courier New", Font.BOLD, 13));
        estLbl.setForeground(DARK);
        lbMonthlyTotal = new JLabel("₱0");
        lbMonthlyTotal.setFont(new Font("Courier New", Font.BOLD, 18));
        lbMonthlyTotal.setForeground(DARK);
        monthlyBar.add(estLbl,         BorderLayout.WEST);
        monthlyBar.add(lbMonthlyTotal, BorderLayout.EAST);

        breakdownOuter.add(breakTitle, BorderLayout.NORTH);
        breakdownOuter.add(breakRows,  BorderLayout.CENTER);

        // ── Action buttons ────────────────────────────────────────────────────
        JPanel actionRow = new JPanel(new GridLayout(1, 2, 8, 0));
        actionRow.setBackground(BG_WHITE);
        actionRow.setAlignmentX(LEFT_ALIGNMENT);
        actionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        reserveBtn = new JButton();   // text set in recalculate()
        reserveBtn.setFont(new Font("Courier New", Font.BOLD, 12));
        reserveBtn.setBackground(YELLOW);
        reserveBtn.setForeground(DARK);
        reserveBtn.setBorder(BorderFactory.createLineBorder(DARK, 2));
        reserveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reserveBtn.setFocusPainted(false);
        reserveBtn.addActionListener(this::reserveBtnActionPerformed);

        purchaseBtn = new JButton("$ INITIATE PURCHASE");
        purchaseBtn.setFont(new Font("Courier New", Font.BOLD, 12));
        purchaseBtn.setBackground(DARK);
        purchaseBtn.setForeground(Color.WHITE);
        purchaseBtn.setBorder(BorderFactory.createLineBorder(DARK, 2));
        purchaseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        purchaseBtn.setFocusPainted(false);
        purchaseBtn.addActionListener(this::purchaseBtnActionPerformed);

        actionRow.add(reserveBtn);
        actionRow.add(purchaseBtn);

        // Assemble right inner
        rightInner.add(ownedBanner);
        rightInner.add(Box.createVerticalStrut(4));
        rightInner.add(ftTitle);
        rightInner.add(Box.createVerticalStrut(6));
        rightInner.add(payToggle);
        rightInner.add(Box.createVerticalStrut(4));
        rightInner.add(financingOptionsPanel);
        rightInner.add(Box.createVerticalStrut(4));
        rightInner.add(breakdownOuter);
        rightInner.add(Box.createVerticalStrut(4));
        rightInner.add(monthlyBar);
        rightInner.add(Box.createVerticalStrut(8));
        rightInner.add(new JSeparator());
        rightInner.add(Box.createVerticalStrut(8));
        rightInner.add(actionRow);

        rightPanel.add(rightInner, BorderLayout.NORTH);

        outerPad.add(leftPanel,  BorderLayout.WEST);
        outerPad.add(rightPanel, BorderLayout.CENTER);
        getContentPane().add(outerPad, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // ── Image panel ────────────────────────────────────────────────────────────

    private JPanel buildImagePanel() {
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setBackground(IMG_PH);
        imgPanel.setPreferredSize(new Dimension(320, 200));

        HouseType ht = property.getHouseType();
        if (ht != null) {
            String fileName = ht.getImageFileName();
            if (fileName != null) {
                URL imgUrl = getClass().getResource("images/" + fileName);
                if (imgUrl != null) {
                    ImageIcon icon = new ImageIcon(imgUrl);
                    Image scaled = icon.getImage().getScaledInstance(320, 200, Image.SCALE_SMOOTH);
                    JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                    imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    imgPanel.setBackground(Color.BLACK);
                    imgPanel.add(imgLabel, BorderLayout.CENTER);
                    return imgPanel;
                }
            }
        }

        // Fallback placeholder
        imgPanel.setBackground(IMG_PH);
        JPanel ph = new JPanel();
        ph.setBackground(IMG_PH);
        ph.setLayout(new BoxLayout(ph, BoxLayout.Y_AXIS));
        JLabel icon = new JLabel("🏠");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        icon.setAlignmentX(CENTER_ALIGNMENT);
        JLabel msg = new JLabel("Image not found");
        msg.setFont(new Font("Courier New", Font.ITALIC, 11));
        msg.setForeground(TEXT_GRAY);
        msg.setAlignmentX(CENTER_ALIGNMENT);
        JLabel hint = new JLabel("Place " + (property.getHouseType() != null
                ? property.getHouseType().getImageFileName() : "image")
                + " in ui/images/");
        hint.setFont(new Font("Courier New", Font.PLAIN, 10));
        hint.setForeground(TEXT_GRAY);
        hint.setAlignmentX(CENTER_ALIGNMENT);
        ph.add(Box.createVerticalGlue());
        ph.add(icon);
        ph.add(Box.createVerticalStrut(6));
        ph.add(msg);
        ph.add(hint);
        ph.add(Box.createVerticalGlue());
        imgPanel.add(ph, BorderLayout.CENTER);
        return imgPanel;
    }

    private JPanel buildSpecsCard() {
        HouseType ht = property.getHouseType();
        JPanel card = new JPanel(new GridLayout(0, 2, 4, 4));
        card.setBackground(new Color(248, 248, 248));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_LT),
                BorderFactory.createEmptyBorder(8, 4, 4, 4)));

        card.add(specLabel("Lot Area:"));
        card.add(specValue(property.getSqm() + " sqm"));

        if (ht != null) {
            card.add(specLabel("Floor Area:"));
            card.add(specValue(ht.floorSqm + " sqm"));
            card.add(specLabel("Bedrooms:"));
            card.add(specValue(String.valueOf(ht.bedrooms)));
            card.add(specLabel("Bathrooms:"));
            card.add(specValue(String.valueOf(ht.bathrooms)));
        }

        card.add(specLabel("Facing:"));
        card.add(specValue(property.getFacing()));
        card.add(specLabel("Block:"));
        card.add(specValue("Block " + property.getBlockNumber()));

        return card;
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private boolean isOwnedReservation() {
        return property.getStatus() == PropertyStatus.RESERVED
            && AppState.currentCustomer != null
            && AppState.currentCustomer.ownsProperty(property);
    }

    // ── Calculation ────────────────────────────────────────────────────────────

    private void recalculate() {
        HouseType ht        = property.getHouseType();
        double finalPrice   = property.getPrice();
        double resvFee      = property.getReservationFee();
        double procFee      = property.getProcessingFee();
        double balanceFee   = procFee - resvFee;       // balance after reservation

        // Update reserve button — disable if this is already the customer's reserved unit
        if (isOwnedReservation()) {
            reserveBtn.setText("✓ ALREADY RESERVED");
            reserveBtn.setEnabled(false);
            reserveBtn.setBackground(new Color(180, 180, 180));
            reserveBtn.setForeground(Color.WHITE);
        } else {
            reserveBtn.setText("RESERVE  (₱" + String.format("%,.0f", resvFee) + ")");
            reserveBtn.setEnabled(true);
            reserveBtn.setBackground(YELLOW);
            reserveBtn.setForeground(DARK);
        }

        // Fill fixed rows (same for cash and financing)
        setRow(lbFinalPrice,    "Total Contract Price:",
                "₱" + fmt(finalPrice), RED_LABEL);
        setRow(lbProcessingFee, "Processing Fee:",
                "₱" + fmt(procFee), TEXT_GRAY);
        setRow(lbReservFee,     "Less Reservation Fee:",
                "₱" + fmt(resvFee), TEXT_GRAY);
        setRow(lbBalanceFee,    "Balance Proc. Fee (6 mos):",
                "₱" + fmt(balanceFee), TEXT_GRAY);

        if (isCash) {
            setRow(lbPrincipal,  "Principal:",     "N/A — full cash", TEXT_GRAY);
            setRow(lbRate,       "Interest Rate:", "0%", TEXT_GRAY);
            setRow(lbMonthlyPI,  "Mortgage P&I:",  "₱0.00 /mo", TEXT_GRAY);
            lbMonthlyTotal.setText("CASH — ₱" + fmt(finalPrice));
        } else {
            int    downPct    = downpaySlider.getValue();
            int    years      = termSlider.getValue();
            double down       = finalPrice * downPct / 100.0;
            double principal  = finalPrice - down;

            downpayLabel.setText(String.format("DOWNPAYMENT (%d%%)      ₱%s",
                    downPct, fmt(down)));
            termLabel.setText(String.format("LOAN TERM   %d YEARS", years));

            PaymentStrategy strategy = getSelectedStrategy(ht);
            double monthly  = strategy.calculateMonthlyPayment(principal, years);
            double rate     = strategy.getInterestRate();

            setRow(lbPrincipal, "Principal (after DP):",
                    "₱" + fmt(principal), TEXT_GRAY);
            setRow(lbRate,      "Rate (" + strategyName(ht) + "):",
                    String.format("%.3f%%", rate * 100), TEXT_GRAY);
            setRow(lbMonthlyPI, "Mortgage P&I:",
                    String.format("₱%s /mo", fmt(monthly)), DARK);
            lbMonthlyTotal.setText(String.format("₱%s /mo", fmt(monthly)));
        }
    }

    private PaymentStrategy getSelectedStrategy(HouseType ht) {
        if (pagibigRadio.isSelected()) {
            double rate = (ht != null) ? ht.getPagIbigRate() : 0.065;
            return new PagIbigLoan(rate);
        }
        if (inhouseRadio.isSelected()) return new InHouseFinancing();
        return new BDOLoan();
    }

    private String strategyName(HouseType ht) {
        if (pagibigRadio.isSelected()) {
            double rate = (ht != null) ? ht.getPagIbigRate() * 100 : 6.5;
            return String.format("PAG-IBIG %.3f%%", rate);
        }
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
        double resvFee = property.getReservationFee();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reserve " + property.getId()
                + " for ₱" + String.format("%,.0f", resvFee) + "?",
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
        // Allow purchase if AVAILABLE, or if it's the customer's own reserved unit
        boolean canPurchase = property.getStatus() == PropertyStatus.AVAILABLE
                           || isOwnedReservation();
        if (!canPurchase) {
            JOptionPane.showMessageDialog(this, "This property is no longer available.",
                    "Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }
        HouseType ht = property.getHouseType();
        PaymentStrategy strategy = isCash ? new CashPayment() : getSelectedStrategy(ht);
        String payLabel = isCash ? "Cash" : strategyName(ht);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Purchase " + property.getId()
                + "\nPrice: ₱" + String.format("%,.2f", property.getPrice())
                + "\nPayment: " + payLabel + "?",
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

    // ── UI Helpers ─────────────────────────────────────────────────────────────

    private void styleCashBtn(JButton b, boolean active) {
        b.setFont(new Font("Courier New", Font.BOLD, 12));
        b.setBackground(active ? DARK : BG_WHITE);
        b.setForeground(active ? Color.WHITE : DARK);
        b.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JRadioButton styledRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(new Font("Courier New", Font.PLAIN, 11));
        rb.setBackground(BG_WHITE);
        return rb;
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

    private JLabel breakdownRow() {
        JLabel l = new JLabel("—");
        l.setFont(new Font("Courier New", Font.PLAIN, 11));
        l.setForeground(TEXT_GRAY);
        return l;
    }

    private void setRow(JLabel lbl, String label, String value, Color valueColor) {
        lbl.setText(String.format("<html><span style='color:#646464'>%-30s</span>"
                + "<b style='color:#%s'>%s</b></html>",
                label,
                String.format("%06X", valueColor.getRGB() & 0xFFFFFF),
                value));
    }

    private JLabel specLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", Font.PLAIN, 11));
        l.setForeground(TEXT_GRAY);
        return l;
    }

    private JLabel specValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", Font.BOLD, 11));
        l.setForeground(DARK);
        return l;
    }

    private String fmt(double v) {
        return String.format("%,.2f", v);
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