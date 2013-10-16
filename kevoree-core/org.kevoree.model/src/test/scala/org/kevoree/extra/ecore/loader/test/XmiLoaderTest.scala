/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kevoree.extra.ecore.loader.test

import org.junit.Assert._
import org.junit.{Test, BeforeClass}
import org.kevoree.loader.XMIModelLoader
import org.kevoree.serializer.XMIModelSerializer
import org.kevoree._
import java.io.{FileInputStream,FileOutputStream, ByteArrayOutputStream, PrintWriter, File}
import scala.collection.JavaConversions._
import scala.Some

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 24/09/11
 * Time: 13:39
 */

object XmiLoaderTest {
  var model: ContainerRoot = null

  @BeforeClass
  def loadXmi() {
    val loader = new XMIModelLoader()
    model = loader.loadModelFromStream(new FileInputStream(new File(getClass.getResource("/defaultlibs.kev").toURI))).get(0).asInstanceOf[ContainerRoot];
    model.setRecursiveReadOnly()
  }
}

class XmiLoaderTest {

  @Test
  def testLoadParameters() {
    val loader = new XMIModelLoader()
    loader.loadModelFromStream(new FileInputStream(new File(getClass.getResource("/ParametersBug.kev").toURI))).get(0);
  }

  @Test
  def testLoadDefaultLibsFqn() {
    val loader = new XMIModelLoader()
    loader.loadModelFromStream(new FileInputStream(new File(getClass.getResource("/defaultlibsFqn.kev").toURI))).get(0);
  }

  @Test
  def testOpposite1(){
    val loader = new XMIModelLoader()
    val m = loader.loadModelFromStream(new FileInputStream(new File(getClass.getResource("/unomas.kev").toURI))).get(0).asInstanceOf[ContainerRoot];
    m.getmBindings.foreach { mb =>
      println("---------->")
      val p = mb.getPort
      assert(mb.getPort != null)
      assert(mb.getPort == p)


      println(">>"+mb.getPort)

      assert(mb.getPort.getBindings.size == 1)
      assert(mb.getPort.getBindings.contains(mb))

      mb.setPort(null)
      assert(mb.getPort == null)
      assert(p.getBindings.size == 0)
      assert(!p.getBindings.contains(mb))

      mb.setPort(p)
      assert(mb.getPort == p)
      assert(mb.getPort.getBindings.size == 1)
      assert(mb.getPort.getBindings.contains(mb))

      p.removeBindings(mb)
      assert(mb.getPort == null)
      assert(p.getBindings.size == 0)
      assert(!p.getBindings.contains(mb))

      p.addBindings(mb)
      assert(mb.getPort == p)
      assert(mb.getPort.getBindings.size == 1)
      assert(mb.getPort.getBindings.contains(mb))

    }
  }


  @Test
  def testSaveAndLoad() {
    //System.out.print("Saving model from memory to tempFile =>")
    val tempFile = File.createTempFile("kmfTest_" + System.currentTimeMillis(), "kev")
    //tempFile.deleteOnExit()
    System.out.println(tempFile.getAbsolutePath)
    val pr = new FileOutputStream(tempFile)
    val ms = new XMIModelSerializer()


    ms.serializeToStream(XmiLoaderTest.model,pr)
    pr.close()
    System.out.println("Loading saved model")
    val loader = new XMIModelLoader()
    val localModel = loader.loadModelFromStream(new FileInputStream(tempFile));
    if(localModel == null) {
      fail("Model not loaded!")
    }
    System.out.println("Loding OK.")
  }

  @Test
  def loadBootstrapModel() {
    val loader = new XMIModelLoader()
    val localModel = loader.loadModelFromStream(new FileInputStream(new File(getClass.getResource("/bootstrapModel0.kev").toURI))).get(0);
    if(localModel == null){
      fail("Model not loaded!")
    }
  }

  @Test
  def loadAndCloneToReadWrite() {
    val factory = new org.kevoree.impl.DefaultKevoreeFactory()
    val loader = new XMIModelLoader()
    val m = loader.loadModelFromStream(new FileInputStream(new File(getClass.getResource("/bootstrapModel0.kev").toURI))).get(0).asInstanceOf[ContainerRoot];

    val newNode = factory.createContainerNode();
    newNode.setName("NewNode");

    m.addNodes(newNode)
    val modelCloner = new org.kevoree.cloner.DefaultModelCloner
    val readOModel = modelCloner.clone(m, true).asInstanceOf[ContainerRoot]
    var errorDetected = false
    try {
      readOModel.addNodes(factory.createContainerNode)
      fail("Model must be not modifiable!")
    } catch {
      case _ => {
        errorDetected = true
      }
    }
    assert(errorDetected)
    errorDetected = false
    try {
      readOModel.getNodes().get(0).setName("SayHelloNode")
      fail("Model must be not modifiable!")
    } catch {
      case _ => {
        errorDetected = true
      }
    }
    assert(errorDetected)


    val newNode2 = factory.createContainerNode
    newNode2.setName("NewNode2")
    m.addNodes(newNode2)


    val writeModel = modelCloner.clone(readOModel, false).asInstanceOf[ContainerRoot]

    val newNode3 = factory.createContainerNode
    newNode3.setName("NewNode3")
    writeModel.addNodes(newNode2)

    val writeModel2 = modelCloner.clone(readOModel).asInstanceOf[ContainerRoot]

    val newNode4 = factory.createContainerNode
    newNode4.setName("NewNode4")
    writeModel2.addNodes(newNode4)

  }


