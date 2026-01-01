// File: com/mid/app/swing/gui/SchedulerPanel.java
package com.mid.app.swing.gui;

import com.mid.app.quartz.job.QuartzSchedulerApp;

import javax.swing.*;
import java.awt.*;

public class SchedulerPanel extends JPanel {
    
    private JLabel statusLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private Timer statusTimer;
    
    public SchedulerPanel() {
        initializeUI();
        startStatusTimer();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Scheduled Job Control");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        
        statusLabel = new JLabel("Checking scheduler status...");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        startButton = createButton("Start Scheduler", new Color(76, 175, 80));
        startButton.addActionListener(e -> startScheduler());
        
        stopButton = createButton("Stop Scheduler", new Color(244, 67, 54));
        stopButton.addActionListener(e -> stopScheduler());
        stopButton.setEnabled(false);
        
        pauseButton = createButton("Pause", new Color(255, 193, 7));
        pauseButton.addActionListener(e -> pauseScheduler());
        pauseButton.setEnabled(false);
        
        resumeButton = createButton("Resume", new Color(33, 150, 243));
        resumeButton.addActionListener(e -> resumeScheduler());
        resumeButton.setEnabled(false);
        
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);
        buttonPanel.add(stopButton);
        
        // Schedule info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Schedule Information"));
        
        JLabel scheduleLabel = new JLabel("Runs every 2 minutes");
        scheduleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel functionLabel = new JLabel("Processes policies created today");
        functionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        infoPanel.add(scheduleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(functionLabel);
        
        // Add components
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        contentPanel.add(titleLabel);
        contentPanel.add(statusPanel);
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(infoPanel);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Update initial status
        updateStatus();
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void startStatusTimer() {
        statusTimer = new Timer(5000, e -> updateStatus()); // Update every 5 seconds
        statusTimer.start();
    }
    
    private void updateStatus() {
        SwingUtilities.invokeLater(() -> {
            try {
                boolean isRunning = QuartzSchedulerApp.isSchedulerRunning();
                
                if (isRunning) {
                    statusLabel.setText("Status: RUNNING");
                    statusLabel.setForeground(new Color(76, 175, 80)); // Green
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    pauseButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                } else {
                    statusLabel.setText("Status: STOPPED");
                    statusLabel.setForeground(new Color(244, 67, 54)); // Red
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                }
            } catch (Exception e) {
                statusLabel.setText("Status: ERROR - " + e.getMessage());
                statusLabel.setForeground(new Color(244, 67, 54));
            }
        });
    }
    
    private void startScheduler() {
        new Thread(() -> {
            try {
                QuartzSchedulerApp.startScheduler();
                JOptionPane.showMessageDialog(this,
                    "Scheduler started successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Failed to start scheduler: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }
    
    private void stopScheduler() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to stop the scheduler?",
            "Confirm Stop",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                try {
                    QuartzSchedulerApp.stopScheduler();
                    JOptionPane.showMessageDialog(this,
                        "Scheduler stopped successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                        "Failed to stop scheduler: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        }
    }
    
    private void pauseScheduler() {
        new Thread(() -> {
            try {
                QuartzSchedulerApp.pauseScheduler();
                resumeButton.setEnabled(true);
                pauseButton.setEnabled(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Failed to pause scheduler: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }
    
    private void resumeScheduler() {
        new Thread(() -> {
            try {
                QuartzSchedulerApp.resumeScheduler();
                resumeButton.setEnabled(false);
                pauseButton.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Failed to resume scheduler: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            statusTimer.stop();
        } else {
            statusTimer.start();
        }
    }
}