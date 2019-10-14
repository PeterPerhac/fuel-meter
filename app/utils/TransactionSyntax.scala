package utils

import cats.effect.{Async, IO}
import doobie.free.connection.ConnectionIO
import repository.DoobieTransactor

trait TransactionSyntax {

  def doobieTransactor: DoobieTransactor

  import doobie.implicits._

  implicit val ACIO: Async[ConnectionIO] = doobie.free.connection.AsyncConnectionIO

  // for convenience, take a ConnectionIO and get an IO representing the operation being run in a transaction
  def transact[A]: ConnectionIO[A] => IO[A] = _.transact(doobieTransactor.tx)

}
