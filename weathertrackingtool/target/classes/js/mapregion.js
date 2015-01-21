var javaBridge = null;
var popup = L.popup();
var map ;
var t=[];
var input = [];

function Logger(id, javaBridge) {
        this.id = id;
        this.javaBridge = javaBridge;
        }

        Logger.prototype.formatMessage = function(message) {
        return "[" + this.id + "] - " + message;
        }

        Logger.prototype.debug = function(message) {
        this.javaBridge.logDebug(this.formatMessage(message));
        }

        function JSEditor(javaBridge) {
        this.logger = new Logger("JSEditor", javaBridge);
        this.logger.debug("Creating JSEditor");
        this.javaBridge = javaBridge;
        this.isReady = true;
        }

        JSEditor.prototype.show = function() {
        bridge.show();
        }


        JSEditor.prototype.addExtraLayer = function(path) {
        addExtraLayer(path);
        }


function init(){

L.mapbox.accessToken = 'pk.eyJ1IjoiYXNsYXRlciIsImEiOiItYTNLNkkwIn0.uf1rwYpeylp32z8EVOjXpg';

var map = L.mapbox.map('map', null ).setView([35.887,-78.751 ], 4);
L.control.scale().addTo(map);
var geocoderControl = L.mapbox.geocoderControl('mapbox.places',{ keepOpen: true,autocomplete: true, position: 'topright' });
geocoderControl.addTo(map);

//map.addControl(L.mapbox.geocoderControl('mapbox.places',{ position: 'topright' }));

var layers = {
         Satellite: L.mapbox.tileLayer('aslater.kj2mcaba'),
        Streets: L.mapbox.tileLayer('examples.map-i87786ca'),
        Outdoors: L.mapbox.tileLayer('examples.ik7djhcc')

  };

  layers.Satellite.addTo(map);
  L.control.layers(layers).addTo(map);

document.getElementById("txt1").value = map.getZoom();
var featureGroup = L.featureGroup().addTo(map);
var circle_options = {
      color: '#fff',      // Stroke color
      opacity: 1,         // Stroke opacity
      weight: 10,         // Stroke weight
      fillColor: '#000',  // Fill color
      fillOpacity: 0.6    // Fill opacity
  };

  var polyline_options = { color: '#000' };
  var drawControl = new L.Control.Draw({ edit: {featureGroup: featureGroup }}).addTo(map);

  map.on('draw:created', function(e) {featureGroup.addLayer(e.layer); });

  map.on('draw:created', function showPolygonArea(e) {
                           featureGroup.clearLayers();
                           featureGroup.addLayer(e.layer);
                           e.layer.bindPopup((LGeo.area(e.layer) / 1000000).toFixed(2) + ' km<sup>2</sup>');
                           e.layer.openPopup();
                         });

  map.on('draw:edited', function showPolygonAreaEdited(e) {
                          e.layers.eachLayer(function(layer) {
                            showPolygonArea({ layer: layer });
                          });
                        });

  map.on('zoomend', function() { var zoom = map.getZoom();document.getElementById("txt1").value = zoom; console.log("Level:"+zoom); });
  map.on('click',function onClick(e){t=e.latlng; console.log("latlng:" +t); input.push(t); });
}
function cache() {

console.log("input.length" +input);
var minLevel = document.getElementById("txt1").value;
document.getElementById("txt1").setAttribute('readonly');
var maxLevel = document.getElementById("txt2").value;
bridge.cacheMap(input,minLevel,maxLevel);
}

function offlineMap() {

bridge.offlineMap();
}

  var bridge = null;

        function createJSEditor(javaBridge) {
        bridge = javaBridge;
        return new JSEditor(javaBridge);
        }


