function LocationMap(client) {
	var location = {
		lat: 51.505,
		lng: -0.09
	};
	
	var maxZoom = 7;
	var map = L.map('map').locate({
		setView: true
	});

	map.on('dblclick', function(e) {
		location = e.latlng;
		setView();
		client.getTrends(location, showTrends, showError);
	});

	map.on('locationfound', geoSuccess);
	map.on('locationerror', geoError);

	function geoSuccess(position) {
		location = position.latlng;
		client.getTrends(location, showTrends, showError);
	}

	function geoError() {
		//askWhereAreYou();
		client.getTrends(location, showTrends, showError);
	}

	function askWhereAreYou() {
		var result = prompt("Where are you?");
	}

	function showTrends(trends) {
		popup(formatTrends(trends));
	}
	
	function showError(xhr, message, error) {
		console.error(message);
		console.error(error);
		popup('<span class="error">Sorry! Something went wrong.</span>');
	}
	
	function formatTrends(trends) {
		function formatTrend(trend) {
			return "<a href=" + trend.url + ">" + trend.name + "</a>";
		}

		trends = trends.length > 5 && trends.slice(0, 5);
		
		var htmlContent = "";
		for (var i = 0; i < trends.length; i++) {
			htmlContent += formatTrend(trends[i]) + "<br />";
		}

		return htmlContent;
	}

	function popup(content) {
		var marker = L.marker(location).addTo(map);
		marker.bindPopup(content).openPopup();
	}

	function setView() {
		map.setView(location, maxZoom);
	}

	function renderMap() {
		L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
			attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
			maxZoom: maxZoom,
			id : MapBox.id,
			accessToken : MapBox.accessToken
		}).addTo(map);
		setView();
	}

	return {
		render: renderMap
	}
};

function TwitterClient() {
	var successCallback;
	var errorCallback;

	function getClosestLocation(location, onSuccess, onError) {
		var closestLocationRoute = jsRoutes.controllers.ApplicationController.locations(location.lat, location.lng)
		$.ajax(closestLocationRoute.url).success(onSuccess).error(onError);
	}

	function trendsAjaxCall(data) {
		var trendsRoute = jsRoutes.controllers.ApplicationController.trends(data[0].woeid);
		$.ajax(trendsRoute.url).success(successCallback).error(errorCallback);
	}
	
	return {
		getTrends: function (location, onSuccess, onError) {
			successCallback = onSuccess;
			errorCallback = onError;
			getClosestLocation(location, trendsAjaxCall, errorCallback);
		}
	}
};

var locationMap = new LocationMap(new TwitterClient());

locationMap.render();