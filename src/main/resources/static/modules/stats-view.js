class StatsView {
	container;
	path;
	includeDeleted;
	retrieverAll;
	retrieverFolder;

	constructor() {
		this.container			= document.getElementById("stats-container");
		this.path				= document.getElementById("stats-path");
		this.includeDeleted		= document.getElementById("stats-del");
		this.retrieverAll		= document.getElementById("stats-retriever-all");
		this.retrieverFolder	= document.getElementById("stats-retriever-folder");
	}

	init() {
		this.retrieverAll.addEventListener("click", () => { this.getAllStats(); });
		this.retrieverFolder.addEventListener("click", () => { this.getFolderStats(); });
	}

	render(list) {
		const style = "cols-2";
		let toRender = "";

		if (!(list['error'] === undefined)) {
			toRender += "Impossibile connettersi alle API di Dropbox...<br>";
			toRender += "<i>Riprovare in seguito</i>";
		} else if (list.length == 0) {
			toRender += "Cartella non presente... Nulla da visualizzare";
		} else {
			/* Add nothing to display */
			/* Add error handling */
			for (let i = 0 ; i < list.length; i++) {
				toRender += "<div class=\"row\">"; // TODO add style
				toRender += this.createTextContainer('Estensione', list[i]['ext'], style);
				toRender += this.createTextContainer('Numero', list[i]['count'], style);
				toRender += this.createTextContainer('Dimensione minima', list[i]['min-size'], style);
				toRender += this.createTextContainer('Dimensione massima', list[i]['max-size'], style);
				toRender += this.createTextContainer('Dimensione media', list[i]['avg-size'], style);
				toRender += "</div>";
			}
		}

		this.container.innerHTML = toRender;
	}

	createTextContainer(fieldName, text, cssClass) {
		return ("<div class=\"" + cssClass + "\"> <span>" + fieldName + ":</span><br>" + text + "</div>");
	}

	getAllStats() {
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = () => {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				this.render(JSON.parse(xhttp.responseText));
			}
		};
		xhttp.open("GET", "/stats/overall?includeDeleted=" + this.includeDeleted.checked, true);
		xhttp.send();
	}

	getFolderStats() {
		let requestBody =	"{ \"path\": \"" + this.path.value + "\", " +
							"\"include-deleted\": " + this.includeDeleted.checked + " }";
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = () => {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				this.render(JSON.parse(xhttp.responseText));
			}
		};
		xhttp.open("POST", "/stats", true);
		xhttp.send(requestBody);
	}
}