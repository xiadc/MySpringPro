

var osm_default = new ol.layer.Tile({
    source: new ol.source.OSM() 
});
    

map = new ol.Map({
    layers: [osm_default]
    , target: 'div_map'
    , interactions: ol.interaction.defaults({
        shiftDragZoom: false,
        doubleClickZoom: false,
        keyboard:false
    }),
    view: new ol.View({
       center: ol.proj.transform([113.33, 23.155], 'EPSG:4326', 'EPSG:3857')
        , zoom: 13
       /* projection:'EPSG:4326',
        center:[113.33, 23.155]
        //center: ol.proj.transform([113.33, 23.155], 'EPSG:4326', 'EPSG:3857')
        , zoom: 10*/
    })
 });
    



//2d缩放
ol.Map.prototype.doZoom = function (factor) {
        // zoom from the current resolution
        var zoom = ol.animation.zoom({
            resolution: this.getView().getResolution()
            , duration: 100
        });
        this.beforeRender(zoom);
        // setting the resolution to a new value will smoothly zoom in or out
        // depending on the factor
        this.getView().setResolution(this.getView().getResolution() * factor);
    }

  
ol.Map.prototype.tranProj=function(data_p) {
    for(var i=0;i<data_p.length;++i)
    {
        var data_t = data_p[i];      
        //   var xy_pix = this.map.getPixelFromCoordinate([data_t.lng,data_t.lat]);
        var xy_= ol.proj.transform([data_t.lng,data_t.lat], 'EPSG:4326', 'EPSG:3857');
        data_t.lng=xy_[0];
        data_t.lat=xy_[1]; 
    }
}

ol.Map.prototype.getLayerByName=function(name){
    var layers=this.getLayers().getArray();
    for(var index=0; index<layers.length;++index)
        if(layers[index]["name"]==name)
            return layers[index];
}



