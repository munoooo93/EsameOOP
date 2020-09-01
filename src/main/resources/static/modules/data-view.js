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

		/* Generazione HTML da renderizzare */
		if (!(list['error'] === undefined)) {
			toRender += "Impossibile connettersi alle API di Dropbox...<br>";
			toRender += "<i>Riprovare in seguito</i>";
		} else if (list.length == 0) {
			toRender += "Cartella non presente... Nulla da visualizzare";
		} else {
			for (let i = 0 ; i < list.length; i++) {
				toRender += "<div class=\"row boxed\">"; // TODO add style
				
				toRender += /*"<div class=\"boxed" + /*style +*/ /*"\">"*/"<span class=\"attr-identifier\">Nome file: </span>" + list[i]["name"] + "<br><br><list>";
				
				toRender += "<ul><span class=\"attr-identifier\">Percorso: </span>" + list[i]["path"] + "</ul>";
				toRender += "<ul><span class=\"attr-identifier\">Estensione: </span>" + list[i]["ext"] + "</ul>";
				toRender += "<ul><span class=\"attr-identifier\">Dimensione: </span>" + list[i]["size"] + "</ul>";
				toRender += "<ul><span class=\"attr-identifier\">Cancellato: </span>" + ((list[i]["deleted"]) ? "&#10004" : "&#10006") + "</ul>";
				toRender += "<ul><span class=\"attr-identifier\">Scaricabile: </span>" + ((list[i]["downloadable"]) ? "&#10004" : "&#10006") + "</ul>";

				toRender += "</list></div>";
				
				/*toRender += "</div>";*/
			}
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