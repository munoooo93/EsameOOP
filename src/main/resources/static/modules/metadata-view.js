class MetadataView {
	container;
	retriever;

	constructor() {
		this.container		= document.getElementById("metadata-container");
		this.retriever		= document.getElementById("metadata-retriever");
	}

	init() {
		this.retriever.addEventListener("click", () => { this.getMetadata(); });
	}

	render(list) {
		let toRender = "";

		toRender += "<h1>Metadati:</h1><div class=\"container\">";

		for (let i = 0 ; i < list.length; i++) {
			toRender += "<button class=\"btn btn-light btn-block\" type=\"button\" data-toggle=\"collapse\" data-target=\"#metadata-content-" + i + "\" aria-expanded=\"false\" aria-controls=\"metadata-content-" + i +"\">";
			toRender += list[i]['sourceField'];
			toRender += "</button>";
			
			toRender += "<div class=\"collapse\" id=\"metadata-content-" + i + "\">";
			toRender += "<div class=\"card card-body\">" + list[i]['type'] + "</div>";
			toRender += "</div><br>";
		}
		toRender += "</div>";

		this.container.innerHTML = toRender;
	}

	getMetadata() {
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = () => {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				this.render(JSON.parse(xhttp.responseText));
				this.retriever.remove();
			}
		};
		xhttp.open("GET", "/metadata", true);
		xhttp.send();
	}
}