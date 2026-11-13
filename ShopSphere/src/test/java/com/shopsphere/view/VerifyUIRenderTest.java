package com.shopsphere.view;

import com.shopsphere.entity.Product;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyUIRenderTest {

    @Test
    public void testRenderAllViewsWithImages() throws Exception {
        System.setProperty("java.awt.headless", "false");

        String outDir = "C:/Users/Dhond/.gemini/antigravity/brain/8266027e-4eb6-4903-baa8-357043cd9d81/scratch/";
        new File(outDir).mkdirs();

        SwingUtilities.invokeAndWait(() -> {
            try {
                // 1. WelcomePage
                WelcomePage wp = new WelcomePage();
                renderFrame(wp, 1200, 800, outDir + "ui_welcome.png");
                wp.dispose();

                // 2. CustomerDashboardPage
                CustomerDashboardPage cdp = new CustomerDashboardPage();
                renderFrame(cdp, 1200, 800, outDir + "ui_dashboard.png");
                cdp.dispose();

                // 3. ProductListingPage
                ProductListingPage plp = new ProductListingPage("All Categories");
                renderFrame(plp, 1200, 800, outDir + "ui_listing.png");
                plp.dispose();

                // 4. ProductDetailsPage
                Product p = new Product();
                p.setProductId(3);
                p.setProductName("WH-1000XM5 Wireless Noise Cancelling Headphones");
                p.setBrand("Sony");
                p.setPrice(29990.00);
                p.setStockQuantity(45);
                p.setDescription("Industry leading noise cancellation with two processors and 8 microphones. Up to 30-hour battery life with quick charging.");
                ProductDetailsPage pdp = new ProductDetailsPage(p);
                renderFrame(pdp, 1200, 800, outDir + "ui_details.png");
                pdp.dispose();

                // 5. MyCartPage
                MyCartPage mcp = new MyCartPage();
                renderFrame(mcp, 1200, 800, outDir + "ui_cart.png");
                mcp.dispose();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        File fWel = new File(outDir + "ui_welcome.png");
        File fDash = new File(outDir + "ui_dashboard.png");
        File fList = new File(outDir + "ui_listing.png");
        File fDet = new File(outDir + "ui_details.png");

        assertTrue(fWel.exists() && fWel.length() > 1000, "Welcome screenshot should exist");
        assertTrue(fDash.exists() && fDash.length() > 1000, "Dashboard screenshot should exist");
        assertTrue(fList.exists() && fList.length() > 1000, "Listing screenshot should exist");
        assertTrue(fDet.exists() && fDet.length() > 1000, "Details screenshot should exist");
    }

    private static void renderFrame(JFrame frame, int w, int h, String outPath) throws Exception {
        frame.setSize(w, h);
        frame.getContentPane().setSize(w, h);
        frame.addNotify();
        frame.validate();
        frame.getContentPane().validate();
        frame.doLayout();
        frame.getContentPane().doLayout();

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);
        frame.getContentPane().printAll(g2);
        g2.dispose();

        ImageIO.write(img, "png", new File(outPath));
        System.out.println("RENDERED: " + outPath);
    }
}
