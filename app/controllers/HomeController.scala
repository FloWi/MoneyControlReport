package controllers

import javax.inject._

import model.MoneyControlReport
import play.api.mvc._

import scala.io.Source

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def generateReport = Action { implicit request =>

    val csvFileContent = Source.fromURI(getClass.getResource("/moneyControlReport.csv").toURI).getLines()
    val report = MoneyControlReport.parse(csvFileContent)

    Ok(views.html.moneyControlReport(report))

  }

}
