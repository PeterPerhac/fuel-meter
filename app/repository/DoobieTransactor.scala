package repository

import cats.effect._
import doobie.util.transactor.Transactor
import javax.sql.DataSource
import play.api.db.Database

import scala.concurrent.ExecutionContext

class DoobieTransactor(db: Database)(implicit executionContext: ExecutionContext) {

  //TODO setup separate execution context for blocking IO
  val blocker: Blocker = Blocker.liftExecutionContext(executionContext)

  implicit val contextShift: ContextShift[IO] = IO.contextShift(executionContext)

  val tx: Transactor.Aux[IO, DataSource] =
    Transactor.fromDataSource[IO](db.dataSource, executionContext, blocker)

}
