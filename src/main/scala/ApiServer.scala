import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}
import scala.concurrent.Future

object ApiServer {
  def main(args: Array[String]): Unit = {
    // Initialize ActorSystem and ExecutionContext
    implicit val system: ActorSystem = ActorSystem("sample-scala-app")
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    // Define the route
    val route: Route =
      pathEndOrSingleSlash {
        get {
          complete(StatusCodes.OK, "Welcome to the Scala API!")
        }
      }

    // Start the server and bind it to port 80
    val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt("0.0.0.0", 80).bind(route)

    // Log success or failure of binding
    bindingFuture.onComplete {
      case Success(binding) =>
        println(s"Server online at http://localhost:80/")
        // Handle graceful shutdown when application is terminated (CTRL+C)
        sys.addShutdownHook {
          println("Shutting down server...")
          binding.unbind().onComplete(_ => system.terminate())
        }
      case Failure(exception) =>
        println(s"Server failed to start: ${exception.getMessage}")
        system.terminate()
    }

    // Ensure the process stays alive indefinitely
    println("Server started successfully. Press CTRL+C to stop the server.")
    while (!system.whenTerminated.isCompleted) {
      Thread.sleep(1000)  // Wait until the ActorSystem is terminated
    }
  }
}
