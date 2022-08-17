/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photoroboto.slideshow;

import com.thebuzzmedia.imgscalr.Scalr;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author sh
 */
public class SlideShowPanel extends JPanel
{

    File[] slides;
    File imagesDirectory;
    BufferedImage slide1;
    BufferedImage slide2;
    float alpha = 0;
    Timer slideShowTimer;
    int slideIndex = 0;
    Dimension screenSize;
    boolean advanceSlideShow = true;
    boolean displayTransition = false;
    boolean newSlideStart = false;
    boolean newSlideEnd = false;

    public SlideShowPanel(File imagesPath)
    {

        this.setDoubleBuffered(true);
        this.setBackground(Color.BLACK);

        imagesDirectory = imagesPath;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        DisplayMode dm = gs[1].getDisplayMode();
        screenSize = new Dimension(dm.getWidth(), dm.getHeight());
        
        loadImages();

        slideShowLoop();
    }

    /**
     * Load all images from a specific directory. Each image is converted into
     * a slide.
     *
     * @param imagesPath path to images directory
     *
     * @throws IOException imagesPath represents a file, an image cannot be
     * read, an image's width doesn't match the slide width (or the height
     * doesn't match the slide height), or fewer than two images are loaded
     */
    private void loadImages()
    {
        if (!imagesDirectory.isDirectory())
        {
            try
            {
                throw new IOException(imagesDirectory.getPath() + " identifies a file");
            }
            catch (IOException ex)
            {
                Logger.getLogger(SlideShowPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        slides = imagesDirectory.listFiles();
    }

    private void slideShowLoop()
    {
        //Display Photo Timer
        ActionListener slideShowTimerListener = new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                ((Timer) e.getSource()).stop();

                loadImages();

                if (slideIndex < slides.length)
                {
                    File image1;
                    File image2;

                    if (slideIndex == slides.length - 1)
                    {
                        image1 = slides[slideIndex];
                        image2 = slides[0];
                        slideIndex++;
                    }
                    else
                    {
                        if (newSlideStart && newSlideEnd)
                        {
                            image1 = slides[slideIndex];
                            image2 = slides[slides.length - 1];
                            newSlideStart = false;
                        }
                        else if (!newSlideStart && newSlideEnd)
                        {
                            image1 = slides[slides.length - 1];
                            image2 = slides[slideIndex + 1];
                            newSlideEnd = false;
                            slideIndex++;
                        }
                        else
                        {
                            image1 = slides[slideIndex];
                            image2 = slides[slideIndex + 1];
                            slideIndex++;
                        }
                    }
                    try
                    {
                        BufferedImage bufferedImage1 = ImageIO.read(image1);
                        BufferedImage bufferedImage2 = ImageIO.read(image2);
                        transitionSlides(bufferedImage1, bufferedImage2);
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(SlideShowPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                else
                {
                    slideIndex = 0;
                    slideShowTimer.start();
                }
            }
        };

        slideShowTimer = new Timer(0, slideShowTimerListener);
        slideShowTimer.setInitialDelay(0);
        slideShowTimer.start();
    }

    private void transitionSlides(BufferedImage image1, BufferedImage image2)
    {

        alpha = 0;
        slide1 = Scalr.resize(image1, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, screenSize.width, screenSize.height);
        slide2 = Scalr.resize(image2, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, screenSize.width, screenSize.height);

        //Display Photo Timer
        ActionListener blendTimerListener = new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {

                if (alpha < 1)
                {
                    repaint();
                    alpha += 0.01;
                }
                else
                {
                    ((Timer) e.getSource()).stop();

                    slideShowTimer.start();
                }
            }
        };

        Timer blendTimer = new Timer(15, blendTimerListener);
        blendTimer.setInitialDelay(3000);
        blendTimer.start();
    }

    public void insertNewSlide()
    {
        newSlideStart = true;
        newSlideEnd = true;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int x = 0;
        int y = 0;

        System.out.println("screenSize.getWidth() = " + screenSize.getWidth());
        System.out.println("slide1.getWidth() = " + slide1.getWidth());


        if (slide1.getWidth() < screenSize.getWidth())
        {
            x = ((int) (screenSize.getWidth() - slide1.getWidth())) / 2;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(slide1, x, y, this);
        if (alpha <= 1)
        {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }

        System.out.println("screenSize.getWidth() = " + screenSize.getWidth());
        System.out.println("slide2.getWidth() = " + slide2.getWidth());



        if (slide2.getWidth() < screenSize.getWidth())
        {
            x = ((int) (screenSize.getWidth() - slide2.getWidth())) / 2;
        }

        g2.drawImage(slide2, x, y, null);
    }
}
