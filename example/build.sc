// -*- mode: scala -*-

import $ivy.`io.get-coursier:interface:0.0.21`

// add mill-scalaxb artifact repo
import mill._
interp.repositories() =
  interp.repositories() ++ Seq(coursierapi.MavenRepository.of("https://jitpack.io"))

@

import mill._, scalalib._

// import both scalaxb and the mill module
import $ivy.`io.github.vic::mill-scalaxb:0.2.0`, mill.scalaxb._
import $ivy.`org.scalaxb::scalaxb:1.7.3`

object hello extends ScalaModule with ScalaxbModule {

  def scalaVersion = "2.12.8"

  // REQUIRED name of the package for generated sources
  def scalaxbDefaultPackage = "example"

  // REQUIRED add the scalaxb runtime dependency
  def ivyDeps = Agg(
    ivy"org.scalaxb::scalaxb:1.7.3",
    ivy"org.glassfish.jaxb:jaxb-runtime:2.3.2"
  )

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
