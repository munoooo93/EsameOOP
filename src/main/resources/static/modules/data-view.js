class DataView {
	container;
	path;
	retriever;

	constructor() {
		this.container	= document.getElementById("data-container");
		this.path		= document.getElementById("data-path");
		this.retriever	= document.getElementById("data-retriever");
	}

	init() {
		this.retriever.addEventListener("click", () => { this.getData(); });
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
				toRender += "<button class=\"btn btn-light btn-block\" type=\"button\" data-toggle=\"collapse\" data-target=\"#data-content-" + i + "\" aria-expanded=\"false\" aria-controls=\"data-content-" + i +"\">";
				toRender += list[i]["name"];
				toRender += "</button>";
				
				toRender += "<div class=\"collapse\" id=\"data-content-" + i + "\">";
				toRender += "<div class=\"card card-body\">";
				toRender += "<ul><b>Percorso:</b> " + list[i]["path"] + "</ul>";
				toRender += "<ul><b>Estensione: </b>" + list[i]["ext"] + "</ul>";
				toRender += "<ul><b>Dimensione: </b>" + list[i]["size"] + "</ul>";
				toRender += "<ul><b>Cancellato: </b>" + ((list[i]["deleted"]) ? "&#10004" : "&#10006") + "</ul>";
				toRender += "<ul><b>Scaricabile: </b>" + ((list[i]["downloadable"]) ? "&#10004" : "&#10006") + "</ul>";
				toRender += "</div></div><br>";
			}
			toRender += "</div></div>";
		}
		
		this.container.innerHTML = toRender;
	}

	getData() {
		let requestBody = "{ \"path\": \"" + this.path.value + "\" }";
		let xhttp = new XMLHttpRequest();
		
		xhttp.onreadystatechange = () => {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				this.render(JSON.parse(xhttp.responseText));
			}
		};
		xhttp.open("POST", "/data", true);
		xhttp.send(requestBody);
	}
}