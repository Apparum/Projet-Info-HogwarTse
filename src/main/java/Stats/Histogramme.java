package Stats;

import java.awt.Color;
import java.awt.Panel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

/**
 *
 * Histogramme.
 *
 */
public class Histogramme {
	Panel p = new Panel();

	/**
	 * Constructeur de l'histogramme.
	 *
	 * @param title
	 *            : Nom de l'histogramme.
	 * @param panel
	 *            : Conteneur de l'histogramme.
	 * @param data
	 *            : Informations de l'histogramme.
	 */
	public Histogramme(final String title, final Panel panel, final double[][] data) {
		final JFreeChart chart = this.createChart(this.createDataset(data), title);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(0, 0, panel.getWidth(), panel.getHeight());
		this.p.setBackground(new Color(13, 31, 45));
		this.p.setLayout(null);
		this.p.add(chartPanel);
		this.p.setBounds(0, 0, panel.getWidth(), panel.getHeight());
		panel.add(this.p);
	}

	/**
	 * Création de l'histogramme et remplissage de ce dernier.
	 *
	 * @param dataset
	 *            : Le DataSet à représenter.
	 * @param title
	 *            : Le titre du graphique.
	 * @return Le graphique.
	 */
	private JFreeChart createChart(final CategoryDataset dataset, final String title) {

		final JFreeChart chart = ChartFactory.createBarChart(title, // Titre
				"Frame", // Titre axe des abscisses
				"Nombre de personnes", // Titre axe des ordonnées
				dataset, // Datatset
				PlotOrientation.VERTICAL, // Orientation des barres
				false, // Inclusion de légendes
				true, false);
		chart.setBackgroundPaint(Color.lightGray);
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setRange(0.0, 10.0);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return chart;
	}

	/**
	 * Création des données au format JFreeChart.
	 *
	 * @param data
	 *            : Les valeurs en type numérique.
	 * @return Le DataSet compréhensible par la bibliothéque.
	 */
	private CategoryDataset createDataset(final double[][] data) {
		return DatasetUtilities.createCategoryDataset("", "", data);
	}
}
