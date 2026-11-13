package com.shopsphere.util;

import com.shopsphere.entity.Product;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * High-performance image loading, caching, and mapping utility for ShopSphere desktop views.
 */
public class ImageUtils {

    private static final Map<String, ImageIcon> iconCache = new ConcurrentHashMap<>();

    private static final String DEFAULT_HERO_BANNER = "/images/banner_hero.png";
    private static final String DEFAULT_HEADPHONES = "/images/headphones.png";
    private static final String DEFAULT_WATCH = "/images/smartwatch.png";
    private static final String DEFAULT_BACKPACK = "/images/backpack.png";
    private static final String DEFAULT_SNEAKERS = "/images/sneakers.png";
    private static final String DEFAULT_SPEAKER = "/images/speaker.png";
    private static final String DEFAULT_LAMP = "/images/desk_lamp.png";
    private static final String DEFAULT_PLANT = "/images/plant.png";
    private static final String DEFAULT_KEYBOARD = "/images/keyboard.png";
    private static final String DEFAULT_DOCK = "/images/charging_dock.png";
    private static final String DEFAULT_BOTTLE = "/images/water_bottle.png";

    /**
     * Load an image by resource path or file system path with smooth high-quality scaling.
     */
    public static ImageIcon loadImage(String pathOrResource, int targetWidth, int targetHeight) {
        if (pathOrResource == null || pathOrResource.trim().isEmpty()) {
            return null;
        }

        String cacheKey = pathOrResource.trim() + "_" + targetWidth + "x" + targetHeight;
        ImageIcon cached = iconCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        try {
            BufferedImage bImg = null;
            // 1. Try classpath resource
            InputStream is = ImageUtils.class.getResourceAsStream(pathOrResource);
            if (is != null) {
                bImg = ImageIO.read(is);
            } else if (!pathOrResource.startsWith("/")) {
                is = ImageUtils.class.getResourceAsStream("/" + pathOrResource);
                if (is != null) {
                    bImg = ImageIO.read(is);
                }
            }

            // 2. Try file system
            if (bImg == null) {
                File f = new File(pathOrResource);
                if (f.exists() && f.isFile()) {
                    bImg = ImageIO.read(f);
                }
            }

            if (bImg == null) {
                // Try relative to workspace
                File f = new File("ShopSphere/src/main/resources" + (pathOrResource.startsWith("/") ? "" : "/") + pathOrResource);
                if (f.exists() && f.isFile()) {
                    bImg = ImageIO.read(f);
                }
            }

            if (bImg != null) {
                Image scaled = bImg.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaled);
                iconCache.put(cacheKey, icon);
                return icon;
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    /**
     * Get image for product matching Figma reference design catalog.
     */
    public static ImageIcon getProductIcon(Product product, int width, int height) {
        String resourcePath = resolveProductResource(product);
        ImageIcon icon = loadImage(resourcePath, width, height);
        if (icon != null) {
            return icon;
        }
        // Fallback to headphones as generic default
        return loadImage(DEFAULT_HEADPHONES, width, height);
    }

    /**
     * Resolve the best image resource path for a given product based on name, category, or ID.
     */
    public static String resolveProductResource(Product product) {
        if (product == null) {
            return DEFAULT_HEADPHONES;
        }

        String url = (product.getImageUrl() != null) ? product.getImageUrl().toLowerCase() : "";
        String name = (product.getProductName() != null) ? product.getProductName().toLowerCase() : "";

        // ── Name-based keyword resolution takes priority so each product gets
        //    a unique image even when all rows share the same image_url in the DB.
        // Keywords detection matching the Figma mockups
        if (name.contains("headphone") || name.contains("wh-1000") || name.contains("sony") || name.contains("earphone")) {
            return DEFAULT_HEADPHONES;
        }
        if (name.contains("watch") || name.contains("fitness") || name.contains("chronograph") || name.contains("fossil")) {
            return DEFAULT_WATCH;
        }
        if (name.contains("backpack") || name.contains("bag") || name.contains("urban")) {
            return DEFAULT_BACKPACK;
        }
        if (name.contains("shoe") || name.contains("sneaker") || name.contains("running") || name.contains("air max") || name.contains("ultraboost")) {
            return DEFAULT_SNEAKERS;
        }
        if (name.contains("speaker") || name.contains("bluetooth") || name.contains("audio") || name.contains("sound")) {
            return DEFAULT_SPEAKER;
        }
        if (name.contains("lamp") || name.contains("diffuser") || name.contains("light") || name.contains("desk lamp")) {
            return DEFAULT_LAMP;
        }
        if (name.contains("plant") || name.contains("planter") || name.contains("succulent") || name.contains("pot") || name.contains("habits")) {
            return DEFAULT_PLANT;
        }
        if (name.contains("keyboard") || name.contains("macbook") || name.contains("laptop") || name.contains("gaming")) {
            return DEFAULT_KEYBOARD;
        }
        if (name.contains("dock") || name.contains("organizer") || name.contains("charger") || name.contains("iphone") || name.contains("galaxy") || name.contains("money")) {
            return DEFAULT_DOCK;
        }
        if (name.contains("bottle") || name.contains("tumbler") || name.contains("casserole") || name.contains("dumbbell") || name.contains("yoga")) {
            return DEFAULT_BOTTLE;
        }

        // If name keywords didn't match, honour a specific non-generic image_url from the DB
        // (but skip the generic "/images/headphones.png" fallback that many rows share)
        if (!url.isEmpty() && (url.startsWith("/images/") || url.endsWith(".png") || url.endsWith(".jpg"))) {
            String stored = product.getImageUrl();
            // Only use if it is not the generic default headphones image
            if (!stored.equalsIgnoreCase("/images/headphones.png") && !stored.equalsIgnoreCase("headphones.png")) {
                return stored;
            }
        }

        // Mapping by Product ID fallback
        switch (product.getProductId()) {
            case 1:
            case 2:
                return DEFAULT_DOCK;
            case 3:
                return DEFAULT_HEADPHONES;
            case 4:
                return DEFAULT_KEYBOARD;
            case 5:
            case 6:
                return DEFAULT_SNEAKERS;
            case 7:
                return DEFAULT_BACKPACK;
            case 8:
                return DEFAULT_WATCH;
            case 9:
                return DEFAULT_LAMP;
            case 10:
            case 11:
            case 12:
                return DEFAULT_BOTTLE;
            case 13:
            case 14:
                return DEFAULT_PLANT;
            default:
                return DEFAULT_HEADPHONES;
        }
    }

    /**
     * Get Hero Banner image icon for Welcome & Customer Dashboard.
     */
    public static ImageIcon getHeroBanner(int width, int height) {
        return loadImage(DEFAULT_HERO_BANNER, width, height);
    }

    /**
     * Get 4 gallery thumbnails for product details view.
     */
    public static List<ImageIcon> getProductGalleryThumbnails(Product product, int width, int height) {
        List<ImageIcon> list = new ArrayList<>();
        list.add(getProductIcon(product, width, height));
        ImageIcon v1 = loadImage("/images/headphones_side.png", width, height);
        ImageIcon v2 = loadImage("/images/headphones_earcup.png", width, height);
        ImageIcon v3 = loadImage("/images/headphones_case.png", width, height);

        list.add(v1 != null ? v1 : getProductIcon(product, width, height));
        list.add(v2 != null ? v2 : getProductIcon(product, width, height));
        list.add(v3 != null ? v3 : getProductIcon(product, width, height));
        return list;
    }

    /**
     * Helper to wrap an ImageIcon in a styled JLabel with rounded border padding.
     */
    public static JLabel createRenderedImageLabel(ImageIcon icon) {
        JLabel label = new JLabel(icon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }
}

