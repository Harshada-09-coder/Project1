package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UIUtils {
    public static final Color COLOR_PRIMARY      = new Color(0x75, 0xA2, 0xC9);
    public static final Color COLOR_PRIMARY_DARK = new Color(0x5E, 0x8D, 0xB5);
    public static final Color COLOR_BG           = new Color(0xF2, 0xF7, 0xFB);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN    = new Color(0x24, 0x34, 0x4D);
    public static final Color COLOR_TEXT_MUTED   = new Color(0x6B, 0x72, 0x80);
    public static final Color COLOR_BORDER       = new Color(0xE1, 0xEA, 0xF2);
    public static final Color COLOR_INPUT_BG     = new Color(0xF3, 0xF6, 0xFA);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);

    public static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;
        private Color border = null;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        public RoundedPanel(int radius, Color bg, Color border) {
            this(radius, bg);
            this.border = border;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, radius, radius));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static JPanel createCommonHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(12, 28, 10, 28));

        // Brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        RoundedPanel logo = new RoundedPanel(32, new Color(0xD6, 0xE6, 0xF2));
        logo.setPreferredSize(new Dimension(34, 34));
        logo.setLayout(new GridBagLayout());
        logo.add(new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, COLOR_PRIMARY_DARK)));

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel divider = new JLabel("|");
        divider.setForeground(COLOR_BORDER);

        JLabel sub = new JLabel("Your Everyday Shopping Destination");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_TEXT_MUTED);

        brand.add(logo);
        brand.add(title);
        brand.add(divider);
        brand.add(sub);

        // Right Status
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        RoundedPanel pill = new RoundedPanel(18, Color.WHITE, COLOR_BORDER);
        pill.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        JLabel dot = new JLabel("●");
        dot.setForeground(COLOR_GREEN);
        JLabel st = new JLabel("Master Node: Online (99.8%)");
        st.setFont(new Font("Segoe UI", Font.BOLD, 11));
        st.setForeground(COLOR_TEXT_MAIN);
        pill.add(dot);
        pill.add(st);

        right.add(pill);
        header.add(brand, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    public static JPanel createCommonFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 28, 12, 28));

        JLabel left = new JLabel("●  ShopSphere Enterprise Gateway — Connected to Master Node (Cluster US-East-1)");
        left.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        left.setForeground(COLOR_TEXT_MUTED);

        JLabel right = new JLabel("© 2026 ShopSphere. All rights reserved.   •   Privacy Policy   •   Terms of Service");
        right.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        right.setForeground(COLOR_TEXT_MUTED);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }
}