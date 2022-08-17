/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PostProcess.java
 *
 * Created on Dec 6, 2011, 11:27:30 PM
 */
package photoroboto.panels;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import photoroboto.enums.PostProcessProperties;

/**
 *
 * @author sh
 */
public class PostProcessPanel extends javax.swing.JPanel
{

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    /** Creates new form PostProcess */
    public PostProcessPanel()
    {
        initComponents();

        //Exposure Label Table
        Hashtable exposureLabelTable = new Hashtable();
        exposureLabelTable.put(new Integer(0), new JLabel("0.0"));
        exposureLabelTable.put(new Integer(50), new JLabel("0.5"));
        exposureLabelTable.put(new Integer(100), new JLabel("1.0"));
        exposureLabelTable.put(new Integer(150), new JLabel("1.5"));
        exposureLabelTable.put(new Integer(200), new JLabel("2.0"));
        exposureLabelTable.put(new Integer(250), new JLabel("2.5"));
        exposureLabelTable.put(new Integer(300), new JLabel("3.0"));
        exposureLabelTable.put(new Integer(350), new JLabel("3.5"));
        exposureLabelTable.put(new Integer(400), new JLabel("4.0"));
        exposureLabelTable.put(new Integer(450), new JLabel("4.5"));
        exposureLabelTable.put(new Integer(500), new JLabel("5.0"));
        exposureSlider.setLabelTable(exposureLabelTable);
        exposureSlider.setPaintLabels(true);

        //Contrast Label Table
        Hashtable contrastLabelTable = new Hashtable();
        contrastLabelTable.put(new Integer(0), new JLabel("0.0"));
        contrastLabelTable.put(new Integer(20), new JLabel("0.2"));
        contrastLabelTable.put(new Integer(40), new JLabel("0.4"));
        contrastLabelTable.put(new Integer(60), new JLabel("0.6"));
        contrastLabelTable.put(new Integer(80), new JLabel("0.8"));
        contrastLabelTable.put(new Integer(100), new JLabel("1.0"));
        contrastLabelTable.put(new Integer(120), new JLabel("1.2"));
        contrastLabelTable.put(new Integer(140), new JLabel("1.4"));
        contrastLabelTable.put(new Integer(160), new JLabel("1.6"));
        contrastLabelTable.put(new Integer(180), new JLabel("1.8"));
        contrastLabelTable.put(new Integer(200), new JLabel("2.0"));
        contrastSlider.setLabelTable(contrastLabelTable);
        contrastSlider.setPaintLabels(true);

        //Brightness Label Table
        Hashtable brightnessLabelTable = new Hashtable();
        brightnessLabelTable.put(new Integer(0), new JLabel("0.0"));
        brightnessLabelTable.put(new Integer(20), new JLabel("0.2"));
        brightnessLabelTable.put(new Integer(40), new JLabel("0.4"));
        brightnessLabelTable.put(new Integer(60), new JLabel("0.6"));
        brightnessLabelTable.put(new Integer(80), new JLabel("0.8"));
        brightnessLabelTable.put(new Integer(100), new JLabel("1.0"));
        brightnessLabelTable.put(new Integer(120), new JLabel("1.2"));
        brightnessLabelTable.put(new Integer(140), new JLabel("1.4"));
        brightnessLabelTable.put(new Integer(160), new JLabel("1.6"));
        brightnessLabelTable.put(new Integer(180), new JLabel("1.8"));
        brightnessLabelTable.put(new Integer(200), new JLabel("2.0"));
        brightnessSlider.setLabelTable(brightnessLabelTable);
        brightnessSlider.setPaintLabels(true);

        //Brightness Label Table
        Hashtable saturationLabelTable = new Hashtable();
        saturationLabelTable.put(new Integer(0), new JLabel("0.0"));
        saturationLabelTable.put(new Integer(20), new JLabel("0.2"));
        saturationLabelTable.put(new Integer(40), new JLabel("0.4"));
        saturationLabelTable.put(new Integer(60), new JLabel("0.6"));
        saturationLabelTable.put(new Integer(80), new JLabel("0.8"));
        saturationLabelTable.put(new Integer(100), new JLabel("1.0"));
        saturationLabelTable.put(new Integer(120), new JLabel("1.2"));
        saturationLabelTable.put(new Integer(140), new JLabel("1.4"));
        saturationLabelTable.put(new Integer(160), new JLabel("1.6"));
        saturationLabelTable.put(new Integer(180), new JLabel("1.8"));
        saturationLabelTable.put(new Integer(200), new JLabel("2.0"));
        saturationSlider.setLabelTable(saturationLabelTable);
        saturationSlider.setPaintLabels(true);

        exposureSlider.addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider s = (JSlider) e.getSource();
                exposureTextField.setText(decimalFormat.format(s.getValue() * 0.01f));
            }
        });

        brightnessSlider.addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider s = (JSlider) e.getSource();
                brightnessTextField.setText(decimalFormat.format(s.getValue() * 0.01f));
            }
        });

        contrastSlider.addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider s = (JSlider) e.getSource();
                contrastTextField.setText(decimalFormat.format(s.getValue() * 0.01f));
            }
        });

        saturationSlider.addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider s = (JSlider) e.getSource();
                saturationTextField.setText(decimalFormat.format(s.getValue() * 0.01f));
            }
        });


