/**
 * Created by stj on 2014/10/29.
 */
import play.api._
import models._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    InitialData.insert()
  }

}

/**
 * Initial set of data to be imported
 * in the sample application.
 */
object InitialData {

  def insert() = {
    if(User.findAll.isEmpty) {
      Seq(
        User("guillaume@sample.com", "Guillaume Bort", "secret"),
        User("admin@sample.com", "admin", "secret"),
        User("sadek@sample.com", "Sadek Drobi", "secret"),
        User("erwan@sample.com", "Erwan Loisant", "secret")
      ).foreach(User.create)

    }

  }

}