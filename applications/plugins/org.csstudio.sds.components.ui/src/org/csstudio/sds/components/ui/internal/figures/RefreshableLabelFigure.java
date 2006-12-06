package org.csstudio.sds.components.ui.internal.figures;

import org.csstudio.sds.dataconnection.StatisticUtil;
import org.csstudio.sds.ui.editparts.IRefreshableFigure;
import org.csstudio.sds.uil.CustomMediaFactory;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

/**
 * A label figure.
 * 
 * @author Sven Wende
 * 
 */
public final class RefreshableLabelFigure extends Label implements
		IRefreshableFigure {
	/**
	 * Default label font.
	 */
	public static final Font FONT = CustomMediaFactory.getInstance().getFont(
			"Arial", 8, SWT.NONE);

	/**
	 * Constructor.
	 */
	public RefreshableLabelFigure() {

		setFont(FONT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void repaint() {
		super.repaint();
		StatisticUtil.getInstance().recordWidgetRefresh(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void randomNoiseRefresh() {
		setText("" + Math.random());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText() {
		return super.getText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(final String s) {
		super.setText(s);
	}

}
