package utils

import cats.effect.IO
import cats.~>
import doobie.free.connection.ConnectionIO
import repository.DoobieTransactor

trait TransactionSyntax {

  def doobieTransactor: DoobieTransactor

  import doobie.implicits._

  // for convenience, take a ConnectionIO and get an IO representing the operation being run in a transaction
  def transact[A]: ConnectionIO[A] => IO[A] = _.transact(doobieTransactor.tx)

  //useful for transacting monad transformers. When the ConnectionIO is the F in OptionT[F, A] or EitherT[F, A, B]
  //then take the transformer and .mapK(runTransaction)
  val runTransaction: ConnectionIO ~> IO = new ~>[ConnectionIO, IO] {
    override def apply[A](fa: ConnectionIO[A]): IO[A] = transact(fa)
  }

}
