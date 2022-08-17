/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CameraSettingsPanel.java
 *
 * Created on Dec 7, 2011, 10:59:00 AM
 */
package photoroboto.panels;

import edsdk.utils.CanonCamera;
import edsdk.utils.CanonTask;
import edsdk.utils.CanonUtils;
import edsdk.utils.properties.AEMode;
import edsdk.utils.properties.AFMode;
import edsdk.utils.properties.Av;
import edsdk.utils.properties.DriveMode;
import edsdk.utils.properties.ISO;
import edsdk.utils.properties.MeteringMode;
import edsdk.utils.properties.Tv;
import edsdk.utils.properties.WhiteBalance;
import java.awt.Color;
import java.util.Map;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import photoroboto.enums.CameraProperties;

/**
 *
 * @author sh
 */
public class CameraSettingsPanel extends javax.swing.JPanel {

    private CanonCamera camera;
    private String iso;
    private String shutterSpeed;
    private String aperture;
    private String driveMode;
    private String whiteBalance;
    private String afMode;
    private String meteringMode;

    /** Creates new form CameraSettingsPanel */
    public CameraSettingsPanel(Properties properties) {
        initComponents();

        if (connectCamera()) {
            getProperties(properties);
            changeCameraSettings();
            initCameraSettings();
        }
    }

    private boolean connectCamera() {
        camera = new CanonCamera();
        boolean connected = camera.openSession();
        if (!connected) {
            cameraTextField.setText("No Camera Detected");
            reconnectButton.setEnabled(true);
        } else {
            String aeMode = CanonUtils.getAEMode(camera.getEdsCamera());
            if (aeMode.equalsIgnoreCase(AEMode.getInstance().getProperties().get(3))) {
                reconnectButton.setEnabled(false);
                enableCameraFields(true);

                Map<Integer, String> isoSpeed = CanonUtils.getISOProperties(camera.getEdsCamera());
                isoComboBox.setModel(new DefaultComboBoxModel(isoSpeed.values().toArray()));
                Map<Integer, String> tv = CanonUtils.getTvProperties(camera.getEdsCamera());
                shutterSpeedComboBox.setModel(new DefaultComboBoxModel(tv.values().toArray()));
                Map<Integer, String> apertures = CanonUtils.getAvProperties(camera.getEdsCamera());
                apertureComboBox.setModel(new DefaultComboBoxModel(apertures.values().toArray()));
                Map<Integer, String> meteringModes = CanonUtils.getMeteringModeProperties(camera.getEdsCamera());
                meteringModeComboBox.setModel(new DefaultComboBoxModel(meteringModes.values().toArray()));
                whiteBalanceComboBox.setModel(new DefaultComboBoxModel(WhiteBalance.getInstance().getProperties().values().toArray()));
                driveModeComboBox.setModel(new DefaultComboBoxModel(DriveMode.getInstance().getProperties().values().toArray()));
                afModeComboBox.setModel(new DefaultComboBoxModel(AFMode.getInstance().getProperties().values().toArray()));
            } else {
                disconnectCamera();
                cameraTextField.setText("Camera Not Connected. Set to Manual Mode and Reconnect");
                connected = false;
            }
        }

        return connected;

    }

    public void disconnectCamera() {
        if (camera.getEdsCamera() != null) {
            camera.closeSession();
            camera.releaseCamera();
        }
        CanonCamera.close();
        reconnectButton.setEnabled(true);
    }

    public CanonCamera getCamera() {
        return camera;
    }

    public void getProperties(Properties properties) {
        if (!properties.isEmpty()) {
            iso = properties.getProperty(CameraProperties.CAMERA_ISO.name());
            shutterSpeed = properties.getProperty(CameraProperties.CAMERA_SHUTTER_SPEED.name());
            aperture = properties.getProperty(CameraProperties.CAMERA_APERTURE.name());
            driveMode = properties.getProperty(CameraProperties.CAMERA_DRIVE_MODE.name());
            whiteBalance = properties.getProperty(CameraProperties.CAMERA_WHITE_BALANCE.name());
            afMode = properties.getProperty(CameraProperties.CAMERA_AF_MODE.name());
            meteringMode = properties.getProperty(CameraProperties.CAMERA_METERING_MODE.name());

            setCameraFields();
            changeCameraSettings();
        }
    }

