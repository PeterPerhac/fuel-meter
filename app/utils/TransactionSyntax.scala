package utils

import cats.effect.IO
import doobie.free.connection.ConnectionIO
import repository.DoobieTransactor

trait TransactionSyntax {

  def doobieTransactor: DoobieTransactor

  import doobie.implicits._
  def transact[A]: ConnectionIO[A] => IO[A] = _.transact(doobieTransactor.tx)


}
