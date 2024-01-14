package repository.order

import domain.Product.productTable
import domain.{Item, Order, Product}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.H2Profile.api._

import java.sql.Timestamp
import scala.concurrent.Future

class OrderRepositoryImpl(db: Database) extends OrderRepository {

    override def findById(id: Long): Future[Option[Order]] = ???

    override def findByInterval(dateFrom: Timestamp, dateTo: Timestamp): Future[Seq[((Order, Item), Product)]] = db.run {
      Order
        .orderTable
        .join(Item.itemTable)
        .on(_.id === _.orderId)
        .join(productTable)
        .on(_._2.productId === _.id)
        .filter {
          case ((order, _), _) =>
            order.registeredDate >= dateFrom
        }
        .filter {
          case ((order, _), _) =>
            order.registeredDate <= dateTo
        }
        .result
    }
}
