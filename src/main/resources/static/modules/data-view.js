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
		const style = "cols-2";
		let toRender = "";

		console.log("Process...");
		console.log(list);

		for (let i = 0 ; i < list.length; i++) {
			console.log(list[i]);
			toRender += "<div class=\"row\">"; // TODO add style
			toRender += this.createTextContainer(list[i]["name"], style);
			toRender += this.createTextContainer(list[i]["path"], style);
			toRender += this.createTextContainer(list[i]["ext"], style);
			toRender += this.createTextContainer(list[i]["size"], style);
			toRender += this.createTextContainer(list[i]["deleted"], style);
			toRender += this.createTextContainer(list[i]["downloadable"], style);
			toRender += "</div>";
		}
		
		console.log("Rendering...");
		
		document.getElementById("data-container").innerHTML = toRender;
	}

	createTextContainer(text, cssClass) {
		return ("<div class=\"" + cssClass + "\">" + text + "</div>");
	}

	getData() {
		let requestBody = "{ \"path\": \"" + document.getElementById("data-path").value + "\" }";
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