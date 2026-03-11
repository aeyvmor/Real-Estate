/*
 * AgentFrame.java
 * fiestaSystem.ui
 * SALES CONTROLLER — Agent dashboard.
 * Action queue (pending transactions with approve/reject),
 * Transaction history panel.
 * All logic delegated to AppState.agent methods.
 */
package fiestaSystem.ui;

import fiestaSystem.AppState;
import fiestaSystem.model.Transaction;
import fiestaSystem.enums.TransactionStatus;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * @author eevee
 */
public class AgentFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AgentFrame.class.getName());

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(37, 99, 235);
    private static final Color CARD_BG   = new Color(255, 255, 255);
    private static final Color DARK      = new Color(10, 10, 10);
    private static final Color GREEN     = new Color(34, 197, 94);
    private static final Color RED       = new Color(239, 68, 68);
    private static final Color YELLOW    = new Color(253, 224, 71);
    private static final Color SAND      = new Color(253, 230, 196);
    private static final Color TEXT_GRAY = new Color(100, 116, 139);
    private static final Color HIST_BG   = new Color(248, 250, 252);

    // ── Components ────────────────────────────────────────────────────────────
    private JPanel    topBar, mainRow, queuePanel, historyPanel;
    private JLabel    titleLabel, pendingBadge;
    private JButton   logoutBtn, refreshBtn;
    private JPanel    queueScroll, historyScroll;

    public AgentFrame() {
        initComponents();
        refreshAll();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("FIESTA HOMES — SALES CONTROLLER");
        setPreferredSize(new Dimension(980, 600));
        setResizable(false);
        getContentPane().setBackground(BG);
        getContentPane().setLayout(new BorderLayout());

        // ── TOP BAR ──────────────────────────────────────────────────────────
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(CARD_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, DARK));
        topBar.setPreferredSize(new Dimension(980, 70));

        // Left: icon + title
        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
        topLeft.setOpaque(false);

        JLabel iconLbl = new JLabel("🧳");
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLbl.setBackground(new Color(255, 120, 30));
        iconLbl.setOpaque(true);
        iconLbl.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleLabel = new JLabel("SALES CONTROLLER");
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 22));
        titleLabel.setForeground(DARK);
        JLabel subLbl = new JLabel("Agent View");
        subLbl.setFont(new Font("Courier New", Font.PLAIN, 12));
        subLbl.setForeground(TEXT_GRAY);
        titleBlock.add(titleLabel);
        titleBlock.add(subLbl);

        topLeft.add(iconLbl);
        topLeft.add(titleBlock);
        topBar.add(topLeft, BorderLayout.WEST);

        // Right: badge + buttons
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 18));
        topRight.setOpaque(false);

        pendingBadge = new JLabel("0 PENDING ACTION(S)");
        pendingBadge.setFont(new Font("Courier New", Font.BOLD, 12));
        pendingBadge.setBackground(YELLOW);
        pendingBadge.setForeground(DARK);
        pendingBadge.setOpaque(true);
        pendingBadge.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        refreshBtn = makeBtn("REFRESH", DARK, CARD_BG, 90, 30);
        refreshBtn.addActionListener(e -> refreshAll());

        logoutBtn = makeBtn("LOGOUT", RED, CARD_BG, 80, 30);
        logoutBtn.addActionListener(this::logoutBtnActionPerformed);

        topRight.add(pendingBadge);
        topRight.add(refreshBtn);
        topRight.add(logoutBtn);
        topBar.add(topRight, BorderLayout.EAST);
        getContentPane().add(topBar, BorderLayout.NORTH);

        // ── MAIN ROW ─────────────────────────────────────────────────────────
        mainRow = new JPanel(new GridLayout(1, 2, 10, 0));
        mainRow.setBackground(BG);
        mainRow.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // ── LEFT: ACTION QUEUE ────────────────────────────────────────────────
        queuePanel = new JPanel(new BorderLayout(0, 8));
        queuePanel.setBackground(BG);

        JLabel queueTitle = sectionLabel("ACTION QUEUE");
        queuePanel.add(queueTitle, BorderLayout.NORTH);

        queueScroll = new JPanel();
        queueScroll.setLayout(new BoxLayout(queueScroll, BoxLayout.Y_AXIS));
        queueScroll.setBackground(BG);

        JScrollPane qsp = new JScrollPane(queueScroll);
        qsp.setBackground(BG);
        qsp.getViewport().setBackground(BG);
        qsp.setBorder(BorderFactory.createEmptyBorder());
        qsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        queuePanel.add(qsp, BorderLayout.CENTER);

        // ── RIGHT: TRANSACTION HISTORY ────────────────────────────────────────
        historyPanel = new JPanel(new BorderLayout(0, 8));
        historyPanel.setBackground(BG);

        JLabel histTitle = sectionLabel("TRANSACTION HISTORY");
        historyPanel.add(histTitle, BorderLayout.NORTH);

        historyScroll = new JPanel();
        historyScroll.setLayout(new BoxLayout(historyScroll, BoxLayout.Y_AXIS));
        historyScroll.setBackground(HIST_BG);
        historyScroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane hsp = new JScrollPane(historyScroll);
        hsp.setBackground(HIST_BG);
        hsp.getViewport().setBackground(HIST_BG);
        hsp.setBorder(BorderFactory.createLineBorder(DARK, 3));
        hsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        historyPanel.add(hsp, BorderLayout.CENTER);

        mainRow.add(queuePanel);
        mainRow.add(historyPanel);
        getContentPane().add(mainRow, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // ── Refresh logic ─────────────────────────────────────────────────────────

    private void refreshAll() {
        buildQueueCards();
        buildHistoryRows();
        int pending = AppState.agent.getPendingTransactions().size();
        pendingBadge.setText(pending + " PENDING ACTION(S)");
        pendingBadge.setBackground(pending > 0 ? YELLOW : new Color(200, 200, 200));
    }

    private void buildQueueCards() {
        queueScroll.removeAll();
        List<Transaction> pending = AppState.agent.getPendingTransactions();
        if (pending.isEmpty()) {
            JLabel empty = new JLabel("  No pending transactions.");
            empty.setFont(new Font("Courier New", Font.ITALIC, 13));
            empty.setForeground(new Color(200, 220, 255));
            empty.setBorder(BorderFactory.createEmptyBorder(16, 8, 0, 0));
            queueScroll.add(empty);
        } else {
            for (Transaction t : pending) {
                queueScroll.add(makeTxCard(t));
                queueScroll.add(Box.createVerticalStrut(10));
            }
        }
        queueScroll.revalidate();
        queueScroll.repaint();
    }

    private void buildHistoryRows() {
        historyScroll.removeAll();
        List<Transaction> all = AppState.agent.getAllTransactions();
        boolean any = false;
        for (Transaction t : all) {
            if (t.getStatus() != TransactionStatus.PENDING) {
                historyScroll.add(makeHistoryRow(t));
                historyScroll.add(Box.createVerticalStrut(4));
                any = true;
            }
        }
        if (!any) {
            JLabel empty = new JLabel("  No completed transactions yet.");
            empty.setFont(new Font("Courier New", Font.ITALIC, 13));
            empty.setForeground(TEXT_GRAY);
            empty.setBorder(BorderFactory.createEmptyBorder(12, 8, 0, 0));
            historyScroll.add(empty);
        }
        historyScroll.revalidate();
        historyScroll.repaint();
    }

    // ── Card builders ──────────────────────────────────────────────────────────

    private JPanel makeTxCard(Transaction t) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(SAND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK, 3),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        card.setAlignmentX(LEFT_ALIGNMENT);

        // Top row: property ID + amount
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        JLabel propLbl = new JLabel(t.getProperty().getId());
        propLbl.setFont(new Font("Courier New", Font.BOLD, 20));
        propLbl.setForeground(DARK);
        JLabel amtLbl = new JLabel("₱" + String.format("%,.0f", t.getAmount()));
        amtLbl.setFont(new Font("Courier New", Font.BOLD, 18));
        amtLbl.setForeground(GREEN.darker());
        topRow.add(propLbl, BorderLayout.WEST);
        topRow.add(amtLbl, BorderLayout.EAST);

        // Mid row: buyer + fee label
        JPanel midRow = new JPanel(new BorderLayout());
        midRow.setOpaque(false);
        JLabel buyerLbl = new JLabel("Buyer: " + t.getCustomerName());
        buyerLbl.setFont(new Font("Courier New", Font.PLAIN, 12));
        buyerLbl.setForeground(DARK);
        String feeLabel = t.getType().name().equals("RESERVATION") ? "Reservation Fee" : "Purchase";
        JLabel feeLbl = new JLabel(feeLabel);
        feeLbl.setFont(new Font("Courier New", Font.PLAIN, 12));
        feeLbl.setForeground(TEXT_GRAY);
        midRow.add(buyerLbl, BorderLayout.WEST);
        midRow.add(feeLbl, BorderLayout.EAST);

        // Tags
        JPanel tagRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        tagRow.setOpaque(false);
        tagRow.add(makeTag(t.getType().name()));
        tagRow.add(makeTag(t.getPaymentMethod().getClass().getSimpleName().toUpperCase()));

        JSeparator sep = new JSeparator();
        sep.setForeground(DARK);

        // Buttons
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setOpaque(false);
        JButton approveBtn = new JButton("✓  APPROVE");
        styleActionBtn(approveBtn, GREEN, Color.BLACK);
        approveBtn.addActionListener(e -> {
            AppState.agent.approveTransaction(t.getId());
            refreshAll();
        });
        JButton rejectBtn = new JButton("✗  REJECT");
        styleActionBtn(rejectBtn, RED, Color.WHITE);
        rejectBtn.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this,
                    "Reject transaction " + t.getId() + "?", "Confirm Reject",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (conf == JOptionPane.YES_OPTION) {
                AppState.agent.rejectTransaction(t.getId());
                refreshAll();
            }
        });
        btnRow.add(approveBtn);
        btnRow.add(rejectBtn);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(topRow);
        content.add(Box.createVerticalStrut(2));
        content.add(midRow);
        content.add(Box.createVerticalStrut(4));
        content.add(tagRow);
        content.add(Box.createVerticalStrut(6));
        content.add(sep);
        content.add(Box.createVerticalStrut(8));
        content.add(btnRow);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel makeHistoryRow(Transaction t) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(CARD_BG);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setAlignmentX(LEFT_ALIGNMENT);

        // Status dot
        boolean approved = t.getStatus() == TransactionStatus.APPROVED;
        JLabel dot = new JLabel(approved ? "●" : "●");
        dot.setForeground(approved ? GREEN : RED);
        dot.setFont(new Font("Courier New", Font.BOLD, 16));
        row.add(dot, BorderLayout.WEST);

        // Info
        JPanel info = new JPanel(new GridLayout(1, 3));
        info.setOpaque(false);
        JLabel idLbl    = monoLabel(t.getId(), Font.BOLD, 12, DARK);
        JLabel propLbl  = monoLabel(t.getProperty().getId(), Font.PLAIN, 11, TEXT_GRAY);
        JLabel custLbl  = monoLabel(t.getCustomerName(), Font.PLAIN, 11, TEXT_GRAY);
        info.add(idLbl);
        info.add(propLbl);
        info.add(custLbl);
        row.add(info, BorderLayout.CENTER);

        // Status label
        JLabel statusLbl = new JLabel(t.getStatus().name());
        statusLbl.setFont(new Font("Courier New", Font.BOLD, 11));
        statusLbl.setForeground(approved ? GREEN.darker() : RED.darker());
        row.add(statusLbl, BorderLayout.EAST);

        return row;
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────

    private JLabel sectionLabel(String text) {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(" " + text + " ");
        lbl.setFont(new Font("Courier New", Font.BOLD, 15));
        lbl.setForeground(CARD_BG);
        lbl.setBackground(DARK);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        // return as wrapper trick — just return the label directly
        return lbl;
    }

    private JLabel makeTag(String text) {
        JLabel t = new JLabel(text);
        t.setFont(new Font("Courier New", Font.BOLD, 10));
        t.setBackground(DARK);
        t.setForeground(CARD_BG);
        t.setOpaque(true);
        t.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return t;
    }

    private void styleActionBtn(JButton b, Color bg, Color fg) {
        b.setFont(new Font("Courier New", Font.BOLD, 14));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setBorder(BorderFactory.createLineBorder(DARK, 2));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(0, 38));
    }

    private JButton makeBtn(String text, Color fg, Color bg, int w, int h) {
        JButton b = new JButton(text);
        b.setFont(new Font("Courier New", Font.BOLD, 11));
        b.setForeground(fg);
        b.setBackground(bg);
        b.setBorder(BorderFactory.createLineBorder(fg, 1));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(w, h));
        return b;
    }

    private JLabel monoLabel(String text, int style, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Courier New", style, size));
        l.setForeground(color);
        return l;
    }

    // ── Action handlers ────────────────────────────────────────────────────────

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
        java.awt.EventQueue.invokeLater(() -> new AgentFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // (all declared as fields above)
    // End of variables declaration//GEN-END:variables
}