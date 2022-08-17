/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PhotoboothSession.java
 *
 * Created on Oct 25, 2011, 5:44:13 PM
 */
package photoroboto;

import edsdk.utils.CanonCamera;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Properties;
import javax.swing.JFrame;

/**
 *
 * @author sh
 */
public class PhotoboothSession extends JFrame
{

    boolean animate = false;
    private PhotoPanel photoPanel;

    /** Creates new form PhotoboothSession */
    public PhotoboothSession(Properties properties, CanonCamera camera)
    {
        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create Invisible Cursor
        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        // Set the blank cursor to the JFrame.
        this.getContentPane().setCursor(blankCursor);

        photoPanel = new PhotoPanel(properties, camera, this);
        this.getContentPane().add(photoPanel, BorderLayout.CENTER);
    }
}
