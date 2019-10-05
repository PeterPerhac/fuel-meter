package utils

import cats.effect.IO
import cats.~>
import doobie.free.connection.ConnectionIO
import repository.DoobieTransactor

trait TransactionSyntax {

  def doobieTransactor: DoobieTransactor

  import doobie.implicits._
  def transact[A]: ConnectionIO[A] => IO[A] = _.transact(doobieTransactor.tx)

  val runTransaction: ConnectionIO ~> IO = new ~>[ConnectionIO, IO] {
    override def apply[A](fa: ConnectionIO[A]): IO[A] = transact(fa)
  }

}
