db.getCollection('readings').remove({reg: "NA08MYW"})
db.getCollection('readings').remove({reg: "HY13VLV"})
db.getCollection('vehicles').save([
    {reg: "NA08MYW", make: "Ford", model: "Focus", year: 2008, color: "Silver"},
    {reg: "HY13VLV", make: "Vauxhall", model: "Zafira Tourer", year: 2013, color: "Red"}
])
db.getCollection('readings').save([
    {reg: "NA08MYW", date: "2014/01/04", total:  74923, mi: 447.0, liters: 46.69, cost: 64.39},
    {reg: "NA08MYW", date: "2014/01/05", total:  75030, mi: 107.0, liters: 10.21, cost: 13.98},
    {reg: "NA08MYW", date: "2014/01/25", total:  75288, mi: 140.0, liters: 14.71, cost: 19.99},
    {reg: "NA08MYW", date: "2014/02/01", total:  75431, mi: 292.0, liters: 33.51, cost: 46.21},
    {reg: "NA08MYW", date: "2014/02/09", total:  75802, mi: 371.2, liters: 38.92, cost: 52.50},
    {reg: "NA08MYW", date: "2014/02/26", total:  76199, mi: 396.4, liters: 46.67, cost: 56.00},
    {reg: "NA08MYW", date: "2014/03/16", total:  76591, mi: 392.2, liters: 43.49, cost: 62.15},
    {reg: "NA08MYW", date: "2014/04/14", total:  76795, mi: 202.9, liters: 32.71, cost: 44.13},
    {reg: "NA08MYW", date: "2014/05/30", total:  77087, mi: 292.2, liters: 35.63, cost: 48.42},
    {reg: "NA08MYW", date: "2014/06/17", total:  77495, mi: 407.2, liters: 36.92, cost: 49.44},
    {reg: "NA08MYW", date: "2014/07/11", total:  77776, mi: 281.6, liters: 29.05, cost: 39.19},
    {reg: "NA08MYW", date: "2014/07/17", total:  78052, mi: 275.4, liters: 26.81, cost: 35.63},
    {reg: "NA08MYW", date: "2014/07/18", total:  78378, mi: 326.1, liters: 29.62, cost: 40.25},
    {reg: "NA08MYW", date: "2014/07/22", total:  78741, mi: 362.8, liters: 36.65, cost: 48.71},
    {reg: "NA08MYW", date: "2014/08/01", total:  78956, mi: 215.4, liters: 20.97, cost: 27.66},
    {reg: "NA08MYW", date: "2014/08/16", total:  79304, mi: 348.4, liters: 33.15, cost: 43.39},
    {reg: "NA08MYW", date: "2014/08/30", total:  79581, mi: 276.4, liters: 28.54, cost: 37.07},
    {reg: "NA08MYW", date: "2014/09/30", total:  79998, mi: 416.6, liters: 42.57, cost: 55.72},
    {reg: "NA08MYW", date: "2014/10/29", total:  80356, mi: 358.4, liters: 35.75, cost: 45.72},
    {reg: "NA08MYW", date: "2014/11/23", total:  80698, mi: 342.0, liters: 38.50, cost: 48.09},
    {reg: "NA08MYW", date: "2014/12/07", total:  80984, mi: 286.6, liters: 29.40, cost: 36.13},
    {reg: "NA08MYW", date: "2014/12/16", total:  81171, mi: 186.5, liters: 18.04, cost: 22.89},
    {reg: "NA08MYW", date: "2015/01/14", total:  81378, mi: 206.8, liters: 19.97, cost: 24.14},
    {reg: "NA08MYW", date: "2015/02/01", total:  81625, mi: 247.2, liters: 25.81, cost: 30.95},
    {reg: "NA08MYW", date: "2015/02/14", total:  81901, mi: 276.5, liters: 28.94, cost: 32.38},
    {reg: "NA08MYW", date: "2015/03/02", total:  82146, mi: 245.6, liters: 26.13, cost: 30.28},
    {reg: "NA08MYW", date: "2015/04/02", total:  82349, mi: 202.8, liters: 23.66, cost: 28.13},
    {reg: "NA08MYW", date: "2015/04/10", total:  82796, mi: 446.9, liters: 46.14, cost: 54.40},
    {reg: "NA08MYW", date: "2015/04/23", total:  83160, mi: 363.8, liters: 37.87, cost: 45.03},
    {reg: "NA08MYW", date: "2015/05/05", total:  83303, mi: 142.6, liters: 16.36, cost: 19.45},
    {reg: "NA08MYW", date: "2015/05/19", total:  83670, mi: 367.1, liters: 39.77, cost: 47.68},
    {reg: "NA08MYW", date: "2015/05/30", total:  83850, mi: 178.8, liters: 17.81, cost: 21.35},
    {reg: "NA08MYW", date: "2015/06/15", total:  84152, mi: 301.5, liters: 30.54, cost: 36.62},
    {reg: "NA08MYW", date: "2015/07/11", total:  84481, mi: 329.4, liters: 33.86, cost: 39.58},
    {reg: "NA08MYW", date: "2015/07/28", total:  84842, mi: 361.2, liters: 38.33, cost: 44.04},
    {reg: "NA08MYW", date: "2015/08/07", total:  85089, mi: 246.8, liters: 26.01, cost: 29.63},
    {reg: "NA08MYW", date: "2015/08/11", total:  85273, mi: 183.5, liters: 17.94, cost: 20.07},
    {reg: "NA08MYW", date: "2015/08/14", total:  85563, mi: 290.0, liters: 28.56, cost: 31.96},
    {reg: "NA08MYW", date: "2015/08/16", total:  85970, mi: 407.4, liters: 39.52, cost: 44.22},
    {reg: "NA08MYW", date: "2015/08/18", total:  86224, mi: 253.5, liters: 22.79, cost: 25.27},
    {reg: "NA08MYW", date: "2015/09/06", total:  86463, mi: 239.2, liters: 23.70, cost: 25.81},
    {reg: "NA08MYW", date: "2015/09/10", total:  86649, mi: 185.9, liters: 20.69, cost: 22.74},
    {reg: "NA08MYW", date: "2015/09/19", total:  86833, mi: 183.4, liters: 18.54, cost: 20.38},
    {reg: "NA08MYW", date: "2015/10/01", total:  87049, mi: 215.9, liters: 23.30, cost: 25.37},
    {reg: "NA08MYW", date: "2015/10/18", total:  87396, mi: 347.6, liters: 37.56, cost: 40.90},
    {reg: "NA08MYW", date: "2015/10/31", total:  87696, mi: 299.6, liters: 30.58, cost: 33.30},
    {reg: "NA08MYW", date: "2015/11/08", total:  88052, mi: 356.3, liters: 39.88, cost: 44.63},
    {reg: "NA08MYW", date: "2015/11/19", total:  88257, mi: 204.9, liters: 20.88, cost: 22.74},
    {reg: "NA08MYW", date: "2015/12/09", total:  88562, mi: 304.9, liters: 33.33, cost: 36.30},
    {reg: "NA08MYW", date: "2015/12/23", total:  88743, mi: 171.3, liters: 20.52, cost: 21.73},
    {reg: "NA08MYW", date: "2015/12/31", total:  88983, mi: 249.2, liters: 24.86, cost: 26.08},
    {reg: "NA08MYW", date: "2016/01/08", total:  89323, mi: 340.6, liters: 37.03, cost: 38.47},
    {reg: "NA08MYW", date: "2016/01/23", total:  89550, mi: 226.3, liters: 25.69, cost: 25.92},
    {reg: "NA08MYW", date: "2016/01/27", total:  89738, mi: 187.9, liters: 19.60, cost: 19.58},
    {reg: "NA08MYW", date: "2016/02/10", total:  89928, mi: 189.9, liters: 20.33, cost: 20.72},
    {reg: "NA08MYW", date: "2016/02/15", total:  90153, mi: 225.5, liters: 24.95, cost: 25.43},
    {reg: "NA08MYW", date: "2016/03/02", total:  90463, mi: 309.3, liters: 32.72, cost: 33.34},
    {reg: "NA08MYW", date: "2016/03/16", total:  90742, mi: 279.1, liters: 31.72, cost: 33.28},
    {reg: "NA08MYW", date: "2016/04/03", total:  91011, mi: 269.6, liters: 28.33, cost: 30.85},
    {reg: "NA08MYW", date: "2016/04/05", total:  91403, mi: 391.7, liters: 37.88, cost: 39.74},
    {reg: "NA08MYW", date: "2016/04/11", total:  91618, mi: 214.5, liters: 23.36, cost: 24.50},
    {reg: "NA08MYW", date: "2016/04/22", total:  91800, mi: 182.3, liters: 19.36, cost: 20.89},
    {reg: "NA08MYW", date: "2016/05/03", total:  92105, mi: 304.9, liters: 32.20, cost: 35.39},
    {reg: "NA08MYW", date: "2016/05/14", total:  92356, mi: 251.1, liters: 24.95, cost: 27.42},
    {reg: "NA08MYW", date: "2016/05/31", total:  92590, mi: 233.8, liters: 25.21, cost: 28.21},
    {reg: "NA08MYW", date: "2016/06/13", total:  92870, mi: 279.6, liters: 29.19, cost: 32.96},
    {reg: "NA08MYW", date: "2016/06/29", total:  93269, mi: 398.7, liters: 41.48, cost: 47.25},
    {reg: "NA08MYW", date: "2016/07/12", total:  93523, mi: 254.1, liters: 25.75, cost: 29.33},
    {reg: "NA08MYW", date: "2016/07/24", total:  93650, mi: 126.9, liters: 13.92, cost: 15.58},
    {reg: "NA08MYW", date: "2016/07/25", total:  93933, mi: 283.5, liters: 29.73, cost: 34.16},
    {reg: "NA08MYW", date: "2016/07/26", total:  94157, mi: 224.3, liters: 22.93, cost: 26.35},
    {reg: "NA08MYW", date: "2016/07/27", total:  94277, mi: 119.9, liters: 13.79, cost: 15.16},
    {reg: "NA08MYW", date: "2016/07/28", total:  94445, mi: 167.8, liters: 16.76, cost: 19.09},
    {reg: "NA08MYW", date: "2016/08/07", total:  94763, mi: 317.7, liters: 33.93, cost: 37.97},
    {reg: "NA08MYW", date: "2016/08/13", total:  94993, mi: 230.1, liters: 25.12, cost: 28.11},
    {reg: "NA08MYW", date: "2016/08/28", total:  95360, mi: 366.6, liters: 37.48, cost: 42.31},
    {reg: "NA08MYW", date: "2016/09/02", total:  95701, mi: 341.1, liters: 34.02, cost: 38.41},
    {reg: "NA08MYW", date: "2016/09/12", total:  96064, mi: 362.7, liters: 38.69, cost: 43.68},
    {reg: "NA08MYW", date: "2016/09/24", total:  96380, mi: 316.1, liters: 33.50, cost: 38.49},
    {reg: "NA08MYW", date: "2016/10/03", total:  96664, mi: 283.7, liters: 27.61, cost: 30.90},
    {reg: "NA08MYW", date: "2016/10/14", total:  96975, mi: 311.8, liters: 34.74, cost: 39.57},
    {reg: "NA08MYW", date: "2016/10/24", total:  97334, mi: 358.5, liters: 38.14, cost: 43.82},
    {reg: "NA08MYW", date: "2016/10/31", total:  97600, mi: 266.5, liters: 29.00, cost: 33.61},
    {reg: "NA08MYW", date: "2016/11/07", total:  97809, mi: 208.2, liters: 22.43, cost: 26.22},
    {reg: "NA08MYW", date: "2016/11/14", total:  98062, mi: 253.8, liters: 28.90, cost: 34.07},
    {reg: "NA08MYW", date: "2016/11/21", total:  98255, mi: 192.9, liters: 20.75, cost: 23.84},
    {reg: "NA08MYW", date: "2016/11/30", total:  98479, mi: 223.8, liters: 25.24, cost: 29.00},
    {reg: "NA08MYW", date: "2016/12/16", total:  98815, mi: 335.0, liters: 38.00, cost: 45.18},
    {reg: "NA08MYW", date: "2017/01/02", total:  99220, mi: 404.5, liters: 43.47, cost: 52.12},
    {reg: "NA08MYW", date: "2017/01/08", total:  99362, mi: 142.2, liters: 16.15, cost: 19.53},
    {reg: "NA08MYW", date: "2017/01/14", total:  99812, mi: 449.8, liters: 44.30, cost: 57.99},
    {reg: "NA08MYW", date: "2017/02/02", total: 100222, mi: 409.8, liters: 45.62, cost: 60.17},
    {reg: "NA08MYW", date: "2017/02/24", total: 100648, mi: 425.6, liters: 45.22, cost: 60.10},
    {reg: "NA08MYW", date: "2017/03/15", total: 101022, mi: 374.7, liters: 43.22, cost: 57.01},
    {reg: "NA08MYW", date: "2017/03/28", total: 101403, mi: 380.5, liters: 42.42, cost: 60.62},
    {reg: "NA08MYW", date: "2017/04/01", total: 101765, mi: 362.1, liters: 35.88, cost: 46.25},
    {reg: "NA08MYW", date: "2017/04/13", total: 102124, mi: 358.7, liters: 39.81, cost: 51.71},
    {reg: "NA08MYW", date: "2017/04/17", total: 102476, mi: 352.3, liters: 36.95, cost: 44.67},
    {reg: "NA08MYW", date: "2017/04/27", total: 102890, mi: 413.3, liters: 38.96, cost: 51.00},
    {reg: "NA08MYW", date: "2017/05/15", total: 103364, mi: 474.4, liters: 44.62, cost: 57.52},
    {reg: "NA08MYW", date: "2017/05/26", total: 103741, mi: 376.6, liters: 37.95, cost: 48.92},
    {reg: "NA08MYW", date: "2017/06/13", total: 104173, mi: 432.7, liters: 43.39, cost: 55.06},
    {reg: "NA08MYW", date: "2017/06/24", total: 104569, mi: 395.1, liters: 42.76, cost: 53.83},
    {reg: "NA08MYW", date: "2017/07/08", total: 104987, mi: 417.9, liters: 43.28, cost: 54.06},
    {reg: "NA08MYW", date: "2017/07/18", total: 105430, mi: 442.8, liters: 46.49, cost: 58.07},
    {reg: "NA08MYW", date: "2017/07/30", total: 105850, mi: 420.8, liters: 43.29, cost: 50.61},
    {reg: "NA08MYW", date: "2017/08/09", total: 106336, mi: 485.2, liters: 47.72, cost: 61.03},
    {reg: "NA08MYW", date: "2017/08/14", total: 106436, mi: 100.0, liters: 10.59, cost: 13.54},
    {reg: "NA08MYW", date: "2017/08/15", total: 106536, mi: 100.2, liters: 9.32, cost: 12.20},
    {reg: "NA08MYW", date: "2017/08/31", total: 106970, mi: 433.5, liters: 42.18, cost: 54.37},
    {reg: "NA08MYW", date: "2017/09/17", total: 107193, mi: 223.2, liters: 27.74, cost: 36.59},
    {reg: "NA08MYW", date: "2017/09/17", total: 107568, mi: 375.0, liters: 38.22, cost: 51.18},
    {reg: "NA08MYW", date: "2017/09/20", total: 107844, mi: 275.5, liters: 26.69, cost: 38.94},
    {reg: "NA08MYW", date: "2017/10/12", total: 108218, mi: 373.4, liters: 43.76, cost: 56.84},
    {reg: "NA08MYW", date: "2017/10/22", total: 108493, mi: 275.2, liters: 29.94, cost: 39.49},
    {reg: "NA08MYW", date: "2017/10/31", total: 108844, mi: 351.0, liters: 35.20, cost: 46.43},
    {reg: "NA08MYW", date: "2017/11/13", total: 109145, mi: 301.1, liters: 33.05, cost: 44.25},
    {reg: "NA08MYW", date: "2017/12/23", total: 109406, mi: 260.4, liters: 33.55, cost: 44.24},
    {reg: "NA08MYW", date: "2018/01/26", total: 109784, mi: 378.3, liters: 45.48, cost: 61.81},
    {reg: "NA08MYW", date: "2018/02/02", total: 109972, mi: 187.5, liters: 20.22, cost: 27.48},
    {reg: "NA08MYW", date: "2018/02/09", total: 110105, mi: 133.0, liters: 14.24, cost: 19.78},
    {reg: "NA08MYW", date: "2018/03/24", total: 110471, mi: 366.1, liters: 41.64, cost: 54.92},
    {reg: "NA08MYW", date: "2018/03/31", total: 110814, mi: 342.8, liters: 35.60, cost: 49.09},
    {reg: "NA08MYW", date: "2018/04/01", total: 110927, mi: 112.5, liters: 12.83, cost: 17.56},
    {reg: "NA08MYW", date: "2018/04/02", total: 111094, mi: 167.6, liters: 18.23, cost: 24.59},
    {reg: "NA08MYW", date: "2018/04/06", total: 111518, mi: 424.1, liters: 35.64, cost: 47.37},
    {reg: "NA08MYW", date: "2018/04/14", total: 111914, mi: 395.3, liters: 35.64, cost: 47.72},
    {reg: "NA08MYW", date: "2018/04/25", total: 112328, mi: 414.0, liters: 41.47, cost: 56.36},
    {reg: "NA08MYW", date: "2018/05/06", total: 112700, mi: 372.3, liters: 36.87, cost: 50.48},
    {reg: "NA08MYW", date: "2018/05/18", total: 113116, mi: 415.7, liters: 42.56, cost: 59.97},
    {reg: "NA08MYW", date: "2018/06/07", total: 113561, mi: 444.9, liters: 46.69, cost: 66.72},
    {reg: "NA08MYW", date: "2018/06/22", total: 113979, mi: 417.8, liters: 42.92, cost: 61.33},
    {reg: "NA08MYW", date: "2018/07/09", total: 114378, mi: 398.7, liters: 46.66, cost: 66.68},
    {reg: "NA08MYW", date: "2018/07/20", total: 114657, mi: 279.3, liters: 29.13, cost: 41.92},
    {reg: "NA08MYW", date: "2018/07/21", total: 114777, mi: 119.9, liters: 10.86, cost: 15.63},
    {reg: "NA08MYW", date: "2018/08/01", total: 115270, mi: 493.4, liters: 47.43, cost: 68.25},
    {reg: "NA08MYW", date: "2018/08/11", total: 115535, mi: 264.4, liters: 28.15, cost: 40.51},
    {reg: "NA08MYW", date: "2018/08/24", total: 115855, mi: 320.4, liters: 28.14, cost: 41.06},
    {reg: "NA08MYW", date: "2018/08/26", total: 116181, mi: 325.9, liters: 30.42, cost: 44.99},
    {reg: "NA08MYW", date: "2018/08/30", total: 116590, mi: 408.7, liters: 37.62, cost: 54.89},
    {reg: "NA08MYW", date: "2018/08/31", total: 116694, mi: 103.8, liters: 7.73, cost: 11.12},
    {reg: "NA08MYW", date: "2018/09/12", total: 117141, mi: 447.4, liters: 44.01, cost: 63.33},
    {reg: "NA08MYW", date: "2018/09/29", total: 117572, mi: 430.7, liters: 45.62, cost: 66.10},
    {reg: "NA08MYW", date: "2018/10/09", total: 117942, mi: 370.1, liters: 40.50, cost: 59.49},
    {reg: "NA08MYW", date: "2018/10/16", total: 118281, mi: 338.7, liters: 35.11, cost: 51.58},
    {reg: "NA08MYW", date: "2018/10/27", total: 118657, mi: 375.4, liters: 37.83, cost: 55.95},
    {reg: "NA08MYW", date: "2018/11/02", total: 119050, mi: 393.1, liters: 35.69, cost: 52.79},
    {reg: "NA08MYW", date: "2018/11/10", total: 119514, mi: 463.8, liters: 43.86, cost: 64.87},
    {reg: "NA08MYW", date: "2018/11/17", total: 119816, mi: 302.4, liters: 30.22, cost: 44.70},
    {reg: "NA08MYW", date: "2018/11/30", total: 120183, mi: 366.6, liters: 41.67, cost: 60.88},
    {reg: "NA08MYW", date: "2018/12/14", total: 120590, mi: 407.2, liters: 44.46, cost: 63.09},
    {reg: "NA08MYW", date: "2019/01/03", total: 120994, mi: 403.4, liters: 43.43, cost: 61.19},
    {reg: "NA08MYW", date: "2019/01/17", total: 121367, mi: 373.0, liters: 41.13, cost: 57.54},
    {reg: "NA08MYW", date: "2019/02/01", total: 121781, mi: 413.9, liters: 45.69, cost: 63.92},
    {reg: "NA08MYW", date: "2019/02/12", total: 122080, mi: 298.7, liters: 32.54, cost: 45.52},
    {reg: "NA08MYW", date: "2019/02/20", total: 122453, mi: 373.1, liters: 36.92, cost: 52.02},
    {reg: "NA08MYW", date: "2019/03/05", total: 122767, mi: 314.5, liters: 32.98, cost: 47.13},
    {reg: "NA08MYW", date: "2019/03/20", total: 123167, mi: 399.8, liters: 45.34, cost: 65.70},
    {reg: "NA08MYW", date: "2019/04/03", total: 123554, mi: 386.7, liters: 40.24, cost: 58.71},
    {reg: "NA08MYW", date: "2019/04/17", total: 123947, mi: 392.7, liters: 40.70, cost: 60.20},
    {reg: "NA08MYW", date: "2019/04/20", total: 124289, mi: 342.5, liters: 33.96, cost: 51.25},
    {reg: "NA08MYW", date: "2019/04/25", total: 124714, mi: 424.7, liters: 41.57, cost: 59.82},
    {reg: "NA08MYW", date: "2019/05/02", total: 124959, mi: 245.1, liters: 22.60, cost: 33.65},
    {reg: "NA08MYW", date: "2019/05/06", total: 125327, mi: 367.9, liters: 34.51, cost: 50.00},
    {reg: "NA08MYW", date: "2019/05/08", total: 125656, mi: 329.2, liters: 29.61, cost: 44.09},
    {reg: "NA08MYW", date: "2019/05/25", total: 125948, mi: 291.8, liters: 34.11, cost: 49.77},
    {reg: "NA08MYW", date: "2019/06/11", total: 126352, mi: 403.5, liters: 41.36, cost: 56.21},
    {reg: "HY13VLV", date: "2019/06/12", total: 41374, mi: 360.0, liters: 43.88, cost: 62.70},
    {reg: "HY13VLV", date: "2019/06/21", total: 41640, mi: 265.6, liters: 33.81, cost: 50.00},
    {reg: "HY13VLV", date: "2019/07/03", total: 41750, mi: 110.1, liters: 18.00, cost: 25.90},
    {reg: "HY13VLV", date: "2019/07/10", total: 41896, mi: 146.4, liters: 20.53, cost: 29.34},
    {reg: "HY13VLV", date: "2019/07/16", total: 42063, mi: 166.6, liters: 21.41, cost: 31.02},
    {reg: "HY13VLV", date: "2019/07/22", total: 42290, mi: 227.1, liters: 29.05, cost: 38.32},
    {reg: "HY13VLV", date: "2019/07/26", total: 42478, mi: 187.8, liters: 24.74, cost: 32.63},
    {reg: "HY13VLV", date: "2019/08/01", total: 42643, mi: 164.8, liters: 20.88, cost: 27.54},
    {reg: "HY13VLV", date: "2019/08/06", total: 42739, mi: 96.1, liters: 13.60, cost: 18.07},
    {reg: "HY13VLV", date: "2019/08/12", total: 42865, mi: 125.6, liters: 17.31, cost: 23.00},
    {reg: "HY13VLV", date: "2019/08/14", total: 43091, mi: 226.8, liters: 22.14, cost: 29.42},
    {reg: "HY13VLV", date: "2019/08/19", total: 43406, mi: 314.2, liters: 33.24, cost: 43.84},
    {reg: "HY13VLV", date: "2019/08/23", total: 43500, mi: 94.3, liters: 11.93, cost: 15.85},
    {reg: "HY13VLV", date: "2019/08/25", total: 43814, mi: 314.3, liters: 35.99, cost: 48.55},
    {reg: "HY13VLV", date: "2019/08/27", total: 44016, mi: 202.2, liters: 18.26, cost: 24.27},
    {reg: "HY13VLV", date: "2019/08/30", total: 44123, mi: 106.8, liters: 13.53, cost: 17.98},
    {reg: "HY13VLV", date: "2019/08/31", total: 44475, mi: 351.1, liters: 33.26, cost: 44.53},
    {reg: "HY13VLV", date: "2019/09/03", total: 44618, mi: 143.5, liters: 15.61, cost: 20.59},
    {reg: "HY13VLV", date: "2019/09/04", total: 44975, mi: 356.4, liters: 37.75, cost: 49.79},
    {reg: "HY13VLV", date: "2019/09/09", total: 45118, mi: 143.4, liters: 17.90, cost: 23.79},
    {reg: "HY13VLV", date: "2019/09/16", total: 45324, mi: 205.8, liters: 26.84, cost: 35.40}
])