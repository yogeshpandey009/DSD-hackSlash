package com.asu.score.hackslash.dialogs;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.nebula.visualization.widgets.figures.MeterFigure;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Show graphs for Display in Git.
 *
 */
public class DisplayGit {
	
	public void showCommitGraph() {

		
	}

	
	/**
	 * Shows monthly commits in the form of a meter.
	 * @param count
	 */
	public void showCommitMeter(int count) {
		
		final Shell shell = new Shell();
		shell.setText("-- Total Commits in last month --");
		shell.setSize(300, 150);
		shell.open();

		// use LightweightSystem to create the bridge between SWT and draw2D
		final LightweightSystem lws = new LightweightSystem(shell);

		// Create Gauge
		final MeterFigure meterFigure = new MeterFigure();

		// Init gauge
		meterFigure.setBackgroundColor(XYGraphMediaFactory.getInstance().getColor(255, 255, 255));

		meterFigure.setBorder(new SchemeBorder(SchemeBorder.SCHEMES.ETCHED));

		meterFigure.setRange(0, 100);
		meterFigure.setLoLevel(0);
		meterFigure.setLoloLevel(0);
		meterFigure.setHiLevel(50);
		meterFigure.setHihiLevel(80);
		meterFigure.setMajorTickMarkStepHint(28);
		
		
		meterFigure.setValue(count);

		lws.setContents(meterFigure);


		Display display = Display.getDefault();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

}
