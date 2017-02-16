package model

import java.text.DecimalFormat

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.util.{Failure, Success, Try}

case class MoneyControlReport(parsingErrors: List[String],
                              entries: List[Entry],
                              fileName: String)

case class Entry(datum: DateTime,
                 typ: String,
                 betrag: BigDecimal,
                 währung: String,
                 kategorie: String,
                 person: String,
                 konto: String,
                 gegenkonto: String,
                 gruppe: String,
                 notiz: String)

object MoneyControlReport {

  def parse(fileName: String, csvFile: Iterator[String]): MoneyControlReport = {

    val (invalids, valids) = csvFile.toList
      .drop(1)
      .map(_.split(";"))
      .map(removeOuterQuotationMarks)
      .map(parseLine)
      .foldLeft((List.empty[String], List.empty[Entry])) {
        case ((errors, results), current) =>
          current match {
            case Left(error) => (error :: errors, results)
            case Right(entry) => (errors, entry :: results)
          }
      }

    MoneyControlReport(invalids, valids, fileName)
    /*
Datum;Typ;Betrag;Währung;Kategorie;Person;Konto;Gegenkonto;Gruppe;Notiz
"01.01.16";"Kontobewegung";"7.187,01";"EUR";"";"";"Einzahlung";"Girokonto";"";"Start"
"01.01.16";"Ausgaben";"-52,50";"EUR";"Strom / TV / Internet";"";"Girokonto";"";"";"GEZ"
"01.01.16";"Kontobewegung";"750,00";"EUR";"";"";"Girokonto";"Sparkonto";"";"Sparen"

   */

  }

  def removeOuterQuotationMarks(line: Array[String]): Seq[String] = {
    line.map { cell =>
      val tmp = if (cell.startsWith("\"")) cell.substring(1) else cell

      if (tmp.endsWith("\"")) tmp.dropRight(1) else tmp
    }
  }

  def parseLine(line: Seq[String]): Either[String, Entry] = {
    line match {
      case Seq(
      datum,
      typ,
      betrag,
      währung,
      kategorie,
      person,
      konto,
      gegenkonto,
      gruppe,
      notiz) =>
        //parseDate(datum)

        for {
          date <- parseDate(datum).right
          amount <- parseBigDecimal(betrag).right
        } yield
          Entry(date,
            typ,
            amount,
            währung,
            kategorie,
            person,
            konto,
            gegenkonto,
            gruppe,
            notiz)
      case _ => Left(line.mkString(";"))
    }
  }

  def parseDate(str: String): Either[String, DateTime] = {
    Try(DateTime.parse(str, DateTimeFormat.shortDate())) match {
      case Success(date) => Right(date)
      case Failure(err) => Left(err.getMessage)
    }
  }

  val bigDecimalNumberFormat = java.text.NumberFormat
    .getNumberInstance(java.util.Locale.GERMAN)
    .asInstanceOf[DecimalFormat]
  bigDecimalNumberFormat.setParseBigDecimal(true)

  def parseBigDecimal(str: String): Either[String, BigDecimal] = {

    Try(bigDecimalNumberFormat.parseObject(str).asInstanceOf[java.math.BigDecimal]) match {
      case Success(amount) => Right(amount)
      case Failure(err) => Left(s"problem with parsing $str as decimal")
    }

  }

}
