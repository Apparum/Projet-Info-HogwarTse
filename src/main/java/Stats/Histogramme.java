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

public class Histogramme {
	Panel p = new Panel();

	// Constructeur
	public Histogramme(final String title, Panel panel, double[][] data) {
		// On a besoin d'un Dataset (les stats...) + de créer un objet Chart
		final JFreeChart chart = this.createChart(this.createDataset(data), title);

		// Ajout du Chart à un Panel
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(0, 0, panel.getWidth(), panel.getHeight());

		this.p.setBackground(new Color(13, 31, 45));
		this.p.setLayout(null);
		this.p.add(chartPanel);
		this.p.setBounds(0, 0, panel.getWidth(), panel.getHeight());
		panel.add(this.p);
	}

	// Création du Dataset (un tableau de valeurs au pif) et on le retourne
	private CategoryDataset createDataset(double[][] data) {
		return DatasetUtilities.createCategoryDataset("", "", data);
	}

	// Création de l'histogramme (qui prendra en valeur le Dataset créé
	// précédemment)
	private JFreeChart createChart(final CategoryDataset dataset, String title) {

		final JFreeChart chart = ChartFactory.createBarChart(title, // Titre
				"Frame", // Titre axe des abscisses
				"Nombre de personnes", // Titre axe des ordonnées
				dataset, // Datatset
				PlotOrientation.VERTICAL, // Orientation des barres
				false, // Inclusion de légendes
				true, false);

		// PARTIE OPTIONNELLE POUR PIMP MY CHART

		// Couleur du fond
		chart.setBackgroundPaint(Color.lightGray);

		// Localisation des axes
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		// change the auto tick unit selection to integer units only...
		// J'ai pas tout capté j'ai trouvé la doc de cette partie sur internet donc je
		// l'ai laissée comme je l'ai trouvée
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setRange(0.0, 10.0);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}
}
