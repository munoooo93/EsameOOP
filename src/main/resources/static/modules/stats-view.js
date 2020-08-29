class StatsView {
	container;
	includeDeleted;
	retriever;

	constructor() {
		this.container		= document.getElementById("stats-container");
		this.includeDeleted	= document.getElementById("stats-del");
		this.retriever		= document.getElementById("stats-retriever");
	}

	init() {
		this.retriever.addEventListener("click", () => { this.getStats(); });
	}

	render(list) {
		const style = "cols-2";
		let toRender = "";

		for (let i = 0 ; i < list.length; i++) {
			toRender += "<div class=\"row\">"; // TODO add style
			toRender += this.createTextContainer(list[i]['ext'], style);
			toRender += this.createTextContainer(list[i]['count'], style);
			toRender += this.createTextContainer(list[i]['min-size'], style);
			toRender += this.createTextContainer(list[i]['max-size'], style);
			toRender += this.createTextContainer(list[i]['avg-size'], style);
			toRender += "</div>";
		}

		this.container.innerHTML = toRender;
	}

	createTextContainer(text, cssClass) {
		return ("<div class=\"" + cssClass + "\">" + text + "</div>");
	}

	getStats() {
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = () => {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				this.render(JSON.parse(xhttp.responseText));
			}
		};
		xhttp.open("GET", "/stats?includeDeleted=" + this.includeDeleted.checked, true);
		xhttp.send();
	}
}