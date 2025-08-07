package com.khoihuynh.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class SafeImage {
   
   public static Icon loadIcon(String path) {
    return loadIcon(path, -1, -1); // Use -1 to indicate "use original size"
}

// Custom size version
public static Icon loadIcon(String path, int width, int height) {
    try {
        if (path != null) {
            java.net.URL imageURL = SafeImage.class.getResource(path);
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                if (icon.getIconWidth() > 0) {
                    // If specific size requested, resize
                    if (width > 0 && height > 0) {
                        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        return new ImageIcon(img);
                    }
                    return icon; // Return original size
                }
            }
        }
    } catch (Exception e) {
        System.err.println("Failed to load icon: " + path);
    }
    
    // Create placeholder with requested size or default
    int placeholderWidth = (width > 0) ? width : 50;
    int placeholderHeight = (height > 0) ? height : 50;
    return createPlaceholderIcon(placeholderWidth, placeholderHeight);
}
    
    public static BufferedImage loadImage(String path) {
        return loadImage(path, 200, 200);
    }
    

    public static BufferedImage loadImage(String path, int width, int height) {
        try {
            if (path != null) {
                java.net.URL imageURL = SafeImage.class.getResource(path);
                if (imageURL != null) {
                    BufferedImage img = ImageIO.read(imageURL);
                    if (img != null) {
                        // Resize if needed
                        if (img.getWidth() != width || img.getHeight() != height) {
                            return resizeImage(img, width, height);
                        }
                        return img;
                    }
                }
            }
        } catch (IOException e) {
            // Silently handle exception
        }
        
        // Return placeholder image
        return createPlaceholderImage(width, height);
    }
    
    public static Image getImage(Icon icon) {
        try {
            if (icon instanceof ImageIcon) {
                Image img = ((ImageIcon) icon).getImage();
                if (img != null) {
                    return img;
                }
            }
        } catch (Exception e) {
            // Silently handle exception
        }
        
        // Return placeholder
        return createPlaceholderImage(50, 50);
    }
    
    // Private helper methods
    private static BufferedImage createPlaceholderImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.GRAY);
        g2.drawRect(0, 0, width - 1, height - 1);
        g2.dispose();
        return img;
    }  
     
    private static Icon createPlaceholderIcon(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.GRAY);
        g2.drawRect(0, 0, width - 1, height - 1);
        g2.dispose();
        return new ImageIcon(img);
    }
    private static BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return resized;
    }
}
