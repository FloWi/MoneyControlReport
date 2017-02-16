// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.12")

//elm plugin
addSbtPlugin("io.finstack" % "sbt-elm" % "0.1.1")

//uncomment these lines to use the latest version from github. 0.1.1 is oficially not ready for elm 0.18.0 yet - but it works anyway...
//lazy val root = project.in(file(".")).dependsOn(sbtElm)
//lazy val sbtElm = uri("https://github.com/choucrifahed/sbt-elm.git")