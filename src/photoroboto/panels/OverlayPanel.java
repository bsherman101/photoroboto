/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OverlayPanel.java
 *
 * Created on Dec 7, 2011, 5:26:28 PM
 */
package photoroboto.panels;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import photoroboto.enums.OverlayPosition;
import photoroboto.enums.OverlayProperties;

/**
 *
 * @author Brian
 */
public class OverlayPanel extends javax.swing.JPanel
{

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    /** Creates new form OverlayPanel */
    public OverlayPanel()
    {
        initComponents();

        //Exposure Label Table
        Hashtable alphaLabelTable = new Hashtable();
        alphaLabelTable.put(new Integer(0), new JLabel("0.0"));
        alphaLabelTable.put(new Integer(20), new JLabel("0.2"));
        alphaLabelTable.put(new Integer(40), new JLabel("0.4"));
        alphaLabelTable.put(new Integer(60), new JLabel("0.6"));
        alphaLabelTable.put(new Integer(80), new JLabel("0.8"));
        alphaLabelTable.put(new Integer(100), new JLabel("1.0"));
        alphaSlider.setLabelTable(alphaLabelTable);
        alphaSlider.setPaintLabels(true);

        alphaSlider.addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider s = (JSlider) e.getSource();
                alphaTextField.setText(decimalFormat.format(s.getValue() * 0.01f));
            }
        });
    }

    public void getProperties(Properties properties)
    {
        if (!properties.isEmpty())
        {
            addOverlayCheckbox.setSelected(Boolean.parseBoolean(properties.getProperty(OverlayProperties.OVERLAY.name())));

            if (Boolean.parseBoolean(properties.getProperty(OverlayProperties.OVERLAY.name())))
            {
                enableOverlayPanel(true);
            }
            else
            {
                enableOverlayPanel(false);
            }
            overlayImageTextField.setText(properties.getProperty(OverlayProperties.OVERLAY_IMAGE_PATH.name()));
            resizeOverlayTextField.setText(properties.getProperty(OverlayProperties.OVERLAY_RESIZE.name()));
            alphaTextField.setText(properties.getProperty(OverlayProperties.OVERLAY_ALPHA.name()));
            alphaSlider.setValue(Math.round(Float.parseFloat(properties.getProperty(OverlayProperties.OVERLAY_ALPHA.name())) * 100));
            horizontalInsetTextField.setText(properties.getProperty(OverlayProperties.OVERLAY_HORIZONTAL_INSET.name()));
            verticalInsetTextField.setText(properties.getProperty(OverlayProperties.OVERLAY_VERTICAL_INSET.name()));
            setSelectedOverlayPosition(OverlayPosition.valueOf(properties.getProperty(OverlayProperties.OVERLAY_POSITION.name())));
        }
    }

    public Properties setProperties(Properties properties)
    {

        properties.setProperty(OverlayProperties.OVERLAY.name(), Boolean.toString(addOverlayCheckbox.isSelected()));
        properties.setProperty(OverlayProperties.OVERLAY_IMAGE_PATH.name(), overlayImageTextField.getText());
        properties.setProperty(OverlayProperties.OVERLAY_RESIZE.name(), resizeOverlayTextField.getText());
        properties.setProperty(OverlayProperties.OVERLAY_ALPHA.name(), alphaTextField.getText());
        properties.setProperty(OverlayProperties.OVERLAY_HORIZONTAL_INSET.name(), horizontalInsetTextField.getText());
        properties.setProperty(OverlayProperties.OVERLAY_VERTICAL_INSET.name(), verticalInsetTextField.getText());
        properties.setProperty(OverlayProperties.OVERLAY_POSITION.name(), getSelectedOverlayPosition().name());

        return properties;
    }

    private OverlayPosition getSelectedOverlayPosition()
    {
        if (this.topLeftPositionRadioButton.isSelected())
        {
            return OverlayPosition.TOP_LEFT;
        }
        else if (this.topPositionRadioButton.isSelected())
        {
            return OverlayPosition.TOP;
        }
        else if (this.topRightPositionRadioButton.isSelected())
        {
            return OverlayPosition.TOP_RIGHT;
        }
        else if (this.leftPositionRadioButton.isSelected())
        {
            return OverlayPosition.LEFT;
        }
        else if (this.centerPositionRadioButton.isSelected())
        {
            return OverlayPosition.CENTER;
        }
        else if (this.rightPositionRadioButton.isSelected())
        {
            return OverlayPosition.RIGHT;
        }
        else if (this.bottomLeftPositionRadioButton.isSelected())
        {
            return OverlayPosition.BOTTOM_LEFT;
        }
        else if (this.bottomPositionRadioButton.isSelected())
        {
            return OverlayPosition.BOTTOM;
        }
        else if (this.bottomRightPositionRadioButton.isSelected())
        {
            return OverlayPosition.BOTTOM_RIGHT;
        }
        else
        {
            return null;
        }
    }

    private void setSelectedOverlayPosition(OverlayPosition position)
    {

        if (position == OverlayPosition.TOP_LEFT)
        {
            topLeftPositionRadioButton.setSelected(true);
        }
        else if (position == OverlayPosition.TOP)
        {
            topPositionRadioButton.setSelected(true);
        }
        else if (position == OverlayPosition.TOP_RIGHT)
        {
            topRightPositionRadioButton.setSelected(true);
        }
        else if (position == OverlayPosition.LEFT)
        {
            leftPositionRadioButton.setSelected(true);
        }
        else if (position == OverlayPosition.CENTER)
        {
            centerPositionRadioButton.setSelected(true);
        }
        else if (position == OverlayPosition.RIGHT)
        {
            rightPositionRadioButton.setSelected(true);
        }
        else if (position == OverlayPosition.BOTTOM_LEFT)
        {
            bottomLeftPositionRadioButton.setSelected(true);
        }
        else if (position == OverlayPosition.BOTTOM)
        {
            bottomPositionRadioButton.setSelected(true);
        }
        else if (position == OverlayPosition.BOTTOM_RIGHT)
        {
            bottomRightPositionRadioButton.setSelected(true);
        }
        else
        {
            centerPositionRadioButton.setSelected(true);
        }
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

        positionButtonGroup = new javax.swing.ButtonGroup();
        alphaLabel = new javax.swing.JLabel();
        resizeOverlayTextField = new javax.swing.JTextField();
        overlayPositionPanel = new javax.swing.JPanel();
        positionLabel = new javax.swing.JLabel();
        topLeftPositionRadioButton = new javax.swing.JRadioButton();
        topPositionRadioButton = new javax.swing.JRadioButton();
        topRightPositionRadioButton = new javax.swing.JRadioButton();
        leftPositionRadioButton = new javax.swing.JRadioButton();
        centerPositionRadioButton = new javax.swing.JRadioButton();
        rightPositionRadioButton = new javax.swing.JRadioButton();
        bottomLeftPositionRadioButton = new javax.swing.JRadioButton();
        bottomPositionRadioButton = new javax.swing.JRadioButton();
        bottomRightPositionRadioButton = new javax.swing.JRadioButton();
        resizeOverlayUnits = new javax.swing.JLabel();
        horizontalInsetLabel = new javax.swing.JLabel();
        addOverlayCheckbox = new javax.swing.JCheckBox();
        overlayImagePanel = new javax.swing.JPanel();
        overlayImageTextField = new javax.swing.JTextField();
        overlayImageBrowseButton = new javax.swing.JButton();
        overlayImageLabel = new javax.swing.JLabel();
        insetPanel = new javax.swing.JPanel();
        horizontalInsetTextField = new javax.swing.JTextField();
        horizontalInsetUnits = new javax.swing.JLabel();
        verticalInsetLabel = new javax.swing.JLabel();
        verticalInsetTextField = new javax.swing.JTextField();
        verticalInsetUnits = new javax.swing.JLabel();
        resizeOverlayLabel = new javax.swing.JLabel();
        alphaSlider = new javax.swing.JSlider();
        alphaTextField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        alphaLabel.setText("Alpha:");
        alphaLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(alphaLabel, gridBagConstraints);

        resizeOverlayTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(resizeOverlayTextField, gridBagConstraints);

        overlayPositionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        overlayPositionPanel.setEnabled(false);
        overlayPositionPanel.setLayout(new java.awt.GridBagLayout());

        positionLabel.setText("Position:");
        positionLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(positionLabel, gridBagConstraints);

        positionButtonGroup.add(topLeftPositionRadioButton);
        topLeftPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(topLeftPositionRadioButton, gridBagConstraints);

        positionButtonGroup.add(topPositionRadioButton);
        topPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(topPositionRadioButton, gridBagConstraints);

        positionButtonGroup.add(topRightPositionRadioButton);
        topRightPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(topRightPositionRadioButton, gridBagConstraints);

        positionButtonGroup.add(leftPositionRadioButton);
        leftPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(leftPositionRadioButton, gridBagConstraints);

        positionButtonGroup.add(centerPositionRadioButton);
        centerPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(centerPositionRadioButton, gridBagConstraints);

        positionButtonGroup.add(rightPositionRadioButton);
        rightPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(rightPositionRadioButton, gridBagConstraints);

        positionButtonGroup.add(bottomLeftPositionRadioButton);
        bottomLeftPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(bottomLeftPositionRadioButton, gridBagConstraints);

        positionButtonGroup.add(bottomPositionRadioButton);
        bottomPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(bottomPositionRadioButton, gridBagConstraints);

        positionButtonGroup.add(bottomRightPositionRadioButton);
        bottomRightPositionRadioButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        overlayPositionPanel.add(bottomRightPositionRadioButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(overlayPositionPanel, gridBagConstraints);

        resizeOverlayUnits.setText("pixels");
        resizeOverlayUnits.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 8);
        add(resizeOverlayUnits, gridBagConstraints);

        horizontalInsetLabel.setText("Horizontal Inset:");
        horizontalInsetLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(horizontalInsetLabel, gridBagConstraints);

        addOverlayCheckbox.setText("Add Overlay");
        addOverlayCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOverlayCheckboxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(addOverlayCheckbox, gridBagConstraints);

        overlayImagePanel.setEnabled(false);
        overlayImagePanel.setLayout(new java.awt.GridBagLayout());

        overlayImageTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        overlayImagePanel.add(overlayImageTextField, gridBagConstraints);

        overlayImageBrowseButton.setText("Browse");
        overlayImageBrowseButton.setEnabled(false);
        overlayImageBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overlayImageBrowseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        overlayImagePanel.add(overlayImageBrowseButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(overlayImagePanel, gridBagConstraints);

        overlayImageLabel.setText("Overlay Image:");
        overlayImageLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(overlayImageLabel, gridBagConstraints);

        insetPanel.setEnabled(false);
        insetPanel.setLayout(new java.awt.GridBagLayout());

        horizontalInsetTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        insetPanel.add(horizontalInsetTextField, gridBagConstraints);

        horizontalInsetUnits.setText("pixels");
        horizontalInsetUnits.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        insetPanel.add(horizontalInsetUnits, gridBagConstraints);

        verticalInsetLabel.setText("Vertical Inset:");
        verticalInsetLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 4);
        insetPanel.add(verticalInsetLabel, gridBagConstraints);

        verticalInsetTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        insetPanel.add(verticalInsetTextField, gridBagConstraints);

        verticalInsetUnits.setText("pixels");
        verticalInsetUnits.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        insetPanel.add(verticalInsetUnits, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(insetPanel, gridBagConstraints);

        resizeOverlayLabel.setText("Resize Overlay:");
        resizeOverlayLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(resizeOverlayLabel, gridBagConstraints);

        alphaSlider.setMajorTickSpacing(20);
        alphaSlider.setPaintTicks(true);
        alphaSlider.setValue(100);
        alphaSlider.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(alphaSlider, gridBagConstraints);

        alphaTextField.setEnabled(false);
        alphaTextField.setMinimumSize(new java.awt.Dimension(40, 30));
        alphaTextField.setPreferredSize(new java.awt.Dimension(40, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(alphaTextField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

private void overlayImageBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overlayImageBrowseButtonActionPerformed
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image", "jpg", "png", "gif");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
        overlayImageTextField.setText(chooser.getSelectedFile().getPath());
    }
}//GEN-LAST:event_overlayImageBrowseButtonActionPerformed

    private void enableOverlayPanel(boolean enable)
    {
        alphaLabel.setEnabled(enable);
        alphaSlider.setEnabled(enable);
        alphaTextField.setEnabled(enable);
        bottomLeftPositionRadioButton.setEnabled(enable);
        bottomPositionRadioButton.setEnabled(enable);
        bottomRightPositionRadioButton.setEnabled(enable);
        centerPositionRadioButton.setEnabled(enable);
        horizontalInsetLabel.setEnabled(enable);
        horizontalInsetTextField.setEnabled(enable);
        horizontalInsetUnits.setEnabled(enable);
        insetPanel.setEnabled(enable);
        leftPositionRadioButton.setEnabled(enable);
        overlayImageBrowseButton.setEnabled(enable);
        overlayImageLabel.setEnabled(enable);
        overlayImagePanel.setEnabled(enable);
        overlayImageTextField.setEnabled(enable);
        overlayPositionPanel.setEnabled(enable);
        positionLabel.setEnabled(enable);
        resizeOverlayLabel.setEnabled(enable);
        resizeOverlayTextField.setEnabled(enable);
        resizeOverlayUnits.setEnabled(enable);
        rightPositionRadioButton.setEnabled(enable);
        topLeftPositionRadioButton.setEnabled(enable);
        topPositionRadioButton.setEnabled(enable);
        topRightPositionRadioButton.setEnabled(enable);
        verticalInsetLabel.setEnabled(enable);
        verticalInsetTextField.setEnabled(enable);
        verticalInsetUnits.setEnabled(enable);
    }

private void addOverlayCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOverlayCheckboxActionPerformed
    if (addOverlayCheckbox.isSelected())
    {
        enableOverlayPanel(true);
    }
    else
    {
        enableOverlayPanel(false);
    }
}//GEN-LAST:event_addOverlayCheckboxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox addOverlayCheckbox;
    private javax.swing.JLabel alphaLabel;
    private javax.swing.JSlider alphaSlider;
    private javax.swing.JTextField alphaTextField;
    private javax.swing.JRadioButton bottomLeftPositionRadioButton;
    private javax.swing.JRadioButton bottomPositionRadioButton;
    private javax.swing.JRadioButton bottomRightPositionRadioButton;
    private javax.swing.JRadioButton centerPositionRadioButton;
    private javax.swing.JLabel horizontalInsetLabel;
    private javax.swing.JTextField horizontalInsetTextField;
    private javax.swing.JLabel horizontalInsetUnits;
    private javax.swing.JPanel insetPanel;
    private javax.swing.JRadioButton leftPositionRadioButton;
    private javax.swing.JButton overlayImageBrowseButton;
    private javax.swing.JLabel overlayImageLabel;
    private javax.swing.JPanel overlayImagePanel;
    private javax.swing.JTextField overlayImageTextField;
    private javax.swing.JPanel overlayPositionPanel;
    private javax.swing.ButtonGroup positionButtonGroup;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JLabel resizeOverlayLabel;
    private javax.swing.JTextField resizeOverlayTextField;
    private javax.swing.JLabel resizeOverlayUnits;
    private javax.swing.JRadioButton rightPositionRadioButton;
    private javax.swing.JRadioButton topLeftPositionRadioButton;
    private javax.swing.JRadioButton topPositionRadioButton;
    private javax.swing.JRadioButton topRightPositionRadioButton;
    private javax.swing.JLabel verticalInsetLabel;
    private javax.swing.JTextField verticalInsetTextField;
    private javax.swing.JLabel verticalInsetUnits;
    // End of variables declaration//GEN-END:variables
}
