package com.siberhus.hswing;

import javax.swing.JViewport;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.Point;

/**
 * a subclass of JViewport that overrides a method in order not
 * to repaint its component many times during a vertical drag
 *
 * @author Brian Cole
 */
public class LazyViewport extends JViewport {

    /**
     * equivalent to <b>new JScrollPane(view)</b> except uses a LazyViewport
     */
    public static JScrollPane createLazyScrollPaneFor(Component view) {
        LazyViewport vp = new LazyViewport();
        vp.setView(view);
        JScrollPane scrollpane = new JScrollPane();
        scrollpane.setViewport(vp);
        return scrollpane;
    }

    /**
     * overridden to not repaint during during a vertical drag
     */
    public void setViewPosition(Point p) {
        Component parent = getParent();
        if ( parent instanceof JScrollPane &&
             ((JScrollPane)parent).getVerticalScrollBar().getValueIsAdjusting() ) {
            // value is adjusting, skip repaint
            return;
        }
        super.setViewPosition(p);
    }

    private static final long serialVersionUID = 2006L;
}
