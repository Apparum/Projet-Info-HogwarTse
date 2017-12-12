package Stats;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Histogramme extends ApplicationFrame {

	// Constructeur
	public Histogramme(final String title) {
		super(title);
		// On a besoin d'un Dataset (les stats...) + de créer un objet Chart
		final CategoryDataset dataset = this.createDataset();
		final JFreeChart chart = this.createChart(dataset);

		// Ajout du Chart à un Panel
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(900, 800));
		this.setContentPane(chartPanel);

	}

	// Création du Dataset (un tableau de valeurs au pif) et on le retourne
	private CategoryDataset createDataset() {
		final double[][] data = new double[][] { { 1.0, 43.0, 35.0, 58.0, 54.0, 77.0, 71.0, 89.0 },
				{ 54.0, 75.0, 63.0, 83.0, 43.0, 46.0, 27.0, 13.0 },
				{ 41.0, 33.0, 22.0, 34.0, 62.0, 32.0, 42.0, 34.0 } };

		return DatasetUtilities.createCategoryDataset("Zone", "Frame ", data);
	}

	// Création de l'histogramme (qui prendra en valeur le Dataset créé
	// précédemment)
	private JFreeChart createChart(final CategoryDataset dataset) {

		final JFreeChart chart = ChartFactory.createBarChart("Histogramme Démo", // Titre
				"Vidéo", // Titre axe des abscisses
				"Nombre de personnes", // Titre axe des ordonnées
				dataset, // Datatset
				PlotOrientation.VERTICAL, // Orientation des barres
				true, // Inclusion de légendes
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
		rangeAxis.setRange(0.0, 100.0);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

	// C'EST LA LUUUUUTTE FINAAAAAALE
	// Hum... Plus sérieusement, code main pour lancer le tout
	public static void main(final String[] args) {

		final Histogramme demo = new Histogramme("Histogramme Démo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

}
