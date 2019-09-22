var drawCharts = function (data) {

    var timeline = $.map(data.readings, function (e) {
        return e.date
    });
    timeline.unshift('x');

    var fuelEconomy = $.map(data.readings, function (e) {
        return e.avgC
    });
    fuelEconomy.unshift('l/100km');
    var averageAvgC = $.map(data.readings, function (e) {
        return data.avgC;
    });
    averageAvgC.unshift('average');
    var sum = 0;
    var count = 1;
    var dataWindow = [];

    c3.generate({
        bindto: '#chart-avgc',
        size: {
            height: 175
        },
        data: {
            x: 'x',
            xFormat: '%Y-%m-%d',
            columns: [
                timeline,
                fuelEconomy,
                averageAvgC
            ]
        },
        axis: {
            x: {
                type: 'timeseries',
                tick: {
                    count: 20,
                    format: '%Y-%m'
                }
            },
            y: {
                label: 'l/100km',
                tick: {
                    count: 5,
                    format: d3.format(".2f")
                }
            }
        },
        point: {
            show: false
        }
    });


    var fuelEconomyMPG = $.map(data.readings, function (e) {
        return e.mpg
    });
    fuelEconomyMPG.unshift('MPG');
    var averageMPG = $.map(data.readings, function (e) {
        return data.mpg;
    });
    averageMPG.unshift('average');


    c3.generate({
        bindto: '#chart-mpg',
        size: {
            height: 175
        },
        data: {
            x: 'x',
            xFormat: '%Y-%m-%d',
            columns: [
                timeline,
                fuelEconomyMPG,
                averageMPG
            ]
        },
        axis: {
            x: {
                type: 'timeseries',
                tick: {
                    count: 20,
                    format: '%Y-%m'
                }
            },
            y: {
                label: 'MPG',
                tick: {
                    count: 5,
                    format: d3.format(".1f")
                }
            }
        },
        point: {
            show: false
        }
    });


    var costOfFuelOverTime = $.map(data.readings, function (e) {
        return e.costOfLitre
    });
    costOfFuelOverTime.unshift('Fuel price');

    c3.generate({
        bindto: '#chart-fuelcost',
        size: {
            height: 175
        },
        data: {
            x: 'x',
            xFormat: '%Y-%m-%d',
            columns: [
                timeline,
                costOfFuelOverTime
            ]
        },
        axis: {
            x: {
                type: 'timeseries',
                tick: {
                    count: 20,
                    format: '%Y-%m'
                }
            },
            y: {
                label: '£ / liter',
                tick: {
                    count: 5,
                    format: d3.format(".2f")
                }
            }
        },
        point: {
            show: false
        }
    });

}