//        exposureSlider.setValue(0);
//        exposureSlider.setValue(100);
//        brightnessSlider.setValue(0);
//        brightnessSlider.setValue(100);
//        contrastSlider.setValue(0);
//        contrastSlider.setValue(100);
//        saturationSlider.setValue(100);
//        saturationSlider.setValue(0);

    }

    public void getProperties(Properties properties)
    {
        if (!properties.isEmpty())
        {
            postProcessCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS.name())));

            if (postProcessCheckBox.isSelected())
            {
                enablePostProcessPanel(true);
            }
            else
            {
                enablePostProcessPanel(false);
            }

            greyscaleCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_GRAY_SCALE.name())));
            exposureCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_EXPOSURE.name())));
            exposureTextField.setText(properties.getProperty(PostProcessProperties.POST_PROCESS_EXPOSURE_VALUE.name()));
            exposureSlider.setValue(Math.round(Float.parseFloat(properties.getProperty(PostProcessProperties.POST_PROCESS_EXPOSURE_VALUE.name())) * 100));
            contrastCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_CONTRAST.name())));
            contrastTextField.setText(properties.getProperty(PostProcessProperties.POST_PROCESS_CONTRAST_VALUE.name()));
            contrastSlider.setValue(Math.round(Float.parseFloat(properties.getProperty(PostProcessProperties.POST_PROCESS_CONTRAST_VALUE.name())) * 100));
            brightnessCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_BRIGHTNESS.name())));
            brightnessTextField.setText(properties.getProperty(PostProcessProperties.POST_PROCESS_BRIGHTNESS_VALUE.name()));
            brightnessSlider.setValue(Math.round(Float.parseFloat(brightnessTextField.getText()) * 100));
            brightnessSlider.setValue(Math.round(Float.parseFloat(properties.getProperty(PostProcessProperties.POST_PROCESS_BRIGHTNESS_VALUE.name())) * 100));            
            saturationCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_SATURATION.name())));
            saturationTextField.setText(properties.getProperty(PostProcessProperties.POST_PROCESS_SATURATION_VALUE.name()));
            saturationSlider.setValue(Math.round(Float.parseFloat(saturationTextField.getText()) * 100));
            saturationSlider.setValue(Math.round(Float.parseFloat(properties.getProperty(PostProcessProperties.POST_PROCESS_SATURATION_VALUE.name())) * 100));            
        }
    }

    public Properties setProperties(Properties properties)
    {

        properties.setProperty(PostProcessProperties.POST_PROCESS.name(), Boolean.toString(postProcessCheckBox.isSelected()));
        properties.setProperty(PostProcessProperties.POST_PROCESS_GRAY_SCALE.name(), Boolean.toString(greyscaleCheckBox.isSelected()));
        properties.setProperty(PostProcessProperties.POST_PROCESS_EXPOSURE.name(), Boolean.toString(exposureCheckBox.isSelected()));
        properties.setProperty(PostProcessProperties.POST_PROCESS_EXPOSURE_VALUE.name(), exposureTextField.getText());
        properties.setProperty(PostProcessProperties.POST_PROCESS_CONTRAST.name(), Boolean.toString(contrastCheckBox.isSelected()));
        properties.setProperty(PostProcessProperties.POST_PROCESS_CONTRAST_VALUE.name(), contrastTextField.getText());
        properties.setProperty(PostProcessProperties.POST_PROCESS_BRIGHTNESS.name(), Boolean.toString(brightnessCheckBox.isSelected()));
        properties.setProperty(PostProcessProperties.POST_PROCESS_BRIGHTNESS_VALUE.name(), brightnessTextField.getText());
        properties.setProperty(PostProcessProperties.POST_PROCESS_SATURATION.name(), Boolean.toString(saturationCheckBox.isSelected()));
        properties.setProperty(PostProcessProperties.POST_PROCESS_SATURATION_VALUE.name(), saturationTextField.getText());

        return properties;
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

        exposureSlider = new javax.swing.JSlider();
        brightnessSlider = new javax.swing.JSlider();
        greyscaleCheckBox = new javax.swing.JCheckBox();
        contrastSlider = new javax.swing.JSlider();
        saturationSlider = new javax.swing.JSlider();
        postProcessCheckBox = new javax.swing.JCheckBox();
        exposureTextField = new javax.swing.JTextField();
        contrastTextField = new javax.swing.JTextField();
        brightnessTextField = new javax.swing.JTextField();
        saturationTextField = new javax.swing.JTextField();
        exposureCheckBox = new javax.swing.JCheckBox();
        brightnessCheckBox = new javax.swing.JCheckBox();
        saturationCheckBox = new javax.swing.JCheckBox();
        contrastCheckBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        exposureSlider.setMajorTickSpacing(100);
        exposureSlider.setMaximum(500);
        exposureSlider.setPaintTicks(true);
        exposureSlider.setValue(100);
        exposureSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        exposureSlider.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(exposureSlider, gridBagConstraints);

        brightnessSlider.setMajorTickSpacing(20);
        brightnessSlider.setMaximum(200);
        brightnessSlider.setPaintTicks(true);
        brightnessSlider.setValue(100);
        brightnessSlider.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(brightnessSlider, gridBagConstraints);

        greyscaleCheckBox.setText("Convert to Greyscale");
        greyscaleCheckBox.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(greyscaleCheckBox, gridBagConstraints);

        contrastSlider.setMajorTickSpacing(20);
        contrastSlider.setMaximum(200);
        contrastSlider.setPaintTicks(true);
        contrastSlider.setValue(100);
        contrastSlider.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(contrastSlider, gridBagConstraints);

        saturationSlider.setMajorTickSpacing(20);
        saturationSlider.setMaximum(200);
        saturationSlider.setPaintTicks(true);
        saturationSlider.setValue(0);
        saturationSlider.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(saturationSlider, gridBagConstraints);

        postProcessCheckBox.setText("Post Process");
        postProcessCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postProcessCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(postProcessCheckBox, gridBagConstraints);

        exposureTextField.setEditable(false);
        exposureTextField.setEnabled(false);
        exposureTextField.setPreferredSize(new java.awt.Dimension(40, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(exposureTextField, gridBagConstraints);

        contrastTextField.setEditable(false);
        contrastTextField.setEnabled(false);
        contrastTextField.setPreferredSize(new java.awt.Dimension(40, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(contrastTextField, gridBagConstraints);

        brightnessTextField.setEditable(false);
        brightnessTextField.setEnabled(false);
        brightnessTextField.setPreferredSize(new java.awt.Dimension(40, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(brightnessTextField, gridBagConstraints);

        saturationTextField.setEditable(false);
        saturationTextField.setEnabled(false);
        saturationTextField.setMinimumSize(new java.awt.Dimension(40, 30));
        saturationTextField.setPreferredSize(new java.awt.Dimension(40, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(saturationTextField, gridBagConstraints);

        exposureCheckBox.setText("Adjust Exposure");
        exposureCheckBox.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(exposureCheckBox, gridBagConstraints);

        brightnessCheckBox.setText("Adjust Brightness");
        brightnessCheckBox.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(brightnessCheckBox, gridBagConstraints);

        saturationCheckBox.setText("Adjust Saturation");
        saturationCheckBox.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(saturationCheckBox, gridBagConstraints);

        contrastCheckBox.setText("Adjust Contrast");
        contrastCheckBox.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(contrastCheckBox, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void postProcessCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postProcessCheckBoxActionPerformed
        if (postProcessCheckBox.isSelected())
        {
            enablePostProcessPanel(true);
        }
        else
        {
            enablePostProcessPanel(false);
        }
    }//GEN-LAST:event_postProcessCheckBoxActionPerformed

    private void enablePostProcessPanel(boolean enable)
    {
        greyscaleCheckBox.setEnabled(enable);
        exposureCheckBox.setEnabled(enable);
        exposureSlider.setEnabled(enable);
        exposureTextField.setEnabled(enable);
        brightnessCheckBox.setEnabled(enable);
        brightnessSlider.setEnabled(enable);
        brightnessTextField.setEnabled(enable);
        contrastCheckBox.setEnabled(enable);
        contrastSlider.setEnabled(enable);
        contrastTextField.setEnabled(enable);
        saturationCheckBox.setEnabled(enable);
        saturationSlider.setEnabled(enable);
        saturationTextField.setEnabled(enable);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox brightnessCheckBox;
    private javax.swing.JSlider brightnessSlider;
    private javax.swing.JTextField brightnessTextField;
    private javax.swing.JCheckBox contrastCheckBox;
    private javax.swing.JSlider contrastSlider;
    private javax.swing.JTextField contrastTextField;
    private javax.swing.JCheckBox exposureCheckBox;
    private javax.swing.JSlider exposureSlider;
    private javax.swing.JTextField exposureTextField;
    private javax.swing.JCheckBox greyscaleCheckBox;
    private javax.swing.JCheckBox postProcessCheckBox;
    private javax.swing.JCheckBox saturationCheckBox;
    private javax.swing.JSlider saturationSlider;
    private javax.swing.JTextField saturationTextField;
    // End of variables declaration//GEN-END:variables
}