    public Properties setProperties(Properties properties) {
        if (camera.getEdsCamera() != null) {
            properties.setProperty(CameraProperties.CAMERA_ISO.name(), iso);
            properties.setProperty(CameraProperties.CAMERA_SHUTTER_SPEED.name(), shutterSpeed);
            properties.setProperty(CameraProperties.CAMERA_APERTURE.name(), aperture);
            properties.setProperty(CameraProperties.CAMERA_DRIVE_MODE.name(), driveMode);
            properties.setProperty(CameraProperties.CAMERA_WHITE_BALANCE.name(), whiteBalance);
            properties.setProperty(CameraProperties.CAMERA_AF_MODE.name(), afMode);
            properties.setProperty(CameraProperties.CAMERA_METERING_MODE.name(), meteringMode);
        }
        return properties;
    }

    private void initCameraSettings() {
        if (camera != null) {
            //Get the Camera Model that is connected
            camera.executeNow(new CanonTask<Boolean>() {

                @Override
                public void run() {
                    cameraTextField.setText(CanonUtils.getCameraModel(camera.getEdsCamera()));
                    String aeMode = CanonUtils.getAEMode(camera.getEdsCamera());
                    aeModeTextField.setText(aeMode);

                    if (aeMode.equalsIgnoreCase(AEMode.getInstance().getProperties().get(3))) {
                        enableCameraFields(true);
                        isoComboBox.setSelectedItem(iso = CanonUtils.getISO(camera.getEdsCamera()));
                        apertureComboBox.setSelectedItem(aperture = CanonUtils.getAv(camera.getEdsCamera()));
                        shutterSpeedComboBox.setSelectedItem(shutterSpeed = CanonUtils.getTv(camera.getEdsCamera()));
                        whiteBalanceComboBox.setSelectedItem(whiteBalance = CanonUtils.getWhiteBalance(camera.getEdsCamera()));
                        afModeComboBox.setSelectedItem(afMode = CanonUtils.getAFMode(camera.getEdsCamera()));
                        driveModeComboBox.setSelectedItem(driveMode = CanonUtils.getDriveMode(camera.getEdsCamera()));
                        meteringModeComboBox.setSelectedItem(meteringMode = CanonUtils.getMeteringMode(camera.getEdsCamera()));

                    } else {
                        enableCameraFields(false);
                    }
                }
            });

            applyCameraSettingsButton.setForeground(Color.BLACK);
        }
    }

    private void changeCameraSettings() {
        if (camera != null) {
            //Get the ISO of the camera
            camera.executeNow(new CanonTask<Boolean>() {

                @Override
                public void run() {
                    CanonUtils.setISO(camera.getEdsCamera(), ISO.getInstance().getKey(iso));
                    CanonUtils.setAv(camera.getEdsCamera(), Av.getInstance().getKey(aperture));
                    CanonUtils.setTv(camera.getEdsCamera(), Tv.getInstance().getKey(shutterSpeed));
                    CanonUtils.setWhiteBalance(camera.getEdsCamera(), WhiteBalance.getInstance().getKey(whiteBalance));
                    CanonUtils.setAFMode(camera.getEdsCamera(), AFMode.getInstance().getKey(afMode));
                    CanonUtils.setDriveMode(camera.getEdsCamera(), DriveMode.getInstance().getKey(driveMode));
                    CanonUtils.setMeteringMode(camera.getEdsCamera(), MeteringMode.getInstance().getKey(meteringMode));
                }
            });

            applyCameraSettingsButton.setForeground(Color.BLACK);
        }
    }

    private void readCameraFields() {
        iso = (String) isoComboBox.getSelectedItem();
        aperture = (String) apertureComboBox.getSelectedItem();
        shutterSpeed = (String) shutterSpeedComboBox.getSelectedItem();
        whiteBalance = (String) whiteBalanceComboBox.getSelectedItem();
        afMode = (String) afModeComboBox.getSelectedItem();
        driveMode = (String) driveModeComboBox.getSelectedItem();
        meteringMode = (String) meteringModeComboBox.getSelectedItem();
    }

