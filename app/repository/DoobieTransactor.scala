package repository

import cats.effect._
import doobie.util.transactor.Transactor
import javax.sql.DataSource
import play.api.db.Database

import scala.concurrent.ExecutionContext

//https://tpolecat.github.io/doobie/docs/14-Managing-Connections.html
class DoobieTransactor(
      db: Database,
      unboundedExecutionContext: ExecutionContext,
      boundedExecutionContext: ExecutionContext
) {

  // ContextShift[M], which provides a CPU-bound pool for non-blocking operations.
  // This is typically backed by ExecutionContext.global.
  implicit val contextShift: ContextShift[IO] = IO.contextShift(unboundedExecutionContext)

  //A cats.effect.Blocker for executing JDBC operations. Because your connection pool limits the number of active connections this should be an unbounded pool.
  val blocker: Blocker = Blocker.liftExecutionContext(unboundedExecutionContext)

  val tx: Transactor.Aux[IO, DataSource] =
    Transactor.fromDataSource[IO](db.dataSource, boundedExecutionContext, blocker)

}
