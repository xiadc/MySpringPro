

//图表类
function ChartControl(containerId, map) {
   
    this.map = map;    
    this.dataSet = null;
    
    this.timeData = null; //折线图使用的时间轴数据   
    this.typeData = null; //柱状图使用的类型数据
    
    this.container = document.getElementById(containerId + ""); //图表总容器
    
    this.timeDiv = null; //时间折线图的容器
    this.typeDiv = null; //类型柱状图的容器
    
    this.timeLineChart = null; //时间折线图对象
    this.typeHistogram = null; //类型柱状图对象
    
    //dataZoom控件私有
    this.dataZoomTag = {bg: 0, ed: 0}; 
    
    //缩放范围
    this.typeTag = {type: -1}; // 类型选择
   
    //添加事件
    this.addEvents = function () {
     
        //折线图添加事件
        if (this.timeLineChart) {
            //缩放改变
            this.timeDiv.onmouseup = function (event) {      
                // updateDatePnt(pntPos_yl,this.dataZoomTag.bg,this.dataZoomTag.ed);    
                this.dataSetting.updateShowPnts();

                }
         
            //获取缩放数据
            this.timeLineChart.on('dataZoom', function (params) {
                if (!this.dataSetting.dataSet) return;
             
                var bg = timeData.title[Math.round(24 * params.start / 100.0)].split("/");
                var ed = timeData.title[Math.round(24 * params.end / 100.0)].split("/");
             
                bg[1] = bg[1] ? bg[1] : "0";
                bg[2] = bg[2] ? bg[2] : "0";
             
                bg[1] = bg[1].length > 1 ? bg[1] : "0" + bg[1];
                bg[2] = bg[2].length > 1 ? bg[2] : "0" + bg[2];
              
                ed[1] = ed[1] ? ed[1] : "0";
                ed[2] = ed[2] ? ed[2] : "0";
              
                ed[1] = ed[1].length > 1 ? ed[1] : "0" + ed[1];
                ed[2] = ed[2].length > 1 ? ed[2] : "0" + ed[2];
              
                this.dataSetting.dataZoomTag.bg = parseFloat(bg[0] + bg[1] + bg[2]);
                this.dataSetting.dataZoomTag.ed = parseFloat(ed[0] + ed[1] + ed[2]);
            });
          
            //点击折线图事件  
            this.timeLineChart.on('mouseup', function (params) {
                if (!this.dataSetting.dataSet) return;
                if (params.componentType === 'series') {
                    // 点击到了 markPoint 上
                    //console.log(params.dataIndex);
                    var bg = timeData.title[params.dataIndex].split("/");
                    bg[1] = bg[1] ? bg[1] : "0";
                    bg[2] = bg[2] ? bg[2] : "0";
                  
                    bg[1] = bg[1].length > 1 ? bg[1] : "0" + bg[1];
                    bg[2] = bg[2].length > 1 ? bg[2] : "0" + bg[2];
                   
                    this.dataSetting.dataZoomTag.bg = parseFloat(bg[0] + bg[1] + bg[2]);
                    this.dataSetting.dataZoomTag.ed = 0;

                    //updateDatePnt(pntPos_yl,parseFloat(bg[0]+bg[1]+bg[2]));
                    this.dataSetting.updateShowPnts();
                }
            });
        }
       
        //直方图添加事件
        if (this.typeHistogram) this.typeHistogram.on('click', function (params) {
            if (params.componentType === 'series') {
                
                this.dataSetting.typeTag.type = params.dataIndex;
                // 点击到了 markPoint 上
                //console.log(params.dataIndex);
                //updateTypePnt(pntPos_yl,params.dataIndex);  //----------
                this.dataSetting.updateShowPnts();
            }    
        });
    }
}

