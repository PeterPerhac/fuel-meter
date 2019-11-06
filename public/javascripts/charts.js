var drawCharts = function (data) {

    function createChartContainer(id) {
        $('<div/>', {id: id}).appendTo('#charts-container');
    }

    function createLineChart(id, columns, label) {
        createChartContainer(id);
        c3.generate({
            bindto: '#' + id,
            size: {
                height: 175
            },
            data: {
                x: 'x',
                xFormat: '%Y-%m-%d',
                columns: columns
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
                    label: label,
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

    function createBarChart(id, column) {
        createChartContainer(id);
        c3.generate({
            bindto: '#' + id,
            size: {
                height: 200
            },
            data: {
                columns: [column],
                type: 'bar'
            },
            axis: {
                x: {
                    type: 'category',
                    tick: {
                        rotate: 75,
                        multiline: false,
                        culling: {
                            max: 10
                        }
                    },
                    categories: months
                },
                y: {
                    tick: {
                        count: 6,
                        format: d3.format(".0f")
                    }
                }
            },
            bar: {
                zerobased: true,
                width: {
                    ratio: 0.5
                }
            }
        });
    }

    if (data.readings.length > 1) {

        var timeline = $.map(data.readings, function (e) {
            return e.date
        });
        timeline.unshift('x');

        var fuelEconomy = $.map(data.readings, function (e) {
            return e.avgC
        });
        fuelEconomy.unshift('l/100km');

        var averageAvgC = $.map(data.readings, function () {
            return data.avgC;
        });
        averageAvgC.unshift('average');

        var fuelEconomyMPG = $.map(data.readings, function (e) {
            return e.mpg
        });
        fuelEconomyMPG.unshift('MPG');

        var averageMPG = $.map(data.readings, function () {
            return data.mpg;
        });
        averageMPG.unshift('average');

        var costOfFuelOverTime = $.map(data.readings, function (e) {
            return e.costOfLitre;
        });
        costOfFuelOverTime.unshift('Fuel price');

        var months = $.map(data.monthlyStats.moneyBurned, function (e) {
            return e.label;
        });

        var milesDrivenPCM = $.map(data.monthlyStats.milesDriven, function (e) {
            return e.value;
        });

        milesDrivenPCM.unshift('Miles driven');
        var moneyBurnedPCM = $.map(data.monthlyStats.moneyBurned, function (e) {
            return e.value;
        });
        moneyBurnedPCM.unshift('Money burned');

        createLineChart('chart-avgc', [timeline, fuelEconomy, averageAvgC], 'l/100km');
        createLineChart('chart-mpg', [timeline, fuelEconomyMPG, averageMPG], 'MPG');
        createLineChart('chart-fuelcost', [timeline, costOfFuelOverTime], '£/liter');

        createBarChart('chart-miles-driven', milesDrivenPCM);
        createBarChart('chart-money-burn', moneyBurnedPCM);

    } // readings.size > 1
}; // draw charts function
