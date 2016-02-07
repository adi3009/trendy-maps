name := """trendy-maps"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.scalatestplus" %% "play" % "1.4.0" % Test,
  "org.mockito" % "mockito-core" % "1.10.19" % Test,
  "com.github.tomakehurst" % "wiremock" % "1.57" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

Keys.fork in Test := false