//图表初始化-时间折线图-类型柱状图
ChartControl.prototype.initCharts = function (timeDataTag , typeDataTag ) {
    //时间折线图
    if (timeDataTag) {
        if (!this.timeLineChart) {
            //创建容器并修改样式
            this.timeDiv = document.createElement("div");
            //this.timeDiv.style.width="100px";
            //this.timeDiv.style.height="100px";
            this.timeDiv.style.cssText += ";position:absolute;left: 2px;bottom: 2px;margin: auto;width: 50%; height: 25%;z-index:999;background: #00FF00;";
            this.container.appendChild(this.timeDiv);
            this.timeLineChart = echarts.init(this.timeDiv);
            this.timeDiv.dataSetting=this.timeLineChart.dataSetting=this;  //将对象绑定到控件上
        }
        //设置以及样式
        option = {
            x: 0
            , y: 0
            , x2: 0
            , y2: 0
            , backgroundColor: "rgba(255, 255, 255, 0.7)"
            , tooltip: {
                trigger: 'axis'
                , position: function (pt) {
                    return [pt[0], '10%'];
                }
            }
            /*  , title: {
                  left: 'center'
                  , text: '时间折线图'
              }*/
            
            , grid: {
                left: 0
                , top: 0
                , right: 0
                , containLabel: true
            }
            , xAxis: {
                data: []
            }
            , yAxis: {
                type: 'value'
                , boundaryGap: [0, '100%']
            }
            , dataZoom: [
                {
                    type: 'inside'
                    , start: 0
                    , end: 10
            }, {
                    start: 0
                    , end: 10
                    , dataBackground: {
                        areaStyle: {
                            color: 'rgba(255, 0, 255, 0.8)'
                        }
                    }
                    , handleSize: '100%'
                    , handleStyle: {
                        color: '#fff'
                        , shadowBlur: 3
                        , shadowColor: 'rgba(0, 0, 0, 0.8)'
                        , shadowOffsetX: 2
                        , shadowOffsetY: 2
                    }
            }]
            , series: [
                {
                    name: '时间点'
                    , type: 'line'
                    , data: []
        }]
        };
        this.timeLineChart.setOption(option);
    }
    
    //类型柱状图
    if (typeDataTag) {
        if (!this.typeHistogram) {
            this.typeDiv = document.createElement("div");
            //this.typeDiv.style.width="100px";
            //this.typeDiv.style.height="100px";
            this.typeDiv.style.cssText += ";position:absolute;right: 2px;top: 2px;margin: auto;width: 20%; height: 40%;z-index:999;";
            this.container.appendChild(this.typeDiv);
            this.typeHistogram = echarts.init(this.typeDiv);
            this.typeDiv.dataSetting=this.typeHistogram.dataSetting=this;  //将对象绑定到控件上
        }
        option = {
            backgroundColor: "rgba(255, 255, 255, 0.7)"
                /*,
                            title: {
                                left: 'center',
                                text: '类型柱状图'
                                , subtext: '犯罪类型'
                            }*/
                
            , tooltip: {
                trigger: 'axis'
                , axisPointer: {
                    type: 'shadow'
                }
            }
            , grid: {
                left: 0
                , top: 0
                , bottom: 0
                , containLabel: true
            }
            , xAxis: {
                type: 'value'
                , boundaryGap: [0, 0.01]
            }
            , yAxis: {
                type: 'category'
                , data: ["类型1", "类型2"]
            }
            , series: [{
                name: '数量'
                , type: 'bar'
                , data: []
            }]
        };
        this.typeHistogram.setOption(option);
    }
    this.addEvents();
}

//更新两个图表 title data   //------------------------更新后需要调整自身大小------------------------------------
ChartControl.prototype.updateChart = function(dataSet,timeData,typeData)
{
    if(timeData)this.updateTimeChart(dataSet,timeData);
    if(typeData)this.updateTypeChart(dataSet,typeData);
}

//更新时间折线图 title  data
ChartControl.prototype.updateTimeChart = function (dataSet,timeData) {
    if (!this.timeLineChart) return;
    this.dataSet=dataSet;
    this.timeData=timeData;

    
    this.timeLineChart.setOption({
        xAxis: {
            type: 'category'
            , boundaryGap: false
            , data: timeData.title
        }
        , series: [
            {
                name: '数量'
                , type: 'line'
                , smooth: true
                , symbol: 'none'
                , sampling: 'average'
                , itemStyle: {
                    normal: {
                        color: 'rgb(255, 70, 131)'
                    }
                }
                , areaStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0
                            , color: 'rgb(255, 158, 68)'
                    }, {
                            offset: 1
                            , color: 'rgb(255, 70, 131)'
                    }])
                    }
                }
                , data: timeData.data
                }
            , {
                name: '数量'
                , type: 'bar'
                , data: timeData.data
                , itemStyle: {
                    normal: {
                        color: 'rgba(0,0,0,0)'
                    }
                }
            }
            ]
    });
}

