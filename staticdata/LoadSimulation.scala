package loadtest.staticdata

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.HeaderNames._
import scala.concurrent.duration._

import loadtest.util._
import loadtest.settings._


class LoadSimulation extends Simulation
                     with Headers
{
  val httpConf = http
    .baseURL(LoadbalancerSettings.BASE_URL)
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .disableFollowRedirect


  val scn = scenario("Read-Settings")

    .exec(RestAPI.get_static_data)
    .pause(40 milliseconds, 120 milliseconds)

    //.exec(RestAPI.get_content_data)
    //.pause(40 milliseconds, 120 milliseconds)


  setUp(
    scn.inject(
      rampUsers(LoadSettings.BURST_USERS)
        over (LoadSettings.BURST_TIME seconds),
      rampUsersPerSec(LoadSettings.RAMP_USERS_START) to (LoadSettings.RAMP_USERS_END)
        during (LoadSettings.RAMP_TIME seconds),
      constantUsersPerSec(LoadSettings.ENDURANCE_USERS)
        during (LoadSettings.ENDURANCE_TIME seconds))
  ).protocols(httpConf)
}
