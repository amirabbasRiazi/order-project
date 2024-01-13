package service

import model.OrderDTO
import repository.order.OrderRepository

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import scala.concurrent.{ExecutionContext, Future}

class OrderService(repository: OrderRepository)(implicit ec: ExecutionContext) {

  def getOrderCount(dateFrom: Timestamp, dateTo: Timestamp, intervals: Seq[(Int, Int)]): Future[Map[String, Int]] =
    for {
      orders <- repository.findByInterval(dateFrom, dateTo)
      orderDTOs = orders.groupBy { case ((order, _), _) => order }
        .map { case (order, pairs) =>
          val (orderWithItem, products) = pairs.unzip
          OrderDTO.from(order, orderWithItem.map(_._2), products.minBy(_.registeredDate))
        }
        .toSeq
      result = groupOrdersByProductAge(orderDTOs, intervals)
    } yield result


  def groupOrdersByProductAge(orders: Seq[OrderDTO], intervals: Seq[(Int, Int)]): Map[String, Int] = {
    orders.map {order =>
      (order.items.map(_.product).minBy(_.registeredDate), order)}
      .groupBy { case (product, order) =>
        val months = calculateProductAgeMonths(
          product.registeredDate.toLocalDateTime,
          order.registeredDate.toLocalDateTime)
        determineIntervalLabel(months, intervals)
      }
      .view.mapValues(_.length)
  }.toMap - "-"


  def calculateProductAgeMonths(dateOne: LocalDateTime, dateTwo: LocalDateTime): Long = {
    ChronoUnit.MONTHS.between(dateOne, dateTwo)
  }

  def determineIntervalLabel(months: Long, intervals: Seq[(Int, Int)]): String = {
    intervals
      .find {
        case (lower, upper) =>
          months >= lower && months <= upper
      }
      .map {
        case (lower, upper) =>
          s"${lower}-${upper}"
      }
      .getOrElse {
        if (months > 12) "> 12"
        else "-"
      }
  }
}