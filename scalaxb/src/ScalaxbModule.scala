package mill.scalaxb

import mill._, os._, scalalib._
import mill.modules.Jvm

trait ScalaxbModule extends ScalaModule {

  val scalaxbVersion = "1.7.3"
  def scalaxbIvyDeps = T { Agg(ivy"org.scalaxb::scalaxb:$scalaxbVersion") }

  def scalaxbDefaultPackage: T[String]
  def scalaxbPackages = T[Seq[(String, String)]] { Seq() }

  // Command line Options as documented on http://scalaxb.org
  def scalaxbOptions = T[Seq[String]] { Seq() }

  def xsdPath = T.sources { millSourcePath }
  def wsdlPath = T.sources { millSourcePath }

  def xsdSources = T.sources { filesUnder(xsdPath(), ".xsd") }
  def wsdlSources = T.sources { filesUnder(wsdlPath(), ".wsdl") }

  def scalaxbArguments =
    T[Seq[String]] {
      scalaxbOptions() ++
        Seq[String]("--default-package", scalaxbDefaultPackage()) ++
        scalaxbPackages().map { case (url, pkg) =>
          s"--package:${url}=${pkg}"
        } ++
        (wsdlSources() ++ xsdSources()).map(_.path.toString)
    }

  override def generatedSources =
    super.generatedSources() ++ scalaxbGeneratedSources()

  def scalaxbClasspath = resolveDeps(scalaxbIvyDeps)

  def scalaxbGeneratedSources =
    T[Seq[PathRef]] {
      val dest = T.ctx().dest
      val args: Seq[String] =
        Seq("--outdir", dest.toString) ++ scalaxbArguments()
      T.ctx().log.info(s"scalaxb ${args.mkString(" ")}")

      Jvm.runSubprocess(
        "scalaxb.compiler.Main",
        scalaxbClasspath().map(_.path),
        forkArgs(),
        forkEnv(),
        args,
        workingDir = forkWorkingDir()
      )

      Seq(PathRef(dest)).flatMap(p => walk(p.path)).map(PathRef(_))
    }

  private def filesUnder(path: Seq[PathRef], extension: String): Seq[PathRef] =
    path
      .flatMap(p => walk(p.path))
      .filter(_.toString.contains(extension))
      .map(PathRef(_))

}
