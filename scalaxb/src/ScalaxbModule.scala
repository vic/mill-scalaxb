package mill.scalaxb

import mill._
import scalalib._
import ammonite.ops._
import ImplicitWd._
import pprint._
import _root_.scalaxb.compiler.Main.{start => scalaxbMain}

trait ScalaxbModule extends ScalaModule {

  def scalaxbDefaultPackage : T[String]
  def scalaxbPackages = T[Seq[(String,String)]] { Seq() }

  // Command line Options as documented on http://scalaxb.org
  def scalaxbOptions = T[Seq[String]] { Seq() }

  def xsdPath = T.sources { millSourcePath }
  def wsdlPath = T.sources { millSourcePath }

  def xsdSources = T.sources{ filesUnder(xsdPath(), ".xsd") }
  def wsdlSources = T.sources{ filesUnder(wsdlPath(), ".wsdl") }


  def scalaxbArguments = T[Seq[String]] {
    scalaxbOptions() ++
      Seq[String]("--default-package", scalaxbDefaultPackage()) ++
      scalaxbPackages().map { case (url, pkg) => s"--package:${url}=${pkg}" } ++
      (wsdlSources() ++ xsdSources()).map(_.path.toString)
  }

  override def generatedSources = super.generatedSources() ++ scalaxbGeneratedSources()

  def scalaxbGeneratedSources = T[Seq[PathRef]] {
    val dest = T.ctx().dest
    val args :Seq[String] = Seq("--outdir", dest.toString) ++ scalaxbArguments()
    T.ctx().log.info(s"scalaxb ${args.mkString(" ")}")
    scalaxbMain(args.toArray[String])
    Seq(PathRef(dest)).flatMap(p => ls.rec(p.path)).map(PathRef(_))
  }

  private def filesUnder(path:Seq[PathRef], extension:String): Seq[PathRef] =
    path.flatMap(p => ls.rec(p.path)).filter(_.toString.contains(extension)).map(PathRef(_))

}
