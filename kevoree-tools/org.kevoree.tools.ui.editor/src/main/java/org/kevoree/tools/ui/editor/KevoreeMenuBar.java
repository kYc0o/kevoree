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
/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kevoree.tools.ui.editor;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.kevoree.tools.ui.editor.command.*;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KevoreeMenuBar extends JMenuBar {

    public KevoreeMenuBar(KevoreeUIKernel kernel) {

        JMenu file, model, kevs, tools = null;

        file = new JMenu("File");

        /* Load command */
        JMenuItem fileOpen = new JMenuItem("Open");
        LoadModelCommandUI cmdLM2 = new LoadModelCommandUI();
        ClearModelCommand cmdLM1 = new ClearModelCommand();
        cmdLM1.setKernel(kernel);
        cmdLM2.setKernel(kernel);
        CompositeCommand cmdLM = new CompositeCommand();
        cmdLM.addCommand(cmdLM1);
        cmdLM.addCommand(cmdLM2);
        fileOpen.addActionListener(new CommandActionListener(cmdLM));
        file.add(fileOpen);

        JMenuItem fileMerge = new JMenuItem("Merge");
        LoadModelCommandUI cmdLMerge = new LoadModelCommandUI();
        cmdLMerge.setKernel(kernel);
        fileMerge.addActionListener(new CommandActionListener(cmdLMerge));
        file.add(fileMerge);

        /* Load command */
        JMenuItem kevsOpen = new JMenuItem("Open from KevScript");
        LoadKevScriptCommandUI cmdKevOpen = new LoadKevScriptCommandUI();
        cmdKevOpen.setKernel(kernel);
        kevsOpen.addActionListener(new CommandActionListener(cmdKevOpen));
        file.add(kevsOpen);

        /* Load remote ui command */
        JMenuItem fileMergeRemote = new JMenuItem("Merge from node");
        LoadRemoteModelUICommand cmdLMRemote = new LoadRemoteModelUICommand();
        cmdLMRemote.setKernel(kernel);
        fileMergeRemote.addActionListener(new CommandActionListener(cmdLMRemote));
        file.add(fileMergeRemote);

        /* Load remote ui command */
        JMenuItem fileOpenRemote = new JMenuItem("Open from node");
        LoadRemoteModelUICommand cmdLMORemote2 = new LoadRemoteModelUICommand();
        ClearModelCommand cmdLMORemote1 = new ClearModelCommand();
        cmdLMORemote2.setKernel(kernel);
        cmdLMORemote1.setKernel(kernel);
        CompositeCommand cmdLMORemote = new CompositeCommand();
        cmdLMORemote.addCommand(cmdLMORemote1);
        cmdLMORemote.addCommand(cmdLMORemote2);
        fileOpenRemote.addActionListener(new CommandActionListener(cmdLMORemote));
        file.add(fileOpenRemote);


        JMenuItem fileSave = new JMenuItem("Save");
        SaveActuelModelCommand cmdSM = new SaveActuelModelCommand();
        cmdSM.setKernel(kernel);
        fileSave.addActionListener(new CommandActionListener(cmdSM));
        file.add(fileSave);

        JMenuItem saveImage = new JMenuItem("SaveAsImage");
        ExportModelImage cmdImage = new ExportModelImage();
        cmdImage.setKernel(kernel);
        saveImage.addActionListener(new CommandActionListener(cmdImage));
        file.add(saveImage);


        JMenuItem saveSVG = new JMenuItem("SaveAsSVG");
        ExportModelSVGImage cmdImageSVG = new ExportModelSVGImage();
        cmdImageSVG.setKernel(kernel);
        saveSVG.addActionListener(new CommandActionListener(cmdImageSVG));
        file.add(saveSVG);


        JMenuItem saveKCS = new JMenuItem("SaveAsKevScript");
        SaveAsKevScriptCommandUI cmdSaveKCS = new SaveAsKevScriptCommandUI();
        cmdSaveKCS.setKernel(kernel);
        saveKCS.addActionListener(new CommandActionListener(cmdSaveKCS));
        file.add(saveKCS);

        JMenuItem openArduino = new JMenuItem("Open from ArduinoNode");
        OpenArduinoNode cmdOpenArduino = new OpenArduinoNode();
        cmdOpenArduino.setKernel(kernel);
        openArduino.addActionListener(new CommandActionListener(cmdOpenArduino));
        file.add(openArduino);



        JMenuItem refresh = new JMenuItem("Refresh");
        RefreshModelCommand cmdRM = new RefreshModelCommand();
        cmdRM.setKernel(kernel);
        refresh.addActionListener(new CommandActionListener(cmdRM));
        file.add(refresh);

        model = new JMenu("Model");
        // JMenuItem addNode = new JMenuItem("Add node");
        //  AddNodeCommand cmdAN = new AddNodeCommand();
        //  cmdAN.setKernel(kernel);
        // addNode.addActionListener(new CommandActionListener(cmdAN));
        // model.add(addNode);
        JMenuItem clearModel = new JMenuItem("Clear");
        ClearModelCommand cmdCM = new ClearModelCommand();
        cmdCM.setKernel(kernel);
        clearModel.addActionListener(new CommandActionListener(cmdCM));
        model.add(clearModel);
        JMenuItem mergeLib = new JMenuItem("Load Library");
        LoadNewLibCommandUI cmdLL = new LoadNewLibCommandUI();
        cmdLL.setKernel(kernel);
        mergeLib.addActionListener(new CommandActionListener(cmdLL));
        model.add(mergeLib);


        JMenu mergelibraries = new JMenu("Load CoreLibrary");

        JMenuItem mergeDefLib0 = new JMenuItem("ALL");
        MergeDefaultLibrary cmdLDEFL0 = new MergeDefaultLibrary(0);
        cmdLDEFL0.setKernel(kernel);
        mergeDefLib0.addActionListener(new CommandActionListener(cmdLDEFL0));
        mergelibraries.add(mergeDefLib0);

        JMenuItem mergeDefLib1 = new JMenuItem("JAVASE");
        MergeDefaultLibrary cmdLDEFL1 = new MergeDefaultLibrary(1);
        cmdLDEFL1.setKernel(kernel);
        mergeDefLib1.addActionListener(new CommandActionListener(cmdLDEFL1));
        mergelibraries.add(mergeDefLib1);

        JMenuItem mergeDefLib2 = new JMenuItem("WEBSERVER");
        MergeDefaultLibrary cmdLDEFL2 = new MergeDefaultLibrary(2);
        cmdLDEFL2.setKernel(kernel);
        mergeDefLib2.addActionListener(new CommandActionListener(cmdLDEFL2));
        mergelibraries.add(mergeDefLib2);

        JMenuItem mergeDefLib3 = new JMenuItem("ARDUINO");
        MergeDefaultLibrary cmdLDEFL3 = new MergeDefaultLibrary(3);
        cmdLDEFL3.setKernel(kernel);
        mergeDefLib3.addActionListener(new CommandActionListener(cmdLDEFL3));
        mergelibraries.add(mergeDefLib3);

        JMenuItem mergeDefLib4 = new JMenuItem("SKY");
        MergeDefaultLibrary cmdLDEFL4 = new MergeDefaultLibrary(4);
        cmdLDEFL4.setKernel(kernel);
        mergeDefLib4.addActionListener(new CommandActionListener(cmdLDEFL4));
        mergelibraries.add(mergeDefLib4);

        JMenuItem mergeDefLib5 = new JMenuItem("ANDROID");
        MergeDefaultLibrary cmdLDEFL5 = new MergeDefaultLibrary(5);
        cmdLDEFL5.setKernel(kernel);
        mergeDefLib5.addActionListener(new CommandActionListener(cmdLDEFL5));
        mergelibraries.add(mergeDefLib5);


        model.add(mergelibraries);


        JMenuItem checkModel = new JMenuItem("Check");
        CheckCurrentModel cmdCheck = new CheckCurrentModel();
        cmdCheck.setKernel(kernel);
        checkModel.addActionListener(new CommandActionListener(cmdCheck));
        model.add(checkModel);


        kevs = new JMenu("KevScript");
        JMenuItem openEditor = new JMenuItem("Open editor");
        OpenKevsShell cmdOpenKevsGUI = new OpenKevsShell();
        cmdOpenKevsGUI.setKernel(kernel);
        openEditor.addActionListener(new CommandActionListener(cmdOpenKevsGUI));
        kevs.add(openEditor);

        tools = new JMenu("Tools");

        JMenuItem serialmonitorItem = new JMenuItem("Serial Monitor");
        DisplaySerialMonitor serialMonitor = new DisplaySerialMonitor();
        serialMonitor.setKernel(kernel);
        serialmonitorItem.addActionListener(new CommandActionListener(serialMonitor));
        tools.add(serialmonitorItem);

        JMenuItem sourceEditorItem = new JMenuItem("Source editor");
        DisplayModelTextEditor sourceEditorCmd = new DisplayModelTextEditor();
        sourceEditorCmd.setKernel(kernel);
        sourceEditorItem.addActionListener(new CommandActionListener(sourceEditorCmd));
        tools.add(sourceEditorItem);

        JMenuItem jmdnsLookup = new JMenuItem("JmDns Lookup");
        JmDnsLookup jmdnsLookupCmd = new JmDnsLookup();
        jmdnsLookupCmd.setKernel(kernel);
        jmdnsLookup.addActionListener(new CommandActionListener(jmdnsLookupCmd));
        tools.add(jmdnsLookup);

        /*
        JMenuItem closeOsgi = new JMenuItem("Clean OSGi Cache");
        KillOSGICommand closeOsgiCmd = new KillOSGICommand();
        closeOsgi.addActionListener(new CommandActionListener(closeOsgiCmd));
        tools.add(closeOsgi);
         */
        JMenu loggerMenu = new JMenu("Logger");

        ButtonGroup logGroup = new ButtonGroup();

        JCheckBoxMenuItem warnLevelItem = new JCheckBoxMenuItem("Warn");
        warnLevelItem.setSelected(true);
        warnLevelItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
                root.setLevel(Level.WARN);
            }
        });
        loggerMenu.add(warnLevelItem);
        logGroup.add(warnLevelItem);

        JCheckBoxMenuItem debugLevelItem = new JCheckBoxMenuItem("Debug");
        debugLevelItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
                root.setLevel(Level.DEBUG);
            }
        });
        loggerMenu.add(debugLevelItem);
        logGroup.add(debugLevelItem);

        JCheckBoxMenuItem infoLevelItem = new JCheckBoxMenuItem("Info");
        infoLevelItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
                root.setLevel(Level.INFO);
            }
        });
        loggerMenu.add(infoLevelItem);
        logGroup.add(infoLevelItem);

        tools.add(loggerMenu);

        this.add(file);
        this.add(model);
        this.add(kevs);
        this.add(tools);


    }

    class CommandActionListener implements ActionListener {

        private Command _command = null;

        public CommandActionListener(Command command) {
            _command = command;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            _command.execute("");
        }
    }


}
