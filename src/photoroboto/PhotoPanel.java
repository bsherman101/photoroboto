/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photoroboto;

import photoroboto.utility.ImageUtilities;
import photoroboto.utility.PrintJobWatcher;
import com.thebuzzmedia.imgscalr.Scalr;
import edsdk.utils.CanonCamera;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import photoroboto.enums.OverlayPosition;
import photoroboto.enums.OverlayProperties;
import photoroboto.enums.PostProcessProperties;
import photoroboto.enums.SessionProperties;
import photoroboto.slideshow.SlideShow;

/**
 *
 * @author Brian
 */
public class PhotoPanel extends JPanel implements Printable
{

    public enum BoothState
    {

        DISPLAY_WELCOME_IMAGE,
        DISPLAY_BEGIN_IMAGE,
        DISPLAY_COUNTDOWN_3,
        DISPLAY_COUNTDOWN_2,
        DISPLAY_COUNTDOWN_1,
        DISPLAY_INSTRUCTIONS,
        DISPLAY_ONE_PHOTO,
        DISPLAY_FADE_OUT,
        COMPOSITE,
        DISPLAY_PRINTING,
        DISPLAY_GOODBYE
    }
    private BufferedImage welcomeImage = null;
    private BufferedImage beginImage = null;
    private BufferedImage countdown3Image = null;
    private BufferedImage countdown2Image = null;
    private BufferedImage countdown1Image = null;
    private BufferedImage printingImage = null;
    private BufferedImage endImage = null;
    private BufferedImage originalPhoto = null;
    private BufferedImage resizedPhoto = null;
    private Dimension screenSize = null;
    private BoothState state;
    private float alpha;
    private float maxAlpha = 0.99f;
    private int count;
    boolean printFinished = false;
    private Properties properties;
    private CanonCamera camera;
    private PhotoboothSession photoboothSession;
    private SlideShow slideShow;

    public PhotoPanel(Properties properties, CanonCamera camera, PhotoboothSession session)
    {

        this.properties = properties;
        this.camera = camera;
        photoboothSession = session;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, screenSize.width, screenSize.height);
        this.setBackground(Color.BLACK);

        this.setDoubleBuffered(true);

        try
        {
            welcomeImage = ImageIO.read(new File(properties.getProperty(SessionProperties.SESSION_WELCOME_IMAGE_PATH.name())));
            beginImage = ImageIO.read(new File(properties.getProperty(SessionProperties.SESSION_BEGIN_IMAGE_PATH.name())));
            countdown3Image = ImageIO.read(new File(properties.getProperty(SessionProperties.SESSION_COUNTDOWN_3_IMAGE_PATH.name())));
            countdown2Image = ImageIO.read(new File(properties.getProperty(SessionProperties.SESSION_COUNTDOWN_2_IMAGE_PATH.name())));
            countdown1Image = ImageIO.read(new File(properties.getProperty(SessionProperties.SESSION_COUNTDOWN_1_IMAGE_PATH.name())));
            printingImage = ImageIO.read(new File(properties.getProperty(SessionProperties.SESSION_PRINTING_IMAGE_PATH.name())));
            endImage = ImageIO.read(new File(properties.getProperty(SessionProperties.SESSION_END_IMAGE_PATH.name())));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        state = BoothState.DISPLAY_WELCOME_IMAGE;

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "StartSession");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "ExitSession");

        ActionMap actionMap = this.getActionMap();

        actionMap.put("StartSession", new AbstractAction()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                removeKeyBindings();
                startCountdown();
            }
        });
        actionMap.put("ExitSession", new AbstractAction()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                slideShow.dispose();
                photoboothSession.dispose();
            }
        });


        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        if (screens.length == 2)
        {

            slideShow = new SlideShow(properties.getProperty(SessionProperties.SESSION_IMAGE_DIRECTORY.name()));
            screens[1].setFullScreenWindow(slideShow);
            slideShow.setVisible(true);



        }

