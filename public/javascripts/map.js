function LocationMap() {
	var location = [ 51.505, -0.09 ];
	var maxZoom = 7;
	var map = L.map('map').locate({
		setView : true
	});

	map.on('dblclick', function(e) {
		location = e.latlng;
		map.setView(location, maxZoom);
		setMarker();
	});

	map.on('locationfound', geoSuccess);
	map.on('locationerror', geoError);

	function geoSuccess(position) {
		location = position.latlng;
		renderMap();
		setMarker();
	}

	function geoError() {
		askWhereAreYou();
		renderMap();
		setMarker();
	}

	function askWhereAreYou() {
		var result = prompt("Where are you?")
	}

	function setLocated() {
		located = true;
	}

	function renderMap() {
		L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
			attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
			maxZoom: maxZoom,
			id : MapBox.id,
			accessToken : MapBox.accessToken
		}).addTo(map);
	}

	function setMarker() {
		L.marker(location).addTo(map);
	}

	return {
		render : function() {
			renderMap();
			setMarker();
		}
	}
};

var locationMap = new LocationMap();

locationMap.render();