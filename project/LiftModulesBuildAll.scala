import sbt._
import sbt.Keys._

object LiftModulesBuildAll extends Build {

	val liftVersion = SettingKey[String]("liftVersion", "The version of the Lift Web Framework to build against")
	
  val scalaVersions =  Seq("2.8.1", "2.9.0-1", "2.9.1", "2.9.2")

	// Git "read-only" URLs appear to be the ones to use here:
	lazy val modules: Seq[ProjectReference] = List(
		uri("git://github.com/liftmodules/paypal.git"),
		uri("git://github.com/liftmodules/widgets.git"),
		uri("git://github.com/liftmodules/amqp.git"),
		uri("git://github.com/liftmodules/facebook.git"),
		uri("git://github.com/liftmodules/imaging.git"),
		uri("git://github.com/liftmodules/jta.git"),
		uri("git://github.com/liftmodules/machine.git"),
		uri("git://github.com/liftmodules/oauth.git"),
		uri("git://github.com/liftmodules/openid.git"),
		uri("git://github.com/liftmodules/scalate.git"),
		uri("git://github.com/liftmodules/textile.git"),
		uri("git://github.com/liftmodules/xmpp.git")
		uri("git://github.com/d6y/liftmodules-googleanalytics.git")
		//uri("git://github.com/d6y/liftmodules-imap-idle.git")
		)
	
	 // Modules that have a dependency on one or more of the modules above
   lazy val dependent: Seq[ProjectReference] = List(
			uri("git://github.com/liftmodules/oauth-mapper.git") // depends on oauth
   	)

	 // Override the version of Lift and publish settings for all modules being built:
   def setting(module: ProjectReference) = List( 
		liftVersion in module := "2.5-SNAPSHOT",
		resolvers in module +=  "Central snapshot" at "https://oss.sonatype.org/content/repositories/snapshots/",
		publishTo in module := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"),
		credentials in module += Credentials( file("/private/liftmodules/sonatype.credentials") )
		)

	lazy val moduleSettings: Seq[Setting[_]] = modules.flatMap(setting)

  lazy val first = Project(id = "first", base = file(".")).
  	aggregate(modules:_*).
 		settings(publish := { }). // don't publish this wrapper project 
 		settings(crossScalaVersions := scalaVersions).
  	settings(moduleSettings:_*)

  lazy val second = Project(id = "second", base = file(".")).
  	aggregate(dependent:_*).
 		settings(publish := { }). 
 		settings(crossScalaVersions := scalaVersions).
  	settings(dependent.flatMap(setting):_*)


}
