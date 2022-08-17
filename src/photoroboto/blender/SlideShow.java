/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photoroboto.blender;

/**
 *
 * @author Brian
 */
// SlideShow.java

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.io.*;

import java.util.ArrayList;

import javax.imageio.*;

import javax.swing.*;

/**
 * This class describes and contains the entry point to the slideshow
 * application.
 */

public class SlideShow
{
  static Projector projector;

  /**
  * Create SlideShow's GUI and start the projector.
  */

  SlideShow ()
  {
   JFrame frame = new JFrame ("SlideShow 1.0");
   frame.addWindowListener (new WindowAdapter ()
                {
                  public void windowClosing (WindowEvent we)
                  {
                   projector.stop ();
                   System.exit (0);
                  }
                });

   Screen screen = new Screen (projector.getSlideWidth (),
                 projector.getSlideHeight ());
   frame.setContentPane (screen);

   frame.pack ();
   frame.setResizable (false);
   frame.setLocationRelativeTo (null); // center frame on screen
   frame.setVisible (true);

   projector.start (screen);
  }

  /**
  * Application entry point.
  *
  * @param args array of command-line arguments
  */

  public static void main (String [] args) throws IOException
  {
   if (args.length != 1)
   {
     System.err.println ("usage: java SlideShow imagesPath");
     return;
   }

   projector = new Projector (args [0]);

   Runnable r = new Runnable ()
          {
            public void run ()
            {
             new SlideShow ();
            }
          };
   EventQueue.invokeLater (r);
  }
}

/**
 * This class describes the projector that runs the slideshow.
 */

class Projector implements ActionListener
{
  /**
  * Create the projector, loading images from the specified path into
  * slides.
  *
  * @param imagesPath path to images (GIFs and/or JPEGS, and/or other
  * formats)
  */

  Projector (String imagesPath) throws IOException
  {
   load (imagesPath);

   slideCount = slides.size ();

   show_timer = new Timer (SLIDE_DELAY, this);
  }

  /**
  * Callback for show_timer -- invoked approximately every SLIDE_DELAY
  * milliseconds.
  *
  * @param ae action event
  */

  public void actionPerformed (ActionEvent ae)
  {
   ActionListener fader;
   fader = new ActionListener ()
       {
         int weight = 100;

         public void actionPerformed (ActionEvent ae)
         {
           // When the projector's stop() method is invoked, it
           // stops the show_timer. However, the fade_timer will
           // continue to run until 100 steps have been completed.
           // The following if statement ensures that the fade timer
           // also stops as quickly as possible.

           if (!show_timer.isRunning ())
           {
             fade_timer.stop ();
             return;
           }

           // Because slideIndex is initialized to 1, slideIndex-1
           // results in the introductory slide being blended with
           // the first real slide of the slideshow.

           BufferedImage biSlide;
           biSlide = blend (slides.get (slideIndex-1),
                   slides.get (slideIndex), weight/100.0);

           // Project the slide onto the screen.

           screen.showSlide (biSlide);

           // Countdown until weight drops below 0. At that point,
           // the fade_timer must stop. Also, the slideshow must
           // advance to the next slide. If there are no more
           // slides, the show_timer must stop.

           if (--weight < 0)
           {
             fade_timer.stop ();

             if (++slideIndex == slideCount)
               show_timer.stop ();
           }
         }
       };

   // The following timer must finish before the next invocation of this
   // method.

   fade_timer = new Timer (FADE_DELAY, fader);
   fade_timer.start ();
  }

  /**
  * Return a slide's height -- each slide must have the same height.
  */

  int getSlideHeight ()
  {
   return slideHeight;
  }

  /**
  * Return a slide's width -- each slide must have the same width.
  */

  int getSlideWidth ()
  {
   return slideWidth;
  }

  /**
  * Start the projector.
  *
  * @param screen the display surface on which slides are projected
  */

  void start (Screen screen)
  {
   this.screen = screen;

   // Obtain introductory slide from the screen and replace null
   // placeholder with this special slide.

   slides.set (0, screen.getIntroSlide ());

   show_timer.start ();
  }

  /**
  * Stop the projector.
  */

  void stop ()
  {
   show_timer.stop ();
  }

  /**
  * Blend the contents of two BufferedImages according to a specified
  * weight.
  *
  * @param bi1 first BufferedImage
  * @param bi2 second BufferedImage
  * @param weight the fractional percentage of the first image to keep
  *
  * @return new BufferedImage containing blended contents of BufferedImage
  * arguments
  */