//更新类型柱状图
ChartControl.prototype.updateTypeChart = function (dataSet,typeData) {
    if(!this.typeHistogram)return;
    this.dataSet=dataSet;
    this.typeData=typeData;

    
    this.typeHistogram.setOption({yAxis: {
        type: 'category'
        , data: typeData.title
    }, series: [
        {
            name: '数量'
            , type: 'bar'
            ,itemStyle: {
                normal: {
                    color: function(params) {
                        // build a color map as your need.
                        var colorList = [
                          '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                           '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                           '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                        ];
                        return colorList[params.dataIndex]
                    }
                }
            }
            , data: typeData.data
        }
    ]});
}

/*
//处理数据过滤掉不符合条件的点
ChartControl.prototype.filterData = function () {
    
    var tempDataSet1 = [];
    
    //过滤时间数据
    if (this.dataZoomTag.tag) {
        //若只存在开始时间则只显示改时间点的数据
        if (!this.dataZoomTag.ed) {
            for (index in this.dataSet)
                if (this.dataSet[index]["time"] === this.dataZoomTag.bg) tempDataSet1.push(this.dataSet[index]);
        }
        else {
            for (index in this.dataSet)
                if (this.dataZoomTag.bg <= this.dataSet[index]["time"] && this.dataZoomTag.ed >= this.dataSet[index]["time"]) tempDataSet1.push(this.dataSet[index]);
        }
        
        this.dataZoomTag.tag=false;
    }
    else 
        tempDataSet1 = this.dataSet;
    
    var tempDataSet2 = [];
    
    //过滤类型数据
    if (this.typeTag.tag) {
        for (index in tempDataSet1)
            if (tempDataSet1[index]["type"] === this.typeTag.type) tempDataSet2.push(tempDataSet1[index]);
        
        this.typeTag.tag=false;
    }
    else 
        tempDataSet2 = tempDataSet1;
    
    return tempDataSet2;
}
*/
//处理数据过滤掉不符合条件的点
ChartControl.prototype.filterData = function () {
    var tempDataSet1 = [];
   
    //过滤时间数据
    //若只存在开始时间则只显示改时间点的数据
    if (!this.dataZoomTag.ed&&this.dataZoomTag.bg) {
        for (index in this.dataSet)
            if (this.dataSet[index]["time"] === this.dataZoomTag.bg) tempDataSet1.push(this.dataSet[index]);
    }
    else if (this.dataZoomTag.bg&&this.dataZoomTag.ed) {
        for (index in this.dataSet)
            if (this.dataZoomTag.bg <= this.dataSet[index]["time"] && this.dataZoomTag.ed >= this.dataSet[index]["time"]) tempDataSet1.push(this.dataSet[index]);
    }
    else 
        tempDataSet1 = this.dataSet;
    var tempDataSet2 = [];
    
    
    //过滤类型数据
    if (this.typeTag.type >= 0) {
        for (index in tempDataSet1)
            if (tempDataSet1[index]["type"] === this.typeTag.type)
                tempDataSet2.push(tempDataSet1[index]);
    }
    else
        tempDataSet2 = tempDataSet1;
    
    return tempDataSet2;
}

//更新其他对象上显示的点需要实现相应接口函数
ChartControl.prototype.updateShowPnts = function () {
    if (!this.map.addPntLayer) {
        throw "请为地图对象实现 addPntLayer(<dataSet>,<name>) 函数";
        return;
    }
    
    this.map.addPntLayer(this.filterData(),"pntTest");
}


ChartControl.prototype.clearChart=function(){
    if(this.timeLineChart){
        this.timeLineChart.clear();
        this.timeLineChart.dispose();
        this.timeLineChart=null;
    }
    if(this.typeHistogram){
        this.typeHistogram.clear();
        this.typeHistogram.dispose();
        this.typeHistogram=null;
    }
}

























