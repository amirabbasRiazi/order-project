package service

import domain.Product
import model.{ItemDTO, OrderDTO}
import org.scalatest.funsuite.AnyFunSuiteLike
import repository.DatabaseProvider.db
import repository.order.OrderRepositoryImpl

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class OrderServiceTest extends AnyFunSuiteLike {

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(5))
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  final val defaultDate = Timestamp.from(dateFormat.parse("2022-05-10 14:20:00").toInstant)
  private final val defaultIntervals = Seq(1 -> 3, 4 -> 6, 7 -> 12)
  var orderService = new OrderService(new OrderRepositoryImpl(db))


  test("testGroupOrdersByProductAge") {

    val orders = initData()
    val customInterval = Seq(1 -> 2, 5 -> 8)
    val result = orderService.groupOrdersByProductAge(orders, defaultIntervals)
    result
      .foreach {
        case (months, ordersInInterval) =>
          println(s"$months months: ${ordersInInterval} orders")
      }
  }


  test("testDetermineIntervalLabel") {
    val res = orderService.determineIntervalLabel(8, defaultIntervals)

    assert(res == "7-12")
  }

  def initData() = {

    val product = Product(
      registeredDate = Timestamp.from(dateFormat.parse("2023-09-01 00:00:00").toInstant),
      id = 10,
      updatedAt = Timestamp.from(Instant.now()),
      name = "some",
      description = Some("some"),
      productType = "none",
      price = 10000,
      quantity = Some(2),
      weight = None,
      height = None)


    val item = ItemDTO(
      product = product,
      id = 1,
      registeredDate = Timestamp.from(Instant.now()),
      updatedAt = Timestamp.from(Instant.now()),
      cost = 1200000,
      taxAmount = 1000,
      shoppingFee = 11000000,
      discount = None
    )

    val order = OrderDTO(
      registeredDate = Timestamp.from(Instant.now()),
      items = Seq(item),
      id = 12,
      updatedAt = Timestamp.from(Instant.now()),
      title = "google",
      state = "payment_verified"
    )

    Seq(
      order.copy(
        items = Seq(item.copy(
          product = product.copy(
            registeredDate = Timestamp.from(dateFormat.parse("2023-09-01 00:00:00").toInstant))
        ))),
      order.copy(
        items = Seq(item.copy(
          product = product.copy(
            registeredDate = Timestamp.from(dateFormat.parse("2023-05-01 00:00:00").toInstant))
        ))),
      order.copy(
        items = Seq(item.copy(
          product = product.copy(
            registeredDate = Timestamp.from(dateFormat.parse("2022-09-01 00:00:00").toInstant))))),
      order.copy(
        items = Seq(item.copy(
          product = product.copy(
            registeredDate = Timestamp.from(dateFormat.parse("2023-11-01 00:00:00").toInstant)))))
    )
  }
}
