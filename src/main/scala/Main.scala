import repository.DatabaseProvider
import repository.order.OrderRepositoryImpl
import service.OrderService

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.concurrent.Executors
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}
import scala.util.Try

object Main {

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4))
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  final val defaultDate = Timestamp.from(dateFormat.parse("2022-05-10 14:20:00").toInstant)
  private final val defaultIntervals = Seq(1 -> 3, 4 -> 6, 7 -> 12)

  val parseTimestamp: String => Timestamp = strDate => Timestamp.from(dateFormat.parse(strDate).toInstant)


  def main(args: Array[String]): Unit = {

    lazy val orderService = new OrderService(new OrderRepositoryImpl(DatabaseProvider.db))

    val dateFrom = Try(
      parseTimestamp(args(0)))
      .toOption
      .getOrElse(defaultDate)


    val dateTo = Try(
      parseTimestamp(args(1)))
      .toOption
      .getOrElse(Timestamp.from(Instant.now()))

    val intervals = args.
      drop(2)
      .map(_.split("-").map(_.toInt))
      .map(interval => interval(0) -> interval(1))
      .toSeq match {
      case Seq() => defaultIntervals
      case interval => interval
    }

    println("Orders grouped by product age:")

    Await.result(
      orderService.getOrderCount(dateFrom, dateTo, intervals),
      5.seconds
    ).foreach {
      case (months, ordersInInterval) =>
        println(s"$months months: ${ordersInInterval} orders")
    }
  }
}