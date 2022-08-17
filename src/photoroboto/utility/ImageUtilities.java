/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package photoroboto.utility;

import com.jhlabs.image.ContrastFilter;
import com.jhlabs.image.ExposureFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.SaturationFilter;
import com.thebuzzmedia.imgscalr.Scalr;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import photoroboto.enums.OverlayPosition;

/**
 *
 * @author Brian
 */
public class ImageUtilities
{

    public static BufferedImage computeOverlay(BufferedImage srcImage, BufferedImage overlayImage, int resize, OverlayPosition position, int horizontalInset, int verticalInset, float overlayAlpha)
    {

        if (overlayImage != null)
        {

            if (resize > 0)
            {
                BufferedImage resizedOverlayImage = Scalr.resize(overlayImage, Scalr.Method.SPEED, resize);
                overlayImage = resizedOverlayImage;
            }

            Graphics2D CompositeGraphics = srcImage.createGraphics();
            CompositeGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, overlayAlpha));

            int srcWidth = srcImage.getWidth();
            int srcHeight = srcImage.getHeight();

            int overlayWidth = overlayImage.getWidth();
            int overlayHeight = overlayImage.getHeight();

            int x = 0;
            int y = 0;

            //if top left
            if (position == OverlayPosition.TOP_LEFT)
            {
                x = 0 + horizontalInset;
                y = 0 + verticalInset;
            }
            else if (position == OverlayPosition.TOP)
            {
                x = (srcWidth / 2) - (overlayWidth / 2) + horizontalInset;
                y = 0 + verticalInset;
            }
            else if (position == OverlayPosition.TOP_RIGHT)
            {
                x = (srcWidth - overlayWidth) - horizontalInset;
                y = 0 + verticalInset;
            }
            else if (position == OverlayPosition.LEFT)
            {
                x = 0 + horizontalInset;
                y = (srcHeight / 2) - (overlayHeight / 2) + verticalInset;
            }
            else if (position == OverlayPosition.CENTER)
            {
                x = (srcWidth / 2) - (overlayWidth / 2) + horizontalInset;
                y = (srcHeight / 2) - (overlayHeight / 2) + verticalInset;
            }
            else if (position == OverlayPosition.RIGHT)
            {
                x = (srcWidth - overlayWidth) - horizontalInset;
                y = (srcHeight / 2) - (overlayHeight / 2) + verticalInset;
            }
            else if (position == OverlayPosition.BOTTOM_LEFT)
            {
                x = 0 + horizontalInset;
                y = (srcHeight - overlayHeight) - verticalInset;
            }
            else if (position == OverlayPosition.BOTTOM)
            {
                x = (srcWidth / 2) - (overlayWidth / 2) + horizontalInset;
                y = (srcHeight - overlayHeight) - verticalInset;
            }
            else if (position == OverlayPosition.BOTTOM_RIGHT)
            {
                x = (srcWidth - overlayWidth) - horizontalInset;
                y = (srcHeight - overlayHeight) - verticalInset;
            }

            CompositeGraphics.drawImage(overlayImage, x, y, null);
            CompositeGraphics.dispose();

            return srcImage;
        }

        return null;
    }

    public static BufferedImage postProcess(BufferedImage srcImage, boolean exposure, float exposureValue, boolean contrast, float contrastValue, boolean brightness, float brightnessValue, boolean grayScale, boolean saturation, float saturationValue)
    {

        if (exposure)
        {
            ExposureFilter exposureFilter = new ExposureFilter();
            exposureFilter.setExposure(exposureValue);
            srcImage = exposureFilter.filter(srcImage, null);
        }

        if (contrast || brightness)
        {
            ContrastFilter contrastFilter = new ContrastFilter();
            contrastFilter.setContrast(contrastValue);
            contrastFilter.setBrightness(brightnessValue);
            srcImage = contrastFilter.filter(srcImage, null);
        }

        if (grayScale)
        {
            GrayscaleFilter grayscaleFilter = new GrayscaleFilter();
            srcImage = grayscaleFilter.filter(srcImage, null);
        }

        if (saturation)
        {
            SaturationFilter saturationFilter = new SaturationFilter();
            saturationFilter.setAmount(saturationValue);
            srcImage = saturationFilter.filter(srcImage, null);
        }

        return srcImage;
    }
}
