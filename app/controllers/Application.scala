package controllers

import com.typesafe.plugin._
import play.api._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

object Application extends Controller {

  case class MailData(email: String)

  val mailForm = Form(
    mapping(
      "email" -> email)(MailData.apply)(MailData.unapply))

  def index = Action {
    Ok(views.html.index(mailForm))
  }

  def sendMail: Action[play.api.mvc.AnyContent] = Action { implicit request =>
    mailForm.bindFromRequest.fold (
    formWithErrors => {
      Redirect("/")
    },
      mailData => {
        /*val mail = use[MailerPlugin].email
            mail.setSubject("Email sent using Scala")
            mail.addRecipient(mailData.email)
            mail.addFrom(mailData.email)
            mail.send("Hello World")*/
        Future {sendEmail(mailData.email)}
            Redirect("/")
      })
 }
  
  def sendEmail(email: String): Unit = {
    val mail = use[MailerPlugin].email
            mail.setSubject("Email sent using Scala")
            mail.addRecipient(email)
            mail.addFrom(email)
            mail.send("Hello World")
  } 
}