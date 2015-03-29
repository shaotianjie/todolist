package controllers


import play.Logger
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.{ User, Task}
import play.Logger

object Application extends Controller with Secured {

  val taskForm = Form (
    "label" -> nonEmptyText
  )

  def index = IsAuthenticated {user => implicit request=>
    Redirect(routes.Application.tasks)
  }

  def tasks = IsAuthenticated {user=> implicit request=>
    Ok(views.html.index(user, Task.allByUser(user), taskForm))
  }

  def newTask = IsAuthenticated {user=> implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(user, Task.allByUser(user), errors)),
      label => {

        Task.create(label,user)
        Redirect(routes.Application.tasks())
      }
    )
  }

  def delete(id: Long) = IsAuthenticated { user=> implicit request =>
    Task.delete(id,user)
    Redirect(routes.Application.tasks())
  }

}


trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def getUser(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Login.login)





  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(getUser, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }

}