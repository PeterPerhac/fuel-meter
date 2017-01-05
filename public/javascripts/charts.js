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


    c3.generate({
        bindto: '#chart-avgc',
        data: {
            x: 'x',
            xFormat: '%Y/%m/%d',
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
                label: 'l/100km'
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
        data: {
            x: 'x',
            xFormat: '%Y/%m/%d',
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
                label: 'MPG'
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
        data: {
            x: 'x',
            xFormat: '%Y/%m/%d',
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
                label: '£ / liter'
            }
        },
        point: {
            show: false
        }
    });

}