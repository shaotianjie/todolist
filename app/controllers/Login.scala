package controllers

import models._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

/**
 * Created by stj on 2014/10/30.
 */
object Login extends Controller{
  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  def login = Action {implicit request =>
    Ok(views.html.login(loginForm))
  }

  def logout = Action {
    Redirect(routes.Application.index).withNewSession
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.Application.index) withSession(
        "email" -> user._1
        )
    )
  }
}
