// -*- mode: scala -*-

import $ivy.`io.get-coursier:interface:0.0.21`

// Dont use sonatype's maven-central as it timeouts in travis.
interp.repositories() =
  List(coursierapi.MavenRepository.of("https://jcenter.bintray.com"))

@

import mill._, scalalib._, publish._

object scalaxb extends ScalaModule with PublishModule {

  def publishVersion = os.read(os.pwd / "VERSION").trim

  // use versions installed from .tool-versions
  def scalaVersion = scala.util.Properties.versionNumberString
  def millVersion = System.getProperty("MILL_VERSION")

  def artifactName = "mill-scalaxb"

  def pomSettings = PomSettings(
    description = "Scalaxb code generation for mill",
    organization = "io.github.vic",
    url = "https://github.com/vic/mill-scalaxb",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github("vic", "mill-scalaxb"),
    developers = Seq(
      Developer("vic", "Victor Borja", "https://github.com/vic")
    )
  )

  def compileIvyDeps = Agg(
    ivy"com.lihaoyi::mill-scalalib:${millVersion}",
    ivy"org.scalaxb::scalaxb:1.7.3"
  )

}
