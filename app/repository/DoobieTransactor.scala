package repository

import cats.effect.IO
import doobie.util.transactor.Transactor
import javax.inject.{Inject, Singleton}
import javax.sql.DataSource
import play.api.db.Database

@Singleton
class DoobieTransactor @Inject()(db: Database) {

  val tx: Transactor.Aux[IO, DataSource] =
    Transactor.fromDataSource[IO](db.dataSource)

}
