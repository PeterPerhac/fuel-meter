var drawCharts = function (data) {

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
        var averageMPG = $.map(data.readings, function () {
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
            return e.costOfLitre;
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


        var mileageOverTime = $.map(data.readings, function (e) {
            return e.total;
        });
        mileageOverTime.unshift('Mileage');

        c3.generate({
            bindto: '#chart-mileage',
            size: {
                height: 175
            },
            data: {
                x: 'x',
                xFormat: '%Y-%m-%d',
                columns: [
                    timeline,
                    mileageOverTime
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
                    label: 'mileage',
                    tick: {
                        count: 5,
                        format: d3.format(".0f")
                    }
                }
            },
            point: {
                show: false
            }
        });


        var months = $.map(data.monthlyStats.moneyBurned, function (e) {
            return e.label;
        });

        var milesDrivenPCM = $.map(data.monthlyStats.milesDriven, function (e) {
            return e.value;
        });
        milesDrivenPCM.unshift('Miles driven');

        c3.generate({
            bindto: '#chart-miles-driven',
            size: {
                height: 200
            },
            data: {
                columns: [
                    milesDrivenPCM
                ],
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
                    tick:{
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

        var moneyBurnedPCM = $.map(data.monthlyStats.moneyBurned, function (e) {
            return e.value;
        });
        moneyBurnedPCM.unshift('Money burned');

        c3.generate({
            bindto: '#chart-money-burn',
            size: {
                height: 200
            },
            data: {
                columns: [
                    moneyBurnedPCM
                ],
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
                    tick:{
                        count: 6,
                        format: d3.format(".2f")
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

        var fuelBurnedPCM = $.map(data.monthlyStats.fuelBurned, function (e) {
            return e.value;
        });
        fuelBurnedPCM.unshift('Fuel burned');

        c3.generate({
            bindto: '#chart-fuel-burn',
            size: {
                height: 200
            },
            data: {
                columns: [
                    fuelBurnedPCM
                ],
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
                    tick:{
                        count: 6,
                        format: d3.format(".2f")
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
};
