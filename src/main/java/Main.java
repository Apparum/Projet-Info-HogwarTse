import Graphic.StartingScreen;
import Graphic.StatScreen;
import Graphic.VideoScreen;

/**
 *
 * La classe Main sert à cadencer l'intégralité du programme. C'est elle qui
 * contient tous les autres objets du programme et décide comment ceux-ci
 * doivent intéragir.
 *
 */
public class Main {
	/*
	 * Les ecrans sont générés ici ainsi que des variables pour naviguer entre ces
	 * derniers.
	 */
	StartingScreen startingScreen = new StartingScreen();
	VideoScreen videoScreen = new VideoScreen();
	StatScreen statScreen = new StatScreen();
	private boolean goToStartingScreen = true;
	private boolean goToVideoScreen = false;
	private boolean goToStatScreen = false;

	/**
	 *
	 * Le main vient créer une instance pour la session à venir.
	 *
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(final String[] args) throws InterruptedException {
		final Main instance = new Main();
		instance.start();
	}

	/**
	 *
	 * Cette fonction est le cadenceur de tout le programme. Elle va récupérer en
	 * permanence les informations de déplacements de fenêtre et s'occuper de faire
	 * passer les informations entre celles-ci. Elle s'occupe également d'adapter
	 * l'affichage et de gérer la fréquence d'affichage.
	 *
	 * @throws InterruptedException
	 */
	public void start() throws InterruptedException {
		while (true) {
			if (this.goToStartingScreen == true) {
				this.startingScreen.setFrameSize(this.videoScreen.getFrameSize());
				this.videoScreen.setLocation(this.startingScreen.getX(), this.startingScreen.getY());
				this.startingScreen.setVisible(true);
				this.videoScreen.setVisible(false);
				this.statScreen.setVisible(false);
				this.videoScreen.setGoToMenu(false);
				this.statScreen.setGoToMenu(false);
			}
			if (this.startingScreen.isSetupVideoReader()) {
				this.videoScreen.setPath(this.startingScreen.getPath());
				this.videoScreen.setHogOrKalman(this.startingScreen.isHogOrKalman());
				this.videoScreen.setHogVisible(this.startingScreen.isHogVisible());
				this.videoScreen.setFrameoff(this.startingScreen.getFrameoff());
				this.videoScreen.setup();
				this.startingScreen.setSetupVideoReader(false);
			}
			if (this.goToVideoScreen == true) {
				this.videoScreen.setFrameSize(this.startingScreen.getFrameSize());
				this.videoScreen.setLocation(this.startingScreen.getX(), this.startingScreen.getY());
				this.videoScreen.setVisible(true);
				this.startingScreen.setVisible(false);
				this.statScreen.setVisible(false);
				this.startingScreen.setGoToVideo(false);
				this.statScreen.setGoToVideo(false);
			}
			if (this.goToStatScreen == true) {
				this.statScreen.setFrameSize(this.videoScreen.getFrameSize());
				this.statScreen.setLocation(this.startingScreen.getX(), this.startingScreen.getY());
				this.statScreen.setVisible(true);
				this.startingScreen.setVisible(false);
				this.videoScreen.setVisible(false);
				this.videoScreen.setGoToStat(false);
				this.statScreen.setSize(this.videoScreen.size());
				this.statScreen.setCurrentFrame(this.videoScreen.getCurrentFrame());
				this.statScreen.setNbPerFrame(this.videoScreen.getNbPerFrame());
				this.statScreen.setRectPerFrame(this.videoScreen.getRectPerFrame());
				this.statScreen.setRectPeoplePerFrame(this.videoScreen.getRectPeoplePerFrame());
				this.statScreen.refresh();
				this.videoScreen.setPlay(false);
			}
			this.goToStatScreen = this.videoScreen.isGoToStat();
			this.goToVideoScreen = (this.statScreen.isGoToVideo() || this.startingScreen.isGoToVideo());
			this.goToStartingScreen = (this.videoScreen.isGoToMenu() || this.statScreen.isGoToMenu());
			Thread.sleep(10);
			if (this.videoScreen.isPlay()) {
				if (!this.videoScreen.moveFrame(1)) {
					this.videoScreen.setPlay(false);
				}
			}
			if (this.startingScreen.isQuit()) {
				break;
			}
		}
	}
}
