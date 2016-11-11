import java.time.LocalDate
import java.time.format._

sealed trait Month

object Jan extends Month

object Feb extends Month

object Mar extends Month

object Apr extends Month

object May extends Month

object Jun extends Month

object Jul extends Month

object Aug extends Month

object Sep extends Month

object Oct extends Month

object Nov extends Month

object Dec extends Month

case class Reading(reg: String, date: String, total: Int, mi: Double, litres: Double, cost: Double)

val readings: Seq[Reading] = Seq(
  Reading(reg = "NA08MYW", date = "04/01/2014", total = 74923, mi = 447.0, litres = 46.69, cost = 64.39),
  Reading(reg = "NA08MYW", date = "05/01/2014", total = 75030, mi = 107.0, litres = 10.21, cost = 13.98),
  Reading(reg = "NA08MYW", date = "25/01/2014", total = 75288, mi = 140.0, litres = 14.71, cost = 19.99),
  Reading(reg = "NA08MYW", date = "01/02/2014", total = 75431, mi = 292.0, litres = 33.51, cost = 46.21),
  Reading(reg = "NA08MYW", date = "09/02/2014", total = 75802, mi = 371.2, litres = 38.92, cost = 52.50),
  Reading(reg = "NA08MYW", date = "26/02/2014", total = 76199, mi = 396.4, litres = 46.67, cost = 56.00),
  Reading(reg = "NA08MYW", date = "16/03/2014", total = 76591, mi = 392.2, litres = 43.49, cost = 62.15),
  Reading(reg = "NA08MYW", date = "14/04/2014", total = 76795, mi = 202.9, litres = 32.71, cost = 44.13),
  Reading(reg = "NA08MYW", date = "30/05/2014", total = 77087, mi = 292.2, litres = 35.63, cost = 48.42),
  Reading(reg = "NA08MYW", date = "17/06/2014", total = 77495, mi = 407.2, litres = 36.92, cost = 49.44),
  Reading(reg = "NA08MYW", date = "11/07/2014", total = 77776, mi = 281.6, litres = 29.05, cost = 39.19),
  Reading(reg = "NA08MYW", date = "17/07/2014", total = 78052, mi = 275.4, litres = 26.81, cost = 35.63),
  Reading(reg = "NA08MYW", date = "18/07/2014", total = 78378, mi = 326.1, litres = 29.62, cost = 40.25),
  Reading(reg = "NA08MYW", date = "22/07/2014", total = 78741, mi = 362.8, litres = 36.65, cost = 48.71),
  Reading(reg = "NA08MYW", date = "01/08/2014", total = 78956, mi = 215.4, litres = 20.97, cost = 27.66),
  Reading(reg = "NA08MYW", date = "16/08/2014", total = 79304, mi = 348.4, litres = 33.15, cost = 43.39),
  Reading(reg = "NA08MYW", date = "30/08/2014", total = 79581, mi = 276.4, litres = 28.54, cost = 37.07),
  Reading(reg = "NA08MYW", date = "30/09/2014", total = 79998, mi = 416.6, litres = 42.57, cost = 55.72),
  Reading(reg = "NA08MYW", date = "29/10/2014", total = 80356, mi = 358.4, litres = 35.75, cost = 45.72),
  Reading(reg = "NA08MYW", date = "23/11/2014", total = 80698, mi = 342.0, litres = 38.50, cost = 48.09),
  Reading(reg = "NA08MYW", date = "07/12/2014", total = 80984, mi = 286.6, litres = 29.40, cost = 36.13),
  Reading(reg = "NA08MYW", date = "16/12/2014", total = 81171, mi = 186.5, litres = 18.04, cost = 22.89),
  Reading(reg = "NA08MYW", date = "14/01/2015", total = 81378, mi = 206.8, litres = 19.97, cost = 24.14),
  Reading(reg = "NA08MYW", date = "01/02/2015", total = 81625, mi = 247.2, litres = 25.81, cost = 30.95),
  Reading(reg = "NA08MYW", date = "14/02/2015", total = 81901, mi = 276.5, litres = 28.94, cost = 32.38),
  Reading(reg = "NA08MYW", date = "02/03/2015", total = 82146, mi = 245.6, litres = 26.13, cost = 30.28),
  Reading(reg = "NA08MYW", date = "02/04/2015", total = 82349, mi = 202.8, litres = 23.66, cost = 28.13),
  Reading(reg = "NA08MYW", date = "10/04/2015", total = 82796, mi = 446.9, litres = 46.14, cost = 54.40),
  Reading(reg = "NA08MYW", date = "23/04/2015", total = 83160, mi = 363.8, litres = 37.87, cost = 45.03),
  Reading(reg = "NA08MYW", date = "05/05/2015", total = 83303, mi = 142.6, litres = 16.36, cost = 19.45),
  Reading(reg = "NA08MYW", date = "19/05/2015", total = 83670, mi = 367.1, litres = 39.77, cost = 47.68),
  Reading(reg = "NA08MYW", date = "30/05/2015", total = 83850, mi = 178.8, litres = 17.81, cost = 21.35),
  Reading(reg = "NA08MYW", date = "15/06/2015", total = 84152, mi = 301.5, litres = 30.54, cost = 36.62),
  Reading(reg = "NA08MYW", date = "11/07/2015", total = 84481, mi = 329.4, litres = 33.86, cost = 39.58),
  Reading(reg = "NA08MYW", date = "28/07/2015", total = 84842, mi = 361.2, litres = 38.33, cost = 44.04),
  Reading(reg = "NA08MYW", date = "07/08/2015", total = 85089, mi = 246.8, litres = 26.01, cost = 29.63),
  Reading(reg = "NA08MYW", date = "11/08/2015", total = 85273, mi = 183.5, litres = 17.94, cost = 20.07),
  Reading(reg = "NA08MYW", date = "14/08/2015", total = 85563, mi = 290.0, litres = 28.56, cost = 31.96),
  Reading(reg = "NA08MYW", date = "16/08/2015", total = 85970, mi = 407.4, litres = 39.52, cost = 44.22),
  Reading(reg = "NA08MYW", date = "18/08/2015", total = 86224, mi = 253.5, litres = 22.79, cost = 25.27),
  Reading(reg = "NA08MYW", date = "06/09/2015", total = 86463, mi = 239.2, litres = 23.70, cost = 25.81),
  Reading(reg = "NA08MYW", date = "10/09/2015", total = 86649, mi = 185.9, litres = 20.69, cost = 22.74),
  Reading(reg = "NA08MYW", date = "19/09/2015", total = 86833, mi = 183.4, litres = 18.54, cost = 20.38),
  Reading(reg = "NA08MYW", date = "01/10/2015", total = 87049, mi = 215.9, litres = 23.30, cost = 25.37),
  Reading(reg = "NA08MYW", date = "18/10/2015", total = 87396, mi = 347.6, litres = 37.56, cost = 40.90),
  Reading(reg = "NA08MYW", date = "31/10/2015", total = 87696, mi = 299.6, litres = 30.58, cost = 33.30),
  Reading(reg = "NA08MYW", date = "08/11/2015", total = 88052, mi = 356.3, litres = 39.88, cost = 44.63),
  Reading(reg = "NA08MYW", date = "19/11/2015", total = 88257, mi = 204.9, litres = 20.88, cost = 22.74),
  Reading(reg = "NA08MYW", date = "09/12/2015", total = 88562, mi = 304.9, litres = 33.33, cost = 36.30),
  Reading(reg = "NA08MYW", date = "23/12/2015", total = 88743, mi = 171.3, litres = 20.52, cost = 21.73),
  Reading(reg = "NA08MYW", date = "31/12/2015", total = 88983, mi = 249.2, litres = 24.86, cost = 26.08),
  Reading(reg = "NA08MYW", date = "08/01/2016", total = 89323, mi = 340.6, litres = 37.03, cost = 38.47),
  Reading(reg = "NA08MYW", date = "23/01/2016", total = 89550, mi = 226.3, litres = 25.69, cost = 25.92),
  Reading(reg = "NA08MYW", date = "27/01/2016", total = 89738, mi = 187.9, litres = 19.60, cost = 19.58),
  Reading(reg = "NA08MYW", date = "10/02/2016", total = 89928, mi = 189.9, litres = 20.33, cost = 20.72),
  Reading(reg = "NA08MYW", date = "15/02/2016", total = 90153, mi = 225.5, litres = 24.95, cost = 25.43),
  Reading(reg = "NA08MYW", date = "02/03/2016", total = 90463, mi = 309.3, litres = 32.72, cost = 33.34),
  Reading(reg = "NA08MYW", date = "16/03/2016", total = 90742, mi = 279.1, litres = 31.72, cost = 33.28),
  Reading(reg = "NA08MYW", date = "03/04/2016", total = 91011, mi = 269.6, litres = 28.33, cost = 30.85),
  Reading(reg = "NA08MYW", date = "05/04/2016", total = 91403, mi = 391.7, litres = 37.88, cost = 39.74),
  Reading(reg = "NA08MYW", date = "11/04/2016", total = 91618, mi = 214.5, litres = 23.36, cost = 24.50),
  Reading(reg = "NA08MYW", date = "22/04/2016", total = 91800, mi = 182.3, litres = 19.36, cost = 20.89),
  Reading(reg = "NA08MYW", date = "03/05/2016", total = 92105, mi = 304.9, litres = 32.20, cost = 35.39),
  Reading(reg = "NA08MYW", date = "14/05/2016", total = 92356, mi = 251.1, litres = 24.95, cost = 27.42),
  Reading(reg = "NA08MYW", date = "31/05/2016", total = 92590, mi = 233.8, litres = 25.21, cost = 28.21),
  Reading(reg = "NA08MYW", date = "13/06/2016", total = 92870, mi = 279.6, litres = 29.19, cost = 32.96),
  Reading(reg = "NA08MYW", date = "29/06/2016", total = 93269, mi = 398.7, litres = 41.48, cost = 47.25),
  Reading(reg = "NA08MYW", date = "12/07/2016", total = 93523, mi = 254.1, litres = 25.75, cost = 29.33),
  Reading(reg = "NA08MYW", date = "24/07/2016", total = 93650, mi = 126.9, litres = 13.92, cost = 15.58),
  Reading(reg = "NA08MYW", date = "25/07/2016", total = 93933, mi = 283.5, litres = 29.73, cost = 34.16),
  Reading(reg = "NA08MYW", date = "26/07/2016", total = 94157, mi = 224.3, litres = 22.93, cost = 26.35),
  Reading(reg = "NA08MYW", date = "27/07/2016", total = 94277, mi = 119.9, litres = 13.79, cost = 15.16),
  Reading(reg = "NA08MYW", date = "28/07/2016", total = 94445, mi = 167.8, litres = 16.76, cost = 19.09),
  Reading(reg = "NA08MYW", date = "07/08/2016", total = 94763, mi = 317.7, litres = 33.93, cost = 37.97),
  Reading(reg = "NA08MYW", date = "13/08/2016", total = 94993, mi = 230.1, litres = 25.12, cost = 28.11),
  Reading(reg = "NA08MYW", date = "28/08/2016", total = 95360, mi = 366.6, litres = 37.48, cost = 42.31),
  Reading(reg = "NA08MYW", date = "02/09/2016", total = 95701, mi = 341.1, litres = 34.02, cost = 38.41),
  Reading(reg = "NA08MYW", date = "12/09/2016", total = 96064, mi = 362.7, litres = 38.69, cost = 43.68),
  Reading(reg = "NA08MYW", date = "24/09/2016", total = 96380, mi = 316.1, litres = 33.50, cost = 38.49),
  Reading(reg = "NA08MYW", date = "03/10/2016", total = 96664, mi = 283.7, litres = 27.61, cost = 30.90),
  Reading(reg = "NA08MYW", date = "14/10/2016", total = 96975, mi = 311.8, litres = 34.74, cost = 39.57),
  Reading(reg = "NA08MYW", date = "24/10/2016", total = 97334, mi = 358.5, litres = 38.14, cost = 43.82),
  Reading(reg = "NA08MYW", date = "31/10/2016", total = 97600, mi = 266.5, litres = 29.00, cost = 33.61),
  Reading(reg = "NA08MYW", date = "07/11/2016", total = 97809, mi = 208.2, litres = 22.43, cost = 26.22)
)

