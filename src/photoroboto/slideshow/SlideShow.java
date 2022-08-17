/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photoroboto.slideshow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;

/**
 *
 * @author sh
 */
public class SlideShow extends JFrame {

    private SlideShowPanel slideShowPanel;

    public SlideShow(String imagesPath) {
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBackground(Color.BLACK);
        
        //Create Invisible Cursor
        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        // Set the blank cursor to the JFrame.
        this.getContentPane().setCursor(blankCursor);

        File imagesDir = new File(imagesPath);
        slideShowPanel = new SlideShowPanel(imagesDir);
        this.getContentPane().add(slideShowPanel, BorderLayout.CENTER);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new SlideShow("C:/Users/Brian/Desktop/imagetest/hello2/test").setVisible(true);
            }
        });
    }

    /**
     * @return the slideShowPanel
     */
    public SlideShowPanel getSlideShowPanel()
    {
        return slideShowPanel;
    }
}
