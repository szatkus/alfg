import sbt._
import Keys._
import Tests._
import com.typesafe.config.ConfigFactory

/**
 * This is a simple sbt setup generating Slick code from the given
 * database before compiling the projects code.
 */
object myBuild extends Build {
    val hwsettings = Defaults.defaultSettings

    lazy val slick = TaskKey[Seq[File]]("gen-tables")
    lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
        val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
        val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
        val url = conf.getString("db.default.url")
        val jdbcDriver = conf.getString("db.default.driver")
        val slickDriver = "slick.driver.SQLiteDriver"
        val pkg = "demo"
        toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
        val fname = outputDir + "/demo/Tables.scala"
        Seq(file(fname))
    }

    lazy val mainProject = Project (
      "root",
      file ("."),
      settings = Defaults.defaultSettings ++ Seq(slick <<= slickCodeGenTask,
      sourceGenerators in Compile <+= slickCodeGenTask)
    )
}