case class R(d: java.time.LocalDate, t: Int, m: Double, l: Double, c: Double)

val parseFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
val groupFormat = DateTimeFormatter.ofPattern("MM-yyyy")
def toR(r: Reading): R = R(LocalDate.parse(r.date, parseFormatter), r.total, r.mi, r.litres, r.cost)
def month: R => String = r => r.d.format(groupFormat)
def lastMonths(n: Integer): R => Boolean = r => r.d.isAfter(LocalDate.now().minusMonths(n.toLong))
def monthFilter(m: Month, y: Integer = 2016): R => Boolean = {
  def monthFun(m: Int) = (r: R) => r.d.getMonth.getValue == m && r.d.getYear == y
  m match {
    case Jan => monthFun(1)
    case Feb => monthFun(2)
    case Mar => monthFun(3)
    case Apr => monthFun(4)
    case May => monthFun(5)
    case Jun => monthFun(6)
    case Jul => monthFun(7)
    case Aug => monthFun(8)
    case Sep => monthFun(9)
    case Oct => monthFun(10)
    case Nov => monthFun(11)
    case Dec => monthFun(12)
  }
}

val rs = readings map toR
val september = rs.filter(monthFilter(Sep))
val lastThreeMonths = rs.filter(lastMonths(3))

rs.groupBy(month)