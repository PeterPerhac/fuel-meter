DELETE FROM vehicle_owner WHERE reg in ('NA08MYW', 'HY13VLV', 'D3VPR0');
DELETE FROM vehicle WHERE reg IN ('NA08MYW', 'HY13VLV', 'D3VPR0');
DELETE FROM reading WHERE reg in ('NA08MYW', 'HY13VLV', 'D3VPR0');

INSERT INTO vehicle(reg, make, model, year, color)
VALUES ('NA08MYW', 'Ford', 'Focus', 2008, 'Silver'),
       ('HY13VLV', 'Vauxhall', 'Zafira Tourer', 2013, 'Red'),
       ('D3VPR0', 'Mitsubishi', 'Pajero', 2017, 'Silver');

INSERT INTO reading(refuel_date, reg, miles, mileage, liters, cost)
VALUES ('2014/01/04', 'NA08MYW', 447.0, 74923, 46.69, 64.39),
       ('2014/01/05', 'NA08MYW', 107.0, 75030, 10.21, 13.98),
       ('2014/01/25', 'NA08MYW', 140.0, 75288, 14.71, 19.99),
       ('2014/02/01', 'NA08MYW', 292.0, 75431, 33.51, 46.21),
       ('2014/02/09', 'NA08MYW', 371.2, 75802, 38.92, 52.50),
       ('2014/02/26', 'NA08MYW', 396.4, 76199, 46.67, 56.00),
       ('2014/03/16', 'NA08MYW', 392.2, 76591, 43.49, 62.15),
       ('2014/04/14', 'NA08MYW', 202.9, 76795, 32.71, 44.13),
       ('2014/05/30', 'NA08MYW', 292.2, 77087, 35.63, 48.42),
       ('2014/06/17', 'NA08MYW', 407.2, 77495, 36.92, 49.44),
       ('2014/07/11', 'NA08MYW', 281.6, 77776, 29.05, 39.19),
       ('2014/07/17', 'NA08MYW', 275.4, 78052, 26.81, 35.63),
       ('2014/07/18', 'NA08MYW', 326.1, 78378, 29.62, 40.25),
       ('2014/07/22', 'NA08MYW', 362.8, 78741, 36.65, 48.71),
       ('2014/08/01', 'NA08MYW', 215.4, 78956, 20.97, 27.66),
       ('2014/08/16', 'NA08MYW', 348.4, 79304, 33.15, 43.39),
       ('2014/08/30', 'NA08MYW', 276.4, 79581, 28.54, 37.07),
       ('2014/09/30', 'NA08MYW', 416.6, 79998, 42.57, 55.72),
       ('2014/10/29', 'NA08MYW', 358.4, 80356, 35.75, 45.72),
       ('2014/11/23', 'NA08MYW', 342.0, 80698, 38.50, 48.09),
       ('2014/12/07', 'NA08MYW', 286.6, 80984, 29.40, 36.13),
       ('2014/12/16', 'NA08MYW', 186.5, 81171, 18.04, 22.89),
       ('2015/01/14', 'NA08MYW', 206.8, 81378, 19.97, 24.14),
       ('2015/02/01', 'NA08MYW', 247.2, 81625, 25.81, 30.95),
       ('2015/02/14', 'NA08MYW', 276.5, 81901, 28.94, 32.38),
       ('2015/03/02', 'NA08MYW', 245.6, 82146, 26.13, 30.28),
       ('2015/04/02', 'NA08MYW', 202.8, 82349, 23.66, 28.13),
       ('2015/04/10', 'NA08MYW', 446.9, 82796, 46.14, 54.40),
       ('2015/04/23', 'NA08MYW', 363.8, 83160, 37.87, 45.03),
       ('2015/05/05', 'NA08MYW', 142.6, 83303, 16.36, 19.45),
       ('2015/05/19', 'NA08MYW', 367.1, 83670, 39.77, 47.68),
       ('2015/05/30', 'NA08MYW', 178.8, 83850, 17.81, 21.35),
       ('2015/06/15', 'NA08MYW', 301.5, 84152, 30.54, 36.62),
       ('2015/07/11', 'NA08MYW', 329.4, 84481, 33.86, 39.58),
       ('2015/07/28', 'NA08MYW', 361.2, 84842, 38.33, 44.04),
       ('2015/08/07', 'NA08MYW', 246.8, 85089, 26.01, 29.63),
       ('2015/08/11', 'NA08MYW', 183.5, 85273, 17.94, 20.07),
       ('2015/08/14', 'NA08MYW', 290.0, 85563, 28.56, 31.96),
       ('2015/08/16', 'NA08MYW', 407.4, 85970, 39.52, 44.22),
       ('2015/08/18', 'NA08MYW', 253.5, 86224, 22.79, 25.27),
       ('2015/09/06', 'NA08MYW', 239.2, 86463, 23.70, 25.81),
       ('2015/09/10', 'NA08MYW', 185.9, 86649, 20.69, 22.74),
       ('2015/09/19', 'NA08MYW', 183.4, 86833, 18.54, 20.38),
       ('2015/10/01', 'NA08MYW', 215.9, 87049, 23.30, 25.37),
       ('2015/10/18', 'NA08MYW', 347.6, 87396, 37.56, 40.90),
       ('2015/10/31', 'NA08MYW', 299.6, 87696, 30.58, 33.30),
       ('2015/11/08', 'NA08MYW', 356.3, 88052, 39.88, 44.63),
       ('2015/11/19', 'NA08MYW', 204.9, 88257, 20.88, 22.74),
       ('2015/12/09', 'NA08MYW', 304.9, 88562, 33.33, 36.30),
       ('2015/12/23', 'NA08MYW', 171.3, 88743, 20.52, 21.73),
       ('2015/12/31', 'NA08MYW', 249.2, 88983, 24.86, 26.08),
       ('2016/01/08', 'NA08MYW', 340.6, 89323, 37.03, 38.47),
       ('2016/01/23', 'NA08MYW', 226.3, 89550, 25.69, 25.92),
       ('2016/01/27', 'NA08MYW', 187.9, 89738, 19.60, 19.58),
       ('2016/02/10', 'NA08MYW', 189.9, 89928, 20.33, 20.72),
       ('2016/02/15', 'NA08MYW', 225.5, 90153, 24.95, 25.43),
       ('2016/03/02', 'NA08MYW', 309.3, 90463, 32.72, 33.34),
       ('2016/03/16', 'NA08MYW', 279.1, 90742, 31.72, 33.28),
       ('2016/04/03', 'NA08MYW', 269.6, 91011, 28.33, 30.85),
       ('2016/04/05', 'NA08MYW', 391.7, 91403, 37.88, 39.74),
       ('2016/04/11', 'NA08MYW', 214.5, 91618, 23.36, 24.50),
       ('2016/04/22', 'NA08MYW', 182.3, 91800, 19.36, 20.89),
       ('2016/05/03', 'NA08MYW', 304.9, 92105, 32.20, 35.39),
       ('2016/05/14', 'NA08MYW', 251.1, 92356, 24.95, 27.42),
       ('2016/05/31', 'NA08MYW', 233.8, 92590, 25.21, 28.21),
       ('2016/06/13', 'NA08MYW', 279.6, 92870, 29.19, 32.96),
       ('2016/06/29', 'NA08MYW', 398.7, 93269, 41.48, 47.25),
       ('2016/07/12', 'NA08MYW', 254.1, 93523, 25.75, 29.33),
       ('2016/07/24', 'NA08MYW', 126.9, 93650, 13.92, 15.58),
       ('2016/07/25', 'NA08MYW', 283.5, 93933, 29.73, 34.16),
       ('2016/07/26', 'NA08MYW', 224.3, 94157, 22.93, 26.35),
       ('2016/07/27', 'NA08MYW', 119.9, 94277, 13.79, 15.16),
       ('2016/07/28', 'NA08MYW', 167.8, 94445, 16.76, 19.09),
       ('2016/08/07', 'NA08MYW', 317.7, 94763, 33.93, 37.97),
       ('2016/08/13', 'NA08MYW', 230.1, 94993, 25.12, 28.11),
       ('2016/08/28', 'NA08MYW', 366.6, 95360, 37.48, 42.31),
       ('2016/09/02', 'NA08MYW', 341.1, 95701, 34.02, 38.41),
       ('2016/09/12', 'NA08MYW', 362.7, 96064, 38.69, 43.68),
       ('2016/09/24', 'NA08MYW', 316.1, 96380, 33.50, 38.49),
       ('2016/10/03', 'NA08MYW', 283.7, 96664, 27.61, 30.90),
       ('2016/10/14', 'NA08MYW', 311.8, 96975, 34.74, 39.57),
       ('2016/10/24', 'NA08MYW', 358.5, 97334, 38.14, 43.82),
       ('2016/10/31', 'NA08MYW', 266.5, 97600, 29.00, 33.61),
       ('2016/11/07', 'NA08MYW', 208.2, 97809, 22.43, 26.22),
       ('2016/11/14', 'NA08MYW', 253.8, 98062, 28.90, 34.07),
       ('2016/11/21', 'NA08MYW', 192.9, 98255, 20.75, 23.84),
       ('2016/11/30', 'NA08MYW', 223.8, 98479, 25.24, 29.00),
       ('2016/12/16', 'NA08MYW', 335.0, 98815, 38.00, 45.18),
       ('2017/01/02', 'NA08MYW', 404.5, 99220, 43.47, 52.12),
       ('2017/01/08', 'NA08MYW', 142.2, 99362, 16.15, 19.53),
       ('2017/01/14', 'NA08MYW', 449.8, 99812, 44.30, 57.99),
       ('2017/02/02', 'NA08MYW', 409.8, 100222, 45.62, 60.17),
       ('2017/02/24', 'NA08MYW', 425.6, 100648, 45.22, 60.10),
       ('2017/03/15', 'NA08MYW', 374.7, 101022, 43.22, 57.01),
       ('2017/03/28', 'NA08MYW', 380.5, 101403, 42.42, 60.62),
       ('2017/04/01', 'NA08MYW', 362.1, 101765, 35.88, 46.25),
       ('2017/04/13', 'NA08MYW', 358.7, 102124, 39.81, 51.71),
       ('2017/04/17', 'NA08MYW', 352.3, 102476, 36.95, 44.67),
       ('2017/04/27', 'NA08MYW', 413.3, 102890, 38.96, 51.00),
       ('2017/05/15', 'NA08MYW', 474.4, 103364, 44.62, 57.52),
       ('2017/05/26', 'NA08MYW', 376.6, 103741, 37.95, 48.92),
       ('2017/06/13', 'NA08MYW', 432.7, 104173, 43.39, 55.06),
       ('2017/06/24', 'NA08MYW', 395.1, 104569, 42.76, 53.83),
       ('2017/07/08', 'NA08MYW', 417.9, 104987, 43.28, 54.06),
       ('2017/07/18', 'NA08MYW', 442.8, 105430, 46.49, 58.07),
       ('2017/07/30', 'NA08MYW', 420.8, 105850, 43.29, 50.61),
       ('2017/08/09', 'NA08MYW', 485.2, 106336, 47.72, 61.03),
       ('2017/08/14', 'NA08MYW', 100.0, 106436, 10.59, 13.54),
       ('2017/08/15', 'NA08MYW', 100.2, 106536, 09.32, 12.20),
       ('2017/08/31', 'NA08MYW', 433.5, 106970, 42.18, 54.37),
       ('2017/09/17', 'NA08MYW', 598.2, 107568, 65.96, 87.77),
       ('2017/09/20', 'NA08MYW', 275.5, 107844, 26.69, 38.94),
       ('2017/10/12', 'NA08MYW', 373.4, 108218, 43.76, 56.84),
       ('2017/10/22', 'NA08MYW', 275.2, 108493, 29.94, 39.49),
       ('2017/10/31', 'NA08MYW', 351.0, 108844, 35.20, 46.43),
       ('2017/11/13', 'NA08MYW', 301.1, 109145, 33.05, 44.25),
       ('2017/12/23', 'NA08MYW', 260.4, 109406, 33.55, 44.24),
       ('2018/01/26', 'NA08MYW', 378.3, 109784, 45.48, 61.81),
       ('2018/02/02', 'NA08MYW', 187.5, 109972, 20.22, 27.48),
       ('2018/02/09', 'NA08MYW', 133.0, 110105, 14.24, 19.78),
       ('2018/03/24', 'NA08MYW', 366.1, 110471, 41.64, 54.92),
       ('2018/03/31', 'NA08MYW', 342.8, 110814, 35.60, 49.09),
       ('2018/04/01', 'NA08MYW', 112.5, 110927, 12.83, 17.56),
       ('2018/04/02', 'NA08MYW', 167.6, 111094, 18.23, 24.59),
       ('2018/04/06', 'NA08MYW', 424.1, 111518, 35.64, 47.37),
       ('2018/04/14', 'NA08MYW', 395.3, 111914, 35.64, 47.72),
       ('2018/04/25', 'NA08MYW', 414.0, 112328, 41.47, 56.36),
       ('2018/05/06', 'NA08MYW', 372.3, 112700, 36.87, 50.48),
       ('2018/05/18', 'NA08MYW', 415.7, 113116, 42.56, 59.97),
       ('2018/06/07', 'NA08MYW', 444.9, 113561, 46.69, 66.72),
       ('2018/06/22', 'NA08MYW', 417.8, 113979, 42.92, 61.33),
       ('2018/07/09', 'NA08MYW', 398.7, 114378, 46.66, 66.68),
       ('2018/07/20', 'NA08MYW', 279.3, 114657, 29.13, 41.92),
       ('2018/07/21', 'NA08MYW', 119.9, 114777, 10.86, 15.63),
       ('2018/08/01', 'NA08MYW', 493.4, 115270, 47.43, 68.25),
       ('2018/08/11', 'NA08MYW', 264.4, 115535, 28.15, 40.51),
       ('2018/08/24', 'NA08MYW', 320.4, 115855, 28.14, 41.06),
       ('2018/08/26', 'NA08MYW', 325.9, 116181, 30.42, 44.99),
       ('2018/08/30', 'NA08MYW', 408.7, 116590, 37.62, 54.89),
       ('2018/08/31', 'NA08MYW', 103.8, 116694, 7.73, 11.12),
       ('2018/09/12', 'NA08MYW', 447.4, 117141, 44.01, 63.33),
       ('2018/09/29', 'NA08MYW', 430.7, 117572, 45.62, 66.10),
       ('2018/10/09', 'NA08MYW', 370.1, 117942, 40.50, 59.49),
       ('2018/10/16', 'NA08MYW', 338.7, 118281, 35.11, 51.58),
       ('2018/10/27', 'NA08MYW', 375.4, 118657, 37.83, 55.95),
       ('2018/11/02', 'NA08MYW', 393.1, 119050, 35.69, 52.79),
       ('2018/11/10', 'NA08MYW', 463.8, 119514, 43.86, 64.87),
       ('2018/11/17', 'NA08MYW', 302.4, 119816, 30.22, 44.70),
       ('2018/11/30', 'NA08MYW', 366.6, 120183, 41.67, 60.88),
       ('2018/12/14', 'NA08MYW', 407.2, 120590, 44.46, 63.09),
       ('2019/01/03', 'NA08MYW', 403.4, 120994, 43.43, 61.19),
       ('2019/01/17', 'NA08MYW', 373.0, 121367, 41.13, 57.54),
       ('2019/02/01', 'NA08MYW', 413.9, 121781, 45.69, 63.92),
       ('2019/02/12', 'NA08MYW', 298.7, 122080, 32.54, 45.52),
       ('2019/02/20', 'NA08MYW', 373.1, 122453, 36.92, 52.02),
       ('2019/03/05', 'NA08MYW', 314.5, 122767, 32.98, 47.13),
       ('2019/03/20', 'NA08MYW', 399.8, 123167, 45.34, 65.70),
       ('2019/04/03', 'NA08MYW', 386.7, 123554, 40.24, 58.71),
       ('2019/04/17', 'NA08MYW', 392.7, 123947, 40.70, 60.20),
       ('2019/04/20', 'NA08MYW', 342.5, 124289, 33.96, 51.25),
       ('2019/04/25', 'NA08MYW', 424.7, 124714, 41.57, 59.82),
       ('2019/05/02', 'NA08MYW', 245.1, 124959, 22.60, 33.65),
       ('2019/05/06', 'NA08MYW', 367.9, 125327, 34.51, 50.00),
       ('2019/05/08', 'NA08MYW', 329.2, 125656, 29.61, 44.09),
       ('2019/05/25', 'NA08MYW', 291.8, 125948, 34.11, 49.77),
       ('2019/06/11', 'NA08MYW', 403.5, 126352, 41.36, 56.21),
       ('2019/06/12', 'HY13VLV', 360.0, 41374, 43.88, 62.70),
       ('2019/06/21', 'HY13VLV', 265.6, 41640, 33.81, 50.00),
       ('2019/07/03', 'HY13VLV', 110.1, 41750, 18.00, 25.90),
       ('2019/07/10', 'HY13VLV', 146.4, 41896, 20.53, 29.34),
       ('2019/07/16', 'HY13VLV', 166.6, 42063, 21.41, 31.02),
       ('2019/07/22', 'HY13VLV', 227.1, 42290, 29.05, 38.32),
       ('2019/07/26', 'HY13VLV', 187.8, 42478, 24.74, 32.63),
       ('2019/08/01', 'HY13VLV', 164.8, 42643, 20.88, 27.54),
       ('2019/08/06', 'HY13VLV', 096.1, 42739, 13.60, 18.07),
       ('2019/08/12', 'HY13VLV', 125.6, 42865, 17.31, 23.00),
       ('2019/08/14', 'HY13VLV', 226.8, 43091, 22.14, 29.42),
       ('2019/08/19', 'HY13VLV', 314.2, 43406, 33.24, 43.84),
       ('2019/08/23', 'HY13VLV', 94.3, 43500, 11.93, 15.85),
       ('2019/08/25', 'HY13VLV', 314.3, 43814, 35.99, 48.55),
       ('2019/08/27', 'HY13VLV', 202.2, 44016, 18.26, 24.27),
       ('2019/08/30', 'HY13VLV', 106.8, 44123, 13.53, 17.98),
       ('2019/08/31', 'HY13VLV', 351.1, 44475, 33.26, 44.53),
       ('2019/09/03', 'HY13VLV', 143.5, 44618, 15.61, 20.59),
       ('2019/09/04', 'HY13VLV', 356.4, 44975, 37.75, 49.79),
       ('2019/09/09', 'HY13VLV', 143.4, 45118, 17.90, 23.79),
       ('2019/09/16', 'HY13VLV', 205.8, 45324, 26.84, 35.4),
       ('2019/09/23', 'HY13VLV', 291.3, 45615, 36.00, 47.48),
       ('2019/09/30', 'HY13VLV', 183.8, 45799, 23.94, 31.82),
       ('2019/10/07', 'HY13VLV', 217.3, 46017, 26.79, 35.60),
       ('2019/10/14', 'HY13VLV', 145.8, 46163, 20.18, 26.62),
       ('2019/10/21', 'HY13VLV', 200.0, 46363, 25.57, 33.73),
       ('2019/10/29', 'HY13VLV', 326.6, 46689, 38.40, 49.88),
       ('2019/11/06', 'HY13VLV', 258.8, 46948, 27.94, 36.01),
       ('2019/11/11', 'HY13VLV', 94.4, 47043, 14.32, 18.60),
       ('2019/11/18', 'HY13VLV', 133.8, 47176, 18.94, 24.60),
       ('2019/11/25', 'HY13VLV', 258.8, 47435, 32.66, 42.43),
       ('2019/12/02', 'HY13VLV', 134.3, 47570, 20.79, 27.01),
       ('2019/12/09', 'HY13VLV', 153.7, 47723, 23.45, 30.46),
       ('2019/12/16', 'HY13VLV', 134.3, 47858, 19.26, 25.02),
       ('2019/12/23', 'HY13VLV', 149.0, 48007, 23.42, 30.42),
       ('2019/12/30', 'HY13VLV', 123.0, 48130, 16.78, 21.97),
       ('2020/01/06', 'HY13VLV', 312.5, 48442, 34.93, 46.42),
       ('2020/01/13', 'HY13VLV', 160.2, 48603, 22.57, 30.00),
       ('2020/01/20', 'HY13VLV', 199.1, 48802, 28.22, 37.79),
       ('2020/01/27', 'HY13VLV', 211.1, 49013, 27.37, 36.65),
       ('2020/02/03', 'HY13VLV', 163.2, 49176, 22.86, 30.61),
       ('2020/02/10', 'HY13VLV', 171.4, 49348, 25.40, 34.01),
       ('2020/02/16', 'HY13VLV', 235.8, 49584, 31.06, 40.66),
       ('2020/02/29', 'HY13VLV', 280.9, 49864, 33.63, 43.35),
       ('2020/03/02', 'HY13VLV', 208.9, 50073, 22.81, 29.17),
       ('2020/03/07', 'HY13VLV', 158.0, 50231, 23.02, 30.59),
       ('2020/03/09', 'HY13VLV', 280.4, 50512, 31.67, 40.51),
       ('2020/03/16', 'HY13VLV', 183.8, 50696, 25.01, 31.74),
       ('2020/03/18', 'HY13VLV', 057.3, 50753, 08.63, 10.52),
       ('2020/03/23', 'HY13VLV', 050.9, 50804, 06.56, 08.19),
       ('2020/05/16', 'HY13VLV', 195.9, 51000, 27.23, 30.47),
       ('2020/06/12', 'HY13VLV', 192.6, 51193, 24.82, 28.02),
       ('2020/07/18', 'HY13VLV', 304.3, 51497, 40.81, 48.52),
       ('2020/08/11', 'HY13VLV', 275.9, 51773, 38.16, 45.75),
       ('2020/08/12', 'HY13VLV', 444.3, 52217, 47.20, 61.62),
       ('2020/08/13', 'HY13VLV', 366.2, 52584, 41.70, 55.46),
       ('2020/08/14', 'HY13VLV', 317.9, 52902, 29.44, 31.60),
       ('2020/08/24', 'HY13VLV', 268.1, 53170, 38.15, 50.53),
       ('2020/08/25', 'HY13VLV', 500.2, 53670, 50.18, 51.23),
       ('2020/08/26', 'HY13VLV', 409.0, 54079, 42.81, 51.33),
       ('2020/09/10', 'HY13VLV', 403.3, 54483, 47.88, 57.41),
       ('2020/09/19', 'HY13VLV', 328.1, 54811, 40.75, 48.86),
       ('2020/10/16', 'HY13VLV', 283.6, 55094, 43.65, 52.34),
       ('2020/10/31', 'HY13VLV', 165.7, 55260, 26.33, 31.57),
       ('2020/12/05', 'HY13VLV', 213.9, 55474, 35.69, 42.79),
       ('2021/01/03', 'HY13VLV', 306.9, 55781, 43.48, 53.00),
       ('2021/02/21', 'HY13VLV', 327.0, 56108, 50.00, 63.45),
       ('2021/04/04', 'HY13VLV', 317.2, 56425, 44.43, 56.83),
       ('2021/04/17', 'HY13VLV', 340.7, 56766, 47.39, 61.56),
       ('2021/05/08', 'HY13VLV', 275.4, 57042, 41.89, 54.42),
       ('2021/05/22', 'HY13VLV', 237.0, 57279, 34.19, 45.10),
       ('2021/06/11', 'HY13VLV', 316.8, 57596, 44.23, 59.67),
       ('2021/06/26', 'HY13VLV', 262.5, 57858, 38.51, 52.34),
       ('2021/07/14', 'HY13VLV', 315.2, 58173, 43.16, 57.25),
       ('2021/07/18', 'HY13VLV', 332.6, 58506, 40.90, 56.40),
       ('2021/07/27', 'HY13VLV', 182.9, 58689, 22.09, 30.02),
       ('2021/07/31', 'HY13VLV', 408.5, 59097, 50.00, 70.11),
       ('2021/08/01', 'HY13VLV', 354.1, 59452, 45.77, 65.00),
       ('2021/08/03', 'HY13VLV', 374.7, 59826, 41.80, 51.59),
       ('2021/08/09', 'HY13VLV', 215.3, 60042, 25.69, 31.06),
       ('2021/08/11', 'HY13VLV', 420.4, 60462, 49.71, 58.61),
       ('2021/08/16', 'HY13VLV', 383.8, 60846, 45.92, 51.12),
       ('2021/08/17', 'HY13VLV', 734.4, 61580, 88.67, 102.20),
       ('2021/08/18', 'HY13VLV', 380.5, 61961, 45.66, 62.97);

INSERT INTO vehicle_owner(reg, owner)
VALUES ('NA08MYW', '44183405'),
       ('HY13VLV', '44183405'),
       ('D3VPR0', '1168844929478606848');

