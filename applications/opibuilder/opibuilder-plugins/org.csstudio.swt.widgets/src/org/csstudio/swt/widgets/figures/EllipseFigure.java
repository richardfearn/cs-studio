/*
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.swt.widgets.figures;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

import org.csstudio.swt.widgets.introspection.Introspectable;
import org.csstudio.swt.widgets.introspection.ShapeWidgetIntrospector;
import org.csstudio.swt.widgets.util.GraphicsUtil;
import org.csstudio.ui.util.CustomMediaFactory;
import org.csstudio.ui.util.Draw2dSingletonUtil;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

/**
 * An ellipse figure.
 *
 * @author Sven Wende, Alexander Will, Xihui Chen (since import from SDS 2009/10)
 *
 */
public final class EllipseFigure extends Ellipse implements Introspectable {

    /**
     * The fill grade (0 - 100%).
     */
    private double fill = 100.0;

    /**
     * The orientation (horizontal==true | vertical==false).
     */
    private boolean horizontalFill = true;

    /**
     * The transparent state of the background.
     */
    private boolean transparent = false;


    private Color lineColor = CustomMediaFactory.getInstance().getColor(
            CustomMediaFactory.COLOR_PURPLE);

    private Color backGradientStartColor =ColorConstants.white;
    private Color foreGradientStartColor =ColorConstants.white;
    private boolean gradient=false;
    private Boolean support3D = null;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillShape(final Graphics graphics) {
        if(support3D==null)
            support3D = GraphicsUtil.testPatternSupported(graphics);
        Rectangle figureBounds = getClientArea();
        if (!transparent) {
                graphics.pushState();
                if(isEnabled())
                    graphics.setBackgroundColor(getBackgroundColor());
                Pattern pattern = null;
                if(gradient && support3D && isEnabled()){
                    pattern = setGradientPattern(graphics, figureBounds, backGradientStartColor, getBackgroundColor());
                }
                graphics.fillOval(figureBounds);
                if(pattern!=null)
                    pattern.dispose();
                graphics.popState();
        }
        if(getFill() > 0){
            Rectangle fillRectangle;
            if (horizontalFill) {
                int newW = (int) Math.round(figureBounds.width * (getFill() / 100));
                fillRectangle = new Rectangle(figureBounds.x, figureBounds.y, newW,
                        figureBounds.height);
            } else {
                int newH = (int) Math
                        .round(figureBounds.height * (getFill() / 100));
                fillRectangle = new Rectangle(figureBounds.x, figureBounds.y
                        + figureBounds.height - newH, figureBounds.width, newH);
            }

            graphics.pushState();

            graphics.setClip(fillRectangle);
            if(isEnabled())
                graphics.setBackgroundColor(getForegroundColor());

            Pattern pattern = null;
            if(gradient && support3D && isEnabled()){
                pattern = setGradientPattern(graphics, figureBounds, foreGradientStartColor, getForegroundColor());
            }
            graphics.fillOval(figureBounds);
            if(pattern!=null)
                pattern.dispose();
            graphics.popState();
        }
    }

    /**
     * @param graphics
     * @param figureBounds
     * @return
     */
    protected Pattern setGradientPattern(final Graphics graphics,
            Rectangle figureBounds, Color gradientStartColor, Color fillColor) {
        Pattern pattern;
        int tx = figureBounds.x;
        int ty = figureBounds.y+figureBounds.height;
        if(!horizontalFill){
            tx=figureBounds.x+figureBounds.width;
            ty=figureBounds.y;
        }
        int alpha = getAlpha()==null?255:getAlpha();
        //Workaround for the pattern zoom bug on ScaledGraphics:
        //The coordinates need to be scaled for ScaledGraphics.
        double scale = graphics.getAbsoluteScale();
        pattern = new Pattern(Display.getCurrent(),
                (int)(figureBounds.x*scale),
                (int)(figureBounds.y*scale),
                (int)(tx*scale),
                (int)(ty*scale),
                gradientStartColor, alpha, fillColor, alpha);
        graphics.setBackgroundPattern(pattern);
        return pattern;
    }


    public BeanInfo getBeanInfo() throws IntrospectionException {
        return new ShapeWidgetIntrospector().getBeanInfo(this.getClass());
    }


    /**
     * Gets the fill grade.
     *
     * @return the fill grade
     */
    public double getFill() {
        return fill;
    }

    /**
     * @return the start color of gradient.
     */
    public Color getBackGradientStartColor() {
        return backGradientStartColor;
    }

    public Color getForeGradientStartColor() {
        return foreGradientStartColor;
    }

    /**
     * @return the lineColor
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Gets the transparent state of the background.
     *
     * @return the transparent state of the background
     */
    public boolean getTransparent() {
        return transparent;
    }

    /**
     * @return true if this figure is filled with gradient.
     */
    public boolean isGradient() {
        return gradient;
    }

    /**
     * Gets the orientation (horizontal==true | vertical==false).
     *
     * @return boolean The orientation
     */
    public boolean isHorizontalFill() {
        return horizontalFill;
    }

    /**
     * Outlines the ellipse.
     * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
     */
    protected void outlineShape(Graphics graphics) {
        float lineInset = Math.max(1.0f, getLineWidth()) / 2.0f;
        int inset1 = (int)Math.floor(lineInset);
        int inset2 = (int)Math.ceil(lineInset);

        Rectangle r = Draw2dSingletonUtil.getRectangle().setBounds(getClientArea());
        r.x += inset1 ;
        r.y += inset1;
        r.width -= inset1 + inset2;
        r.height -= inset1 + inset2;
        graphics.pushState();
        if(isEnabled())
            graphics.setForegroundColor(lineColor);
        graphics.drawOval(r);
        graphics.popState();
    }


    /**
     * Sets the fill grade.
     *
     * @param fill
     *            the fill grade.
     */
    public void setFill(final double fill) {
        if(this.fill == fill)
            return;
        this.fill = fill;
        repaint();
    }

    public void setGradient(boolean gradient) {
        this.gradient = gradient;
        repaint();
    }

    public void setBackGradientStartColor(Color gradientStartColor) {
        this.backGradientStartColor = gradientStartColor;
        repaint();
    }

    public void setForeGradientStartColor(Color foreGradientStartColor) {
        this.foreGradientStartColor = foreGradientStartColor;
        repaint();
    }



    /**
     * Sets the orientation (horizontal==true | vertical==false).
     *
     * @param horizontal
     *            The orientation.
     */
    public void setHorizontalFill(final boolean horizontal) {
        if(this.horizontalFill == horizontal)
            return;
        this.horizontalFill = horizontal;
        repaint();
    }

    public void setLineColor(Color lineColor) {
        if(this.lineColor != null && this.lineColor.equals(lineColor))
            return;
        this.lineColor = lineColor;
        repaint();
    }

    /**
     * Sets the transparent state of the background.
     *
     * @param transparent
     *            the transparent state.
     */
    public void setTransparent(final boolean transparent) {
        if(this.transparent == transparent)
            return;
        this.transparent = transparent;
        repaint();
    }


}
