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
		let toRender = "";

		if (!(list['error'] === undefined)) {
			toRender = "<div class=\"container\" style=\"padding-top:10px;\"><div class=\"container\"><div class=\"alert alert-danger\" style=\"float:center\"> Impossibile connettersi alle API di Dropbox... </div></div></div>";
		} else if (list.length == 0) {
			toRender = "<div class=\"container\" style=\"padding-top:10px;\"><div class=\"container\"><div class=\"alert alert-warning\" style=\"float:center\"> Nulla da visualizzare... </div></div></div>";
		} else {
			
			toRender += "<div class=\"container\"><div class=\"container\">";
			for (let i = 0 ; i < list.length; i++) {
				toRender += "<button class=\"btn btn-light btn-block\" type=\"button\" data-toggle=\"collapse\" data-target=\"#stats-content-" + i + "\" aria-expanded=\"false\" aria-controls=\"stats-content-" + i +"\">";
				toRender += (list[i]['ext'] === "unknown") ? list[i]['ext'] : ("." + list[i]['ext']);
				toRender += "</button>";
				
				toRender += "<div class=\"collapse\" id=\"stats-content-" + i + "\">";
				toRender += "<div class=\"card card-body\">";
				//toRender += "<ul><b>Esyensione:</b> " + list[i]['ext'] + "</ul>";
				toRender += "<ul><b>Numero: </b>" + list[i]['count'] + "</ul>";
				toRender += "<ul><b>Dim. minima: </b>" + list[i]['min-size'] + "</ul>";
				toRender += "<ul><b>Dim. massima: </b>" + list[i]['max-size'] + "</ul>";
				toRender += "<ul><b>Dim. media: </b>" + list[i]['avg-size'] + "</ul>";
				toRender += "</div></div><br>";
			}
			toRender += "</div></div>";
		}

		this.container.innerHTML = toRender;
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