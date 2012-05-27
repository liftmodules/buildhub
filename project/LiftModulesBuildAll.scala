import sbt._
import sbt.Keys._

object LiftModulesBuildAll extends Build {

	val liftVersion = SettingKey[String]("liftVersion", "The version of the Lift Web Framework to build against")
	
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
		// NB: oauth-mapper build is triggered by a build of oauth and this build, so no need to include it here
		// uri("git://github.com/liftmodules/oauth-mapper.git"),
		uri("git://github.com/liftmodules/openid.git"),
		uri("git://github.com/liftmodules/scalate.git"),
		uri("git://github.com/liftmodules/textile.git"),
		uri("git://github.com/liftmodules/xmpp.git")
		//uri("git://github.com/d6y/liftmodules-googleanalytics.git")
		//uri("git://github.com/d6y/liftmodules-imap-idle.git")
		)
	
	// Override the version of Lift and publish settings for all modules being built:
	lazy val moduleSettings: Seq[Setting[_]] = modules.flatMap { module => List( 
		liftVersion in module := "2.5-SNAPSHOT",
		resolvers in module += ScalaToolsSnapshots,
		publishTo in module := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"),
		credentials in module += Credentials( file("/private/liftmodules/sonatype.credentials") )
		)
	}

  lazy val all = Project(id = "all", base = file(".")).
  	aggregate(modules:_*).
 		settings(publish := { }). // don't publish this wrapper project 
 		settings(crossScalaVersions := Seq("2.8.1", "2.9.0-1", "2.9.1", "2.9.2")).
  	settings(moduleSettings:_*)

}
