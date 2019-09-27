package repository

import cats.effect.IO
import doobie.util.transactor.Transactor
import javax.sql.DataSource
import play.api.db.Database

class DoobieTransactor(db: Database) {

  val tx: Transactor.Aux[IO, DataSource] =
    Transactor.fromDataSource[IO](db.dataSource)

}