    public void setCameraFields() {
        isoComboBox.setSelectedItem(iso);
        apertureComboBox.setSelectedItem(aperture);
        shutterSpeedComboBox.setSelectedItem(shutterSpeed);
        whiteBalanceComboBox.setSelectedItem(whiteBalance);
        afModeComboBox.setSelectedItem(afMode);
        driveModeComboBox.setSelectedItem(driveMode);
        meteringModeComboBox.setSelectedItem(meteringMode);
    }

    private void enableCameraFields(boolean enable) {
        isoComboBox.setEnabled(enable);
        shutterSpeedComboBox.setEnabled(enable);
        apertureComboBox.setEnabled(enable);
        meteringModeComboBox.setEnabled(enable);
        whiteBalanceComboBox.setEnabled(enable);
        driveModeComboBox.setEnabled(enable);
        afModeComboBox.setEnabled(enable);
        applyCameraSettingsButton.setEnabled(enable);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSeparator3 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        isoLabel = new javax.swing.JLabel();
        isoComboBox = new javax.swing.JComboBox();
        apertureLabel = new javax.swing.JLabel();
        apertureComboBox = new javax.swing.JComboBox();
        shutterSpeedLabel = new javax.swing.JLabel();
        shutterSpeedComboBox = new javax.swing.JComboBox();
        whiteBalanceLabel = new javax.swing.JLabel();
        whiteBalanceComboBox = new javax.swing.JComboBox();
        afModeComboBox = new javax.swing.JComboBox();
        driveModeLabel = new javax.swing.JLabel();
        driveModeComboBox = new javax.swing.JComboBox();
        meteringModeLabel = new javax.swing.JLabel();
        meteringModeComboBox = new javax.swing.JComboBox();
        afModeLabel = new javax.swing.JLabel();
        applyCameraSettingsButton = new javax.swing.JButton();
        cameraLabel = new javax.swing.JLabel();
        cameraTextField = new javax.swing.JTextField();
        reconnectButton = new javax.swing.JButton();
        aeModeLabel = new javax.swing.JLabel();
        aeModeTextField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(jSeparator3, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        isoLabel.setText("ISO:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(isoLabel, gridBagConstraints);

        isoComboBox.setModel(new DefaultComboBoxModel(ISO.getInstance().getProperties().values().toArray()));
        isoComboBox.setEnabled(false);
        isoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isoComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        jPanel1.add(isoComboBox, gridBagConstraints);

        apertureLabel.setText("Aperture:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(apertureLabel, gridBagConstraints);

        apertureComboBox.setModel(new DefaultComboBoxModel(Av.getInstance().getProperties().values().toArray()));
        apertureComboBox.setEnabled(false);
        apertureComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apertureComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        jPanel1.add(apertureComboBox, gridBagConstraints);

        shutterSpeedLabel.setText("Shutter Speed:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(shutterSpeedLabel, gridBagConstraints);

        shutterSpeedComboBox.setModel(new DefaultComboBoxModel(Tv.getInstance().getProperties().values().toArray()));
        shutterSpeedComboBox.setEnabled(false);
        shutterSpeedComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shutterSpeedComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        jPanel1.add(shutterSpeedComboBox, gridBagConstraints);

        whiteBalanceLabel.setText("White Balance:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        jPanel1.add(whiteBalanceLabel, gridBagConstraints);

        whiteBalanceComboBox.setModel(new DefaultComboBoxModel(WhiteBalance.getInstance().getProperties().values().toArray()));
        whiteBalanceComboBox.setEnabled(false);
        whiteBalanceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whiteBalanceComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(whiteBalanceComboBox, gridBagConstraints);

        afModeComboBox.setModel(new DefaultComboBoxModel(AFMode.getInstance().getProperties().values().toArray()));
        afModeComboBox.setEnabled(false);
        afModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afModeComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(afModeComboBox, gridBagConstraints);

        driveModeLabel.setText("Drive Mode:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(driveModeLabel, gridBagConstraints);

        driveModeComboBox.setModel(new DefaultComboBoxModel(DriveMode.getInstance().getProperties().values().toArray()));
        driveModeComboBox.setEnabled(false);
        driveModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driveModeComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        jPanel1.add(driveModeComboBox, gridBagConstraints);

        meteringModeLabel.setText("Metering Mode:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        jPanel1.add(meteringModeLabel, gridBagConstraints);

        meteringModeComboBox.setModel(new DefaultComboBoxModel(MeteringMode.getInstance().getProperties().values().toArray()));
        meteringModeComboBox.setEnabled(false);
        meteringModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meteringModeComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(meteringModeComboBox, gridBagConstraints);

        afModeLabel.setText("AF Mode:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        jPanel1.add(afModeLabel, gridBagConstraints);

        applyCameraSettingsButton.setText("Apply Changes to Camera");
        applyCameraSettingsButton.setEnabled(false);
        applyCameraSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyCameraSettingsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(applyCameraSettingsButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(jPanel1, gridBagConstraints);

        cameraLabel.setText("Camera:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(cameraLabel, gridBagConstraints);

        cameraTextField.setEditable(false);
        cameraTextField.setText("No Camera Detected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(cameraTextField, gridBagConstraints);

        reconnectButton.setText("Connect");
        reconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reconnectButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(reconnectButton, gridBagConstraints);

        aeModeLabel.setText("AE Mode:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(aeModeLabel, gridBagConstraints);

        aeModeTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(aeModeTextField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void isoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isoComboBoxActionPerformed
        applyCameraSettingsButton.setForeground(Color.RED);
}//GEN-LAST:event_isoComboBoxActionPerformed

    private void shutterSpeedComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shutterSpeedComboBoxActionPerformed
        applyCameraSettingsButton.setForeground(Color.RED);
}//GEN-LAST:event_shutterSpeedComboBoxActionPerformed

    private void whiteBalanceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whiteBalanceComboBoxActionPerformed
        applyCameraSettingsButton.setForeground(Color.RED);
}//GEN-LAST:event_whiteBalanceComboBoxActionPerformed

    private void reconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reconnectButtonActionPerformed
        if (connectCamera()) {
            initCameraSettings();
        }
}//GEN-LAST:event_reconnectButtonActionPerformed

    private void applyCameraSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyCameraSettingsButtonActionPerformed
        readCameraFields();
        changeCameraSettings();
}//GEN-LAST:event_applyCameraSettingsButtonActionPerformed

private void apertureComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apertureComboBoxActionPerformed
    applyCameraSettingsButton.setForeground(Color.RED);
}//GEN-LAST:event_apertureComboBoxActionPerformed

private void driveModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driveModeComboBoxActionPerformed
    applyCameraSettingsButton.setForeground(Color.RED);
}//GEN-LAST:event_driveModeComboBoxActionPerformed

private void afModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_afModeComboBoxActionPerformed
    applyCameraSettingsButton.setForeground(Color.RED);
}//GEN-LAST:event_afModeComboBoxActionPerformed

private void meteringModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meteringModeComboBoxActionPerformed
    applyCameraSettingsButton.setForeground(Color.RED);
}//GEN-LAST:event_meteringModeComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aeModeLabel;
    private javax.swing.JTextField aeModeTextField;
    private javax.swing.JComboBox afModeComboBox;
    private javax.swing.JLabel afModeLabel;
    private javax.swing.JComboBox apertureComboBox;
    private javax.swing.JLabel apertureLabel;
    private javax.swing.JButton applyCameraSettingsButton;
    private javax.swing.JLabel cameraLabel;
    private javax.swing.JTextField cameraTextField;
    private javax.swing.JComboBox driveModeComboBox;
    private javax.swing.JLabel driveModeLabel;
    private javax.swing.JComboBox isoComboBox;
    private javax.swing.JLabel isoLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JComboBox meteringModeComboBox;
    private javax.swing.JLabel meteringModeLabel;
    private javax.swing.JButton reconnectButton;
    private javax.swing.JComboBox shutterSpeedComboBox;
    private javax.swing.JLabel shutterSpeedLabel;
    private javax.swing.JComboBox whiteBalanceComboBox;
    private javax.swing.JLabel whiteBalanceLabel;
    // End of variables declaration//GEN-END:variables
}