  @Test
  def deepCheck() {
    XmiLoaderTest.model.getTypeDefinitions.find(td => td.getName.equals("ArduinoNode")) match {
      case Some(typeDef) => {

        import scala.collection.JavaConversions._
        XmiLoaderTest.model.getDeployUnits.foreach{ du =>
             System.out.println(du.getGenerated_KMF_ID+"-"+du.getGroupName);
        }


        XmiLoaderTest.model.getDeployUnits.find(du => ((du.getGroupName != null) && du.getGroupName.equals("org.kevoree.library.arduino")) &&  ((du.getUnitName != null) && du.getUnitName.equals("org.kevoree.library.arduino.nodeType"))) match {
          case Some(du) => {
            assertTrue("TypeDefinition does not contain its deploy unit.", typeDef.getDeployUnits.contains(du))
          }
          case None => fail("DeployUnit org.kevoree.library.arduino not found")
        }

       typeDef.asInstanceOf[NodeType].getDictionaryType match {
          case dico:DictionaryType => {
            dico.getAttributes.find {
              att => att.getName.equals("boardTypeName")
            } match {
              case Some(att) => {
                assertTrue(att.getOptional)
                assertTrue(att.getDatatype.equals("enum=uno,atmega328,mega2560"))
                dico.getDefaultValues.find(defVal => defVal.getAttribute.equals(att)) match {
                  case Some(default) => {
                    assertTrue(default.getValue.equals("uno"))
                  }
                  case None => fail("No default value for att:" + att.getName)
                }
              }
              case None => fail("No attribute named boardTypeName found in ArduinoNode type dictionary")
            }
          }
          case _ => fail("No dictionaryType loaded for ArduinoNode")
        }
      }
      case None => fail("Arduino Node Type not found !")
    }
  }

  //@Test
  def checkRepositories() {
    val repList = XmiLoaderTest.model.getRepositories
    assertTrue("Wrong number of repositories in model." + repList.size, repList.size == 6);
    repList.foreach {
      elem =>
        assertNotNull("eContainer not set for Repository:" , elem.eContainer)
    }

  }

  /*
  @Test
  def checkLibraries() {
    val libList = XmiLoaderTest.model.getLibraries
    assertTrue("Wrong number of libraries in model:" + libList.size, libList.size == 9);
    libList.foreach {
      lib =>
        System.out.println("Lib[name:" + lib.getName + ", sub:" + lib.getSubTypes.mkString("[", ", ", "]") + "]")
      // assertFalse("Lib has no name.",lib.getName.equals(""))
      // assertNotNull("eContainer not set for Library:" + lib.getName,lib.eContainer)
    }

  }

  @Test
  def checkDeployUnits() {
    val duList = XmiLoaderTest.model.getDeployUnits
    assertTrue("Wrong number of DeployUnits in model." + duList.size, duList.size == 20);
    duList.foreach {
      du =>
        assertNotNull("DeployUnit name is null", du.getName)
        assertNotNull("eContainer not set for DeployUnit:" + du.getName, du.eContainer)
        System.out.println("DeployUnit[name:" + du.getName
          + ", groupName:" + du.getGroupName
          + ", unitName:" + du.getUnitName
          + ", version:" + du.getVersion
          + ", url:" + du.getUrl
          + "]") /*
         du.getName match {
           case "" => {
             assertFalse("DeployUnit.getName:"+du.getName+" du.groupName:" +du.getGroupName,du.getGroupName.equals(""))
             assertFalse("DeployUnit.getName:"+du.getName+" du.unitName:"+du.getUnitName,du.getUnitName.equals(""))
             assertFalse("DeployUnit.getName:"+du.getName+" du.version:"+du.getVersion,du.getVersion.equals(""))
           }
           case _ => {
              assertFalse("DeployUnit.getName:"+du.getName+" du.url:"+du.getUrl,du.getUrl.equals(""))
           }
         }
         */
    }
  }


  @Test
  def checkTypeDefinitions() {
    val tdList = XmiLoaderTest.model.getTypeDefinitions
    assertTrue("Wrong number of TypeDefinitions in model." + tdList.size, tdList.size == 33);

    tdList.foreach {
      typeDef =>
        assertNotNull("eContainer not set for TypeDef:" + typeDef.getName, typeDef.eContainer)
        typeDef match {
          case ct: ComponentType => {
            val provPortList = ct.getProvided

            provPortList.foreach(port => assertNotNull("eContainer not set for Port:" + port.getName + " in " + typeDef.getName, port.eContainer))


            if (ct.getName.equals("DigitalLight")) {
              assertTrue("No provided port in " + ct.getName, ct.getProvided != null)
              assertTrue("Not enough providedPorts in " + ct.getName + " : " + ct.getProvided.size, ct.getProvided.size == 3)
            }
          }
          case _ => //No test for other types for the moment//TODO: ADD TESTS
        }

    }
  }    */

}