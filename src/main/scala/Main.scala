import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object Main {

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4))
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  final val defaultDate = Timestamp.from(dateFormat.parse("2022-05-10 14:20:00").toInstant)
  private final val defaultIntervals = Seq(1 -> 3, 4 -> 6, 7 -> 12)

  def main(args: Array[String]): Unit = {



  }
}