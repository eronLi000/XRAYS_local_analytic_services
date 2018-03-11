var jsonData = {heatmap};

console.log(jsonData);
var xscale = d3.scale.linear()
               .domain([0,50.0])
               .range([0,720]),
    yscale = d3.scale.linear()
               .domain([0,33.79])
               .range([0,487]),
    map = d3.floorplan().xScale(xscale).yScale(yscale),
    imagelayer = d3.floorplan.imagelayer(),
    heatmap = d3.floorplan.heatmap(),
    mapdata = {};

mapdata[imagelayer.id()] = [{
    url: image_path,
    x: 0,
    y: 0,
    height: 33.79,
    width: 50.0
     }];

map.addLayer(imagelayer)
   .addLayer(heatmap);

var loadData = function(data) {
	mapdata[heatmap.id()] = data.heatmap;
	
	d3.select("#heatmap").append("svg")
		.attr("height", 487).attr("width",720)
		.datum(mapdata).call(map);
};

loadData(jsonData);