//        photoboothSession.setVisible(true);
//        photoboothSession.requestFocusInWindow();

    }

    private void removeKeyBindings()
    {
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "None");
    }

    private void addKeyBindings()
    {
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "StartSession");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "ExitSession");
    }

    private void startCountdown()
    {

        ActionListener countdownListener = new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (count == 3)
                {
                    state = BoothState.DISPLAY_COUNTDOWN_3;
                }
                else if (count == 2)
                {
                    state = BoothState.DISPLAY_COUNTDOWN_2;
                }
                else if (count == 1)
                {
                    state = BoothState.DISPLAY_COUNTDOWN_1;
                }
                else
                {
                    ((Timer) e.getSource()).stop();

                    if (camera.getEdsCamera() != null)
                    {
                        takeOnePhoto();
                    }
                }
                repaint();
                count--;
            }
        };
        count = 3;
        state = BoothState.DISPLAY_BEGIN_IMAGE;
        repaint();
        Timer countdownTimer = new Timer(1000, countdownListener);
        countdownTimer.setInitialDelay(3000);
        countdownTimer.start();

    }

    public void takeOnePhoto()
    {
        state = BoothState.DISPLAY_ONE_PHOTO;

        originalPhoto = camera.shootBufferedImage();

        //if post process was selected
        if (Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS.name())))
        {
            originalPhoto = ImageUtilities.postProcess(
                    originalPhoto,
                    Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_EXPOSURE.name())),
                    Float.parseFloat(properties.getProperty(PostProcessProperties.POST_PROCESS_EXPOSURE_VALUE.name())),
                    Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_CONTRAST.name())),
                    Float.parseFloat(properties.getProperty(PostProcessProperties.POST_PROCESS_CONTRAST_VALUE.name())),
                    Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_BRIGHTNESS.name())),
                    Float.parseFloat(properties.getProperty(PostProcessProperties.POST_PROCESS_BRIGHTNESS_VALUE.name())),
                    Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_GRAY_SCALE.name())),
                    Boolean.parseBoolean(properties.getProperty(PostProcessProperties.POST_PROCESS_SATURATION.name())),
                    Float.parseFloat(properties.getProperty(PostProcessProperties.POST_PROCESS_SATURATION_VALUE.name())));
        }

        //if overlay was selected
        if (Boolean.parseBoolean(properties.getProperty(OverlayProperties.OVERLAY.name())))
        {
            try
            {
                BufferedImage overlayImage = ImageIO.read(new File(properties.getProperty(OverlayProperties.OVERLAY_IMAGE_PATH.name())));
                originalPhoto = ImageUtilities.computeOverlay(
                        originalPhoto,
                        overlayImage,
                        Integer.parseInt(properties.getProperty(OverlayProperties.OVERLAY_RESIZE.name())),
                        OverlayPosition.valueOf(properties.getProperty(OverlayProperties.OVERLAY_POSITION.name())),
                        Integer.parseInt(properties.getProperty(OverlayProperties.OVERLAY_HORIZONTAL_INSET.name())),
                        Integer.parseInt(properties.getProperty(OverlayProperties.OVERLAY_VERTICAL_INSET.name())),
                        Float.parseFloat(properties.getProperty(OverlayProperties.OVERLAY_ALPHA.name())));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        String dir = properties.getProperty(SessionProperties.SESSION_IMAGE_DIRECTORY.name());
        File directory = new File(dir);
        if (!directory.exists())
        {
            directory.mkdirs();
        }

        String imagePath = dir + "\\" + properties.getProperty(SessionProperties.SESSION_IMAGE_PREFIX.name()) + "_" + System.currentTimeMillis() + ".jpg";

        File outputFile = new File(imagePath);

        try
        {
            ImageIO.write(originalPhoto, "JPG", outputFile);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        slideShow.getSlideShowPanel().insertNewSlide();

        //------------------------------------------------------------------------------------------

        resizedPhoto = Scalr.resize(originalPhoto, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, screenSize.width, screenSize.height);
        repaint();

        //Display Photo Timer
        ActionListener displayPhotoListenr = new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                ((Timer) e.getSource()).stop();
                printing();
            }
        };
        Timer displayPhotoTimer = new Timer(0, displayPhotoListenr);
        displayPhotoTimer.setInitialDelay(3000);
        displayPhotoTimer.start();
    }

    public void printing()
    {
        if (Boolean.parseBoolean(properties.getProperty(SessionProperties.SESSION_PRINT_IMAGE.name())))
        {
            //Printing Timer
            ActionListener printingListener = new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e)
                {

                    ((Timer) e.getSource()).stop();
                    endSession();
                }
            };
            state = BoothState.DISPLAY_PRINTING;
            repaint();
            Timer printingTimer = new Timer(0, printingListener);
            printingTimer.setInitialDelay(6000);
            printingTimer.start();

            /* Construct the print request specification.
             * The print data is a Printable object.
             * the request additonally specifies a job name, 1 copy, and
             * landscape orientation of the media.
             */
            Runnable printTask = new Runnable()
            {

                @Override
                public void run()
                {
                    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
                    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                    aset.add(OrientationRequested.LANDSCAPE);
                    aset.add(new Copies(1));
                    aset.add(new JobName("Photoroboto Image", null));

                    /* locate a print service that can handle the request */
                    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
                    for (PrintService printer : printServices)
                    {
                        if (printer.getName().equalsIgnoreCase(properties.getProperty(SessionProperties.SESSION_PRINTER.name())))
                        {
                            /* create a print job for the chosen service */
                            DocPrintJob pj = printer.createPrintJob();

                            try
                            {
                                /*
                                 * Create a Doc object to hold the print data.
                                 */
                                Doc doc = new SimpleDoc(PhotoPanel.this, flavor, null);

                                PrintJobWatcher printJobWatcher = new PrintJobWatcher(pj);

                                /* print the doc as specified */


                                pj.print(doc, aset);

                                printJobWatcher.waitForDone();

                                printFinished = true;

                                /*
                                 * Do not explicitly call System.exit() when print returns.
                                 * Printing can be asynchronous so may be executing in a
                                 * separate thread.
                                 */

                            }
                            catch (PrintException e)
                            {
                                System.err.println(e);
                            }
                        }
                    }
                }
            };
            new Thread(printTask).start();
        }

        endSession();

    }

    public void endSession()
    {
        //Scale Timer
        ActionListener endSessionListener = new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                ((Timer) e.getSource()).stop();
                state = BoothState.DISPLAY_WELCOME_IMAGE;
                repaint();
                addKeyBindings();
            }
        };

        state = BoothState.DISPLAY_GOODBYE;
        repaint();
        Timer endSessionTimer = new Timer(0, endSessionListener);
        endSessionTimer.setInitialDelay(4000);
        endSessionTimer.start();
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex)
    {

        if (pageIndex == 0)
        {
            Graphics2D g2d = (Graphics2D) g;

            g2d.translate(pf.getImageableX(), pf.getImageableY());

            g2d.drawImage(resizedPhoto, 0, 0, null);

            return Printable.PAGE_EXISTS;
        }
        else
        {
            return Printable.NO_SUCH_PAGE;
        }
    }

    public void fadeOut()
    {
        //Scale Timer
        ActionListener fadeOutListener = new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                state = BoothState.DISPLAY_FADE_OUT;
                if (alpha <= maxAlpha)
                {
                    alpha += 0.05;
                    repaint();
                }
                else
                {
                    ((Timer) e.getSource()).stop();
                    printing();
                }
            }
        };
        alpha = 0.0f;
        Timer fadeOutTimer = new Timer(10, fadeOutListener);
        fadeOutTimer.setInitialDelay(0);
        fadeOutTimer.start();
    }

    @Override
    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (state == BoothState.DISPLAY_WELCOME_IMAGE)
        {
            g2.drawImage(welcomeImage, 0, 0, this);
        }
        else if (state == BoothState.DISPLAY_BEGIN_IMAGE)
        {
            g2.drawImage(beginImage, 0, 0, this);
        }
        else if (state == BoothState.DISPLAY_COUNTDOWN_3)
        {
            g2.drawImage(countdown3Image, 0, 0, this);
        }
        else if (state == BoothState.DISPLAY_COUNTDOWN_2)
        {
            g2.drawImage(countdown2Image, 0, 0, this);
        }
        else if (state == BoothState.DISPLAY_COUNTDOWN_1)
        {
            g2.drawImage(countdown1Image, 0, 0, this);
        }
        else if (state == BoothState.DISPLAY_ONE_PHOTO)
        {
            int x = 0;
            int y = 0;
            if (resizedPhoto.getWidth() < screenSize.getWidth())
            {
                x = ((int) (screenSize.getWidth() - resizedPhoto.getWidth())) / 2;
            }

            g2.drawImage(resizedPhoto, x, y, null);
        }
        else if (state == BoothState.DISPLAY_PRINTING)
        {
            g2.drawImage(printingImage, 0, 0, null);
        }
        else if (state == BoothState.DISPLAY_GOODBYE)
        {
            g2.drawImage(endImage, 0, 0, null);
        }
    }
}
