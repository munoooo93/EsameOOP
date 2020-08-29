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
		const style = "";
		let toRender = "";

		for (let i = 0 ; i < list.length; i++) {
			toRender += "<div class=\"\">"; // TODO add style
			toRender += this.createTextContainer(list[i]['sourceField'], style);
			toRender += this.createTextContainer(list[i]['type'], style)
			toRender += "</div>";
		}

		this.container.innerHTML = toRender;
	}

	createTextContainer(text, cssClass) {
		return ("<div class=\"" + cssClass + "\">" + text + "</div>");
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