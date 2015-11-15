package com.asu.score.hackslash.git.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

public class GraphTest2 {

	public static void main(String[] args) {
		Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Scatter Chart");
        shell.setSize(500, 400);
        shell.setLayout(new FillLayout());
        Composite com = shell;
		
        Chart chart = new Chart(com, SWT.NONE);
        
     // set titles
     chart.getTitle().setText("Category Axis Example");
     chart.getAxisSet().getXAxis(0).getTitle().setText("Month");
     chart.getAxisSet().getYAxis(0).getTitle().setText("Amplitude");

     // set category
     chart.getAxisSet().getXAxis(0).enableCategory(true);
     chart.getAxisSet().getXAxis(0).setCategorySeries(
             new String[] { "Jan", "Feb", "Mar", "Apr", "May" });

     // create bar series
     IBarSeries barSeries1 = (IBarSeries) chart.getSeriesSet().createSeries(
             SeriesType.BAR, "bar series 1");
     barSeries1.setYSeries(new double[] {3, 4 ,5 ,7 ,2});
     barSeries1.setBarColor(Display.getDefault().getSystemColor(
             SWT.COLOR_GREEN));

     IBarSeries barSeries2 = (IBarSeries) chart.getSeriesSet().createSeries(
             SeriesType.BAR, "bar series 2");
     barSeries2.setYSeries(new double[] {3, 4 ,5 ,7 ,2});

     // adjust the axis range
     chart.getAxisSet().adjustRange();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

}
