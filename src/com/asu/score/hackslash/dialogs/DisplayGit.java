package com.asu.score.hackslash.dialogs;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.nebula.visualization.widgets.figures.MeterFigure;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

import com.asu.score.hackslash.statistics.GitData;

/**
 * Show graphs for Display in Git.
 *
 */
public class DisplayGit {
	
	public void showUserCommitsGraph() throws IOException {

		Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setText("User Commit Statistics");
        shell.setSize(500, 400);
        shell.setLayout(new FillLayout());
        Composite com = shell;
		GitData gitData = new GitData();
        Chart chart = new Chart(com, SWT.NONE);
        
     // set titles
     chart.getTitle().setText("User Commit Statistics");
     chart.getAxisSet().getXAxis(0).getTitle().setText("Contributors");
     chart.getAxisSet().getYAxis(0).getTitle().setText("Commits");

     // set category
     Map<String,Integer> contributors = gitData.getCommitsPerContributor();
		String[] users = new String[contributors.size()];
		double[] commits = new double[contributors.size()];
		int index =0;
		int max = 0;
		for (Map.Entry<String, Integer> entry : contributors.entrySet()) {
			users[index] = entry.getKey();
			commits[index] = entry.getValue();
			index++;
			if(entry.getValue() > max){
				max = entry.getValue();
			}
		}

     chart.getAxisSet().getXAxis(0).enableCategory(true);
     chart.getAxisSet().getXAxis(0).setCategorySeries(users);

     // create bar series
     IBarSeries barSeries1 = (IBarSeries) chart.getSeriesSet().createSeries(
             SeriesType.BAR, "Number of Commits per User");
     barSeries1.setYSeries(commits);
     barSeries1.setBarColor(Display.getDefault().getSystemColor(
             SWT.COLOR_BLUE));
     // adjust the axis range
     chart.getAxisSet().adjustRange();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	
	}

	
	/**
	 * Shows monthly commits in the form of a meter.
	 * @param count
	 * @throws IOException 
	 */
	public void showCommitMeter() throws IOException {
		
		final Shell shell = new Shell();
		shell.setText("-- Total Commits in last month --");
		shell.setSize(1000, 700);
		shell.open();
		//Getting total number of commits
		GitData gitData = new GitData();
		int count = gitData.getTotalCurrentMonthCommits();
		

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
	
	public void showMonthlyCommits() throws IOException {

		Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Monthly Commit Statistics");
        shell.setSize(1000, 800);
        shell.setLayout(new FillLayout());
        Composite com = shell;
		GitData gitData = new GitData();
        Chart chart = new Chart(com, SWT.NONE);
        
     // set titles
     chart.getTitle().setText("Monthly Commit Statistics");
     chart.getAxisSet().getXAxis(0).getTitle().setText("Months");
     chart.getAxisSet().getYAxis(0).getTitle().setText("Commits");
      // set category
     List<List<String>> all = gitData.getMonthlyCommits();
 	 String[] months = new String[12];
	 double[] commits = new double[12];
	 for (int i=0;i<12;i++){
		 List<String> month = all.get(i);
		 months[i] = month.get(0);
		 commits[i] = Double.parseDouble(month.get(1)); 
	 }
 
     chart.getAxisSet().getXAxis(0).enableCategory(true);
     chart.getAxisSet().getXAxis(0).setCategorySeries(months);

     // create bar series
     IBarSeries barSeries1 = (IBarSeries) chart.getSeriesSet().createSeries(
             SeriesType.BAR, "Number of Commits per Month");
     barSeries1.setYSeries(commits);
     barSeries1.setBarColor(Display.getDefault().getSystemColor(
             SWT.COLOR_YELLOW));

    /* IBarSeries barSeries2 = (IBarSeries) chart.getSeriesSet().createSeries(
             SeriesType.BAR, "bar series 2");
     barSeries2.setYSeries(new double[] {3, 4 ,5 ,7 ,2});
*/
     // adjust the axis range
     chart.getAxisSet().adjustRange();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	
	}

	public static void main(String[] args) throws IOException{
		DisplayGit dg = new DisplayGit();
		dg.showMonthlyCommits();
	}

}
