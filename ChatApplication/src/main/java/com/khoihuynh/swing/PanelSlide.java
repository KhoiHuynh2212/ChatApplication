package com.khoihuynh.swing;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class PanelSlide extends javax.swing.JPanel {
    
    // Animation settings
    private int animateSpeed = 15;        // Pixels per frame (increased from 1)
    private int animationDelay = 8;       // Milliseconds between frames (decreased from default)
    private int animationDuration = 300;  // Total animation time in milliseconds
    
    public int getAnimateSpeed() {
        return animateSpeed;
    }
    
    public void setAnimateSpeed(int animateSpeed) {
        this.animateSpeed = animateSpeed;
        updateAnimationSettings();
    }
    
    public int getAnimationDelay() {
        return animationDelay;
    }
    
    public void setAnimationDelay(int animationDelay) {
        this.animationDelay = animationDelay;
        timer.setDelay(animationDelay);
    }
    
    public int getAnimationDuration() {
        return animationDuration;
    }
    
    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        updateAnimationSettings();
    }
    
    // Legacy method for backward compatibility
    public int getAnimate() {
        return animateSpeed;
    }
    
    public void setAnimate(int animate) {
        setAnimateSpeed(animate);
    }
    
    public PanelSlide() {
        list = new ArrayList<>();
        timer = new Timer(animationDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                animate();
            }
        });
    }
    
    private final List<Component> list;
    private final Timer timer;
    private Component comExit;
    private Component comShow;
    private int currentShowing;
    private boolean animateRight;
    
    // Animation tracking
    private int startPosition;
    private int targetPosition;
    private long animationStartTime;
    
    public void init(Component... com) {
        if (com.length > 0) {
            for (Component c : com) {
                list.add(c);
                c.setSize(getPreferredSize());
                c.setVisible(false);
                this.add(c);
            }
            // Get first component to show on panel when init
            Component show = list.get(0);
            show.setVisible(true);
            show.setLocation(0, 0);
        }
    }
    
    public void show(int index) {
        if (!timer.isRunning()) {
            if (list.size() > 1 && index < list.size() && index != currentShowing) {
                comShow = list.get(index);
                comExit = list.get(currentShowing);
                animateRight = index < currentShowing;
                currentShowing = index;
                comShow.setVisible(true);
                
                // Set initial positions
                if (animateRight) {
                    comShow.setLocation(-comShow.getWidth(), 0);
                    startPosition = -comShow.getWidth();
                } else {
                    comShow.setLocation(getWidth(), 0);
                    startPosition = getWidth();
                }
                
                targetPosition = 0;
                animationStartTime = System.currentTimeMillis();
                updateAnimationSettings();
                timer.start();
            }
        }
    }
    
    private void updateAnimationSettings() {
        if (getWidth() > 0) {
            // Calculate speed based on duration for smooth animation
            int distance = getWidth();
            int frames = animationDuration / animationDelay;
            animateSpeed = Math.max(1, distance / frames);
        }
    }
    
    // Optimized animation method
    private void animate() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - animationStartTime;
        
        // Use easing for smoother animation
        double progress = Math.min(1.0, (double) elapsed / animationDuration);
        progress = easeInOutCubic(progress); // Apply easing function
        
        int currentShowX, currentExitX;
        
        if (animateRight) {
            currentShowX = (int) (startPosition + progress * (targetPosition - startPosition));
            currentExitX = (int) (progress * getWidth());
        } else {
            currentShowX = (int) (startPosition - progress * (startPosition - targetPosition));
            currentExitX = (int) (-progress * getWidth());
        }
        
        comShow.setLocation(currentShowX, 0);
        comExit.setLocation(currentExitX, 0);
        
        // Check if animation is complete
        if (progress >= 1.0) {
            // Ensure final positions are exact
            comShow.setLocation(0, 0);
            timer.stop();
            comExit.setVisible(false);
        }
        
        // Force repaint for smoother animation
        repaint();
    }
    
    // Easing function for smoother animation
    private double easeInOutCubic(double t) {
        return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
    }
    
    // Alternative: Simple linear animation (faster but less smooth)
    private void animateLinear() {
        if (animateRight) {
            if (comShow.getLocation().x < 0) {
                int newX = Math.min(0, comShow.getLocation().x + animateSpeed);
                comShow.setLocation(newX, 0);
                comExit.setLocation(comExit.getLocation().x + animateSpeed, 0);
            } else {
                comShow.setLocation(0, 0);
                timer.stop();
                comExit.setVisible(false);
            }
        } else {
            if (comShow.getLocation().x > 0) {
                int newX = Math.max(0, comShow.getLocation().x - animateSpeed);
                comShow.setLocation(newX, 0);
                comExit.setLocation(comExit.getLocation().x - animateSpeed, 0);
            } else {
                comShow.setLocation(0, 0);
                timer.stop();
                comExit.setVisible(false);
            }
        }
        repaint();
    }
    
    // Method to instantly switch without animation
    public void showInstant(int index) {
        if (list.size() > 1 && index < list.size() && index != currentShowing) {
            if (timer.isRunning()) {
                timer.stop();
                if (comExit != null) {
                    comExit.setVisible(false);
                }
            }
            
            // Hide current component
            if (currentShowing >= 0 && currentShowing < list.size()) {
                list.get(currentShowing).setVisible(false);
            }
            
            // Show new component
            Component show = list.get(index);
            show.setVisible(true);
            show.setLocation(0, 0);
            currentShowing = index;
            repaint();
        }
    }
    
    // Quick setup methods for common animation speeds
    public void setAnimationFast() {
        setAnimationDuration(150);  // Very fast
        setAnimationDelay(8);
    }
    
    public void setAnimationMedium() {
        setAnimationDuration(300);  // Medium speed
        setAnimationDelay(10);
    }
    
    public void setAnimationSlow() {
        setAnimationDuration(600);  // Slower, smoother
        setAnimationDelay(15);
    }
    
    public void setAnimationCustom(int durationMs, int delayMs) {
        setAnimationDuration(durationMs);
        setAnimationDelay(delayMs);
    }
}