import com.typesafe.sbt.packager.docker._

import Dependencies._

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(dockerSettings)
  .enablePlugins(AshScriptPlugin)

lazy val commonSettings = Seq(
  organization := "eu.radusw",
  scalaVersion := "2.12.1",
  version      := "1.0",
  name         := "monix-akka-http-client",
  resolvers ++= projectResolvers,
  libraryDependencies ++= dependencies,
  scalacOptions ++= compileSettings,
  fork in run := true,
  fork in Test := true,
  fork in testOnly := true,
  connectInput in run := true,
  javaOptions in run ++= forkedJvmOption,
  javaOptions in Test ++= forkedJvmOption
)


lazy val compileSettings = Seq(
  "-encoding", "UTF-8",
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:_",
  "-target:jvm-1.8",
  "-unchecked",
  "-Ydelambdafy:method",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Xlog-free-terms",
  "-Xlint:adapted-args", // warn if an argument list is modified to match the receiver
  "-Xlint:nullary-unit", // warn when nullary methods return Unit
  "-Xlint:inaccessible", // warn about inaccessible types in method signatures
  "-Xlint:nullary-override", // warn when non-nullary `def f()' overrides nullary `def f'
  "-Xlint:infer-any", // warn when a type argument is inferred to be `Any`
  "-Xlint:-missing-interpolator", // disables missing interpolator warning
  "-Xlint:doc-detached", // a ScalaDoc comment appears to be detached from its element
  "-Xlint:private-shadow", // a private field (or class parameter) shadows a superclass field
  "-Xlint:type-parameter-shadow", // a local type parameter shadows a type already in scope
  "-Xlint:poly-implicit-overload", // parameterized overloaded implicit methods are not visible as view bounds
  "-Xlint:option-implicit", // Option.apply used implicit view
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit
  "-Xlint:by-name-right-associative", // By-name parameter of right associative operator
  "-Xlint:package-object-classes", // Class or object defined in package object
  "-Xlint:unsound-match" // Pattern match may not be typesafe
)

lazy val forkedJvmOption = Seq(
  "-server",
  "-Dfile.encoding=UTF8",
  "-Duser.timezone=UTC",
  "-Xss1m",
  "-Xms2048m",
  "-Xmx2048m",
  "-XX:+CMSClassUnloadingEnabled",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+DoEscapeAnalysis",
  "-XX:+UseConcMarkSweepGC",
  "-XX:+UseParNewGC",
  "-XX:+UseCodeCacheFlushing",
  "-XX:+UseCompressedOops"
)


lazy val dockerSettings = Seq(
  defaultLinuxInstallLocation in Docker := "/opt/monix-akka-http-client",
  dockerCommands := Seq(
    Cmd("FROM", "alpine:3.3"),
    Cmd("RUN apk upgrade --update && apk add --update openjdk8-jre"),
    Cmd("ADD", "opt /opt"),
    ExecCmd("ENTRYPOINT", "/opt/monix-akka-http-client/bin/monix-akka-http-client -Dlogger.resource=logback.docker.xml -Dpidfile.path=/dev/null")
  ),
  dockerExposedPorts := Seq(9000),

  version in Docker := version.value,
  maintainer in Docker := "Radu Gancea <radu.gancea@gmail.com>",
  dockerRepository := Some("radusw")
)