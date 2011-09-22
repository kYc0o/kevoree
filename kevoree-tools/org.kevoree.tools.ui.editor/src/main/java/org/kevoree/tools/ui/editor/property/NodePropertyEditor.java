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
/* $Id: NodePropertyEditor.java 13934 2010-12-19 21:56:29Z francoisfouquet $ 
 * License    : EPL 								
 * Copyright  : IRISA / INRIA / Universite de Rennes 1 */
package org.kevoree.tools.ui.editor.property;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.explodingpixels.macwidgets.plaf.HudComboBoxUI;
import com.explodingpixels.macwidgets.plaf.HudLabelUI;
import com.explodingpixels.macwidgets.plaf.HudTextFieldUI;
import org.kevoree.*;
import org.kevoree.tools.ui.editor.KevoreeUIKernel;
import org.kevoree.tools.ui.editor.command.*;
import org.kevoree.tools.ui.editor.widget.JCommandButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ffouquet
 */
public class NodePropertyEditor extends InstancePropertyEditor {

    public NodePropertyEditor(final Instance elem, KevoreeUIKernel _kernel) {

        super(elem, _kernel);

        final ContainerNode node = (ContainerNode) elem;


        JPanel pnameLayout = new JPanel(new SpringLayout());
        pnameLayout.setBorder(null);
        pnameLayout.setOpaque(false);


        JLabel physicalNodeNameLabel = new JLabel("Host node", JLabel.TRAILING);
        physicalNodeNameLabel.setUI(new HudLabelUI());
        pnameLayout.add(physicalNodeNameLabel);


        DefaultComboBoxModel hostNodeModel = new DefaultComboBoxModel();

        for (ContainerNode loopNode : ((ContainerRoot) elem.eContainer()).getNodes()) {
            NodeType ntype = (org.kevoree.NodeType) loopNode.getTypeDefinition();
            boolean hostedCapable = false;
            for (AdaptationPrimitiveType ptype : ntype.getManagedPrimitiveTypes()) {
                if (ptype.getName().toLowerCase().equals("startnode")) {
                    hostedCapable = true;
                }
                if (ptype.getName().toLowerCase().equals("stopnode")) {
                    hostedCapable = true;
                }
            }
            if (hostedCapable) {
                hostNodeModel.addElement(loopNode.getName() + ":" + ntype.getName());
                if (loopNode.getHosts().contains(node)) {
                    hostNodeModel.setSelectedItem(loopNode.getName() + ":" + ntype.getName());
                }
            }
        }
        final JComboBox hostNodeComboBox = new JComboBox(hostNodeModel);
        hostNodeComboBox.setUI(new HudComboBoxUI());
        physicalNodeNameLabel.setLabelFor(hostNodeComboBox);
        pnameLayout.add(hostNodeComboBox);

        SpringUtilities.makeCompactGrid(pnameLayout,
                1, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);

        this.addCenter(pnameLayout);

        final UpdatePhysicalNode commandUpdate = new UpdatePhysicalNode();
        commandUpdate.setKernel(kernel);
        commandUpdate.setTargetCNode(node);

        hostNodeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                commandUpdate.execute(hostNodeComboBox.getSelectedItem());
            }
        });

        /*
     JTable table = new JTable(new InstanceTableModel(node));
     JScrollPane scrollPane = new JScrollPane(table);
     table.setFillsViewportHeight(true);

     scrollPane.setPreferredSize(new Dimension(300, 150));

     this.addCenter(scrollPane);
        */

        this.addCenter(new NetworkPropertyEditor(node));

        final SynchNodeTypeCommand sendNodeType = new SynchNodeTypeCommand();
        JCommandButton btPushNodeType = new JCommandButton("Push") {
            @Override
            public void doBeforeExecution() {
                sendNodeType.setDestNodeName(elem.getName());
            }
        };

        sendNodeType.setKernel(_kernel);
        sendNodeType.setDestNodeName(node.getName());
        btPushNodeType.setCommand(sendNodeType);
        this.addCenter(btPushNodeType);


        //   JTextField ip = new JTextField("ip:port");
        /*
        JCommandButton btPushNodeIP = new JCommandButton("Push");
        SynchNodeIPCommand sendNodeIP = new SynchNodeIPCommand();


        sendNodeIP.setKernel(_kernel);
        sendNodeIP.setDestNodeName(node.getName());
        btPushNodeIP.setCommand(sendNodeIP);
        //this.addCenter(ip);
        this.addCenter(btPushNodeIP);
              */

    }
}
