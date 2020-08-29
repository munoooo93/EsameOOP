class App {
	dataView;
	metadataView;
	statsView;

	constructor() {
		this.dataView		= new DataView();
		this.metadataView	= new MetadataView();
		this.statsView		= new StatsView();
	}

	run() {
		this.dataView.init();
		this.metadataView.init();
		this.statsView.init();
	}
};

const app = new App();
app.run();