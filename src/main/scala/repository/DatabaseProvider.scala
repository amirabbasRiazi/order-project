package repository

import slick.jdbc.JdbcBackend.Database

object DatabaseProvider {
  lazy val db = Database.forConfig("postgres")
}