  private BufferedImage blend (BufferedImage bi1, BufferedImage bi2,
                double weight)
  {
   BufferedImage bi3 = new BufferedImage (slideWidth, slideHeight,
                       BufferedImage.TYPE_INT_RGB);
   Graphics2D g2d = bi3.createGraphics ();
   g2d.drawImage (bi1, null, 0, 0);
   g2d.setComposite (AlphaComposite.getInstance (AlphaComposite.SRC_OVER,
                          (float) (1.0-weight)));
   g2d.drawImage (bi2, null, 0, 0);
   g2d.dispose ();

   return bi3;
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

  private void load (String imagesPath) throws IOException
  {
   File imageFilesPath = new File (imagesPath);
   if (!imageFilesPath.isDirectory ())
     throw new IOException (imagesPath+" identifies a file");

   slides = new ArrayList<BufferedImage> ();

   // To simplify the previous action listener, a null placeholder for the
   // screen's introductory slide is added to the slides array list via the
   // following method call. This placeholder is replaced with the actual
   // introductory slide during the projector's start() method call.

   slides.add (null);

   File [] imageFiles = imageFilesPath.listFiles ();
   for (File imageFile: imageFiles)
   {
      // Skip subdirectories.

      if (imageFile.isDirectory ())
        continue;

      // Read next image file. Skip non-image files.

      BufferedImage slide = ImageIO.read (imageFile);
      if (slide == null)
        continue;

      // Capture initial slide width and height as standard slide width
      // and height.

      if (slideWidth == 0)
      {
        slideWidth = slide.getWidth ();
        slideHeight = slide.getHeight ();
      }

      // All slides must have the same width and height.

      if (slide.getWidth () != slideWidth ||
        slide.getHeight () != slideHeight)
        throw new IOException ("slide width/height does not match "+
                   "widths/heights of previous slides");

      slides.add (slide);
   }

   if (slides.size () < 2)
     throw new IOException ("at least one image must be loaded");
  }

  final static int FADE_DELAY = 10;

  final static int SLIDE_DELAY = 4500;

  private ArrayList<BufferedImage> slides;

  private int slideHeight, slideCount, slideIndex = 1, slideWidth;

  private Screen screen;

  private Timer fade_timer, show_timer;
}

/**
 * This class creates a panel that displays each slide.
 */

class Screen extends JPanel
{
  /**
  * Create the screen with a specified size. Also create an introductory
  * slide, which the projector later acquires at startup.
  *
  * @param width screen's preferred width
  * @param height screen's preferred height
  */

  Screen (int width, int height)
  {
   size = new Dimension (width, height);

   biIntroSlide = new BufferedImage (width, height,
                    BufferedImage.TYPE_INT_RGB);
   Graphics2D g2d = biIntroSlide.createGraphics ();
   g2d.setColor (Color.white);
   g2d.fillRect (0, 0, width, height);
   g2d.setRenderingHint (RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);

   g2d.setFont (new Font ("Arial", Font.BOLD | Font.ITALIC, 36));
   FontMetrics fm = g2d.getFontMetrics ();
   Rectangle2D r2d = fm.getStringBounds (TITLE, g2d);
   int text_width = (int)((Rectangle2D.Float) r2d).width;
   int text_height = fm.getHeight ();

   g2d.setColor (Color.black);
   g2d.drawString (TITLE, (width-text_width)/2+SHADOW_OFFSET,
           height/2+SHADOW_OFFSET);

   g2d.setColor (Color.blue);
   g2d.drawString (TITLE, (width-text_width)/2, height/2);
   g2d.dispose ();
  }

  /**
  * Return the introductory slide.
  */

  BufferedImage getIntroSlide ()
  {
   return biIntroSlide;
  }

  /**
  * Make the screen's preferred size available to the windowing toolkit for
  * layout.
  */

  public Dimension getPreferredSize ()
  {
   return size;
  }

  /**
  * Paint the screen.
  *
  * @param g graphics context used to performing painting
  */

  public void paintComponent (Graphics g)
  {
   Graphics2D g2d = (Graphics2D) g;

   // The screen will appear before the projector starts running. Because
   // there is no regular slide at this point, the contents of the
   // introductory slide will be displayed. The introductory slide will
   // eventually fade out and the first projector slide will fade in.

   if (biSlide != null)
     g2d.drawImage (biSlide, null, 0, 0);
   else
     g2d.drawImage (biIntroSlide, null, 0, 0);
  }

  /**
  * Display the next slide.
  *
  * @param biSlide next slide to be presented on the screen
  */

  void showSlide (BufferedImage biSlide)
  {
   this.biSlide = biSlide;
   repaint ();
  }

  final static int SHADOW_OFFSET = 3;

  final static String TITLE = "SlideShow 1.0";

  private BufferedImage biSlide, biIntroSlide;

  private Dimension size;
}