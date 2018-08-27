// -*- mode: scala -*-

// add mill-scalaxb artifact repo
import mill._
interp.repositories() =
  interp.repositories() ++ Seq(coursier.MavenRepository("https://jitpack.io"))

@

import mill._, scalalib._

// import both scalaxb and the mill module
import $ivy.`io.github.vic::mill-scalaxb:0.0.2`, mill.scalaxb._
import $ivy.`org.scalaxb::scalaxb:1.5.2`

object hello extends ScalaModule with ScalaxbModule {

  def scalaVersion = "2.12.6"

  // REQUIRED name of the package for generated sources
  def scalaxbDefaultPackage = "example"

  // REQUIRED add the scalaxb runtime dependency
  def ivyDeps = Agg(ivy"org.scalaxb::scalaxb:1.5.2")

  // optionally map namespace URIs to packages
  // def scalaxbPackages = T[Seq[(String,String)]] { Seq("http://some/uri" -> "some.pkg") }

  // optionally define options as documented on http://scalaxb.org
  def scalaxbOptions = Seq(
    "--no-dispatch-client",
    "--named-attributes")

  // optionally override the directory for wsdl (for example when using sbt layout)
  // def wsdlPath = millSourcePath / 'src / 'main / 'wsdl
  // def xsdPath = millSourcePath / 'src / 'main / 'xsd

}
