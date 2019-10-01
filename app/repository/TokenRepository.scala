package repository

import doobie.free.connection.ConnectionIO
import doobie.implicits._
import scalaj.http.Token

object TokenRepository {

  private def findTokenQuery(key: String) =
    sql"""SELECT token, secret FROM token WHERE token=$key""".query[Token]

  def findToken(key: String): ConnectionIO[Option[Token]] =
    findTokenQuery(key).option

  def getToken(key: String): ConnectionIO[Token] =
    findTokenQuery(key).unique

  def saveToken(token: Token): ConnectionIO[Int] =
    findToken(token.key).flatMap {
      case Some(t) => sql"""UPDATE token SET secret=${token.secret} where token=${t.key}""".update.run
      case None    => sql"""INSERT INTO token(token, secret) VALUES (${token.key}, ${token.secret})""".update.run
    }

}
