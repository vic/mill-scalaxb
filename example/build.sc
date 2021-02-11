// -*- mode: scala -*-

import $ivy.`com.github.vic::mill-scalaxb:latest`
import mill._, scalalib._, mill.scalaxb.ScalaxbModule

object hello extends ScalaModule with ScalaxbModule {

  def scalaVersion = scala.util.Properties.versionNumberString

  // REQUIRED name of the package for generated sources
  def scalaxbDefaultPackage = "example"

  // REQURED add the scalaxb runtime dependency to your classpath
  def ivyDeps = 
    super.ivyDeps() ++ scalaxbIvyDeps() ++ Agg(
   // this project specific deps
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
