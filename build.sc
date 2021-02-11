// -*- mode: scala -*-

import mill._, scalalib._, publish._

val crossVersions = Seq("2.13.2")

object scalaxb extends Cross[Scalaxb](crossVersions: _*)
class Scalaxb(val crossScalaVersion: String)
    extends CrossScalaModule
    with PublishModule {

  override def publishVersion = T { os.read(os.pwd / "VERSION").trim }
  override def artifactName = "mill-scalaxb"

  override def sources = T.sources(millOuterCtx.millSourcePath / "src")

  def pomSettings =
    PomSettings(
      description = "Scalaxb code generation for mill",
      organization = "io.github.vic",
      url = "https://github.com/vic/mill-scalaxb",
      licenses = Seq(License.`Apache-2.0`),
      versionControl = VersionControl.github("vic", "mill-scalaxb"),
      developers = Seq(
        Developer("vic", "Victor Borja", "https://github.com/vic")
      )
    )

  def compileIvyDeps =
    Agg(
      ivy"com.lihaoyi::mill-scalalib:latest.stable"
    )
}
