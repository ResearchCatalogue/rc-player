name := "rc-player"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "be.doeraene"   %%% "scalajs-jquery"  % "0.8.0",
  "org.scala-js"  %%% "scalajs-dom"     % "0.8.1",
  "com.lihaoyi"   %%% "scalatags"       % "0.5.2"
)

enablePlugins(ScalaJSPlugin)
