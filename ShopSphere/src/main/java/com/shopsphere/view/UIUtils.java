package com.shopsphere.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * UI Utilities and visual theme components for the ShopSphere application.
 */
public class UIUtils {

    public static final Color COLOR_PRIMARY      = new Color(0x75, 0xA2, 0xC9);
    public static final Color COLOR_PRIMARY_DARK = new Color(0x5F, 0x8E, 0xB8);
    public static final Color COLOR_LIGHT_BLUE   = new Color(0xD6, 0xE6, 0xF2);
    public static final Color COLOR_BG           = new Color(0xF2, 0xF7, 0xFB);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN    = new Color(0x24, 0x34, 0x4D);
    public static final Color COLOR_TEXT_MUTED   = new Color(0x6B, 0x72, 0x80);
    public static final Color COLOR_BORDER       = new Color(0xE1, 0xEA, 0xF2);
    public static final Color COLOR_INPUT_BG     = new Color(0xF4, 0xF7, 0xFB);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);
    public static final Color COLOR_DANGER       = new Color(0xEF, 0x44, 0x44);
    public static final Color COLOR_RED          = new Color(0xEF, 0x44, 0x44);
    public static final Color COLOR_ORANGE       = new Color(0xF5, 0x9E, 0x0B);

    public static JPanel createCommonFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 32, 12, 32));

        JPanel leftStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftStatus.setOpaque(false);

        JLabel dot = new JLabel("●");
        dot.setForeground(COLOR_GREEN);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel statusText = new JLabel("ShopSphere Enterprise System  •  MySQL Connected  •  Cluster Master Node");
        statusText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusText.setForeground(COLOR_TEXT_MUTED);

        leftStatus.add(dot);
        leftStatus.add(statusText);

        JLabel rightText = new JLabel("© 2026 ShopSphere. All rights reserved.");
        rightText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rightText.setForeground(COLOR_TEXT_MUTED);

        footer.add(leftStatus, BorderLayout.WEST);
        footer.add(rightText, BorderLayout.EAST);

        return footer;
    }

    public static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;
        private Color borderColor = null;

        public RoundedPanel(int radius, Color bg) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bg;
            setOpaque(false);
        }

        public RoundedPanel(int radius, Color bg, Color border) {
            this(radius, bg);
            this.borderColor = border;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
