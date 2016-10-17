How to modify map list:

maps.json contains list of all available application maps.

		{
			"name":"Главная карта",
			"id":"main",
			"file":"map.png",
			"attrs":{
				"type":"outdoor",
				"entity":"notifications"
			},
			"tiles":[
				{
				"name":"Локатор ОРЛ-А",
				"file":"orl-a-icon.png",
				"x":926, "y":1205,
				"link-id":"orla"
				},
			]
		},


file: map file name, must be in current dir
name: Displayed name
id: used for references to this map from other map's tiles


			"tiles":[
				{
				"name":"Локатор ОРЛ-А",
				"file":"orl-a-icon.png",
				"x":926, "y":1205,
				"link-id":"orla"
				},
			]

tiles are overpainted to map and work as hyperlink to other map

name: Displayed name
file: tile image file
x,y: position to draw tile on map
link-id: Switch to that map on click

